package com.mquinn.trainer.sl_extensions;

import org.opencv.core.Mat;

public class SvmTrainingData {

    public Mat samples;
    public Mat labels;

    public SvmTrainingData(){
        samples = new Mat();
        labels = new Mat();
    }

}
