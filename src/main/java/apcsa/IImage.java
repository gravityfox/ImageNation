package apcsa;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Fox on 1/29/2016.
 * Project: SandBox
 */
public interface IImage {

    int getWidth(); // get the width of the picture in pixels

    int getHeight(); // get the height of the picture in pixels

    Image getImage(); // get the image from the picture

    BufferedImage getBufferedImage(); // get the buffered image

    int getBasicPixel(int x, int y); // get the pixel information as an int

    void setBasicPixel(int x, int y, int rgb); // set the pixel information

    Pixel getPixel(int x, int y); // get the pixel information as an object

    Pixel[] getPixels(); // get all pixels in row-major order

    Pixel[][] getPixels2D(); // get 2-D array of pixels in row-major order

    boolean write(String fileName); // write out a file

    IImage copy();
}
