import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class MainFrame extends JDialog {
    private MainFrame mainFrame;
    private JPanel contentPane;
    private JButton buttonInfo;
    private JButton buttoFindReader;
    private JButton buttonFindBook;
    private JButton buttonAddBook;
    private JButton buttonAddReader;
    private JTextPane warningTextPane;

    public MainFrame() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonInfo);

        buttonInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onInfo();
                } catch (IOException ex) {
                }
            }
        });

        buttonAddBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAddBook();
            }
        });

        buttonFindBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onFindBook();
            }
        });

        buttonAddReader.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAddReader();
            }
        });

        buttoFindReader.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onFindReader();
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

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    private void onInfo() throws IOException {
        mainFrame.contentPane.setVisible(false);
        InfoDialog info = new InfoDialog();
        // info.setLocation();
        info.pack();
        info.setVisible(true);
        mainFrame.contentPane.setVisible(true);
    }

    private void onAddBook() {
        mainFrame.contentPane.setVisible(false);
        AddBookDialog addBookDialog = new AddBookDialog();
        addBookDialog.pack();
        addBookDialog.setVisible(true);
        mainFrame.contentPane.setVisible(true);
    }

    private void onFindBook() {
        mainFrame.contentPane.setVisible(false);
        FindBookDialog findBookDialog = new FindBookDialog();
        findBookDialog.setDialog(findBookDialog);
        findBookDialog.pack();
        findBookDialog.setVisible(true);
        mainFrame.contentPane.setVisible(true);
    }

    private void onAddReader() {
        mainFrame.contentPane.setVisible(false);
        AddReaderDialog addReaderDialog = new AddReaderDialog();
        addReaderDialog.pack();
        addReaderDialog.setVisible(true);
        mainFrame.contentPane.setVisible(true);
    }

    private void onFindReader() {
        mainFrame.contentPane.setVisible(false);
        FindReaderDialog findReaderDialog = new FindReaderDialog();
        findReaderDialog.setDialog(findReaderDialog);
        findReaderDialog.pack();
        findReaderDialog.setVisible(true);
        mainFrame.contentPane.setVisible(true);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


}
