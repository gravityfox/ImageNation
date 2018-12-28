package apcsa;

import java.awt.*;

/**
 * Created by Fox on 1/31/2016.
 * Project: SandBox
 */
public class Pixel {

    private IImage picture;
    private int x, y;

    private Mode mode;
    private Mode.Hx mode2;
    private Mode.Hx.Sx mode3;

    private double alpha = 1.0;

    private double red = 0;
    private double green = 0;
    private double blue = 0;

    private double hue = 0;
    private double saturation = 0;
    private double lightness = 0;
    private double value = 0;
    private double chroma = 0;
    private double luma = 0;

    public Pixel(IImage picture, int x, int y) {
        this.picture = picture;
        this.x = x;
        this.y = y;
        this.mode = Mode.RGB;
        this.mode2 = Mode.Hx.S;
        this.mode3 = Mode.Hx.Sx.V;
        int argb = 0;
        if (picture != null) argb = picture.getBasicPixel(x, y);
        this.alpha = (double) ((argb >> 24) & 0xFF) / 255;
        this.red = (double) ((argb >> 16) & 0xFF) / 255;
        this.green = (double) ((argb >> 8) & 0xFF) / 255;
        this.blue = (double) (argb & 0xFF) / 255;
        update();
    }

    public Pixel(IImage picture, Pixel pixel) {
        this.picture = picture;
        this.x = pixel.x;
        this.y = pixel.y;
        this.mode = pixel.mode;
        this.mode2 = pixel.mode2;
        this.mode3 = pixel.mode3;
        this.alpha = pixel.alpha;
        this.red = pixel.red;
        this.green = pixel.green;
        this.blue = pixel.blue;
        this.hue = pixel.hue;
        this.saturation = pixel.saturation;
        this.lightness = pixel.lightness;
        this.value = pixel.value;
        this.chroma = pixel.chroma;
        this.luma = pixel.luma;
    }

    public double getAlpha() {
        return alpha;
    }

    public Pixel setAlpha(double alpha) {
        this.alpha = alpha;
        update();
        return this;
    }

    public double getRed() {
        return red;
    }

    public Pixel setRed(double red) {
        this.red = red;
        mode = Mode.RGB;
        update();
        return this;
    }

    public double getGreen() {
        return green;
    }

    public Pixel setGreen(double green) {
        this.green = green;
        mode = Mode.RGB;
        update();
        return this;
    }

    public double getBlue() {
        return blue;
    }

    public Pixel setBlue(double blue) {
        this.blue = blue;
        mode = Mode.RGB;
        update();
        return this;
    }

    public double getHue() {
        return hue;
    }

    public Pixel setHue(double hue) {
        while (hue < 0) {
            hue += 360;
        }
        this.hue = hue % 360;
        mode = Mode.H;
        update();
        return this;
    }

    public double getSaturation() {
        return saturation;
    }

    public Pixel setSaturation(double saturation) {
        this.saturation = saturation;
        mode = Mode.H;
        mode2 = Mode.Hx.S;
        update();
        return this;
    }

    public double getLightness() {
        return lightness;
    }

    public Pixel setLightness(double lightness) {
        this.lightness = lightness;
        mode = Mode.H;
        mode2 = Mode.Hx.S;
        mode3 = Mode.Hx.Sx.L;
        update();
        return this;
    }

    public double getValue() {
        return value;
    }

    public Pixel setValue(double value) {
        this.value = value;
        mode = Mode.H;
        mode2 = Mode.Hx.S;
        mode3 = Mode.Hx.Sx.V;
        update();
        return this;
    }

    public double getChroma() {
        return chroma;
    }

    public Pixel setChroma(double chroma) {
        this.chroma = chroma;
        mode = Mode.H;
        mode2 = Mode.Hx.CL;
        update();
        return this;
    }

    public double getLuma() {
        return luma;
    }

    public Pixel setLuma(double luma) {
        this.luma = luma;
        mode = Mode.H;
        mode2 = Mode.Hx.CL;
        update();
        return this;
    }

    public Pixel setToPixel(Pixel pixel) {
        this.mode = pixel.mode;
        this.mode2 = pixel.mode2;
        this.mode3 = pixel.mode3;
        this.alpha = pixel.alpha;
        this.red = pixel.red;
        this.green = pixel.green;
        this.blue = pixel.blue;
        this.hue = pixel.hue;
        this.saturation = pixel.saturation;
        this.lightness = pixel.lightness;
        this.value = pixel.value;
        this.chroma = pixel.chroma;
        this.luma = pixel.luma;
        update();
        return this;
    }

    public Pixel copy() {
        return new Pixel(null, this);
    }

    public Pixel invertRGB() {
        this.red = 1 - this.red;
        this.green = 1 - this.green;
        this.blue = 1 - this.blue;
        mode = Mode.RGB;
        update();
        return this;
    }

    public Pixel toContrastingBW() {
        lightness = 1 - Math.round((2 * lightness + luma) / 3);
        mode = Mode.H;
        mode2 = Mode.Hx.S;
        mode3 = Mode.Hx.Sx.L;
        update();
        return this;
    }

    public Color toAWTColor() {
        return new Color((int) Math.max(0, Math.min(255, Math.round(red * 255))),
                (int) Math.max(0, Math.min(255, Math.round(green * 255))),
                (int) Math.max(0, Math.min(255, Math.round(blue * 255))),
                (int) Math.max(0, Math.min(255, Math.round(alpha * 255))));
    }

    public int toIntColor() {
        int a = (int) Math.max(0, Math.min(255, Math.round(alpha * 255)));
        int r = (int) Math.max(0, Math.min(255, Math.round(red * 255)));
        int g = (int) Math.max(0, Math.min(255, Math.round(green * 255)));
        int b = (int) Math.max(0, Math.min(255, Math.round(blue * 255)));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private void update() {
        int a, r, g, b;
        a = (int) Math.max(0, Math.min(255, Math.round(alpha * 255)));
        if (mode == Mode.RGB) {
            double M = Math.max(Math.max(red, green), blue);
            double m = Math.min(Math.min(red, green), blue);
            chroma = M - m;
            if (chroma == 0) {
                hue = 0;
            } else if (red == M) {
                hue = 60 * (((green - blue) / chroma + 6) % 6);
            } else if (green == M) {
                hue = 60 * (((blue - red) / chroma + 2) % 6);
            } else if (blue == M) {
                hue = 60 * (((red - green) / chroma + 4) % 6);
            } else {
                hue = 0;
            }
            value = M;
            if (chroma == 0 || value == 0) {
                saturation = 0;
            } else {
                saturation = chroma / value;
            }
            lightness = (M + m) / 2;
            luma = 0.3 * red + 0.59 * green + 0.11 * blue;
        } else {
            double r2 = 0, g2 = 0, b2 = 0;
            double c, h, x, v = 0;
            h = (hue / 60) % 6;
            if (mode2 == Mode.Hx.CL) {
                c = chroma;
            } else {
                if (mode3 == Mode.Hx.Sx.V) {
                    c = value * saturation;
                    v = value - c;
                } else {
                    c = (1 - Math.abs(2 * lightness - 1)) * saturation;
                    v = lightness - chroma / 2;
                }
            }
            x = c * (1 - Math.abs(h % 2 - 1));
            switch ((int) h) {
                case 0:
                    r2 = c;
                    g2 = x;
                    b2 = 0;
                    break;
                case 1:
                    r2 = x;
                    g2 = c;
                    b2 = 0;
                    break;
                case 2:
                    r2 = 0;
                    g2 = c;
                    b2 = x;
                    break;
                case 3:
                    r2 = 0;
                    g2 = x;
                    b2 = c;
                    break;
                case 4:
                    r2 = x;
                    g2 = 0;
                    b2 = c;
                    break;
                case 5:
                    r2 = c;
                    g2 = 0;
                    b2 = x;
                    break;
            }


            if (mode2 == Mode.Hx.CL) {
                v = luma - (0.3 * r2 + 0.59 * g2 + 0.11 * b2);
            }

            red = r2 + v;
            green = g2 + v;
            blue = b2 + v;

            double M = Math.max(Math.max(red, green), blue);
            double m = Math.min(Math.min(red, green), blue);
            if (mode2 != Mode.Hx.CL) {
                chroma = c;
                luma = 0.3 * red + 0.59 * green + 0.11 * blue;
            } else {
                if (mode3 == Mode.Hx.Sx.V) {
                    value = M;
                } else {
                    lightness = (M + m) / 2;
                }
            }

        }
        if (picture != null) {
            r = (int) Math.max(0, Math.min(255, Math.round(red * 255)));
            g = (int) Math.max(0, Math.min(255, Math.round(green * 255)));
            b = (int) Math.max(0, Math.min(255, Math.round(blue * 255)));
            picture.setBasicPixel(x, y, (a << 24) | (r << 16) | (g << 8) | b);
        }
    }

    @Override
    public String toString() {
        return "Pixel{" +
                "alpha=" + alpha +
                ", red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", hue=" + hue +
                ", saturation=" + saturation +
                ", lightness=" + lightness +
                ", value=" + value +
                ", chroma=" + chroma +
                ", luma=" + luma +
                '}';
    }

    private enum Mode {
        RGB, H;

        public enum Hx {
            S, CL;

            public enum Sx {
                L, V
            }
        }
    }
}
