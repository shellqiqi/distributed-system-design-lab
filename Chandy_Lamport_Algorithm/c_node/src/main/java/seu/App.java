package seu;

import seu.pojo.Snapshot;
import seu.simulation.SimulateApp;
import seu.socket.Sender;

import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import static seu.utility.ConfigUtil.*;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入对端IP：");
        OPPOSITE_IP = scanner.next();
        System.out.print("请输入资源转移次数：");
        TRANSMIT_TIMES = scanner.nextInt();
        System.out.print("请输入快照次数：");
        SNAPSHOT_TIMES = scanner.nextInt();
        System.out.print("请输入随机数种子：");
        RANDOM = new Random(scanner.nextInt());

        System.out.println("快照答案：");
        SimulateApp simulateApp = new SimulateApp();
        for (Map.Entry<Integer, Snapshot> snapshot : simulateApp.snapshots.entrySet()) {
            System.out.print(snapshot.getKey() + ": " + snapshot.getValue().toString() + " total: ");
            System.out.println(snapshot.getValue().total());
        }

        Sender sender = new Sender(simulateApp.controllerMessageSequence);

        System.out.println("主程序结束");
    }
}
