package com.mquinn.trainer;

import com.mquinn.trainer.sl_extensions.PcaData;
import com.mquinn.trainer.sl_extensions.SvmInputData;
import com.mquinn.trainer.sl_extensions.SvmService;
import mquinn.sign_language.svm.LetterClass;
import org.opencv.core.Core;
import org.opencv.ml.SVM;

public class SvmTestRunner {

    private SVM svm;
    private SvmInputData testData;

    private PcaData pcaData;

    private int[][] resultsMat = new int[26][26];

    private LetterClass actualResult;

    private long testingTotal, testingEnd, testingStart;

    private ResultLoggerService logger;

    public SvmTestRunner ( SvmInputData inputData){
        svm = SvmService.getInstance().getInMemorySVM();
        pcaData = SvmService.getInstance().getPcaData();
        testData = inputData;
        logger = ResultLoggerService.getInstance(false);
    }

    public void runTests(){

        if (SvmService.getInstance().getPcaUse()) {
            pcaProjectTestData();
        }

        int rows = testData.labels.rows();

        String[][] results = new String[rows][2];
        int[][] intResults = new int[rows][2];

        testingStart = System.currentTimeMillis();

        for (int row = 0; row < rows; row++){

            String correctLabel = LetterClass.getLetter((int)testData.labels.get(row,0)[0]).toString();

            float response = svm.predict(testData.samples.row(row));
            actualResult = LetterClass.getLetter((int)response);

            results[row][0] = actualResult.toString();
            results[row][1] = correctLabel;

            intResults[row][0] = (int)response;
            intResults[row][1] = (int)testData.labels.get(row,0)[0];

            // fill in confusion matrix
            resultsMat[(int)testData.labels.get(row,0)[0] - 1][(int)response - 1]++;

        }

        testingEnd = System.currentTimeMillis();
        testingTotal = testingEnd - testingStart;

        logger.log("Actual Testing Time: " + TimeFormatter.millisToTime(testingTotal), true);

        int[][][] finalMatrix = new int[26][4][rows];

        for (int i=0; i<26; i++){
            for (int j=0; j < rows; j++){
                int sample = j+1;
                // for any non empty cells
                if (intResults[j][0] != 0 && intResults[j][1] != 0) {
                    // for correct predictions equal to this class in the loop
                    if (i == intResults[j][1]-1 && i == intResults[j][0]-1) {
                        // for correct predictions
                        if (intResults[j][0] == intResults[j][1]) {
                            // True Positive
                            // add true positive value to this class in the matrix
                            finalMatrix[i][0][j] = sample;
                            // True Negative
                            // add true negative to every other class in the matrix
                            for (int k=0; k<26; k++){
                                if (k != (intResults[j][1])-1) {
                                    finalMatrix[k][1][j] = sample;
                                }
                            }
                        }
                    // otherwise for non matching class predictions
                    } else {
                        // if predicted class is current class
                        if (i == intResults[j][1]-1) {
                            // False Positive
                            finalMatrix[i][2][j] = sample;
                        // else if actual class was current class
                        } else if (i == intResults[j][0]-1) {
                            // False Negative
                            finalMatrix[i][3][j] = sample;
                        }
                    }
                }
            }
        }

        // Print sample category matrix
        logger.log("", true);
        logger.log("SAMPLE CATEGORY MATRIX", true);
        logger.log("______________________", true);
        logger.log("", true);
        for (int i=0; i< 26 ; i++) {
            logger.log("CLASS: " + (char)(i+65), true);
            for (int j=0; j < 4 ; j++){
                switch (j){
                    case 0:
                        logger.log("TP: ", false);
                        break;
                    case 1:
                        logger.log("TN: ", false);
                        break;
                    case 2:
                        logger.log("FP: ", false);
                        break;
                    case 3:
                        logger.log("FN: ", false);
                        break;
                    default:
                        // Do nothing
                        break;
                }
                for (int k=0; k < rows ; k++)
                    if (finalMatrix[i][j][k] != 0){
                        logger.log(finalMatrix[i][j][k] + " ", false);
                    }
                logger.log("", true);
            }
            logger.log("", true);
        }

        // Print confusion matrix simple
        logger.log("SIMPLE CONFUSION MATRIX", true);
        logger.log("______________________", true);
        logger.log("", true);
        for (int i=0; i< 26 ; i++) {
            for (int j=0; j < 26 ; j++){
                logger.log(resultsMat[i][j] + " ", false);
            }
            // Log
            logger.log("", true);
        }
        logger.log("", true);

    }

    private void pcaProjectTestData(){

        Core.PCAProject(testData.samples, pcaData.mean, pcaData.eigen, testData.samples);

    }

}
