package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sample.structures.Book;

import java.io.IOException;
import java.util.ArrayList;

public class SearchPeopleController {
    JSONParser parser = new JSONParser();
    public static String person = "";
    public static String person_id = "";
    public static ArrayList<Book> lentbooks = new ArrayList<>();
    @FXML
    private TextField name;
    @FXML
    private Label error_field;
    @FXML
    private TextField card_id;
    @FXML
    private TextField passport_id;
    @FXML
    private Button search_card;
    @FXML
    private Button search_passport;
    @FXML
    private Button search_name;

    @FXML
    public void initialize(){
        person="";
        person_id="";
        lentbooks.clear();
        error_field.setText("");
    }
    @FXML
    public void searchCard(){
        search();
    }
    @FXML
    public void searchPassport(){
        search();
    }
    @FXML
    public void searchName(){
        search();
    }
    @FXML
    public void onInput(){
        if (name.getText().equals("") && passport_id.getText().equals("") && card_id.getText().equals("")){
            name.setDisable(false);
            passport_id.setDisable(false);
            card_id.setDisable(false);
            search_name.setDisable(false);
            search_passport.setDisable(false);
            search_card.setDisable(false);
        }
        if (!name.getText().equals("") && passport_id.getText().equals("") && card_id.getText().equals("")){
            name.setDisable(false);
            passport_id.setDisable(true);
            card_id.setDisable(true);
            search_name.setDisable(false);
            search_passport.setDisable(true);
            search_card.setDisable(true);
        }
        if (name.getText().equals("") && !passport_id.getText().equals("") && card_id.getText().equals("")){
            name.setDisable(true);
            passport_id.setDisable(false);
            card_id.setDisable(true);
            search_name.setDisable(true);
            search_passport.setDisable(false);
            search_card.setDisable(true);
        }
        if (name.getText().equals("") && passport_id.getText().equals("") && !card_id.getText().equals("")){
            name.setDisable(true);
            passport_id.setDisable(true);
            card_id.setDisable(false);
            search_name.setDisable(true);
            search_passport.setDisable(true);
            search_card.setDisable(false);
        }
    }
    @FXML
    public void toMainMenu(){
        Main.GlobalState = Main.State.MAIN;
        try {
            Main.changeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void search(){
        error_field.setText("");
        if (name.getText().equals("") &&
                card_id.getText().equals("") &&
                passport_id.getText().equals("")) {
            error_field.setText("Заполните хотя бы одно поле!");
            return;
        }
        boolean success_search = false;
        String answer = "";
        try {
            String[] FIO = name.getText().split(" ");
            if (FIO.length >= 2)
                answer = Main.controller.sendToServer(JSONBuilder.makeJsonGetReader(card_id.getText(), passport_id.getText(), FIO[2].replace("_"," "), FIO[1].replace("_"," "), FIO[0].replace("_"," ")));
            else
                answer = Main.controller.sendToServer(JSONBuilder.makeJsonGetReader(card_id.getText(), passport_id.getText(), "", "", ""));
            if (!answer.equals(""))
                success_search = true;
        } catch (IOException e) {
            e.printStackTrace();
            error_field.setText("Нет соединения с интернетом, либо сервер не отвечает.");
        }
        if (success_search)
            try {
                JSONObject jsonObject = (JSONObject) parser.parse(answer);
                System.out.println(jsonObject.toString());
                if (((Long)jsonObject.get("status")).intValue() == 500) {
                    error_field.setText("Такого пользователя не существует.");
                    return;
                }
                person += jsonObject.get("last_name") + " " + jsonObject.get("first_name") + " " + jsonObject.get("middle_name");
                person_id = ""+((Long)jsonObject.get("card_id")).intValue();
                getBooks(jsonObject);
                Main.GlobalState = Main.State.ACC_INFO;
                Main.changeWindow();
            } catch (IOException | ParseException e) {
                error_field.setText("Ошибка чтения ответа с сервера.");
                e.printStackTrace();
            }
    }
    public void getBooks(JSONObject jsonObject){
        lentbooks.clear();
        JSONObject data = (JSONObject) jsonObject.get("data");
        JSONArray array = (JSONArray) data.get("books");
        for (int i = 0; i < array.size(); i++) {
            JSONObject book_id = (JSONObject) array.get(i);
            Book b = new Book();
            System.out.println(book_id.toString());
            b.book_id = (int)(long)book_id.get("book_id");
            String bookAnswer = "null";
            try {
                bookAnswer = Main.controller.sendToServer(JSONBuilder.makeJsonGetBook(""+b.book_id));
                JSONObject book_JO = (JSONObject) parser.parse(bookAnswer);
                b.title = book_JO.get("title").toString();
                b.author = book_JO.get("author").toString();
                b.science_field = book_JO.get("science_field").toString();
                b.key_words = book_JO.get("key_words").toString();
                b.publication_year = ((Long)book_JO.get("publication_year")).intValue();
                b.edition = ((Long)book_JO.get("edition")).intValue();
                b.storage_id = ((Long)book_JO.get("storage_id")).intValue();
            } catch (IOException | ParseException e) {
                error_field.setText("Ошибка чтения ответа с сервера.");
                e.printStackTrace();
            }
            lentbooks.add(b);
        }
    }

}

