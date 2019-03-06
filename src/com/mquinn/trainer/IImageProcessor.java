package com.mquinn.trainer;

import com.mquinn.trainer.sl_extensions.PcaData;

import java.io.File;

public interface IImageProcessor {
    void process(File inputFile);
    void finalise();
}
