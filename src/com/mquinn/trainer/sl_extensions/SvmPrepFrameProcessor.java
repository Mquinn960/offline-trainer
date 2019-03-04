package com.mquinn.trainer.sl_extensions;

import mquinn.sign_language.imaging.IFrame;
import mquinn.sign_language.processing.IFrameProcessor;
import mquinn.sign_language.svm.LetterClass;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Size;

import java.io.File;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SvmPrepFrameProcessor implements IFrameProcessor {


    private IFrame workingFrame;
    private MatOfPoint features;
    private File workingFile;
    private SvmInputData trainingData;
    private Mat flatFeatures, singleLabel;
    private int letterLabel;

    public SvmPrepFrameProcessor(){

        trainingData = new SvmInputData();

        flatFeatures = new Mat();
        flatFeatures.convertTo(flatFeatures, CvType.CV_32FC1);

        trainingData.samples.convertTo(trainingData.samples,  CvType.CV_32FC1);

    }

    public IFrame process(IFrame inputFrame) {

        workingFrame = inputFrame;

//        if (isEligibleToClassify()) {
            compileData();
//        }

        return workingFrame;

    }

    public void setInputFile(File inputFile){
        workingFile = inputFile;
    }

    public SvmInputData getTrainingData(){
        return trainingData;
    }

    private void compileData(){
        flattenFeatures();

        flatFeatures.convertTo(flatFeatures, CvType.CV_32FC1);

        String letter = workingFile.getName().substring(0,1);

        if (letter.equals("_")){
            letterLabel = LetterClass.getIndex("NONE");
        } else if (letter.matches("[A-Z]+")){
            letterLabel = LetterClass.getIndex(letter);
        } else {
            letterLabel = LetterClass.getIndex("ERROR");
        }

        singleLabel = new Mat( new Size( 1, 1 ), CvType.CV_32SC1 );
        singleLabel.put(0,0, (int)letterLabel);

        trainingData.labels.push_back(singleLabel);
        trainingData.samples.push_back(flatFeatures);

        flatFeatures.release();
        singleLabel.release();

        Logger.getAnonymousLogger().log(Level.INFO, "LETTER CLASS: " + LetterClass.getLetter(letterLabel));

    }

    private boolean isEligibleToClassify() {
//        if (!workingFrame.getFeatures().isEmpty()) {
//            Iterator<MatOfPoint> allMatOfPoint = workingFrame.getFeatures().iterator();
//            while (allMatOfPoint.hasNext()) {
//                MatOfPoint temp = allMatOfPoint.next();
//                if (temp.toList().size() == 15){
//                    features = temp;
//                    return true;
//                }
//            }
//        }
//        return false;


        if (!workingFrame.getFeatures().isEmpty()) {
            Iterator<MatOfPoint> allMatOfPoint = workingFrame.getFeatures().iterator();
            while (allMatOfPoint.hasNext()) {
                features = allMatOfPoint.next();
                return true;
            }
        }
        return false;

    }

    private void flattenFeatures(){

//        flatFeatures = features.reshape(1,1);


//        flatFeatures = workingFrame.getHuMomentFeat();
//        flatFeatures = flatFeatures.reshape(1,1);


        flatFeatures = workingFrame.getHogDesc();
        flatFeatures = flatFeatures.reshape(1,1);

    }

}
