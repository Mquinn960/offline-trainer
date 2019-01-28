package com.mquinn.trainer.sl_extensions;

import mquinn.sign_language.imaging.IFrame;
import mquinn.sign_language.processing.IFrameProcessor;

public class StaticFrameAdapter {

    private IFrameProcessor resizer;
    private IFrame outputFrame;

    public StaticFrameAdapter(IFrameProcessor resizingFrameProcessor) {
        resizer = resizingFrameProcessor;
    }

    public IFrame process(IFrame inputFrame) {

        // Resize image
        outputFrame = resizer.process(outputFrame);

        return outputFrame;

    }

}
