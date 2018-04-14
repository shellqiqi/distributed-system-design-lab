package seu;

import java.util.Random;
import java.util.Scanner;

import static seu.ConfigUtil.*;

public class App {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入对端IP：");
        OPPOSITE_IP = scanner.next();
        System.out.print("请输入资源转移次数：");
        TRANSMIT_TIMES = scanner.nextInt();
        System.out.print("请输入快照次数：");
        SNAPSHOT_TIMES = scanner.nextInt();
        System.out.print("请输入随机数种子：");
        RANDOM = new Random(scanner.nextInt());

        System.out.println("启动Receiver");
        Thread receiver = new Thread(new Receiver());
        receiver.start();
        System.out.println("Receiver启动结束");

        receiver.join();
        System.out.println("主程序结束");
    }
}
