package com.mycompany.sportsmanagement.common;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BarcodeScanner {

    public static String scanBarcode() {
        Webcam webcam = Webcam.getDefault();
        if (webcam == null) {
            System.out.println("No webcam detected.");
            return null;
        }

        webcam.setViewSize(new java.awt.Dimension(640, 480));


        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setMirrored(true);

        JFrame window = new JFrame("📷 Scanning... Show Barcode to Camera");
        window.add(panel);
        window.setResizable(false);
        window.pack();
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setVisible(true);

        String resultText = null;

        try {
            webcam.open();
            long start = System.currentTimeMillis();

            while ((System.currentTimeMillis() - start) < 10000 && resultText == null) {
                BufferedImage image = webcam.getImage();
                if (image == null) continue;

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    Result result = new MultiFormatReader().decode(bitmap);
                    resultText = result.getText();
                } catch (NotFoundException ignored) {
                    // No barcode in this frame
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            webcam.close();
            window.dispose();
        }

        return resultText;
    }
}
