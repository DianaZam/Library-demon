package Desk;

public class DeskSenderStart {
    public static void main(String[] args) {
        System.out.println("Я есть десктоп.");
        DeskSender deskSender = new DeskSender();
        try {
            deskSender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
