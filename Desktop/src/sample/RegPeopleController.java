package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Класс для управления окном регистрации
 */
public class RegPeopleController {

    /**
     * Джысон парсер для сборки запроса и чтения ответа
     */
    JSONParser parser = new JSONParser();
    @FXML
    private Button reg_button;
    @FXML
    private TextField first_name;
    @FXML
    private TextField last_name;
    @FXML
    private TextField middle_name;
    @FXML
    private TextField passport;
    @FXML
    private DatePicker birthdate;
    @FXML
    private Label new_id;
    @FXML
    private Label error_field;

    /**
     * Метод, вызываемый при инициализации окна(каждый раз при появлении окна)
     */
    @FXML
    public void initialize(){
        error_field.setText("");
    }

    /**
     * Метод, возвращающий в главное меню
     */
    @FXML
    public void toMainMenu(){
        Main.GlobalState = Main.State.MAIN;
        try {
            Main.changeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод, который "слизывает" все с полей и готовит запрос на сервер
     */
    @FXML
    public void regPerson(){
        error_field.setText("");
        //Если какое-то из полей пустое, вывод ошибки
        if (first_name.getText().equals("") ||
        last_name.getText().equals("") ||
        middle_name.getText().equals("") ||
        passport.getText().equals("") ||
        birthdate.getValue() == null
        ) {
            error_field.setText("Заполните все поля!");
            return;
        }

        //Приведение даты рождения к формату запроса
        String birthday = "";
        if (birthdate.getValue().getDayOfMonth() < 10)
            birthday += "0";
        birthday += birthdate.getValue().getDayOfMonth() + ".";
        if (birthdate.getValue().getMonth().getValue() < 10)
            birthday += "0";
        birthday += birthdate.getValue().getMonth().getValue() + ".";
        birthday += birthdate.getValue().getYear();

        //Отправка запроса на сервер и чтение ответа
        String answer = "";
        try {
            System.out.println();
            answer = Main.controller.sendToServer(JSONBuilder.makeJsonAddReader(
                    passport.getText(),
                    first_name.getText(),
                    middle_name.getText(),
                    last_name.getText(),
                    birthday));
        } catch (IOException e) {
            e.printStackTrace();
            //если тут возникли проблемы - проблемы с сервером, не иначе
            error_field.setText("Проблемы с подключением к серверу, попробуйте еще раз через несколько секунд.");
            return;
        }
        try {
            //Пытаемся распарсить ответ
            System.out.println(answer);
            JSONObject obj = (JSONObject)parser.parse(answer);
            String id = "" + ((Long)(obj.get("card_id"))).intValue();
            //Новый id заносится в интерфейс
            System.out.println("id=" + id);
            new_id.setText(id);
            reg_button.setDisable(true);
        } catch (ParseException e) {
            e.printStackTrace();
            //Вывод ошибки если не смогли прочитать json
            error_field.setText("Сервер отправил неожиданные данные.");
        }
    }
}