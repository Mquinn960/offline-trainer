package com.mquinn.trainer;

import org.opencv.core.Core;

public class Main {

    public static void main(String[] args) {

        long trainingStart, trainingEnd, trainingTotal,
             testingStart, testingEnd, testingTotal;

        String runNumber = "1";
        String kernel = "linear";
        String features = "none";
        String dimreduce = "none";

        String baseFolder = "F:\\Hand Dataset\\Live\\";

        String[] datasets = new String[]{
                "own",
//                "grassnoted",
//                "pugeault",
//                "mon95",
//                "ownbsl"
        };

//        String baseFolder = "F:\\Hand Dataset\\Debug\\";
//
//        String[] datasets = new String[]{
//                "single",
//                "single2",
//                "single3"
//        };

        for (String dataset: datasets) {

            String logTitle = String.format("%s_%s_%s_%s_%s", runNumber, features, dataset, kernel, dimreduce);

            ResultLogger logger = new ResultLogger(logTitle);

            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            trainingStart = System.currentTimeMillis();

            // Run training
            ImageRunner trainingRunner = new ImageRunner(baseFolder + dataset + "\\train",
                    new ImageProcessor(Operation.TRAIN));
            trainingRunner.getFilesDeep();

            trainingEnd = System.currentTimeMillis();
            trainingTotal = trainingEnd - trainingStart;

            testingStart = System.currentTimeMillis();

            // Run testing
            ImageRunner testingRunner = new ImageRunner(baseFolder + dataset + "\\test",
                    new ImageProcessor(Operation.TEST));
            testingRunner.getFilesDeep();

            testingEnd = System.currentTimeMillis();
            testingTotal = testingEnd - testingStart;

            logger.log("Training Time: " + TimeFormatter.millisToTime(trainingTotal));
            logger.log("Testing Time: " + TimeFormatter.millisToTime(testingTotal));
        }

    }

}
