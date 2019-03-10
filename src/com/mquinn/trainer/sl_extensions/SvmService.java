package com.mquinn.trainer.sl_extensions;

import com.mquinn.trainer.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.SVM;

import static org.opencv.core.CvType.CV_32FC1;
import static org.opencv.core.CvType.CV_32SC1;
import static org.opencv.ml.Ml.ROW_SAMPLE;

public class SvmService {

    private SVM svm;
    private SvmInputData trainingData, testingData;

    private PcaData pcaData = new PcaData();

    private final String TRAINED_PATH = "trained.xml";

    private int runCounter = 1;

    private Mat projectVec = new Mat();

    private ResultLoggerService logger;

    private long pcaTotal, pcaEnd, pcaStart,
                 trainingTotal, trainingEnd, trainingStart;

    private static SvmService instance = null;

    protected SvmService(){

        svm = SVM.create();

        svm.setType(SVM.C_SVC);
        svm.setKernel(SVM.LINEAR);
        svm.setTermCriteria(new TermCriteria(TermCriteria.MAX_ITER, 100, 1e-6));

        logger = ResultLoggerService.getInstance(false);

    }

    public static SvmService getInstance(){
        if (instance == null){
            instance = new SvmService();
        }
        return instance;
    }

    public void finaliseSVMTraining(SvmInputData inputTrainingData) {

        trainingData = inputTrainingData;

        trainingData.labels.convertTo(trainingData.labels, CV_32SC1);
        trainingData.samples.convertTo(trainingData.samples, CV_32FC1);

        Core.normalize(trainingData.samples, trainingData.samples, 1, 0, Core.NORM_MINMAX);

//        pcaStart = System.currentTimeMillis();
//
//        pcaReduce();
//
//        pcaEnd = System.currentTimeMillis();
//        pcaTotal = pcaEnd - pcaStart;

        trainingStart = System.currentTimeMillis();

//        svm.train(trainingData.samples, ROW_SAMPLE, trainingData.labels);
        svm.trainAuto(trainingData.samples, ROW_SAMPLE, trainingData.labels);

        trainingEnd = System.currentTimeMillis();
        trainingTotal = trainingEnd - trainingStart;

        logger.log("Actual SVM Training Time: " + TimeFormatter.millisToTime(trainingTotal), true);
        logger.log("Actual PCA Time: " + TimeFormatter.millisToTime(pcaTotal), true);

        logger.log("", true);

        svm.save(runCounter + TRAINED_PATH);
        runCounter += 4;

    }

    public SVM getTrainedSVM(){
        return SVM.load(TRAINED_PATH);
    }

    public SVM getInMemorySVM(){
        return svm;
    }

    public PcaData getPcaData() {
        return pcaData;
    }

    private void pcaReduce () {

        Mat test = new Mat();
        test.convertTo(test, CV_32FC1);

        Mat mean = new Mat();
        mean.convertTo(mean, CV_32FC1);

        Mat vectors = new Mat();
        vectors.convertTo(vectors, CV_32FC1);

        Mat values = new Mat();
        values.convertTo(values, CV_32FC1);

        test = trainingData.samples;

        Core.PCACompute2(test, mean, vectors, values, 0.95);
//        Core.PCACompute2(test, mean, vectors, values, vectors.cols());

        projectVec.convertTo(projectVec, CV_32FC1);

        Core.PCAProject(test, mean, vectors, projectVec);

        pcaData.mean = mean;
        pcaData.eigen = vectors;

        trainingData.samples = projectVec;

    }

}
