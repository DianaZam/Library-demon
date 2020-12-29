import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class AddBookDialog extends JDialog {
    private JPanel contentPane;
    private JButton addBookButton;
    private JTextField titleField;
    private JTextPane resultPlane;
    private JTextField authorField;
    private JTextField scienceField;
    private JTextField keyWordsField;
    private JTextField yearField;
    private JTextField editionField;
    private JTextField storageField;
    private JTextField countField;
    private JSONObject answerJSON;


    public AddBookDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(addBookButton);


        addBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onAdd();
                } catch (IOException ex) { }
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

    private void onAdd() throws IOException {
        boolean error = false;
        String errorMessage = "";
        int publication_year = 0;
        int edition = 0;
        int storage_id = 0;
        int count = 0;
        try {
            if(!yearField.getText().isEmpty())
            publication_year = Integer.valueOf(yearField.getText());
        }
        catch (NumberFormatException e){
            errorMessage+="Год должен быть числом!\n";
            error = true;
        }
        try {
            if (!editionField.getText().isEmpty()) {
                edition = Integer.valueOf(editionField.getText());
                if (edition <= 0) {
                    errorMessage += "Издание должно быть числом больше 0!\n";
                    error = true;
                }
            }
        }
        catch (NumberFormatException e){
            errorMessage+="Издание должно быть числом!\n";
            error = true;
        }
        try {
            if(!storageField.getText().isEmpty()) {
                storage_id = Integer.valueOf(storageField.getText());
                if (storage_id <= 0) {
                    errorMessage += "Код места должен быть числом больше 0!\n";
                    error = true;
                }
            }
        }
        catch (NumberFormatException e){
            errorMessage+="Код места  должен быть числом!\n";
            error = true;
        }
        try {
            if(!countField.getText().isEmpty()) {
                count = Integer.valueOf(countField.getText());
                if (count <= 0) {
                    error = true;
                    errorMessage += "Количество должно быть числом больше 0!\n";
                }
            }
        }
        catch (NumberFormatException e){
            errorMessage+="Количество должно быть числом больше 0!\n";
            error = true;
        }

        if (titleField.getText().isEmpty()|| authorField.getText().isEmpty()|| scienceField.getText().isEmpty()|| keyWordsField.getText().isEmpty()
                || yearField.getText().isEmpty() || editionField.getText().isEmpty()|| storageField.getText().isEmpty()|| countField.getText().isEmpty()){
            error=true;
            errorMessage+="Все поля должны быть заполнены!\n";
        }
        if (error) resultPlane.setText(errorMessage);
        else{
            JSONObject requestJSON = new JSONObject();
            requestJSON.put("method", "AddBook");
            requestJSON.put("title", titleField.getText());
            requestJSON.put("author",authorField.getText());
            requestJSON.put("key_words",keyWordsField.getText());
            requestJSON.put("science_field",scienceField.getText());
            requestJSON.put("edition",edition);
            requestJSON.put("publication_year",publication_year);
            requestJSON.put("storage_id",storage_id);
            requestJSON.put("count",count);
            answerJSON = (JSONObject) JSONValue.parse(RequestSender.sendToServer(requestJSON.toJSONString()));

            resultPlane.setText("ID новой книги: "+ (Long) answerJSON.get("book_id"));
        }

    }

    private void onCancel() {
        dispose();
    }

}
