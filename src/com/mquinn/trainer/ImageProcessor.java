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

    private DetectionMethod detectionMethod;
    private Operation operation;

    private int imgCounter = 0;

    private ResultLoggerService logger;

    public ImageProcessor(Operation inputOperation, DetectionMethod method) {
        operation = inputOperation;
        setProcessors(method);
        svmService = SvmService.getInstance();
        logger = ResultLoggerService.getInstance(false);
    }

    public void process(File inputFile){

        preProcessedFrame = preProcessor.process(inputFile);

        processedFrame = mainFrameProcessor.process(preProcessedFrame);

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
                    svmService.finaliseSVMTraining(trainingData, true);
                break;
            case TEST:
                    trainingData = svmPrepper.getTrainingData();
                    SvmTestRunner svmTestRunner = new SvmTestRunner(trainingData);
                    svmTestRunner.runTests();
                break;
            default:
                break;
        }

    }

    private void setProcessors(DetectionMethod method){

        detectionMethod = method;

        preProcessor = new StaticFramePreProcessor(new ResizingFrameProcessor(SizeOperation.UP), new DownSamplingFrameProcessor());

        mainFrameProcessor = new MainFrameProcessor(detectionMethod);

        svmPrepper = new SvmPrepFrameProcessor();

    }

}
