package apcsa;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

/**
 * Created by Fox on 1/28/2016.
 * Project: SandBox
 */
public class ImagePanel extends JPanel {

    private Image image;
    private Dimension size;
    private double rotation = 0;
    private AffineTransform transform = new AffineTransform();


    public ImagePanel(Image theImage) {
        this.image = theImage;
        if (image != null) {
            this.size = new Dimension(image.getWidth(this), image.getHeight(this));
        }
        MouseInputAdapter adapter = new MouseInputAdapter() {
            private int lastX = 0, lastY = 0;

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                Dimension frame = ImagePanel.this.getSize();
                double delta = Math.pow(0.5, e.getPreciseWheelRotation() / 5);
                AffineTransform temp = (AffineTransform) transform.clone();
                int mx = e.getX(), my = e.getY();
                AffineTransform shift = new AffineTransform(), scale = new AffineTransform();
                shift.translate(-mx + frame.width / 2, -my + frame.height / 2);
                scale.scale(delta, delta);
                temp.preConcatenate(shift);
                temp.preConcatenate(scale);
                shift.setToTranslation(mx - frame.width / 2, my - frame.height / 2);
                temp.preConcatenate(shift);
                if (temp.getScaleX() > 0.1 && temp.getScaleX() < 50) transform = temp;
                ImagePanel.this.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                this.lastX = e.getX();
                this.lastY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX() - lastX;
                int y = e.getY() - lastY;

                AffineTransform shift = new AffineTransform();
                shift.translate(x, y);
                transform.preConcatenate(shift);

                this.lastX = e.getX();
                this.lastY = e.getY();
                ImagePanel.this.repaint();
            }
        };
        this.addMouseListener(adapter);
        this.addMouseMotionListener(adapter);
        this.addMouseWheelListener(adapter);

        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        revalidate();
    }

    public void setImage(Image image, boolean center) {
        this.image = image;
        this.size = new Dimension(image.getWidth(this), image.getHeight(this));
        if (center) centerImage();
    }

    public void centerImage() {
        this.transform = new AffineTransform();
        if (size.width > this.getWidth() || size.height > this.getHeight()) {
            double scale;
            scale = (double) size.width / this.getWidth() > (double) size.height / this.getHeight()
                    ? (double) this.getWidth() / size.width
                    : (double) this.getHeight() / size.height;
            transform.scale(scale, scale);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);
        Rectangle rect = g.getClipBounds();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
        g2.translate(rect.width / 2, rect.height / 2);
        g2.transform(transform);
        g2.rotate(rotation);
        if (image != null) {
            g.drawImage(image, -size.width / 2, -size.height / 2, this);
        }
    }
}
