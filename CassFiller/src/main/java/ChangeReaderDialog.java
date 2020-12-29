import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class ChangeReaderDialog extends JDialog {
    private JPanel contentPane;
    private JTextPane resultPlane;
    private JButton changeReaderButton;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField middleNameField;
    private JTextField passportField;
    private JTextField birthdayField;
    private JSONObject answerJSON;
    private JSONObject readerJSON;


    public ChangeReaderDialog(JSONObject readerJSON) {
        this.readerJSON=readerJSON;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(changeReaderButton);

        firstNameField.setText((String) readerJSON.get("first_name"));
        lastNameField.setText((String) readerJSON.get("last_name"));
        middleNameField.setText((String) readerJSON.get("middle_name"));
        passportField.setText((String) readerJSON.get("passport"));
        birthdayField.setText((String) readerJSON.get("birthday"));

        changeReaderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onChange();
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


    private void onChange() throws IOException {
        boolean error = false;
        String errorMessage = "";
        int day;
        int month;
        int year;
        try {
            if(!passportField.getText().isEmpty()) {
                long passport = Long.valueOf(passportField.getText());
                if (passportField.getText().length() != 10) {
                    error = true;
                    errorMessage += "Пасспорт не указан или указан неверно .\n";
                }
            }
        }
        catch (NumberFormatException e){
            error=true;
            errorMessage+="Формат пасспорта неверен.\n";
        }
        try {
            if(!birthdayField.getText().isEmpty()){
                day = Integer.valueOf(birthdayField.getText().substring(0,2));
                if (day<1 || day>31 ){
                    error=true;
                    errorMessage+="День указан некорректно.\n";
                }
                month = Integer.valueOf(birthdayField.getText().substring(3,5));
                if (month<1 || month>12 ){
                    error=true;
                    errorMessage+="Месяц указан некорректно.\n";
                }
                year = Integer.valueOf(birthdayField.getText().substring(6));
                if (year<0 || year>2100 ){
                    error=true;
                    errorMessage+="Год указан некорректно.\n";
                }
                if (!birthdayField.getText().substring(2,3).equals(".")||!birthdayField.getText().substring(5,6).equals(".")){
                    error=true;
                    errorMessage+="Разделителями должны быть точки.\n";
                }
            }
        }
        catch (NumberFormatException e){
            errorMessage+="Дата некорректна.\n";
            error = true;
        }

        if (firstNameField.getText().isEmpty()|| lastNameField.getText().isEmpty()|| middleNameField.getText().isEmpty()||
                passportField.getText().isEmpty()  || birthdayField.getText().isEmpty() ){
            error=true;
            errorMessage+="Все поля должны быть заполнены!\n";
        }
        if (error) resultPlane.setText(errorMessage);
        else{
            JSONObject requestJSON = new JSONObject();
            requestJSON.put("method", "ChangeReader");
            requestJSON.put("card_id", (Long) readerJSON.get("card_id"));
            requestJSON.put("passport", passportField.getText());
            requestJSON.put("first_name",firstNameField.getText());
            requestJSON.put("middle_name",middleNameField.getText());
            requestJSON.put("last_name",lastNameField.getText());
            requestJSON.put("birthday",birthdayField.getText());

            answerJSON = (JSONObject) JSONValue.parse(RequestSender.sendToServer(requestJSON.toJSONString()));

            resultPlane.setText("Читатель "+ (Long) readerJSON.get("card_id")+" изменён.");
        }

    }


    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
