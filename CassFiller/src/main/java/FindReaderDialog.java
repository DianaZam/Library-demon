import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class FindReaderDialog extends JDialog {
    private JPanel contentPane;
    private JTextPane resultPlane;
    private JButton findReaderButton;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField cardIdField;
    private JList <String> readerList;
    private JTextField middleNameField;
    private JTextField passportField;
    private JSONArray readers;
    private JSONObject reader;
    private FindReaderDialog dialog;


    public FindReaderDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(findReaderButton);

        resultPlane.setText("Поиск ведётся по id, или пасспорту, или ФИО, или ФИ.");

        findReaderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onFind();
                } catch (IOException ex) { }
            }
        });

        readerList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList readerList = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    int index = readerList.locationToIndex(evt.getPoint());
                    reader = (JSONObject) readers.get(index);
                    dialog.contentPane.setVisible(false);
                    ChangeReaderDialog changeReaderDialog = new ChangeReaderDialog(reader);
                    changeReaderDialog.pack();
                    changeReaderDialog.setVisible(true);
                    dialog.contentPane.setVisible(true);
                    try {
                        dialog.onFind();
                    } catch (IOException e) { }
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
        int card_id=0;
        String errorMessage = "";
        try {
            if(!passportField.getText().isEmpty()){
                long passport = Long.valueOf(passportField.getText());
                if(passportField.getText().length()!=10){
                    error=true;
                    errorMessage+="Пасспорт не указан или указан неверно .\n";
                }
            }
        }
        catch (NumberFormatException e){
            error=true;
            errorMessage+="Формат пасспорта неверен.\n";
        }
        try {
            if (!cardIdField.getText().isEmpty()) {
                card_id = Integer.valueOf(cardIdField.getText());
                if (card_id <= 0) {
                    error = true;
                    errorMessage += "ID должен быть больше 0!\n";
                }
            }
        }
        catch (NumberFormatException e){
            if(!cardIdField.getText().isEmpty()) {
                errorMessage += "ID должно быть числом!\n";
                error = true;
            }
        }


        if (firstNameField.getText().isEmpty() && lastNameField.getText().isEmpty() && middleNameField.getText().isEmpty()&&
                passportField.getText().isEmpty()  && cardIdField.getText().isEmpty() ){
            error=true;
            errorMessage+="Заполните хотя бы 1 поле!\n";
        }else if ((firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) &&
                passportField.getText().isEmpty()  && cardIdField.getText().isEmpty() ){
            error=true;
            errorMessage+="Заполните (id) или (паспорт) или (ФИО и/или ФИ)!\n";
        }
        if (error) resultPlane.setText(errorMessage);
        else{

            JSONObject requestJSON = new JSONObject();
            requestJSON.put("method", "GetReaders");
            requestJSON.put("card_id", card_id);
            if (passportField.getText().isEmpty())requestJSON.put("passport", null);
            else requestJSON.put("passport", passportField.getText());
            if (firstNameField.getText().isEmpty())requestJSON.put("first_name", null);
            else requestJSON.put("first_name", firstNameField.getText());
            if (middleNameField.getText().isEmpty())requestJSON.put("middle_name", null);
            else requestJSON.put("middle_name", middleNameField.getText());
            if (lastNameField.getText().isEmpty())requestJSON.put("last_name", null);
            else requestJSON.put("last_name", lastNameField.getText());


            JSONObject answerJSON = (JSONObject) JSONValue.parse(RequestSender.sendToServer(requestJSON.toJSONString()));
            readers = (JSONArray) answerJSON.get("readers");


            DefaultListModel dlm = new DefaultListModel();
            for (String r: jsonArrayToString(readers)){
                dlm.addElement(r);
            }
            readerList.setModel(dlm);




            resultPlane.setText("Для изменения данных читателя дважды щёлкните по нужной записи.\n" +
                    "Если поиск не дал результата - порверьте введённые данные.");
        }

    }

    private void onCancel() {

        dispose();
    }

    private String[] jsonArrayToString(JSONArray arr){
        String[] strings = new String[arr.size()];
        for(int i=0; i< arr.size(); i++){
            JSONObject reader = (JSONObject) arr.get(i);
            strings[i]=((Long) reader.get("card_id")+", "+(String) reader.get("last_name")
                    +" "+(String) reader.get("first_name")+" "+(String) reader.get("middle_name")
                    +", "+(String) reader.get("birthday"));
        }

        return  strings;
    }

    public void setDialog(FindReaderDialog dialog) {
        this.dialog = dialog;
    }

    public FindReaderDialog getDialog() {
        return dialog;
    }
}
