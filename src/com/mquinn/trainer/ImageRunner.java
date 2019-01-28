package com.mquinn.trainer;

import com.mquinn.trainer.sl_extensions.SvmService;
import com.mquinn.trainer.sl_extensions.SvmTrainingData;
import com.mquinn.trainer.sl_extensions.TrainingFrameProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class ImageRunner {

    File dir;
    ImageProcessor processor;
    TrainingFrameProcessor trainingFrameProcessor;
    SvmTrainingData trainingData;
    SvmService svmService;

    public ImageRunner (String inputPath) {
        dir = new File(inputPath);
        trainingFrameProcessor = new TrainingFrameProcessor();
        processor = new ImageProcessor(trainingFrameProcessor);
        svmService = new SvmService();
    }

    // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
            "gif", "png", "bmp", "jpg" // and other formats you need
    };

    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };

    public void getFiles2(){
        getFilesRecursive(dir);
        if (trainingFrameProcessor.getTrainingData() != null){
            trainingData = trainingFrameProcessor.getTrainingData();
            svmService.finaliseSVM(trainingData);
        }
    }

    public void getFiles() {

        if (dir.isDirectory()) { // make sure it's a directory
            for (final File f : dir.listFiles(IMAGE_FILTER)) {
                BufferedImage img = null;

                try {
                    img = ImageIO.read(f);

                    // actual image processing
                    processor.process(f);

                } catch (final IOException e) {
                    // handle errors here
                }
            }
        }

        if (trainingFrameProcessor.getTrainingData() != null){
            trainingData = trainingFrameProcessor.getTrainingData();
            svmService.finaliseSVM(trainingData);
        }

    }

    private void getFilesRecursive(File curDir) {

        File[] filesList = curDir.listFiles();
        if (filesList != null && filesList.length > 0) {
            for(File f : filesList){
                if(f.isDirectory())
                    getFilesRecursive(f);
                if(f.isFile()){
                    System.out.println(f.getName());
                    BufferedImage img = null;

                    try {
                        img = ImageIO.read(f);

                        // actual image processing
                        processor.process(f);

                    } catch (final IOException e) {
                        // handle errors here
                    }

                    // Do something
                }
            }
        }

    }

}
