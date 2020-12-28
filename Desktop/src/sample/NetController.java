package sample;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Класс для соединения с сервером
 */

public class NetController {
    private final int portClient = 8080;
    Socket toServer;

    /**
     * Функция, единственная цель которой - отправлять на сервер строку JSON и читать ответ из нее.
     * Вызывается в коде каждый раз при нажатии на любую кнопку, кроме кнопки "назад".
     */
    public String sendToServer(String jsonString) throws IOException {
        // создаем сокет на сервер

        toServer = new Socket();
        toServer.connect(new InetSocketAddress("26.218.82.130", portClient), 3000);
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
