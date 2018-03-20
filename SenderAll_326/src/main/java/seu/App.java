package seu;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.print("请输入种子：");
        Scanner scanner = new Scanner(System.in);
        int seed = scanner.nextInt();
        System.out.print("请输入超时阈值(ms)：");
        int timeout = scanner.nextInt();
        EventCreator eventCreator = new EventCreator(seed);
        eventCreator.start(timeout);
    }
}
