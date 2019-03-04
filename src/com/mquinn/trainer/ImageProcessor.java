package com.mquinn.trainer;

import com.mquinn.trainer.sl_extensions.StaticFramePreProcessor;
import com.mquinn.trainer.sl_extensions.SvmService;
import com.mquinn.trainer.sl_extensions.SvmInputData;
import com.mquinn.trainer.sl_extensions.SvmPrepFrameProcessor;
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

    public ImageProcessor(Operation inputOperation) {
        operation = inputOperation;
        setProcessors(DetectionMethod.CANNY_EDGES);
        svmService = new SvmService();
    }

    public void process(File inputFile){

        // Generate Frame from input frame and downsample
        preProcessedFrame = preProcessor.process(inputFile);

        // Generate useful information from frame
        processedFrame = mainFrameProcessor.process(preProcessedFrame);

        // Feed into training data
        svmPrepper.setInputFile(inputFile);
        svmPrepper.process(processedFrame);

    }

    public void finalise(){

        switch (operation){
            case TRAIN:
                if (svmPrepper.getTrainingData() != null){
                    trainingData = svmPrepper.getTrainingData();
                    svmService.finaliseSVMTraining(trainingData);
                }
                break;
            case TEST:
                if (svmPrepper.getTrainingData() != null){
                    trainingData = svmPrepper.getTrainingData();
                    SvmTestRunner svmTestRunner = new SvmTestRunner(svmService.getTrainedSVM(), trainingData);
                    svmTestRunner.runTests();
                }
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
        preProcessor = new StaticFramePreProcessor(new ResizingFrameProcessor(SizeOperation.DOWN));

        // Frame Processors
        mainFrameProcessor = new MainFrameProcessor(detectionMethod);

        // Svm data housing class
        svmPrepper = new SvmPrepFrameProcessor();

    }

}
