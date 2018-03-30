package seu;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class App {

    public static int resource = 300;

    public static Logger logger;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");

    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入随机数种子：");
        int seed = scanner.nextInt();
        System.out.print("请输入本机端口：");
        int localPort = scanner.nextInt();
        System.out.print("请输入目标IP1：");
        String IP1 = scanner.next();
        System.out.print("请输入目标端口1：");
        int port1 = scanner.nextInt();
        System.out.print("请输入目标IP2：");
        String IP2 = scanner.next();
        System.out.print("请输入目标端口2：");
        int port2 = scanner.nextInt();
        System.out.println("输入参数结束");

        System.out.println("初始化...");
        Thread transmissionEventCreatorThread = new Thread(new TransmissionEventCreator(IP1, port1, IP2, port2, seed));
        Thread summationRequestCreatorThread = new Thread(new SummationRequestCreator(IP1, port1, IP2, port2));
        Thread receiverThread = new Thread(new Receiver(localPort));
        logger = new Logger(localPort);
        System.out.println("初始化结束");

        System.out.println("启动receiver");
        receiverThread.start();
        System.out.println("receiver启动结束");

        System.out.print("输入y启动sender：");
        if (scanner.next().equals("y")) {
            transmissionEventCreatorThread.start();
            summationRequestCreatorThread.start();
        }

        summationRequestCreatorThread.join();
        transmissionEventCreatorThread.join();
        receiverThread.join();
        logger.close();
        System.out.println("主程序结束");
    }

    public static long getRandomInterval(int second) {
        return Math.round(second * -1000 * Math.log(Math.random()));
    }
}
