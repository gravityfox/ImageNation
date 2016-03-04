package apcsa;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Main window of ImageNation, as well as the entry point.
 * <p>
 * This program allows images to be opened, generated, modified, and saved.
 * <p>
 * The goal of this project is to provide an environment for students to write code to modify images
 * without having to worry about implementation details. Methods for modifying the image are kept isolated
 * in their own class, namely {@link CustomImage}. any methods written into this class can be invoked from
 * the GUI sidebar via reflection, allowing students to run their methods as much or as little as they want,
 * in addition to being able to call those same methods from the main method.
 * The implementation details of reflection are not of concern to the students, and are there to shift focus toward
 * writing the methods themselves.
 *
 * @author Michael Liu - mikefoxliu@outlook.com
 */
public class ImageNationFrame extends JFrame {

    /**
     * Max size of the undo stack. Needed because undo states are not incremental, and therefore very large.
     */
    private static final int UNDO_LIMIT = 20;
    /**
     * Path of images relative to this class
     */
    private static final String IMAGE_PATH = "../images";

    /**
     * Static instance to the GUI frame
     */
    private static ImageNationFrame instance;

    /**
     * List of image names on the left of the GUI
     */
    private JList<String> images;
    /**
     * List of methods on the right side of the GUI. Wrapper is used to avoid using {@link Method#toString()}.
     */
    private JList<Wrapper.Method> methods;
    /**
     * Menu Bar of the GUI. Holds most of the functionality access.
     */
    private INMenuBar menuBar;
    /**
     * Specialized JPanel for displaying the currently opened image. It supports zooming and panning with the mouse.
     */
    private ImagePanel imagePanel;
    /**
     * Reference to the current picture loaded.
     */
    private IImage picture;

    /**
     * Undo stack for change history
     */
    private LinkedList<IImage> undoStack;

    /**
     * Redo stack for change history
     */
    private LinkedList<IImage> redoStack;

    /**
     * Constuctor for main frame. The entire GUI is constructed from start to finish here, before being shown.
     */
    public ImageNationFrame() {
        this.init();
        this.construct();
        this.addListeners();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setMinimumSize(new Dimension(300, 200));
        this.setSize((int) (dim.width * 0.75), (int) (dim.height * 0.75));
        this.setLocation((dim.width - this.getWidth()) / 2, (dim.height - this.getHeight()) / 2);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        this.setVisible(true);
    }

    private void init() {

        this.getContentPane().setLayout(new BorderLayout());
        List<String> pictures = getImages();
        this.images = new JList<>(pictures.toArray(new String[pictures.size()]));
        List<Wrapper.Method> methods = getMethods();
        this.methods = new JList<>(methods.toArray(new Wrapper.Method[methods.size()]));
        this.menuBar = new INMenuBar(this);
        this.picture = null;
        this.imagePanel = new ImagePanel(null);
        this.undoStack = new LinkedList<>();
        this.redoStack = new LinkedList<>();
        this.menuBar.updateEnabled();
    }

    private void construct() {
        this.images.setFont(images.getFont().deriveFont(14f));
        this.images.setBorder(new EmptyBorder(10, 20, 10, 20));
        this.images.setBackground(Color.LIGHT_GRAY);
        this.getContentPane().add(images, BorderLayout.WEST);
        this.methods.setBorder(new EmptyBorder(10, 20, 10, 20));
        this.methods.setFont(methods.getFont().deriveFont(14f));
        this.methods.setBackground(Color.LIGHT_GRAY);
        this.getContentPane().add(methods, BorderLayout.EAST);
        this.getContentPane().add(imagePanel, BorderLayout.CENTER);
        this.setJMenuBar(this.menuBar);
    }

    private void addListeners() {
        this.methods.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    if (picture != null) {
                        pushHistory();
                        try {
                            methods.getModel().getElementAt(methods.locationToIndex(e.getPoint())).get().invoke(picture);
                        } catch (IllegalAccessException | InvocationTargetException e1) {
                            e1.printStackTrace();
                        }
                        imagePanel.repaint();
                    } else {
                        JOptionPane.showMessageDialog(ImageNationFrame.this, "Please load an image first!", "Alert!", JOptionPane.ERROR_MESSAGE);
                        //JOptionPane.showInputDialog(window, "Please enter paramater 1: ", "Data", JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Test", "Hello"}, "Test");
                    }
                }
            }
        });
        this.images.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    pushHistory();
                    setBufferedImage(loadImage(images.getModel().getElementAt(images.locationToIndex(e.getPoint()))), true);
                }
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                imagePanel.repaint();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                imagePanel.repaint();
            }

            @Override
            public void componentShown(ComponentEvent e) {
                imagePanel.repaint();
            }
        });
    }

    private List<Wrapper.Method> getMethods() {
        List<Wrapper.Method> list = new ArrayList<>();
        for (Method method : CustomImage.class.getDeclaredMethods()) {
            Class<?>[] parameters = method.getParameterTypes();
            if (!Modifier.isStatic(method.getModifiers())
                    && Modifier.isPublic(method.getModifiers())
                    && parameters.length == 0
                    && method.getReturnType().equals(Void.TYPE)) {
                list.add(new Wrapper.Method(method));
            }
        }
        Collections.sort(list);
        return list;
    }

    private List<String> getImages() {
        List<String> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(IMAGE_PATH)));
        String filename;
        try {
            while ((filename = reader.readLine()) != null) {
                list.add(filename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void setBufferedImage(BufferedImage image, boolean center) {
        SimpleImage picture = new CustomImage();
        picture.setBufferedImage(image);
        this.picture = picture;

        this.imagePanel.setImage(image, center);
    }

    private BufferedImage loadImage(String name) {
        try {
            return ImageIO.read(this.getClass().getResource(IMAGE_PATH + "/" + name));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            BufferedImage img = new BufferedImage(500, 200, BufferedImage.TYPE_INT_ARGB);
            Graphics g = img.getGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(20f));
            g.drawString("Could not load \"" + name + "\"!", 20, 100);
            return img;
        }
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            IImage pic = undoStack.pop();
            redoStack.push(picture.copy());
            this.picture = pic;
        }
        setBufferedImage(picture.getBufferedImage(), false);
        imagePanel.repaint();
        menuBar.updateEnabled();
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            IImage pic = redoStack.pop();
            undoStack.push(picture.copy());
            this.picture = pic;
        }
        setBufferedImage(picture.getBufferedImage(), false);
        imagePanel.repaint();
        menuBar.updateEnabled();
    }

    public void pushHistory() {
        if (picture != null)
            undoStack.push(picture.copy());
        if (undoStack.size() > UNDO_LIMIT) {
            undoStack.removeLast();
        }
        redoStack.clear();
        menuBar.updateEnabled();
    }

    public IImage getPicture() {
        return picture;
    }

    public ImagePanel getImagePanel() {
        return imagePanel;
    }

    public static ImageNationFrame getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        instance = new ImageNationFrame();

    }
}
