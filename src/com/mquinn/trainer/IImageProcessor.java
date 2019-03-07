package com.mquinn.trainer;

import java.io.File;

public interface IImageProcessor {
    void process(File inputFile);
    void finalise();
}
