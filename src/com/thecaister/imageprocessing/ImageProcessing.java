package com.thecaister.imageprocessing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessing {
    public static boolean checkStringIsInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException ignored) {
        }
        return false;
    }

    public static String combineStringArray(String[] array) {
        StringBuilder output = new StringBuilder();

        for (String s : array) {
            output.append(s);
        }

        return output.toString();
    }

    public static File findFile(String filename) {
        File file;
        file = new File(filename);

        if (!file.exists()) {
            file = new File("images/" + filename + ".jpg");
        }

        if (!file.exists()) {
            file = new File("images/" + filename + ".png");
        }

        if (!file.exists()) {
            file = new File("images/NoImagePlaceholder.png");
        }

        return file;
    }

    public static void GreyScale(String filename) {
        BufferedImage image = null;
        File file = null;

        try {
            file = findFile(filename);
            image = ImageIO.read(file);
        } catch (IOException e) {
            System.out.println(e);
        }

        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                int avg = (r + g + b) / 3;
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                image.setRGB(x, y, p);
            }
        }
        try {
            String newPathName = file.getName();

            // e.g. house_0.png splits into [0] = "house_0" and [1] = "png"
            String[] splitFromExtension = newPathName.split("\\.");

            // e.g. house_0 splits into [0] = "house" and [1] = "0"
            String[] splitFromVersion = splitFromExtension[0].split("_");

            int splitFromExtensionLastIndex = splitFromExtension.length - 1;
            int splitFromVersionLastIndex = splitFromVersion.length - 1;


            int versionNumber;

            if (checkStringIsInt(splitFromVersion[splitFromVersionLastIndex])) {
                versionNumber = Integer.parseInt(splitFromVersion[splitFromVersionLastIndex]);

            } else {
                versionNumber = 0;

            }

            System.out.println(newPathName);
            StringBuilder fileNameWithoutVersion = new StringBuilder();

            for (int i = 0; i < splitFromVersionLastIndex; i++) {
                fileNameWithoutVersion.append(splitFromVersion[i]);
                fileNameWithoutVersion.append("_");
            }
            fileNameWithoutVersion.append(splitFromVersion[splitFromVersionLastIndex]);

            // Next, we check to see which version this next image will be.
            // If there is a house_0.png already, we should make an image called house_1.png
            file = new File("images/" + fileNameWithoutVersion + "_" + versionNumber + "." + splitFromExtension[splitFromExtensionLastIndex]);


            while (file.exists()) {
                versionNumber++;
                file = new File("images/" + fileNameWithoutVersion + "_" + versionNumber + "." + splitFromExtension[splitFromExtensionLastIndex]);

                System.out.println();
            }

            ImageIO.write(image, "png", file);
            System.out.println("Successfully converted a colored image into a grayscale image");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
