package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sample.structures.Book;

import java.io.IOException;
import java.util.ArrayList;

public class SearchBooksController {

    public static ArrayList<Book> b = new ArrayList<>();
    public static ListView<String> sel_book_list = new ListView<>();

    @FXML
    public ListView<String> book_list;
    @FXML
    Label error_field;
    @FXML
    TextField t_book_id;
    @FXML
    TextField t_title;
    @FXML
    TextField t_author;
    @FXML
    TextField t_science_field;
    @FXML
    TextField t_tags;

    @FXML
    public void initialize(){
        error_field.setText("");
        b.clear();
        book_list.getItems().clear();
    }
    @FXML
    public void back(){
        Main.GlobalState = Main.State.SEARCH_PEOPLE;
        try {
            Main.changeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void searchBooks(){
        if (t_author.getText().equals("") && t_title.getText().equals("") && t_science_field.getText().equals("") && t_tags.getText().equals("") && t_book_id.getText().equals("")) {
            error_field.setText("Заполните хотя бы одно поле для поиска!");
            return;
        }
        b.clear();
        book_list.getItems().clear();
        String answer = "null";
        try {
            answer = Main.controller.sendToServer(JSONBuilder.makeJsonGetBooks(t_book_id.getText(), t_title.getText(), t_author.getText(), t_science_field.getText(), t_tags.getText()));
        } catch (IOException e) {
            error_field.setText("Нет соединения с интернетом, либо сервер не отвечает.");
            e.printStackTrace();
        }
        try {
            JSONObject json_answer = (JSONObject) new JSONParser().parse(answer);
            if (((JSONObject)json_answer.get("data")).get("count").toString().equals("0")) {
                //error_field
                return;
            }
            fillBooks((JSONObject)json_answer.get("data"));
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        for (Book book : b) {
            book_list.getItems().add("\"" + book.title + "\" " + book.author);
        }
        error_field.setText("");
    }
    @FXML
    public void selectBook(){
        int id = book_list.getSelectionModel().getSelectedIndex();
        if (id == -1){
            error_field.setText("Выделите нужную книгу!");
            return;
        }
        sel_book_list = book_list;
        Main.GlobalState = Main.State.SELECT_BOOK;
        try {
            Main.changeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void onInput(){
        if (t_author.getText().equals("") && t_title.getText().equals("") &&  t_science_field.getText().equals("") && t_tags.getText().equals("") && t_book_id.getText().equals("")){
            t_author.setDisable(false);
            t_title.setDisable(false);
            t_science_field.setDisable(false);
            t_tags.setDisable(false);
            t_book_id.setDisable(false);
        }
        if ((!t_author.getText().equals("") || !t_title.getText().equals("")) &&  t_science_field.getText().equals("") && t_tags.getText().equals("") && t_book_id.getText().equals("")){
            t_author.setDisable(false);
            t_title.setDisable(false);
            t_science_field.setDisable(true);
            t_tags.setDisable(true);
            t_book_id.setDisable(true);
        }
        if (t_author.getText().equals("") && t_title.getText().equals("") &&  (!t_science_field.getText().equals("") || !t_tags.getText().equals("")) && t_book_id.getText().equals("")){
            t_author.setDisable(true);
            t_title.setDisable(true);
            t_science_field.setDisable(false);
            t_tags.setDisable(false);
            t_book_id.setDisable(true);
        }
        if (t_author.getText().equals("") && t_title.getText().equals("") && t_science_field.getText().equals("") && t_tags.getText().equals("") && !t_book_id.getText().equals("")){
            t_author.setDisable(true);
            t_title.setDisable(true);
            t_science_field.setDisable(true);
            t_tags.setDisable(true);
            t_book_id.setDisable(false);
        }
    }

    private void fillBooks(JSONObject data){
        JSONArray books = (JSONArray) data.get("books");
        Book temp;
        for (Object book : books) {
            temp = new Book();
            JSONObject t_book = (JSONObject) book;
            temp.publication_year = ((Long) t_book.get("publication_year")).intValue();
            temp.author = t_book.get("author").toString();
            temp.storage_id = ((Long) t_book.get("storage_id")).intValue();
            temp.key_words = t_book.get("key_words").toString();
            temp.edition = ((Long) t_book.get("edition")).intValue();
            temp.book_id = ((Long) t_book.get("book_id")).intValue();
            temp.title = t_book.get("title").toString();
            temp.science_field = t_book.get("science_field").toString();
            b.add(temp);
        }
    }
}
