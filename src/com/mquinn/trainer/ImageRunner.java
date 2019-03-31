package com.mquinn.trainer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageRunner {

    private File dir;
    private IImageProcessor processor;

    private ResultLoggerService logger;

    private long imgprocStart, imgprocEnd, imgProcTotal;

    private int imgCounter = 0;

    public ImageRunner (String inputPath, IImageProcessor inputProcessor) {
        dir = new File(inputPath);
        processor = inputProcessor;
        logger = ResultLoggerService.getInstance(false);
    }

    static final String[] EXTENSIONS = new String[]{
            "gif", "png", "bmp", "jpg"
    };

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

        imgprocStart = System.currentTimeMillis();

        getFilesRecursive(dir);

        imgprocEnd = System.currentTimeMillis();
        imgProcTotal= imgprocEnd - imgprocStart;

        logger.log("", true);

        logger.log("Total Image Processing Time: " + TimeFormatter.millisToTime(imgProcTotal), true);
        logger.log("Average Image Processing Time: " + TimeFormatter.millisToTime(imgProcTotal/imgCounter), true);

        logger.log("", true);

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
                    imgCounter++;
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
            Logger.getAnonymousLogger().log(Level.WARNING,"Image processing failed" + e.getMessage());
        }
    }

}
