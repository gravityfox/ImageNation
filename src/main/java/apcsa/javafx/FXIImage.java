package apcsa.javafx;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 * Created by Fox on 1/29/2016.
 * Project: SandBox
 */
public interface FXIImage {

    int getWidth(); // get the width of the picture in pixels

    int getHeight(); // get the height of the picture in pixels

    Image getImage(); // get the image from the picture

    WritableImage getWritableImage(); // get the writable image from the picture

    int getBasicPixel(int x, int y); // get the pixel information as an int

    void setBasicPixel(int x, int y, int rgb); // set the pixel information

    FXPixel getPixel(int x, int y); // get the pixel information as an object

    FXPixel[] getPixels(); // get all pixels in row-major order

    FXPixel[][] getPixels2D(); // get 2-D array of pixels in row-major order

    boolean write(String fileName); // write out a file

    FXIImage copy();
}
