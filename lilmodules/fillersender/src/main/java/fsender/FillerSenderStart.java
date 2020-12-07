package fsender;

public class FillerSenderStart {
    public static void main(String[] args) {
        System.out.println("Я есть кладовка.");
        FillerSender fillerSender = new FillerSender();
        try {
            fillerSender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
