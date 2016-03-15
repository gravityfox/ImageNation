package apcsa.javafx;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.FileChooser;

import static javafx.scene.input.KeyCombination.CONTROL_DOWN;
import static javafx.scene.input.KeyCombination.SHIFT_DOWN;

/**
 * Created by Fox on 3/14/2016.
 * Project: ImageNation
 */
public class FXINMenuBar extends MenuBar {
    private ImageNationApp app;

    private Menu file;
    private MenuItem newCanvas;
    private MenuItem newFractal;
    private MenuItem saveAs;

    private Menu edit;
    private MenuItem undo;
    private MenuItem redo;

    private Menu view;
    private MenuItem center;

    public FXINMenuBar(ImageNationApp app) {
        this.app = app;
        init();
        construct();
        addShortcuts();
        registerListeners();
    }

    public void updateEnabled() {
        undo.setDisable(!app.canUndo());
        redo.setDisable(!app.canRedo());
    }

    private void init() {
        file = new Menu("_File");
        newCanvas = new MenuItem("_New Canvas...");
        newFractal = new MenuItem("New _Fractal...");
        saveAs = new MenuItem("_Save As...");

        edit = new Menu("_Edit");
        undo = new MenuItem("_Undo");
        redo = new MenuItem("_Redo");

        view = new Menu("_View");
        center = new MenuItem("_Center Image");
    }

    private void construct() {
        this.getMenus().addAll(file, edit, view);
        file.getItems().addAll(newCanvas, newFractal, saveAs);
        edit.getItems().addAll(undo, redo);
        view.getItems().addAll(center);
    }

    private void addShortcuts() {
        newCanvas.setAccelerator(new KeyCodeCombination(KeyCode.N, CONTROL_DOWN));
        newFractal.setAccelerator(new KeyCodeCombination(KeyCode.N, CONTROL_DOWN, SHIFT_DOWN));
        saveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, CONTROL_DOWN));
        undo.setAccelerator(new KeyCodeCombination(KeyCode.Z, CONTROL_DOWN));
        redo.setAccelerator(new KeyCodeCombination(KeyCode.Z, CONTROL_DOWN, SHIFT_DOWN));
        center.setAccelerator(new KeyCodeCombination(KeyCode.C, CONTROL_DOWN, SHIFT_DOWN));
    }

    private void registerListeners() {
        center.addEventHandler(ActionEvent.ACTION, event -> app.getImageNode().centerImage());
        undo.addEventHandler(ActionEvent.ACTION, event -> app.undo());
        redo.addEventHandler(ActionEvent.ACTION, event -> app.redo());
        saveAs.addEventHandler(ActionEvent.ACTION, event -> {
            if (app.getPicture() != null) {
                FileChooser fc = new FileChooser();
                fc.setTitle("Save Image As...");
                fc.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Portable Network Graphic", "*.png"),
                        new FileChooser.ExtensionFilter("JPEG", "*.jpg", "*.jpeg"),
                        new FileChooser.ExtensionFilter("Graphics Interchange Format", "*.gif"),
                        new FileChooser.ExtensionFilter("Bitmap", "*.bmp")
                );
                File file = fc.showSaveDialog(null);
                if (file != null) {
                    String[] parts = file.getName().split("\\.");
                    String ext = parts.length > 0 ? parts[parts.length - 1] : "";
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(app.getPicture().getWritableImage(), null), ext, file);
                        app.setFileName(file.getName());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            } else {
                Dialogs.showErrorDialog("Please load an image first!", "Alert!");
            }
        });
    }
}
