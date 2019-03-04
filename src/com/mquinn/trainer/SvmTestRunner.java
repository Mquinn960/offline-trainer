package com.mquinn.trainer;

import com.mquinn.trainer.sl_extensions.SvmInputData;
import mquinn.sign_language.svm.LetterClass;
import org.opencv.ml.SVM;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SvmTestRunner {

    private SVM svm;
    private SvmInputData testData;

    private LetterClass actualResult;

    public SvmTestRunner (SVM inputSvm, SvmInputData inputData){
        svm = inputSvm;
        testData = inputData;
    }

    public void runTests(){

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

}
