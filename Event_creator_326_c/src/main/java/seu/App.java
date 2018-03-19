package seu;

import java.util.Scanner;

public class App {
    public static void main(String[] args) throws InterruptedException {
        System.out.print("请输入种子：");
        Scanner scanner = new Scanner(System.in);
        int seed = scanner.nextInt();
        EventCreator eventCreator = new EventCreator(seed);
        eventCreator.start();
    }
}
