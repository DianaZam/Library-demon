import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RequestSender {

    static Socket toServer;

    public static String sendToServer(String jsonString) throws IOException {
        // создаем сокет на сервер
        toServer = new Socket("127.00.1", 8282);
        // создаем поток вывода
        ObjectOutputStream oos = new ObjectOutputStream(toServer.getOutputStream());
        // создаем поток записи
        ObjectInputStream ois = new ObjectInputStream(toServer.getInputStream());
        // в поток вывода пихаем принятую строку джысон
        oos.writeUTF(jsonString);
        // смываем(отправляем) все что у нас накопилось
        oos.flush();
        // в строку записываем ответ (в это время прога висит и лучше ее не трогать)
        String answer = ois.readUTF();
        // закрываем потоки
        oos.close();
        ois.close();
        // отключаемся от сервака
        toServer.close();
        return answer;
    }
}
