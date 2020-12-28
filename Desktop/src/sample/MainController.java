package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainController {
    NetController controller = new NetController();


    @FXML
    private Button b1;
    @FXML
    private Button b2;
    @FXML
    public void toSearchPeople(){
        try {
            Main.GlobalState = Main.State.SEARCH_PEOPLE;
            Main.changeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void toRegPeople(){
        try {
            Main.GlobalState = Main.State.REG_PEOPLE;
            Main.changeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
