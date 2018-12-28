package apcsa.gui;

import apcsa.IImage;
import apcsa.Pixel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import static java.awt.RenderingHints.*;
import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
import static java.awt.event.InputEvent.BUTTON2_DOWN_MASK;
import static java.awt.event.InputEvent.BUTTON3_DOWN_MASK;

/**
 * Created by Fox on 1/28/2016.
 * Project: SandBox
 */
public class ImagePanel extends JPanel {

    private IImage image;
    private Dimension size;
    private boolean overlay = true;
    private Color overlayColor;

    private double rotation = 0;
    private AffineTransform transform = new AffineTransform();

    private boolean renderMouse;
    private int mouseX, mouseY;
    private int mouseXImage, mouseYImage;


    public ImagePanel(IImage theImage) {
        this.image = theImage;
        if (image != null) {
            this.size = new Dimension(image.getImage().getWidth(this), image.getImage().getHeight(this));
        }
        MouseInputAdapter adapter = new MouseInputAdapter() {
            private int lastX = 0, lastY = 0;

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                Dimension frame = ImagePanel.this.getSize();
                double delta = Math.pow(0.5, e.getPreciseWheelRotation() / (e.isShiftDown() ? 25 : 5));
                AffineTransform temp = (AffineTransform) transform.clone();
                int mx = e.getX(), my = e.getY();
                AffineTransform shift = new AffineTransform(), scale = new AffineTransform();
                shift.translate(-mx + (float)frame.width / 2, -my + (float)frame.height / 2);
                scale.scale(delta, delta);
                temp.preConcatenate(shift);
                temp.preConcatenate(scale);
                shift.setToTranslation(mx - (float) frame.width / 2, my - (float) frame.height / 2);
                temp.preConcatenate(shift);
                if (temp.getScaleX() > 0.1 && temp.getScaleX() < 500) transform = temp;
                mouseX = e.getX();
                mouseY = e.getY();
                renderMouse = true;
                computeMousePositionRelativeToImage();
                ImagePanel.this.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                this.lastX = e.getX();
                this.lastY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if ((e.getModifiersEx() & (BUTTON1_DOWN_MASK | BUTTON2_DOWN_MASK | BUTTON3_DOWN_MASK)) == BUTTON1_DOWN_MASK) {
                    int x = e.getX() - lastX;
                    int y = e.getY() - lastY;

                    AffineTransform shift = new AffineTransform();
                    shift.translate(x, y);
                    transform.preConcatenate(shift);
                }
                this.lastX = e.getX();
                this.lastY = e.getY();
                mouseX = e.getX();
                mouseY = e.getY();
                renderMouse = true;
                computeMousePositionRelativeToImage();
                ImagePanel.this.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                renderMouse = true;
                computeMousePositionRelativeToImage();
                ImagePanel.this.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
//                mouseX = -1;
//                mouseY = -1;
                renderMouse = false;
                ImagePanel.this.repaint();
            }
        };
        this.addMouseListener(adapter);
        this.addMouseMotionListener(adapter);
        this.addMouseWheelListener(adapter);

        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        revalidate();
    }

    public void setImage(IImage image, boolean center) {
        this.image = image;
        this.size = new Dimension(image.getImage().getWidth(this), image.getImage().getHeight(this));
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
        try {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            Rectangle clipBounds = g.getClipBounds();
            g.setColor(new Color(32, 32, 32));
            g.fillRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);
            AffineTransform imageTransform = new AffineTransform();
            imageTransform.translate(clipBounds.width / 2, clipBounds.height / 2);
            imageTransform.concatenate(transform);
            imageTransform.rotate(rotation);
       /* g2.translate(rect.width / 2, rect.height / 2);
        g2.transform(transform);
        g2.rotate(rotation);*/
            g2.transform(imageTransform);
            if (image != null) {
                if (imageTransform.getScaleX() < 5) {
                    g2.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR);
                }
                g.drawImage(image.getImage(), -size.width / 2, -size.height / 2, this);
            } else return;
            if (!renderMouse|| !overlay) return;
            AffineTransform imageShift = new AffineTransform();
            imageShift.translate(size.width / 2, size.height / 2);
            try {
                imageShift.invert();
            } catch (NoninvertibleTransformException ignored) {
            }
            imageTransform.concatenate(imageShift);
            Point2D mousePixelLower = new Point(mouseXImage, mouseYImage);
            Point2D mousePixelUpper = new Point2D.Double(mousePixelLower.getX() + 1, mousePixelLower.getY() + 1);
            Point2D mousePG1 = imageTransform.transform(mousePixelLower, null);
            Point2D mousePG2 = imageTransform.transform(mousePixelUpper, null);
            int x1 = (int) mousePG1.getX();
            int y1 = (int) mousePG1.getY();
            int x2 = (int) mousePG2.getX();
            int y2 = (int) mousePG2.getY();
            int xm = x1 + (x2 - x1) / 2;
            int ym = y1 + (y2 - y1) / 2;
            g2.setColor(Color.RED);
            g2.setTransform(new AffineTransform());
            g.drawRect(x1, y1, x2 - x1, y2 - y1);
            g.drawLine(xm, (int) clipBounds.getMinY(), xm, y1);
            g.drawLine(xm, y2, xm, (int) clipBounds.getMaxY());
            g.drawLine((int) clipBounds.getMinX(), ym, x1, ym);
            g.drawLine(x2, ym, (int) clipBounds.getMaxX(), ym);
            g2.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(new Font("helvetica", Font.PLAIN, 18));
            String str;
            str = "X: " + mouseXImage;
            g2.drawString(str, Math.max(x2 + 8, (int) clipBounds.getWidth() - 8 - g2.getFontMetrics().stringWidth(str)), ym + 24);
            str = "Y: " + mouseYImage;
            g2.drawString(str, xm - 8 - g2.getFontMetrics().stringWidth(str), Math.min(y1 - 8, 24));
            if (mouseXImage < 0 || mouseXImage >= image.getWidth() || mouseYImage < 0 || mouseYImage >= image.getHeight()) {
                return;
            }
            Pixel p = image.getPixel(mouseXImage, mouseYImage);
            if (imageTransform.getScaleX() > 205) {
                str = "alpha = " + f(p.getAlpha()) + "\n" +
                        "red = " + f(p.getRed()) + "\n" +
                        "green = " + f(p.getGreen()) + "\n" +
                        "blue = " + f(p.getBlue()) + "\n" +
                        "hue = " + f(p.getHue()) + "\n" +
                        "saturation = " + f(p.getSaturation()) + "\n" +
                        "lightness = " + f(p.getLightness()) + "\n" +
                        "value = " + f(p.getValue()) + "\n" +
                        "chroma = " + f(p.getChroma()) + "\n" +
                        "luma = " + f(p.getLuma());
                g2.setFont(g2.getFont().deriveFont(14f));
                g.setColor(p.copy().toContrastingBW().toAWTColor());
                int offset = 0;
                for (String s : str.split("\\n")) {
                    g.drawString(s, x1 + 12, y1 + 18 + offset);
                    offset += 20;
                }
            }
        } finally {
            renderMouse = false;
        }
    }

    private void computeMousePositionRelativeToImage() {
        if (image == null || !renderMouse || mouseX == -1 || mouseY == -1) return;
        Dimension rect = this.getSize();
        AffineTransform mapping = new AffineTransform();
        mapping.translate(rect.width / 2, rect.height / 2);
        mapping.concatenate(transform);
        mapping.rotate(rotation);
        Point mouse = new Point(mouseX, mouseY);
        AffineTransform inverse = new AffineTransform(mapping);
        try {
            inverse.invert();
        } catch (NoninvertibleTransformException ignored) {
        }
        AffineTransform shift = new AffineTransform();
        shift.translate(size.width / 2, size.height / 2);
        inverse.preConcatenate(shift);
        Point2D point1 = inverse.transform(mouse, null);
        this.mouseXImage = (int) Math.floor(point1.getX());
        this.mouseYImage = (int) Math.floor(point1.getY());
    }

    private static String f(double val) {
        return String.format("%.3f", val);
    }

    public boolean isOverlayEnabled() {
        return overlay;
    }

    public void setOverlay(boolean overlay) {
        this.overlay = overlay;
        this.renderMouse = true;
        this.repaint();
    }

    public Color getOverlayColor() {
        return overlayColor;
    }

    public void setOverlayColor(Color overlayColor) {
        this.overlayColor = overlayColor;
        this.renderMouse = true;
        this.repaint();
    }
}
