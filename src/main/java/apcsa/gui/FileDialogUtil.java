package apcsa.gui;

import javafx.application.Platform;
import javafx.stage.FileChooser;

import java.io.File;

public class FileDialogUtil {

    public static File openImage() {
        return operate(Action.OPEN, null);
    }

    public static File saveImage(String fileName) {
        return operate(Action.SAVE, fileName);
    }

    private static File operate(Action action, String fileName) {
        FileChooser fc = new FileChooser();
        fc.setTitle(action.dialog);
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Portable Network Graphic", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("Graphics Interchange Format", "*.gif"),
                new FileChooser.ExtensionFilter("Bitmap", "*.bmp")
        );

        if (fileName != null)
            fc.setInitialFileName(fileName);

        final File[] fileTemp = {null};

        try {
            synchronized (fileTemp) {
                Platform.runLater(() -> {
                    fileTemp[0] = action.showDialog(fc);
                    synchronized (fileTemp) {
                        fileTemp.notifyAll();
                    }
                });
                fileTemp.wait();
            }
        } catch (InterruptedException ignored) {
        }

        return fileTemp[0];
    }

    private enum Action {
        OPEN("Open Image...") {
            @Override
            File showDialog(FileChooser fc) {
                File file = fc.showOpenDialog(null);
                return file;
            }
        },
        SAVE("Save Image As...") {
            @Override
            File showDialog(FileChooser fc) {
                return fc.showSaveDialog(null);
            }
        };

        final String dialog;

        Action(String dialog) {
            this.dialog = dialog;
        }

        abstract File showDialog(FileChooser fc);
    }

}
