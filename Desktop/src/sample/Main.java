package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Главный класс
 */
public class Main extends Application {
    public enum State{MAIN, SEARCH_PEOPLE, SEARCH_BOOKS, REG_PEOPLE, ACC_INFO, SELECT_BOOK}

    /**
     * Система "состояний" программы - на каком
     * она окне в данный момент определяется полем GlobalState.
     * Все возможные состояния программы описаны в поле State
     */
    public static State GlobalState = State.MAIN;
    static Parent root;
    static Stage primaryStage;

    /**
     * Общественный контроллер для отправки
     * данных на сервер (им пользуются все классы)
     */
    public static NetController controller = new NetController();

    /**
     * Метод, инициализируюзий стартовое окно
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
        changeWindow();
        Main.primaryStage.setTitle("Library Client");
        Main.primaryStage.setResizable(false);
        Main.primaryStage.setAlwaysOnTop(true);
        Main.primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("images/logo.png")));
        Main.primaryStage.show();
    }

    /**
     * Метод, меняющий текущее окно на другое
     */
    static public void changeWindow() throws IOException {
        //подгружаем файлик нужный разметки
        if (GlobalState == State.MAIN)
            root = FXMLLoader.load(Main.class.getResource("frontend/main.fxml"));
        if (GlobalState == State.REG_PEOPLE)
            root = FXMLLoader.load(Main.class.getResource("frontend/reg_people.fxml"));
        if (GlobalState == State.SEARCH_BOOKS)
            root = FXMLLoader.load(Main.class.getResource("frontend/search_books.fxml"));
        if (GlobalState == State.SEARCH_PEOPLE)
            root = FXMLLoader.load(Main.class.getResource("frontend/search_people.fxml"));
        if (GlobalState == State.ACC_INFO)
            root = FXMLLoader.load(Main.class.getResource("frontend/account_info.fxml"));
        if (GlobalState == State.SELECT_BOOK)
            root = FXMLLoader.load(Main.class.getResource("frontend/select_book.fxml"));
        //ставим его основным
        primaryStage.setScene(new Scene(root));
        //показываем пользователю
        primaryStage.show();
    }

    /**
     * Поехали!
     */
    public static void main(String[] args) {
        launch(args);
    }
}
