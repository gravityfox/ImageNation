package apcsa.gui;

import apcsa.ImageNationFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import static java.awt.event.KeyEvent.*;

/**
 * Created by Fox on 2/2/2016.
 * Project: SandBox
 */
public class INMenuBar extends JMenuBar {

    private ImageNationFrame frame;

    private JMenu file;
    private JMenuItem newCanvas;
    private JMenuItem newFractal;
    private JMenuItem open;
    private JMenuItem saveAs;

    private JMenu edit;
    private JMenuItem undo;
    private JMenuItem redo;

    private JMenu view;
    private JMenuItem center;
    private JCheckBoxMenuItem overlay;


    public INMenuBar(ImageNationFrame frame) {
        this.frame = frame;
        init();
        construct();
        addShortcuts();
        registerListeners();
    }

    public void updateEnabled() {
        undo.setEnabled(frame.canUndo());
        redo.setEnabled(frame.canRedo());
    }

    private void init() {
        file = new JMenu("File");
        newCanvas = new JMenuItem("New Canvas...");
        newFractal = new JMenuItem("New Fractal...");
        open = new JMenuItem("Open...");
        saveAs = new JMenuItem("Save As...");

        edit = new JMenu("Edit");
        undo = new JMenuItem("Undo");
        redo = new JMenuItem("Redo");

        view = new JMenu("View");
        center = new JMenuItem("Center Image");
        overlay = new JCheckBoxMenuItem("Overlay", true);
    }

    private void construct() {
        this.add(file);
        file.add(newCanvas);
        file.add(newFractal);
        file.add(open);
        file.add(saveAs);

        this.add(edit);
        edit.add(undo);
        edit.add(redo);

        this.add(view);
        view.add(center);
        view.add(overlay);

    }

    @SuppressWarnings("Duplicates")
    private void addShortcuts() {
        file.setMnemonic(VK_F);
        newCanvas.setMnemonic(VK_N);
        newCanvas.setAccelerator(KeyStroke.getKeyStroke(VK_N, CTRL_DOWN_MASK));
        newFractal.setMnemonic(VK_F);
        newFractal.setAccelerator(KeyStroke.getKeyStroke(VK_N, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        open.setMnemonic(VK_O);
        open.setAccelerator(KeyStroke.getKeyStroke(VK_O, CTRL_DOWN_MASK));
        saveAs.setMnemonic(VK_S);
        saveAs.setAccelerator(KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK));

        edit.setMnemonic(VK_E);
        undo.setMnemonic(VK_U);
        undo.setAccelerator(KeyStroke.getKeyStroke(VK_Z, CTRL_DOWN_MASK));
        redo.setMnemonic(VK_R);
        redo.setAccelerator(KeyStroke.getKeyStroke(VK_Z, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));

        view.setMnemonic(VK_V);
        center.setMnemonic(VK_C);
        center.setAccelerator(KeyStroke.getKeyStroke(VK_C, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        overlay.setMnemonic(VK_O);
        overlay.setAccelerator(KeyStroke.getKeyStroke(VK_L, CTRL_DOWN_MASK));

    }

    private void registerListeners() {
        center.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getImagePanel().centerImage();
            }
        });
        overlay.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getImagePanel().setOverlay(overlay.getState());
            }
        });
        undo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.undo();
            }
        });
        redo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.redo();
            }
        });
        newFractal.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FractalDialog dialog = new FractalDialog(frame, "Fractal Generator");
            }
        });
        open.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = FileDialogUtil.openImage();
                if (file != null) {
                    try {
                        frame.openImage(file.toURI().toURL(), file.getName());
                        frame.setFileName(file.getName());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Not a valid file!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        saveAs.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frame.getPicture() != null) {
                    File file = FileDialogUtil.saveImage(frame.getFileName());
                    if (file != null) {
                        String[] parts = file.getName().split("\\.");
                        String ext = parts.length > 0 ? parts[parts.length - 1] : "";
                        try {
                            ImageIO.write(frame.getPicture().getBufferedImage(), ext, file);
                            frame.setFileName(file.getName());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please load an image first!", "Alert!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
