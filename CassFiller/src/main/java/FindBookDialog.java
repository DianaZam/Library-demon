import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class FindBookDialog extends JDialog {
    private JPanel contentPane;
    private JTextPane resultPlane;
    private JButton findBookButton;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField bookIdField;
    private JList bookList;
    private JTextField scienceField;
    private JTextField keyWordsField;
    private JSONArray books;
    private JSONObject book;
    private FindBookDialog dialog;


    public FindBookDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(findBookButton);

        resultPlane.setText("Поиск ведётся по id, или (названию и/или автору), или (научной области и/или ключевым словам). Если не ввести ни один параметр - в списке выведутся все книги в БД.");

        findBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onFind();
                } catch (IOException ex) {
                }
            }
        });

        bookList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList bookList = (JList) evt.getSource();
                if (evt.getClickCount() == 2) {
                    int index = bookList.locationToIndex(evt.getPoint());
                    book = (JSONObject) books.get(index);
                    dialog.contentPane.setVisible(false);
                    ChangeBookDialog changeBookDialog = new ChangeBookDialog(book);
                    changeBookDialog.pack();
                    changeBookDialog.setVisible(true);
                    dialog.contentPane.setVisible(true);
                    try {
                        dialog.onFind();
                    } catch (IOException e) {
                    }
                }
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


    private void onFind() throws IOException {
        boolean error = false;
        String errorMessage = "";
        int book_id = 0;
        try {
            if (!bookIdField.getText().isEmpty()) {
                book_id = Integer.valueOf(bookIdField.getText());
                if (book_id <= 0) {
                    errorMessage += "ID должно быть числом больше 0!\n";
                    error = true;
                }
            }
        } catch (NumberFormatException e) {
            errorMessage += "ID должно быть числом!\n";
            error = true;
        }


        if (error) resultPlane.setText(errorMessage);
        else {
            JSONObject requestJSON = new JSONObject();
            requestJSON.put("method", "GetBooks");
            requestJSON.put("book_id", book_id);
            if (titleField.getText().isEmpty()) requestJSON.put("title", null);
            else requestJSON.put("title", titleField.getText());
            if (authorField.getText().isEmpty()) requestJSON.put("author", null);
            else requestJSON.put("author", authorField.getText());
            if (keyWordsField.getText().isEmpty()) requestJSON.put("key_words", null);
            else requestJSON.put("key_words", keyWordsField.getText());
            if (scienceField.getText().isEmpty()) requestJSON.put("science_field", null);
            else requestJSON.put("science_field", scienceField.getText());

            JSONObject answerJSON = (JSONObject) JSONValue.parse(RequestSender.sendToServer(requestJSON.toJSONString()));
            books = (JSONArray) answerJSON.get("books");


            DefaultListModel dlm = new DefaultListModel();
            for (String r : jsonArrayToString(books)) {
                dlm.addElement(r);
            }
            bookList.setModel(dlm);


            resultPlane.setText("Для изменения данных читателя дважды щёлкните по нужной записи.\n" +
                    "Если поиск не дал результата - порверьте введённые данные.");
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private String[] jsonArrayToString(JSONArray arr) {
        String[] strings = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            JSONObject reader = (JSONObject) arr.get(i);
            strings[i] = ((Long) reader.get("book_id") + ", \"" + (String) reader.get("title")
                    + "\", " + (String) reader.get("author") + ", издание " + (Long) reader.get("edition")
                    + ", полка " + (Long) reader.get("storage_id"));
        }

        return strings;
    }

    public void setDialog(FindBookDialog dialog) {
        this.dialog = dialog;
    }

    public FindBookDialog getDialog() {
        return dialog;
    }


}
