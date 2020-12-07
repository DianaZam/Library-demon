public class Starter {
    public static void main(String[] args) {
        MainFrame dialog = new MainFrame();
        dialog.setMainFrame(dialog);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
