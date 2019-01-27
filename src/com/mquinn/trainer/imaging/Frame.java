package com.mquinn.trainer.imaging;

import com.mquinn.trainer.svm.LetterClass;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Size;

import java.util.List;

public class Frame extends Mat implements IFrame {

    // Classified letter result of the input image
    private LetterClass letter;

    // Input camera mat RGBA values
    private Mat rGBA;

    // Skin mask found by threshold contouring
    private List<MatOfPoint> contours;

    // Hierarchy of founf contours
    private Mat hierarchy;

    // Downsampled starting mat
    private Mat downSampledMat;

    // Features found by feature extraction
    private List<MatOfPoint> features;

    // Area of hand with no inner details
    private Mat maskedImage;

    // Inner hand image with details
    private Mat windowMask;

    // Drawn mat of skeleton contours
    private Mat skeleton;

    // List of skeleton contour vectors
    private List<MatOfPoint> skeletonContours;

    // Drawn mat of canny edges
    private Mat cannyEdgeMask;

    // List of canny edge contour vectors
    private List<MatOfPoint> cannyEdges;

    // Original size of the input frame
    private Size originalSize;

    public Frame(Mat inputRGBA) {
        rGBA = inputRGBA;
    }

    public Size getOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(Size inputOriginalSize) {
        originalSize = inputOriginalSize;
    }

    public void setCountours(List<MatOfPoint> inputContours) {
        contours = inputContours;
    }

    public List<MatOfPoint> getContours() {
        return contours;
    }

    public List<MatOfPoint> getCannyEdges() {
        return cannyEdges;
    }

    public void setCannyEdges(List<MatOfPoint> inputCannyEdges) {
        cannyEdges = inputCannyEdges;
    }

    public Mat getCannyEdgeMask() {
        return cannyEdgeMask;
    }

    public void setCannyEdgeMask(Mat inputCannyEdgeMask) {
        cannyEdgeMask = inputCannyEdgeMask;
    }

    public Mat getRGBA() {
        return rGBA;
    }

    public void setRGBA(Mat inputRGBA) {
        rGBA = inputRGBA;
    }

    public Mat getDownSampledMat() {
        return downSampledMat;
    }

    public void setDownSampledMat(Mat inputDownSampledMat) {
        downSampledMat = inputDownSampledMat;
    }

    public List<MatOfPoint> getFeatures() {
        return features;
    }

    public void setFeatures(List<MatOfPoint> inputFeatures) {
        features = inputFeatures;
    }

    public Mat getSkeleton() {
        return skeleton;
    }

    public void setSkeleton(Mat inputSkeleton) {
        skeleton = inputSkeleton;
    }

    public List<MatOfPoint> getSkeletonContours() {
        return skeletonContours;
    }

    public void setSkeletonContours(List<MatOfPoint> inputSkeletonContours) {
        skeletonContours = inputSkeletonContours;
    }

    public Mat getMaskedImage() {
        return maskedImage;
    }

    public Mat getWindowMask() {
        return windowMask;
    }

    public void setWindowMask(Mat inputWindowMask) {
        windowMask = inputWindowMask;
    }

    public void setMaskedImage(Mat inputMaskedImage) {
        maskedImage = inputMaskedImage;
    }

    public LetterClass getLetterClass() {
        return letter;
    }

    public void setLetterClass(LetterClass letter) {
        this.letter = letter;
    }
}
