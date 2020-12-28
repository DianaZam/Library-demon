package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sample.structures.Book;

import java.io.IOException;
import java.util.ArrayList;

public class AccInfoController {

    @FXML
    private Label person_id;
    @FXML
    private Label person;
    @FXML
    private Label error_field;
    @FXML
    private ListView<String> listView;

    @FXML
    public void initialize() {
        person.setText(SearchPeopleController.person);
        person_id.setText(SearchPeopleController.person_id);
        listView.getItems().clear();
        for (Book b : SearchPeopleController.lentbooks)
            listView.getItems().add("\"" + b.title + "\" " + b.author);
    }

    @FXML
    public void lendBook() {
        Main.GlobalState = Main.State.SEARCH_BOOKS;
        try {
            Main.changeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void returnBook() {
        String result = "null";
        try {
            if(listView.getSelectionModel().getSelectedIndex() == -1){
                error_field.setText("Выберите книгу!");
                return;
            }
            String answer = Main.controller.sendToServer(JSONBuilder.makeJsonReturnBook(
                    "" + SearchPeopleController.lentbooks.get(listView.getSelectionModel().getSelectedIndex()).book_id,
                    SearchPeopleController.person_id));
            JSONObject object = (JSONObject) new JSONParser().parse(answer);
            result = object.get("result").toString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        if (result.equals("false")) {
            error_field.setText("Ошибка возврата книги.");
        } else if (result.equals("true")) {
            listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());
        }

    }

    @FXML
    public void toMainMenu() {
        Main.GlobalState = Main.State.MAIN;
        try {
            Main.changeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
