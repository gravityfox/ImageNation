package apcsa;

/**
 * Created by Fox on 1/29/2016.
 * Project: SandBox
 */
public class CustomImage extends SimpleImage {

    public void soDomething(){
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                Pixel p = this.getPixel(x,y);
                p.setHue((double)x / this.getWidth() * 360);
            }
        }
    }

    public void aFunction(){
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                Pixel p = this.getPixel(x, y);
                p.setHue(Math.random() * 360);
            }
        }
    }

    public void fadeOut(){
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                Pixel p = this.getPixel(x, y);
                p.setChroma(0.5 + Math.sin((double)y * Math.PI / 50) / 2);

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

    public void red(){
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                Pixel p = this.getPixel(x, y);
                p.setRed(1);
                p.setGreen(0);
            }
        }
    }

    public void flip(){

    }
}
