import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Client client;

    @FXML
    private Pane label;

    @FXML
    private Button button;

    @FXML
    TextField msgField;

    @FXML
    TextArea mainArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = new Client((args) -> {
            mainArea.appendText((String) args[0]);
        });
    }

    public void sendMsgAction(ActionEvent actionEvent) {
        client.sendMessage(msgField.getText());
        msgField.clear();
        msgField.requestFocus();
    }

    public void exitAction() {
        client.close();
        Platform.exit();
    }
    @FXML
    private void selectFile(ActionEvent event) {
        System.out.println("You clicked me!");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбрать фото");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter
                ("Картинки", "*.jpg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(Main.javaFXC);
        Image im = new Image(file.toURI().toString());
        ImageView imv = new ImageView(im);
        label.getChildren().add(imv);
    }
}