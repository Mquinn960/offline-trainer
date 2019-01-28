package com.mquinn.trainer;

import com.mquinn.trainer.sl_extensions.StaticFramePreProcessor;
import com.mquinn.trainer.sl_extensions.TrainingFrameProcessor;
import mquinn.sign_language.imaging.IFrame;
import mquinn.sign_language.processing.*;

import java.io.File;

public class ImageProcessor {

    private IFrame preProcessedFrame, processedFrame, postProcessedFrame, classifiedFrame;

    private IFrameProcessor mainFrameProcessor;

    private TrainingFrameProcessor frameTrainer;

    private StaticFramePreProcessor preProcessor;

    private DetectionMethod detectionMethod;

    public ImageProcessor(TrainingFrameProcessor inputFrameTrainer) {
        frameTrainer = inputFrameTrainer;
        setProcessors(DetectionMethod.CANNY_EDGES);
    }

    public void process(File inputFile){

        // Generate Frame from input frame and downsample
        preProcessedFrame = preProcessor.process(inputFile);

        // Generate useful information from frame
        processedFrame = mainFrameProcessor.process(preProcessedFrame);

        // Feed into training data
        frameTrainer.setInputFile(inputFile);
        frameTrainer.process(processedFrame);

    }

    private void setProcessors(DetectionMethod method){
        // Set detection method
        detectionMethod = method;

        // Pre processors
        preProcessor = new StaticFramePreProcessor(new ResizingFrameProcessor(SizeOperation.DOWN));

        // Frame Processors
        mainFrameProcessor = new MainFrameProcessor(detectionMethod);

    }

}
