package apcsa;

/**
 * Created by Fox on 1/29/2016.
 * Project: SandBox
 */
public class CustomImage extends SimpleImage {

    public void soDomething() {
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                Pixel p = this.getPixel(x, y);
                p.setHue((double) x / this.getWidth() * 360);
            }
        }
    }

    public void aFunction() {
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                Pixel p = this.getPixel(x, y);
                p.setHue(Math.random() * 360);
            }
        }
    }

    public void fadeOut() {
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                Pixel p = this.getPixel(x, y);
                p.setChroma(0.5 + Math.sin((double) y * Math.PI / 50) / 2);

            }
        }
    }

    public void intense() {
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                Pixel p = this.getPixel(x, y);
                p.setChroma(1);

            }
        }
    }

    public void intense2() {
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                Pixel p = this.getPixel(x, y);
                p.setSaturation(1);

            }
        }
    }

    public void red() {
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                Pixel p = this.getPixel(x, y);
                p.setRed(1);
                p.setGreen(0);
            }
        }
    }

    @ParamNames({"Hue", "Chroma"})
    public void number(double hue, double testvar) {
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                Pixel p = this.getPixel(x, y);
                p.setHue(hue);
            }
        }
    }

    public void string(String test) {
        System.out.println(test);
    }

    public void mirror() {
        for (int x = 0; x < this.getWidth() / 2; x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                Pixel p = this.getPixel(x, y);
                Pixel p2 = this.getPixel(this.getWidth() - x - 1, y);
                p2.setRed(p.getRed());
                p2.setGreen(p.getGreen());
                p2.setBlue(p.getBlue());
            }
        }
    }

    @ParamNames({"Width", "Height"})
    public void canvas(int width, int height) {
        Pixel[][] newPixels = new Pixel[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                newPixels[x][y] = new Pixel(null, x, y).setAlpha(1).setLightness(1);
            }
        }
        setPixels(newPixels);
    }

    public void pastel() {
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                Pixel p = this.getPixel(x, y);
                p.setSaturation(p.getSaturation() / 2);
                p.setValue(p.getValue() / 2 + 0.5);
            }
        }
    }

}
