package apcsa.javafx;

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

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by Fox on 3/8/2016.
 * Project: ImageNation
 */
public class ImageNationApp extends Application {

    /**
     * Max size of the undo stack. Needed because undo states are not incremental, and therefore very large.
     */
    private static final int UNDO_LIMIT = 20;
    /**
     * Path of images relative to this class
     */
    private static final String IMAGE_PATH = "../../images";

    private Stage mainStage;

    //private Scene mainScene;

    private BorderPane root;

    private ListView<String> images;

    //private ListView<Wrapper.Method> methods;

    private ImageNode imageNode;

    private FXINMenuBar menuBar;


    /**
     * Reference to the current picture loaded.
     */
    private FXIImage picture;

    /**
     * The file name of the currently open image. It is used in the title.
     */
    private String fileName;

    /**
     * Undo stack for change history
     */
    private LinkedList<FXIImage> undoStack;

    /**
     * Redo stack for change history
     */
    private LinkedList<FXIImage> redoStack;

    @Override
    public void init() throws Exception {
        root = new BorderPane();
        undoStack = new LinkedList<>();
        redoStack = new LinkedList<>();
        imageNode = new ImageNode(null);
        menuBar = new FXINMenuBar(this);
        List<String> imagesList = getImages();
        this.images = new ListView<>();
        this.images.setCellFactory(param -> {
            ListCell<String> cell = new ListCell<>();
            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                    pushHistory();
                    String name = cell.getItem();
                    setWritableImage(loadWritableImage(name), true);
                    setFileName(name);
                }
            });
            return cell;
        });
        this.images.setItems(FXCollections.observableArrayList(
                imagesList.toArray(new String[imagesList.size()])
        ));
        List<Wrapper.Method> methodsList = getMethods();
        this.methods = new ListView<>();
        this.methods.setCellFactory(param -> {
            ListCell<Wrapper.Method> cell = new ListCell<>();
            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                    if (picture != null) {
                        pushHistory();
                        try {
                            cell.getItem().get().invoke(picture);
                        } catch (IllegalAccessException | InvocationTargetException e1) {
                            e1.printStackTrace();
                        }
                        imageNode.paint();
                    } else {
                        Dialogs.showErrorDialog("Please load an image first!", "Alert!");
                    }
                }
            });
            return cell;
        });
        this.methods.setItems(FXCollections.observableArrayList(
                methodsList.toArray(new Wrapper.Method[methodsList.size()])
        ));
        picture = null;

        root.setTop(menuBar);
        root.setCenter(imageNode);
        root.setLeft(this.images);
        root.setRight(this.methods);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        this.updateTitle();

        Rectangle2D dim = Screen.getPrimary().getVisualBounds();
        System.out.println(dim);
        mainStage.setMinWidth(300);
        mainStage.setMinHeight(300);
        mainStage.setWidth(dim.getWidth() * 0.75);
        mainStage.setHeight(dim.getHeight() * 0.75);
        mainStage.setX(dim.getMinX() + (dim.getWidth() - mainStage.getWidth()) / 2);
        mainStage.setY(dim.getMinY() + (dim.getHeight() - mainStage.getHeight()) / 2);

        mainStage.initStyle(StageStyle.DECORATED);

        mainStage.setScene(new Scene(root));
        mainStage.show();
    }

    @Override
    public void stop() throws Exception {

    }

   /* private List<Wrapper.Method> getMethods() {
        List<Wrapper.Method> list = new ArrayList<>();
        for (Method method : FXCustomImage.class.getDeclaredMethods()) {
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
    }*/

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

    private void setWritableImage(WritableImage image, boolean center) {
        FXSimpleImage picture = new FXCustomImage();
        picture.setWritableImage(image);
        this.picture = picture;
        //this.imagePanel.setImage(image, center);
        this.updateTitle();
    }

    public void setFileName(String name) {
        this.fileName = name;
        this.updateTitle();
    }

    private WritableImage loadWritableImage(String name) {
        try {
            return SwingFXUtils.toFXImage(ImageIO.read(this.getClass().getResource(IMAGE_PATH + "/" + name)), null);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            Canvas canvas = new Canvas(500, 200);
            GraphicsContext g = canvas.getGraphicsContext2D();
            g.setFill(Color.BLACK);

            g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            g.setFill(Color.WHITE);
            g.setFont(new Font("calibri", 20));
            g.fillText("Could not load \"" + name + "\"!", 20, 100);
            return canvas.snapshot(null, null);
        }
    }

    public void updateTitle() {
        if (picture != null && fileName != null && !fileName.isEmpty()) {
            mainStage.setTitle("ImageNation - " + fileName + " - " + picture.getWidth() + "x" + picture.getHeight());
        } else {
            mainStage.setTitle("ImageNation");
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
            FXIImage pic = undoStack.pop();
            redoStack.push(picture.copy());
            this.picture = pic;
        }
        setWritableImage(picture.getWritableImage(), false);
        //imagePanel.repaint();
        //menuBar.updateEnabled();
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            FXIImage pic = redoStack.pop();
            undoStack.push(picture.copy());
            this.picture = pic;
        }
        setWritableImage(picture.getWritableImage(), false);
        //imagePanel.repaint();
        //menuBar.updateEnabled();
    }

    public void pushHistory() {
        if (picture != null)
            undoStack.push(picture.copy());
        if (undoStack.size() > UNDO_LIMIT) {
            undoStack.removeLast();
        }
        redoStack.clear();
        //menuBar.updateEnabled();
    }

    public FXIImage getPicture() {
        return picture;
    }

    public ImageNode getImageNode() {
        return imageNode;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
