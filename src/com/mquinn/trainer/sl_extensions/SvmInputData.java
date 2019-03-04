package com.mquinn.trainer.sl_extensions;

import org.opencv.core.Mat;

public class SvmInputData {

    public Mat samples;
    public Mat labels;

    public SvmInputData(){
        samples = new Mat();
        labels = new Mat();
    }

}
