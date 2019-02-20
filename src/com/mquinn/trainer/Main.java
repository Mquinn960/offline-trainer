package com.mquinn.trainer;

import org.opencv.core.Core;

public class Main {

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Recursive file operation
        ImageRunner runner = new ImageRunner("F:\\Hand Dataset\\test\\grass");
        runner.getFiles2();

        // Single dir operation
//        ImageRunner runner = new ImageRunner("F:\\Hand Dataset\\test\\mon95\\a");
//        runner.getFiles();

    }

}
