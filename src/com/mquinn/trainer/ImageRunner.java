package com.mquinn.trainer;

import com.mquinn.trainer.sl_extensions.PcaData;

import java.io.File;
import java.io.FilenameFilter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageRunner {

    private File dir;
    private IImageProcessor processor;

    public ImageRunner (String inputPath, IImageProcessor inputProcessor) {
        dir = new File(inputPath);
        processor = inputProcessor;
    }

    // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
            "gif", "png", "bmp", "jpg" // and other formats you need
    };

    // File
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

    // Full recursive method for file processing in and below input dir
    public void getFilesDeep(){
        getFilesRecursive(dir);
        // PCA Stuff
        processor.finalise();
    }

    // Get files in current folder and process
    public void getFiles() {
        if (dir.isDirectory()) {
            if (dir != null && dir.listFiles().length > 0){
                for (final File file : dir.listFiles(IMAGE_FILTER)) {
                    processFile(file);
                }
            }
        }

        processor.finalise();
    }

    // Recursively process files in all nested directories
    private void getFilesRecursive(File curDir) {
        File[] filesList = curDir.listFiles();
        if (filesList != null && filesList.length > 0) {
            for(File file : filesList){
                if(file.isDirectory())
                    getFilesRecursive(file);
                if(file.isFile()){
                    processFile(file);
                }
            }
        }

    }

    // Perform actual file processing
    private void processFile(File file){
        System.out.println("Processing: " + file.getName());
        try {
            processor.process(file);
        } catch (final Exception e) {
            Logger.getAnonymousLogger().log(Level.WARNING,"File read failed");
        }
    }

}
