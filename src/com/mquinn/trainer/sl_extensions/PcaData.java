package com.mquinn.trainer.sl_extensions;

import org.opencv.core.Mat;

public class PcaData {

    public Mat mean;
    public Mat eigen;

    public PcaData(){
        mean = new Mat();
        eigen = new Mat();
    }

}
