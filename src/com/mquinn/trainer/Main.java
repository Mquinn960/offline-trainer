package com.mquinn.trainer;

import com.mquinn.trainer.sl_extensions.SvmService;
import org.opencv.core.Core;

public class Main {

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        long trainingPhaseStart, trainingEnd, trainingTotal,
             testingPhaseStart, testingEnd, testingTotal;

        EmailNotifier emailNotifier = new EmailNotifier();

        int runNumber = 1;
        String features = "canny";

        String baseFolder = "F:\\Hand Dataset\\Live\\";
        String resultsFolder = "F:\\Hand Dataset\\Results\\";

        String[] kernels = new String[]{
//                "linear",
                "rbf"
        };

        String[] dimReduceType = new String[]{
                "none",
//                "pca"
        };

//        String[] datasets = new String[]{
//                "own",
//                "grassnoted",
//                "pugeault",
//                "mon95",
//                "ownbsl"
//        };

        baseFolder = "F:\\Hand Dataset\\Debug\\";

        String[] datasets = new String[]{
                "own_neg",
//                "single2",
//                "single3",
//                "single4"
        };

        emailNotifier.sendNotification("A new batch training run is commencing.", "Batch run beginning");

        for (String svmKernel: kernels) {

            SvmService.getInstance().setKernelType(svmKernel);

            for (String dimType: dimReduceType) {

                SvmService.getInstance().setPcaUse(dimType);

                for (String dataset: datasets)  {

                    SvmService.getInstance().setRunCounter(runNumber);

                    emailNotifier.sendNotification("Run " + runNumber + " has begun." + "\n\n" +
                                    "Features: " + features + "\r\n" +
                                    "Kernel: " + svmKernel + "\r\n" +
                                    "Dimensionality Reduction: " + dimType + "\r\n" +
                                    "Dataset: " + dataset + "\r\n",
                            "Run " + runNumber + " commencing");

                    String logTitle = String.format("%s_%s_%s_%s_%s", runNumber, features, svmKernel, dimType, dataset);
                    String logFile = resultsFolder + logTitle;

                    ResultLoggerService logger = ResultLoggerService.getInstance(false);
                    logger.setLogFile(logFile);

                    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

                    logger.log("RUN " + runNumber, true);
                    logger.log("Features: " + features, true);
                    logger.log("SVM Kernel: " + svmKernel, true);
                    logger.log("Dimensionality Reduction: " + dimType, true);
                    logger.log("Dataset: " + dataset, true);
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

                    emailNotifier.sendNotification("Run " + runNumber + " completed in " + TimeFormatter.millisToTime(testingTotal + trainingTotal),
                            "Run " + runNumber + " completed");

                    runNumber ++;

                    logger.destroy();

                }

                SvmService.getInstance().destroy();

            }

            SvmService.getInstance().destroy();

        }

        emailNotifier.sendNotification("The current batch run has completed", "Batch run complete");

    }

}
