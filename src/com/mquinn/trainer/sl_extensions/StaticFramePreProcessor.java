package com.mquinn.trainer.sl_extensions;

import mquinn.sign_language.imaging.Frame;
import mquinn.sign_language.imaging.IFrame;
import mquinn.sign_language.processing.IFrameProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_UNCHANGED;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class StaticFramePreProcessor implements IFrameProcessor {

    private IFrameProcessor downSampler, frameAdapter;
    private IFrame outputFrame;
    private IFrame inputFrame;

    public StaticFramePreProcessor (IFrameProcessor inputFrameAdapter,
                                    IFrameProcessor downSamplingFrameProcessor) {
        frameAdapter = inputFrameAdapter;
        downSampler = downSamplingFrameProcessor;
    }

    public IFrame process(IFrame inputFrame) {

        outputFrame = frameAdapter.process(inputFrame);

        return outputFrame;

    }

    public IFrame process(File inputFile) {

        Mat image = Imgcodecs.imread(inputFile.getAbsolutePath(), IMREAD_UNCHANGED);

        cvtColor(image, image, Imgproc.COLOR_BGR2RGBA);

        inputFrame = new Frame(image);

        outputFrame = frameAdapter.process(inputFrame);

        outputFrame.setOriginalSize(image.size());

        outputFrame = downSampler.process(outputFrame);

        return outputFrame;

    }

}
