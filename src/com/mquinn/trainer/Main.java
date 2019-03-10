package com.mquinn.trainer;

import org.opencv.core.Core;

public class Main {

    public static void main(String[] args) {

        long trainingPhaseStart, trainingEnd, trainingTotal,
             testingPhaseStart, testingEnd, testingTotal;

        int runNumber = 21;
        String kernel = "linear";
        String features = "canny";
        String dimreduce = "none";

        String baseFolder = "F:\\Hand Dataset\\Live\\";
        String resultsFolder = "F:\\Hand Dataset\\Results\\";
        String logExtension = ".txt";

        String[] datasets = new String[]{
                "own",
                "grassnoted",
                "pugeault",
                "mon95",
                "ownbsl"
        };

//        baseFolder = "F:\\Hand Dataset\\Debug\\";
//
//        String[] datasets = new String[]{
//                "single",
//                "single2",
//                "single3"
//        };

        for (String dataset: datasets) {

            String logTitle = String.format("%s_%s_%s_%s_%s", runNumber, features, dataset, kernel, dimreduce);
            String logFile = resultsFolder + logTitle + logExtension;

            runNumber += 4;

            ResultLoggerService logger = ResultLoggerService.getInstance(false);
            logger.setLogFile(logFile);

            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            logger.log("RUN " + runNumber, true);
            logger.log("Features: " + features, true);
            logger.log("Dataset: " + dataset, true);
            logger.log("SVM Kernel: " + kernel, true);
            logger.log("Dimensionality Reduction: " + dimreduce, true);
            logger.log("___________________________________________________________", true);
            logger.log("", true);

            trainingPhaseStart = System.currentTimeMillis();

            // Run training
            ImageRunner trainingRunner = new ImageRunner(baseFolder + dataset + "\\train",
                    new ImageProcessor(Operation.TRAIN));
            trainingRunner.getFilesDeep();

            trainingEnd = System.currentTimeMillis();
            trainingTotal = trainingEnd - trainingPhaseStart;

            testingPhaseStart = System.currentTimeMillis();

            // Run testing
            ImageRunner testingRunner = new ImageRunner(baseFolder + dataset + "\\test",
                    new ImageProcessor(Operation.TEST));
            testingRunner.getFilesDeep();

            testingEnd = System.currentTimeMillis();
            testingTotal = testingEnd - testingPhaseStart;

            logger.log("", true);
            logger.log("Training Phase Time: " + TimeFormatter.millisToTime(trainingTotal), true);
            logger.log("Testing Phase Time: " + TimeFormatter.millisToTime(testingTotal),true);
            logger.log("Total Run Time: " + TimeFormatter.millisToTime(testingTotal + trainingTotal),true);

            logger.destroy();
        }

    }

}
