package com.mquinn.trainer.sl_extensions;

import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.SVM;
import sun.rmi.runtime.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.opencv.core.CvType.*;
import static org.opencv.ml.Ml.ROW_SAMPLE;

public class SvmService {

    SVM svm;
    SvmTrainingData trainingData;

    public SvmService(){

        svm = SVM.create();

        svm.setType(SVM.C_SVC);
        svm.setKernel(SVM.LINEAR);
        svm.setTermCriteria(new TermCriteria(TermCriteria.MAX_ITER, 100, 1e-6));

    }

    public void finaliseSVM(SvmTrainingData inputTrainingData) {

        trainingData = inputTrainingData;

        trainingData.labels.convertTo(trainingData.labels, CV_32SC1);
        trainingData.samples.convertTo(trainingData.samples, CV_32FC1);

//        String labels = trainingData.labels.dump();
//        Logger.getAnonymousLogger().log(Level.INFO, labels);
//
//        String samples = trainingData.samples.dump();
//        Logger.getAnonymousLogger().log(Level.INFO, samples);

//        svm.train(trainingData.samples, ROW_SAMPLE, trainingData.labels);
        svm.trainAuto(trainingData.samples, ROW_SAMPLE, trainingData.labels);

        svm.save("trained.xml");

    }

}
