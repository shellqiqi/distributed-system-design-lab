package seu;

import java.util.Scanner;

import static seu.ConfigUtil.*;

public class App {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入本机端口：");
        NODE = scanner.next().charAt(0);
        System.out.print("请输入对端IP：");
        OPPOSITE_IP = scanner.next();

        System.out.println("启动Receiver");
        Thread receiver = new Thread(new Receiver());
        receiver.start();
        System.out.println("Receiver启动结束");

        receiver.join();
        System.out.println("主程序结束");
    }
}
