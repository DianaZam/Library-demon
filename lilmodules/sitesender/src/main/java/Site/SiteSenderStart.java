package Site;

public class SiteSenderStart {
    public static void main(String[] args) {
        System.out.println("Я сайтовик.");
        SiteSender siteSender = new SiteSender();
        try {
            siteSender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
