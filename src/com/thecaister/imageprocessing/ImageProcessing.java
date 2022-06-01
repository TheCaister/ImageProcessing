package com.thecaister.imageprocessing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessing {
    /**
     * Checks if a String is an Integer.
     * @param input String to be checked.
     * @return True if the input is an Integer and False otherwise.
     */
    public static boolean checkStringIsInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException ignored) {
        }
        return false;
    }

    /**
     * Tries to find and retrieve a File using the provided filename.
     * @param filename Name of the file to search for.
     * @return File handle to the desired file if it is found.
     */
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

    /**
     * Converts an image to greyscale
     * @param filename Name of the image file to apply the greyscale transformation to.
     */
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
                // Returns RGB value as an int. It's split up into 4 chunks of 8 bits
                // since an int is 32 bits.
                int p = image.getRGB(x, y);

                // Extracting the different RGBA values.
                int alpha = (p >> 24) & 0xff;
                int red = (p >> 16) & 0xff;
                int green = (p >> 8) & 0xff;
                int blue = p & 0xff;

                //Getting the average of red, green and blue values.
                int avg = (red + green + blue) / 3;

                // Rebuilding the pixel, replacing the RGB parts with their average value to make it greyscale.
                p = (alpha << 24) | (avg << 16) | (avg << 8) | avg;

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

            ImageIO.write(image, splitFromExtension[splitFromExtensionLastIndex], file);
            System.out.println("Successfully converted a colored image into a grayscale image");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
