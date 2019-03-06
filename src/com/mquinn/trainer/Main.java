package com.mquinn.trainer;

import org.opencv.core.Core;

public class Main {

    public static void main(String[] args) {

        long trainingStart, trainingEnd, trainingTotal,
             testingStart, testingEnd, testingTotal;

        ResultLogger logger = new ResultLogger("Run 1");

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        trainingStart = System.currentTimeMillis();

        // Run training
        ImageRunner trainingRunner = new ImageRunner("F:\\Hand Dataset\\train\\single",
                                     new ImageProcessor(Operation.TRAIN));
        trainingRunner.getFilesDeep();

        trainingEnd = System.currentTimeMillis();
        trainingTotal = trainingEnd - trainingStart;

        testingStart = System.currentTimeMillis();

        // Run testing
        ImageRunner testingRunner = new ImageRunner("F:\\Hand Dataset\\test\\single",
                                    new ImageProcessor(Operation.TEST));
        testingRunner.getFilesDeep();

        testingEnd = System.currentTimeMillis();
        testingTotal = testingEnd - testingStart;

        logger.log("Training Time: " + TimeFormatter.millisToTime(trainingTotal));
        logger.log("Testing Time: " + TimeFormatter.millisToTime(testingTotal));

        System.out.println("Training Time: " + TimeFormatter.millisToTime(trainingTotal));
        System.out.println("Testing Time: " + TimeFormatter.millisToTime(testingTotal));


    }

}
