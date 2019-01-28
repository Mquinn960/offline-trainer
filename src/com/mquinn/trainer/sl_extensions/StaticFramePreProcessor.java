package com.mquinn.trainer.sl_extensions;

import mquinn.sign_language.imaging.Frame;
import mquinn.sign_language.imaging.IFrame;
import mquinn.sign_language.processing.IFrameProcessor;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;

public class StaticFramePreProcessor implements IFrameProcessor {

    private IFrameProcessor frameAdapter;
    private IFrame outputFrame;
    private IFrame inputFrame;

    public StaticFramePreProcessor (IFrameProcessor inputFrameAdapter) {
        frameAdapter = inputFrameAdapter;
    }

    public IFrame process(IFrame inputFrame) {

        outputFrame = frameAdapter.process(inputFrame);

        return outputFrame;

    }

    public IFrame process(File inputFile) {

        Mat image = Imgcodecs.imread(inputFile.getAbsolutePath());

        inputFrame = new Frame(image);

        inputFrame.setOriginalSize(image.size());

        outputFrame = frameAdapter.process(inputFrame);

        return outputFrame;

    }

}
