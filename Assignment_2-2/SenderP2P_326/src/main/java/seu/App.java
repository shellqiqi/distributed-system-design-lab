package seu;

import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException {
        System.out.print("请输入种子：");
        Scanner scanner = new Scanner(System.in);
        int seed = scanner.nextInt();
        System.out.print("请输入超时阈值(ms)：");
        int timeout = scanner.nextInt();
        System.out.print("请主机1的IP（监听端口888）：");
        String host1 = scanner.next();
        System.out.print("请主机2的IP（监听端口999）：");
        String host2 = scanner.next();
        EventCreator eventCreator = new EventCreator(seed);
        eventCreator.start(host1, host2, timeout);
    }
}
