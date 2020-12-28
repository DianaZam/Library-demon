package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class SelectBookController {

    @FXML
    Label l_book_id;
    @FXML
    Label l_title;
    @FXML
    Label l_author;
    @FXML
    Label l_science_field;
    @FXML
    Label l_tags;
    @FXML
    Label l_publication_year;
    @FXML
    Label l_edition;
    @FXML
    Label l_stockpile;
    @FXML
    Button b_select;

    @FXML
    public void initialize(){
        int sel_id = SearchBooksController.sel_book_list.getSelectionModel().getSelectedIndex();
        System.out.println(sel_id);
        l_book_id.setText("" + SearchBooksController.b.get(sel_id).book_id);
        l_title.setText("" + SearchBooksController.b.get(sel_id).title);
        l_author.setText("" + SearchBooksController.b.get(sel_id).author);
        l_science_field.setText("" + SearchBooksController.b.get(sel_id).science_field);
        l_tags.setText("" + SearchBooksController.b.get(sel_id).key_words);
        l_publication_year.setText("" + SearchBooksController.b.get(sel_id).publication_year);
        l_edition.setText("" + SearchBooksController.b.get(sel_id).edition);
        l_stockpile.setText(getStockpile(l_book_id.getText()));
        if (l_stockpile.getText().equals("0"))
            b_select.setDisable(true);
    }

    @FXML
    public void cancel() {
        Main.GlobalState = Main.State.SEARCH_BOOKS;
        try {
            Main.changeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void select(){
        String result = "null";
        try {
            String answer = Main.controller.sendToServer(JSONBuilder.makeJsonGiveBook(l_book_id.getText(), SearchPeopleController.person_id));
            JSONObject object = (JSONObject) new JSONParser().parse(answer);
            result = object.get("result").toString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        if (result.equals("false")){
            //error_field
        }
        else if (result.equals("true"))
            Main.GlobalState = Main.State.SEARCH_BOOKS;
        try {
            Main.changeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getStockpile(String book_id){
        int count = -1;
        try {
            String answer = Main.controller.sendToServer(JSONBuilder.makeJsonGetStockpile(book_id));
            JSONObject object = (JSONObject) new JSONParser().parse(answer);
            count = ((Long)object.get("count")).intValue();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return ""+count;
    }
}

