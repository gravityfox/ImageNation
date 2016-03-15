package apcsa.javafx;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Fox on 3/14/2016.
 * Project: ImageNation
 */
public class Dialogs {

    public static void showErrorDialog(String message, String title) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(5,5,5,5));
        box.setSpacing(10);
        Text text = new Text(message);
        Button button = new Button("OK");
        button.addEventHandler(ActionEvent.ACTION, event -> stage.close());
        box.getChildren().addAll(text, button);

        stage.setScene(new Scene(box, 200, 75));
        stage.show();
    }
}
