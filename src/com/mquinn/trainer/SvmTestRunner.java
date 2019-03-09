package com.mquinn.trainer;

import com.mquinn.trainer.sl_extensions.PcaData;
import com.mquinn.trainer.sl_extensions.SvmInputData;
import com.mquinn.trainer.sl_extensions.SvmService;
import mquinn.sign_language.svm.LetterClass;
import org.opencv.core.Core;
import org.opencv.ml.SVM;

import java.util.ArrayList;

public class SvmTestRunner {

    private SVM svm;
    private SvmInputData testData;

    private PcaData pcaData;

    private int[][] resultsMat = new int[26][26];

    private LetterClass actualResult;

    public SvmTestRunner ( SvmInputData inputData){
        svm = SvmService.getInstance().getInMemorySVM();
        pcaData = SvmService.getInstance().getPcaData();
        testData = inputData;
    }

    public void runTests(){

//        pcaProjectTestData();

        int rows = testData.labels.rows();

        String[][] results = new String[rows][2];
        int[][] intResults = new int[rows][2];

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

        int[][][] finalMatrix = new int[26][4][rows];

        for (int i=0; i<26; i++){
            for (int j=0; j<rows; j++){
                int sample = j+1;
                // Positive
                if (intResults[j][0] != 0 && intResults[j][1] != 0){
                    if ((intResults[j][0] == intResults[j][1]) && (i == intResults[j][1] && i == intResults[j][0])){
                        // True Positive
                        finalMatrix[i-1][0][j] = sample;
                        // True Negative
                        for (int k=0; k<26; k++){
                            if (k != (intResults[j][1])-1){
                                finalMatrix[k][1][j] = sample;
                            }
                        }
                    }
                } else {
                    // False Negative
                    finalMatrix[i][3][j] = sample;
                    // False Positive
                    finalMatrix[intResults[j][i]][2][j] = sample;
                }
            }
        }

        System.out.print("SAMPLE CATEGORY MATRIX");
        System.out.print("______________________");
        System.out.println();
        for (int i=0; i< 26 ; i++) {
            System.out.print("CLASS: " + (char)(i+65));
            System.out.println();
            for (int j=0; j < 4 ; j++){
                System.out.print("TYPE:");
                System.out.println();
                switch (j){
                    case 0:
                        System.out.print("TP");
                        break;
                    case 1:
                        System.out.print("TN");
                        break;
                    case 2:
                        System.out.print("FP");
                        break;
                    case 3:
                        System.out.print("FN");
                        break;
                    default:
                        // Do nothing
                        break;
                }
                System.out.println();
                for (int k=0; k < rows ; k++)
                    if (finalMatrix[i][j][k] != 0){
                        System.out.print(finalMatrix[i][j][k] + " ");
                    }
                // Log
                System.out.println();
            }
            System.out.println();
        }

        // Print confusion matrix simple
        System.out.print("SIMPLE CONFUSION MATRIX");
        System.out.println();
        for (int i=0; i< 26 ; i++) {
            for (int j=0; j < 26 ; j++)
                System.out.print(resultsMat[i][j] + " ");
            // Log
            System.out.println();
        }

    }

    private void pcaProjectTestData(){

        Core.PCAProject(testData.samples, pcaData.mean, pcaData.eigen, testData.samples);

    }

}
