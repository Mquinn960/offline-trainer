package com.mquinn.trainer;

import com.mquinn.trainer.sl_extensions.PcaData;
import com.mquinn.trainer.sl_extensions.SvmInputData;
import com.mquinn.trainer.sl_extensions.SvmService;
import mquinn.sign_language.svm.LetterClass;
import org.opencv.core.Core;
import org.opencv.ml.SVM;

import java.util.Arrays;

public class SvmTestRunner {

    private SVM svm;
    private SvmInputData testData;

    private PcaData pcaData;

    private LetterClass actualResult;

    public SvmTestRunner ( SvmInputData inputData){
        svm = SvmService.getInstance().getInMemorySVM();
        pcaData = SvmService.getInstance().getPcaData();
        testData = inputData;
    }

    public void runTests(){

        pcaProjectTestData();

        int rows = testData.labels.rows();

        String[][] results = new String[rows][2];

        for (int row = 0; row < rows; row++){

            String correctLabel = LetterClass.getLetter((int)testData.labels.get(row,0)[0]).toString();

            float response = svm.predict(testData.samples.row(row));
            actualResult = LetterClass.getLetter((int)response);

            results[row][0] = correctLabel;
            results[row][1] = actualResult.toString();
        }

        System.out.println(Arrays.deepToString(results));

    }

    private void pcaProjectTestData(){

        Core.PCAProject(testData.samples, pcaData.mean, pcaData.eigen, testData.samples);

    }

}
