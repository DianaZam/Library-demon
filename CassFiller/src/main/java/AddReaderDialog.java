import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class AddReaderDialog extends JDialog {
    private JPanel contentPane;
    private JTextPane resultPlane;
    private JButton addReaderButton;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField middleNameField;
    private JTextField passportField;
    private JTextField birthdayField;
    private JSONObject answerJSON;

    public AddReaderDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(addReaderButton);

        addReaderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onAdd();
                } catch (IOException ex) {}
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
        int day;
        int month;
        int year;
        try {
            if (!passportField.getText().isEmpty()) {
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
            requestJSON.put("method", "AddReader");
            requestJSON.put("passport", passportField.getText());
            requestJSON.put("first_name",firstNameField.getText());
            requestJSON.put("middle_name",middleNameField.getText());
            requestJSON.put("last_name",lastNameField.getText());
            requestJSON.put("birthday",birthdayField.getText());

            answerJSON = (JSONObject) JSONValue.parse(RequestSender.sendToServer(requestJSON.toJSONString()));

            resultPlane.setText("ID нового читателя: "+ (Long) answerJSON.get("card_id"));
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


}
