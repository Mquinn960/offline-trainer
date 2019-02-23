package com.mquinn.trainer.sl_extensions;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import static org.opencv.core.CvType.CV_32SC1;


public class SvmTrainingData {

    public Mat samples;
    public Mat labels;

    public SvmTrainingData(){
        samples = new Mat();
        labels = new Mat();
    }

}
