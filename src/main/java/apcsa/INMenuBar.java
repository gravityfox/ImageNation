package apcsa;

import apcsa.fractal.FractalDialog;
import javafx.application.Platform;
import javafx.stage.FileChooser;

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
    private JMenuItem saveAs;

    private JMenu edit;
    private JMenuItem undo;
    private JMenuItem redo;

    private JMenu view;
    private JMenuItem center;


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
        saveAs = new JMenuItem("Save As...");

        edit = new JMenu("Edit");
        undo = new JMenuItem("Undo");
        redo = new JMenuItem("Redo");

        view = new JMenu("View");
        center = new JMenuItem("Center Image");
    }

    private void construct() {
        this.add(file);
        file.add(newCanvas);
        file.add(newFractal);
        file.add(saveAs);

        this.add(edit);
        edit.add(undo);
        edit.add(redo);

        this.add(view);
        view.add(center);

    }

    private void addShortcuts() {
        file.setMnemonic(VK_F);
        newCanvas.setMnemonic(VK_N);
        newCanvas.setAccelerator(KeyStroke.getKeyStroke(VK_N, CTRL_MASK));
        newFractal.setMnemonic(VK_F);
        newFractal.setAccelerator(KeyStroke.getKeyStroke(VK_N, CTRL_MASK | SHIFT_MASK));
        saveAs.setMnemonic(VK_S);
        saveAs.setAccelerator(KeyStroke.getKeyStroke(VK_S, CTRL_MASK));

        edit.setMnemonic(VK_E);
        undo.setMnemonic(VK_U);
        undo.setAccelerator(KeyStroke.getKeyStroke(VK_Z, CTRL_MASK));
        redo.setMnemonic(VK_R);
        redo.setAccelerator(KeyStroke.getKeyStroke(VK_Z, CTRL_MASK | SHIFT_MASK));

        view.setMnemonic(VK_V);
        center.setMnemonic(VK_C);
        center.setAccelerator(KeyStroke.getKeyStroke(VK_C, CTRL_MASK | SHIFT_MASK));

    }

    private void registerListeners() {
        center.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getImagePanel().centerImage();
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
        saveAs.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frame.getPicture() != null) {
                    FileChooser fc = new FileChooser();
                    fc.setTitle("Save Image As...");
                    fc.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("Portable Network Graphic", "*.png"),
                            new FileChooser.ExtensionFilter("JPEG", "*.jpg", "*.jpeg"),
                            new FileChooser.ExtensionFilter("Graphics Interchange Format", "*.gif"),
                            new FileChooser.ExtensionFilter("Bitmap", "*.bmp")
                    );
                    final File[] fileTemp = {null};

                    try {
                        synchronized (fileTemp) {
                            Platform.runLater(() -> {
                                fileTemp[0] = fc.showSaveDialog(null);
                                synchronized (fileTemp) {
                                    fileTemp.notifyAll();
                                }
                            });
                            fileTemp.wait();
                        }
                    } catch (InterruptedException ignored) {
                    }

                    File file = fileTemp[0];
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
