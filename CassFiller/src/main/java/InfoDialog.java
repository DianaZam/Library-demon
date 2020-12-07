
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class InfoDialog extends JDialog {
    private JPanel contentPane;
    private JButton infoButton;
    private JButton readersButton;
    private JButton booksButton;
    private JTextPane textPane1;
    private JSONObject answerJSON;


    public InfoDialog() throws IOException {
        setContentPane(contentPane);
        setModal(true);

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("method", "Info");
        answerJSON = (JSONObject) JSONValue.parse(RequestSender.sendToServer(requestJSON.toJSONString()));


        infoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onInfo();
            }
        });

        readersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onReaders();
            }
        });

        booksButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBooks();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onInfo() {
        textPane1.setText("БД: Cassandra.");
    }

    private void onReaders() {
        textPane1.setText("Количество читателей: " + (Long) answerJSON.get("reader_count"));
    }

    private void onBooks() {
        textPane1.setText("Книг в БД: " + (Long) answerJSON.get("book_count"));
    }

    private void onCancel() {
        dispose();
    }

}
