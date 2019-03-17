package com.mquinn.trainer;

import com.mquinn.trainer.sl_extensions.*;
import mquinn.sign_language.imaging.IFrame;
import mquinn.sign_language.processing.*;

import java.io.File;

public class ImageProcessor implements IImageProcessor {

    private IFrame preProcessedFrame, processedFrame;
    private IFrameProcessor mainFrameProcessor;
    private SvmPrepFrameProcessor svmPrepper;
    private StaticFramePreProcessor preProcessor;
    private SvmService svmService;

    private SvmInputData trainingData = new SvmInputData();

    private StaticImageUtils imgUtils = new StaticImageUtils();
    private DetectionMethod detectionMethod;
    private Operation operation;

    private int imgCounter = 0;

    private ResultLoggerService logger;

    private PcaData pcaData = new PcaData();

    public ImageProcessor(Operation inputOperation) {
        operation = inputOperation;
        setProcessors(DetectionMethod.CONTOUR_MASK);
        svmService = SvmService.getInstance();
        logger = ResultLoggerService.getInstance(false);
    }

    public void process(File inputFile){

        // Generate Frame from input frame and downsample
        preProcessedFrame = preProcessor.process(inputFile);

        // Generate useful information from frame
        processedFrame = mainFrameProcessor.process(preProcessedFrame);

        // Feed into training data
        svmPrepper.setInputFile(inputFile);
        svmPrepper.process(processedFrame);

        imgCounter++;

        String operationTag = (operation == Operation.TRAIN) ? "Train": "Test";

        logger.log(operationTag + " Sample " + imgCounter + ": " + inputFile.getName(), true);

    }

    public void finalise(){

        switch (operation){
            case TRAIN:
                    trainingData = svmPrepper.getTrainingData();
                    svmService.finaliseSVMTraining(trainingData);
                break;
            case TEST:
                    trainingData = svmPrepper.getTrainingData();
                    SvmTestRunner svmTestRunner = new SvmTestRunner(trainingData);
                    svmTestRunner.runTests();
                break;
            default:
                // do nothing
                break;
        }

    }

    private void setProcessors(DetectionMethod method){
        // Set detection method
        detectionMethod = method;

        // Pre processors
        preProcessor = new StaticFramePreProcessor(new ResizingFrameProcessor(SizeOperation.UP), new DownSamplingFrameProcessor());

        // Frame Processors
        mainFrameProcessor = new MainFrameProcessor(detectionMethod);

        // Svm data housing class
        svmPrepper = new SvmPrepFrameProcessor();

    }

}
