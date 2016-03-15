package apcsa;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Created by Fox on 1/31/2016.
 * Project: SandBox
 */
public class SimpleImage implements IImage {

    private BufferedImage image;
    private Pixel[][] pixels;

    public SimpleImage() {
        image = null;
        pixels = null;
    }

    public static BufferedImage copyBufferedImmage(BufferedImage bi) {
        ColorModel colorModel = bi.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public BufferedImage getBufferedImage() {
        return image;
    }

    public void setBufferedImage(BufferedImage image) {
        this.image = image;
        int width = getWidth();
        int height = getHeight();
        pixels = new Pixel[width][height];

        // loop through height rows from top to bottom
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                pixels[x][y] = new Pixel(this, x, y);
    }

    @Override
    public int getBasicPixel(int x, int y) {
        return image.getRGB(x, y);
    }

    @Override
    public void setBasicPixel(int x, int y, int rgb) {
        image.setRGB(x, y, rgb);
    }

    @Override
    public Pixel getPixel(int x, int y) {
        return pixels[x][y];
    }

    @Override
    public Pixel[] getPixels() {
        int width = getWidth();
        int height = getHeight();
        Pixel[] pixelArray = new Pixel[width * height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                pixelArray[y * width + x] = pixels[x][y];

        return pixelArray;
    }

    public void setPixels(Pixel[][] pixels) {
        this.pixels = pixels;
    }

    @Override
    public Pixel[][] getPixels2D() {
        return pixels;
    }

    @Override
    public boolean write(String fileName) {
        return false;
    }

    @Override
    public IImage copy() {
        CustomImage pic = new CustomImage();
        pic.setBufferedImage(copyBufferedImmage(this.image));
        int width = getWidth();
        int height = getHeight();
        Pixel[][] picPixels = new Pixel[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                picPixels[x][y] = new Pixel(pic, this.pixels[x][y]);
        return pic;
    }
}
