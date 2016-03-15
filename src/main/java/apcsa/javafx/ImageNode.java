package apcsa.javafx;


import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGRegion;
import com.sun.prism.Graphics;

import javafx.animation.Transition;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.util.Duration;

/**
 * Created by Fox on 3/8/2016.
 * Project: ImageNation
 */
public class ImageNode extends Region {

    private RenderNode pgNode;
    private Image image;
    private Dimension2D size;
    private double rotation = 0;
    private Affine transform = new Affine();

    public ImageNode(Image image) {
        setCache(false);
        this.image = image;
        if (image != null) {
            this.size = new Dimension2D(image.getWidth(), image.getHeight());
        }
    }

    /*public void paint() {
        GraphicsContext g ;//= this.getGraphicsContext2D();
        g.setFill(Color.DARKGRAY);
        g.fill();
        g.translate(this.getWidth() / 2, this.getHeight() / 2);
        g.transform(transform);
        g.rotate(rotation);
        if (image != null) {
            g.drawImage(image, -this.getWidth() / 2, -this.getHeight() / 2);
        }
    }*/

    public void centerImage() {
        this.transform = new Affine();
        if (size.getWidth() > this.getWidth() || size.getHeight() > this.getHeight()) {
            double scale;
            scale = size.getWidth() / this.getWidth() > size.getHeight() / this.getHeight()
                    ? this.getWidth() / size.getWidth()
                    : this.getHeight() / size.getHeight();
            transform.appendScale(scale, scale);
        }
        //paint();
    }

    @Override
    public NGNode impl_createPeer() {
        pgNode = new RenderNode();

        // bind this to an fps property
        double framerate = 60;
        new Transition(framerate) {
            {
                setCycleDuration(Duration.INDEFINITE);
            }

            @Override
            protected void interpolate(double frac) {
                impl_updatePeer();
            }
        }.play();

        return pgNode;
    }



    private class RenderNode extends NGRegion{
        @Override
        protected void renderContent(Graphics g) {
            super.renderContent(g);
        }
    }

}
