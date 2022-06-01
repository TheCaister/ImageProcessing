package com.thecaister.imageprocessing;

import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageProcessing {
    public static void GreyScale(String filename){
        BufferedImage image = null;
        File file = null;

        try
        {
            file = new File(filename);

            if(!file.exists()){
                file = new File("images/" + filename + ".jpg");
            }

            if(!file.exists()){
                file = new File("images/" + filename + ".png");
            }

            if(!file.exists()){
                file = new File("images/Among_Us_cover_art.jpg");
            }

            image = ImageIO.read(file);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }

        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int p = image.getRGB(x,y);
                int a = (p>>24) & 0xff;
                int r = (p>>16) & 0xff;
                int g = (p>>8) & 0xff;
                int b = p & 0xff;
                int avg = (r+g+b)/3;
                p = (a<<24) | (avg<<16) | (avg<<8) | avg;
                image.setRGB(x, y, p);
            }
        }
        try
        {
            String newPathName = file.getName();

            // e.g. house_0.png splits into [0] = "house_0" and [1] = "png"
            String[] splitFromExtension = newPathName.split("\\.");

            // e.g. house_0 splits into [0] = "house" and [1] = "0"
            String[] splitFromVersion = splitFromExtension[0].split("_");

            int versionNumber;

            if(splitFromVersion.length == 1){
                versionNumber = 0;
            }
            else{
                versionNumber = Integer.parseInt(splitFromVersion[1]);
            }

            System.out.println(newPathName);

            // Next, we check to see which version this next image will be.
            // If there is a house_0.png already, we should make an image called house_1.png
            file = new File("images/" + splitFromVersion[0] + "_" + versionNumber + "." + splitFromExtension[1]);

            while(file.exists()){
                versionNumber++;
                file = new File("images/" + splitFromVersion[0] + "_" + versionNumber + "." + splitFromExtension[1]);
                System.out.println();
            }


            //System.out.println(parts[0] + parts[1]);
            //file = new File("images/amongus.png");
            //file = new File("images/amongus.png");
            ImageIO.write(image, "png", file);
            System.out.println("Successfully converted a colored image into a grayscale image");
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }
}
