package seu;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class App {

    public static int resource = 100;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入随机数种子：");
        int seed = scanner.nextInt();
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
        Thread senderThread = new Thread(new Sender(IP1, port1, IP2, port2, seed));
        //TODO: init receiver.
        System.out.println("初始化结束");

        System.out.println("启动receiver");
        //TODO: start receiver.
        System.out.println("receiver启动结束");

        System.out.print("输入y启动sender：");
        if (scanner.next().equals("y")) {
            senderThread.start();
        }

        //TODO: wait for receiver thread to join.
        senderThread.join();
        System.out.println("程序结束");
    }

    public static void log(String operation, InetSocketAddress target, int transfer) {
        System.out.printf("%-6s%-24s%-7d%-7d%-14s",
                operation,
                target.getAddress().getHostAddress() + ":" + target.getPort(),
                transfer,
                resource,
                dateFormat.format(new Date()));
    }

    public static long getRandomInterval(int second) {
        return Math.round(second * -1000 * Math.log(Math.random()));
    }
}
