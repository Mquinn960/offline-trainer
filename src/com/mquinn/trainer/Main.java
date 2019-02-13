package com.mquinn.trainer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Recursive file operation
        ImageRunner runner = new ImageRunner("D:\\Hand Dataset\\test\\mon95");
        runner.getFiles2();

        // Single dir operation
//        ImageRunner runner = new ImageRunner("D:\\Hand Dataset\\test\\mon95\\a");
//        runner.getFiles();

    }

}
