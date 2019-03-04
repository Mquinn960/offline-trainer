package com.mquinn.trainer;

import org.opencv.core.Core;

public class Main {

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Run training
        ImageRunner trainingRunner = new ImageRunner("F:\\Hand Dataset\\train\\single",
                                     new ImageProcessor(Operation.TRAIN));
        trainingRunner.getFilesDeep();

        // Run testing
        ImageRunner testingRunner = new ImageRunner("F:\\Hand Dataset\\test\\single",
                                    new ImageProcessor(Operation.TEST));
        testingRunner.getFilesDeep();

        // Single dir operation
        testingRunner = new ImageRunner("F:\\Hand Dataset\\test\\single",
                new ImageProcessor(Operation.TEST));

    }

}
