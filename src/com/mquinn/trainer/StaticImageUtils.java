package com.mquinn.trainer;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class StaticImageUtils {

    public static void showResult(Mat img, boolean BGR, boolean resize) {

        // This is a static debug class for examining image matrices "in-flight"
        // Example usage (can also be called form immediate evaluation window):
        // StaticImageUtils.showResult(workingFrame.getRGBA(), true, false);

        if (resize) {
            // Optionally resize input image
            Imgproc.resize(img, img, new Size(640, 480));
        }

        if (BGR){
            // Convert any RGB images back to BGR before display to display normally
            cvtColor(img, img, Imgproc.COLOR_RGB2BGR);
        }

        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
