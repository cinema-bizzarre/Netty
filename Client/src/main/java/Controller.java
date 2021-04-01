import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
        private Client client;


        @FXML
    TextField msgField;

        @FXML
    TextArea mainArea;

        @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = new Client();
    }

    public void sendMsgAction(ActionEvent actionEvent) {
            client.sendMessage(msgField.getText());
    }
}
