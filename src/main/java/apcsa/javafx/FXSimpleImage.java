package apcsa.javafx;

import apcsa.CustomImage;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * Created by Fox on 1/31/2016.
 * Project: SandBox
 */
public class FXSimpleImage implements FXIImage {

    private WritableImage image;
    private PixelReader reader;
    private PixelWriter writer;
    private FXPixel[][] pixels;

    public FXSimpleImage() {
        image = null;
        pixels = null;
        reader = null;
        writer = null;
    }

    public static WritableImage copyWritableImage(Image wi) {
        return new WritableImage(wi.getPixelReader(), (int) wi.getWidth(), (int) wi.getHeight());
    }

    @Override
    public int getWidth() {
        return (int) image.getWidth();
    }

    @Override
    public int getHeight() {
        return (int) image.getHeight();
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public WritableImage getWritableImage() {
        return image;
    }

    public void setWritableImage(WritableImage image) {
        this.image = image;
        this.reader = image.getPixelReader();
        this.writer = image.getPixelWriter();
        int width = getWidth();
        int height = getHeight();
        pixels = new FXPixel[width][height];

        // loop through height rows from top to bottom
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                pixels[x][y] = new FXPixel(this, x, y);
    }

    @Override
    public int getBasicPixel(int x, int y) {
        return reader.getArgb(x, y);
    }

    @Override
    public void setBasicPixel(int x, int y, int rgb) {
        writer.setArgb(x, y, rgb);
    }

    @Override
    public FXPixel getPixel(int x, int y) {
        return pixels[x][y];
    }

    @Override
    public FXPixel[] getPixels() {
        int width = getWidth();
        int height = getHeight();
        FXPixel[] pixelArray = new FXPixel[width * height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                pixelArray[y * width + x] = pixels[x][y];

        return pixelArray;
    }

    public void setPixels(FXPixel[][] pixels) {
        this.pixels = pixels;
    }

    @Override
    public FXPixel[][] getPixels2D() {
        return pixels;
    }

    @Override
    public boolean write(String fileName) {
        return false;
    }

    @Override
    public FXIImage copy() {
        FXCustomImage pic = new FXCustomImage();
        pic.setWritableImage(copyWritableImage(this.image));
        int width = getWidth();
        int height = getHeight();
        FXPixel[][] picPixels = new FXPixel[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                picPixels[x][y] = new FXPixel(pic, this.pixels[x][y]);
        return pic;
    }
}
