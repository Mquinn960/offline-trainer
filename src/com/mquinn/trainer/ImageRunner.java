package com.mquinn.trainer;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class ImageRunner {

    File dir;

    public ImageRunner (String inputPath) {
        dir = new File(inputPath);
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
    }

    public void getFiles() {

        if (dir.isDirectory()) { // make sure it's a directory
            for (final File f : dir.listFiles(IMAGE_FILTER)) {
                BufferedImage img = null;

                try {
                    img = ImageIO.read(f);

                    Mat something = Imgcodecs.imread(f.getAbsolutePath());

                    // you probably want something more involved here
                    // to display in your UI
                    System.out.println("image: " + f.getName());
                    System.out.println(" width : " + img.getWidth());
                    System.out.println(" height: " + img.getHeight());
                    System.out.println(" size  : " + f.length());
                } catch (final IOException e) {
                    // handle errors here
                }
            }
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
                    // Do something
                }
            }
        }
    }

}
