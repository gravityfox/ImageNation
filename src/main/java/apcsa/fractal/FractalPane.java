package apcsa.fractal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Fox on 3/1/2016.
 * Project: ImageNation
 */
public class FractalPane extends JPanel {

    BufferedImage bufferedImage;

    public FractalPane() {
        registerListeners();
    }

    public void rebuffer() {
        if (this.getWidth() > 0 && this.getHeight() > 0) {
            bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < bufferedImage.getWidth(); i++) {
                for (int j = 0; j < bufferedImage.getHeight(); j++) {
                    bufferedImage.setRGB(i, j, 0xffff0000);
                }
            }
        }
    }

    private void registerListeners() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                rebuffer();
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage, 0, 0, this);
    }
}
