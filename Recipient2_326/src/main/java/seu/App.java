package seu;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("监听端口999");
        Recipient recipient = new Recipient();
        recipient.start(999);
    }
}
