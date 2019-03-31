package com.mquinn.trainer;

import com.mquinn.trainer.sl_extensions.SvmService;
import mquinn.sign_language.processing.DetectionMethod;
import org.opencv.core.Core;

public class Main {

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        long trainingPhaseStart, trainingEnd, trainingTotal,
             testingPhaseStart, testingEnd, testingTotal;

        EmailNotifier emailNotifier = new EmailNotifier();

        int runNumber = 1;

        final String DATASETS_FOLDER = "F:\\Hand Dataset\\Live\\";
        final String RESULTS_FOLDER = "F:\\Hand Dataset\\Results\\";

        // Feature detection methods
        DetectionMethod[] detectionMethods = new DetectionMethod[]{
            DetectionMethod.CONTOUR_MASK,
            DetectionMethod.CANNY_EDGES,
            DetectionMethod.SKELETON
        };

        // SVM kernel types
        String[] kernels = new String[]{
            "linear",
            "rbf"
        };

        // Methods of dimensionality reduction
        String[] dimReduceType = new String[]{
            "none",
            "pca"
        };

        // List of datasets for processing available in the DATASETS_FOLDER
        String[] datasets = new String[]{
            "own",
            "grassnoted",
            "pugeault",
            "mon95",
            "ownbsl"
        };

        emailNotifier.sendNotification("A new batch training run is commencing.", "Batch run beginning");

        for (DetectionMethod method: detectionMethods) {

            for (String svmKernel: kernels) {

                SvmService.getInstance().setKernelType(svmKernel);

                for (String dimType: dimReduceType) {

                    SvmService.getInstance().setPcaUse(dimType);

                    for (String dataset: datasets)  {

                        SvmService.getInstance().setRunCounter(runNumber);

                        emailNotifier.sendNotification("Run " + runNumber + " has begun." + "\n\n" +
                                        "Features: " + method.name() + "\r\n" +
                                        "Kernel: " + svmKernel + "\r\n" +
                                        "Dimensionality Reduction: " + dimType + "\r\n" +
                                        "Dataset: " + dataset + "\r\n",
                                "Run " + runNumber + " commencing");

                        String logTitle = String.format("%s_%s_%s_%s_%s", runNumber, method.name(), svmKernel, dimType, dataset);
                        String logFile = RESULTS_FOLDER + logTitle;

                        ResultLoggerService logger = ResultLoggerService.getInstance(false);
                        logger.setLogFile(logFile);

                        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

                        logger.log("RUN " + runNumber, true);
                        logger.log("Features: " + method.name(), true);
                        logger.log("SVM Kernel: " + svmKernel, true);
                        logger.log("Dimensionality Reduction: " + dimType, true);
                        logger.log("Dataset: " + dataset, true);
                        logger.log("___________________________________________________________", true);
                        logger.log("", true);

                        trainingPhaseStart = System.currentTimeMillis();

                        // Run training
                        ImageRunner trainingRunner = new ImageRunner(DATASETS_FOLDER + dataset + "\\train",
                                new ImageProcessor(Operation.TRAIN, method));
                        trainingRunner.getFilesDeep();

                        trainingEnd = System.currentTimeMillis();
                        trainingTotal = trainingEnd - trainingPhaseStart;

                        testingPhaseStart = System.currentTimeMillis();

                        // Run testing
                        ImageRunner testingRunner = new ImageRunner(DATASETS_FOLDER + dataset + "\\test",
                                new ImageProcessor(Operation.TEST, method));
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

        }

        emailNotifier.sendNotification("The current batch run has completed", "Batch run complete");

    }

}
