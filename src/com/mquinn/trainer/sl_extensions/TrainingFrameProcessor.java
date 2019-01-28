package com.mquinn.trainer.sl_extensions;

import mquinn.sign_language.imaging.IFrame;
import mquinn.sign_language.processing.IFrameProcessor;
import mquinn.sign_language.svm.LetterClass;
import org.opencv.core.*;
import org.opencv.ml.SVM;

import java.io.File;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrainingFrameProcessor implements IFrameProcessor {


    IFrame workingFrame;
    MatOfPoint features;
    LetterClass result;
    File workingFile;
    SvmTrainingData trainingData;
    Mat flatFeatures, singleLabel;

    public TrainingFrameProcessor(){

        trainingData = new SvmTrainingData();

        flatFeatures = new Mat();
        flatFeatures.convertTo(flatFeatures, CvType.CV_32F);

        trainingData.samples.convertTo(trainingData.samples,  CvType.CV_32F);

    }

    public IFrame process(IFrame inputFrame) {

        workingFrame = inputFrame;

        if (isEligibleToClassify()) {
            train();
        }

        return workingFrame;

    }

    public void setInputFile(File inputFile){
        workingFile = inputFile;
    }

    public SvmTrainingData getTrainingData(){
        return trainingData;
    }

    private void train(){
        flattenFeatures();

//        result = LetterClass.getLetter((int)response);

        String letter = workingFile.getName().substring(0,1);
        int letterLabel = LetterClass.getIndex(letter);

        singleLabel = new Mat( new Size( 1, 1 ), CvType.CV_32FC1 );
        singleLabel.put(1,1, letterLabel);

        trainingData.labels.push_back(singleLabel);
        trainingData.samples.push_back(flatFeatures);

        flatFeatures.release();

        Logger.getAnonymousLogger().log(Level.INFO, "LETTER CLASS: " + LetterClass.getLetter(letterLabel));

    }

    private boolean isEligibleToClassify() {
        // SVM Prediction
        if (!workingFrame.getFeatures().isEmpty()) {
            Iterator<MatOfPoint> allMatOfPoint = workingFrame.getFeatures().iterator();
            while (allMatOfPoint.hasNext()) {
                MatOfPoint temp = allMatOfPoint.next();
                if (temp.toList().size() == 20){
                    features = temp;
                    return true;
                }
            }
        }
        return false;
    }

    private void flattenFeatures(){

        features.channels();
        flatFeatures = features.reshape(1,1);

//        trainingData.convertTo( trainingData, CvType.CV_32FC1 );
    }

}
