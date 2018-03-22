package seu;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class EventCreator {

    private int counter0 = 0;
    private int counter1 = 0;
    private Random random;
    private SimpleDateFormat dateFormat;

    private ObjectOutputStream outputStreamFor888;
    private ObjectOutputStream outputStreamFor999;

    public EventCreator(int seed) {
        this.random = new Random(seed);
        this.dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
    }

    public void start(String host1, String host2, int timeout) throws IOException {
        Socket socketFor888 = new Socket(host1, 888);
        Socket socketFor999 = new Socket(host2, 999);
        outputStreamFor888 = new ObjectOutputStream(socketFor888.getOutputStream());
        outputStreamFor999 = new ObjectOutputStream(socketFor999.getOutputStream());
        System.out.printf("%-10s%-10s%-15s\n", "random1", "random2", "timer");
        Date start = new Date();
        for (int i = 1; i <= 20; i++) {
            if (new Date().getTime() - start.getTime() > timeout) {
                endBroadcast();
                break;
            }
            long interval = getRandomInterval();
            if (counter0 >= 15) {
                event1(interval);
                counter1++;
            } else if (counter1 >= 5) {
                event0(interval);
                counter0++;
            } else {
                int random = (this.random.nextFloat() >= 0.25f) ? 0 : 1;
                if (random == 0) {
                    event0(interval);
                    counter0++;
                } else {
                    event1(interval);
                    counter1++;
                }
            }
        }
        outputStreamFor888.close();
        outputStreamFor999.close();
        socketFor888.close();
        socketFor999.close();
        System.out.println("Done.");
    }

    /**
     * 单播
     * @param message 信息
     * @param port 端口
     */
    private void unicast(Message message, int port) {
        try {
            if (port == 888) {
                outputStreamFor888.writeObject(message);
                outputStreamFor888.flush();
                logLine(message);
            } else if (port == 999) {
                outputStreamFor999.writeObject(message);
                outputStreamFor999.flush();
                logLine(message);
            } else {
                throw new Exception("Unsupported port - Only port 888 and port 999 are supported.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 广播
     * @param message 信息
     */
    private void broadcast(Message message) {
        try {
            outputStreamFor888.writeObject(message);
            outputStreamFor999.writeObject(message);
            outputStreamFor888.flush();
            outputStreamFor999.flush();
            logLine(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void endBroadcast() {
        broadcast(new Message(2, 0,  dateFormat.format(new Date())));
    }

    /**
     * 事件0
     * @param interval 随机间隔
     */
    private void event0(long interval) {
        try {
            Thread.sleep(interval);
            delayForEvent0();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int random2 = new Random().nextInt(100) + 101;
        unicast(new Message(0, random2, dateFormat.format(new Date())), 888);
    }

    /**
     * 事件0推迟
     * @throws InterruptedException 由Thread.sleep抛出
     */
    private void delayForEvent0() throws InterruptedException {
        final int MILLISECOND = Calendar.getInstance().get(Calendar.MILLISECOND);
        if (MILLISECOND >= 150 && MILLISECOND <= 350 ||
                MILLISECOND >= 650 && MILLISECOND <= 850) {
        } else if (MILLISECOND < 150) {
            long nextMillisecond = Math.round(Math.random() * 0.2 * 1000) + 150;
            Thread.sleep(nextMillisecond - MILLISECOND);
        } else if (MILLISECOND > 850) {
            long nextMillisecond = Math.round(Math.random() * 0.2 * 1000) + 150;
            Thread.sleep(1000 - MILLISECOND + nextMillisecond);
        } else {
            long nextMillisecond = Math.round(Math.random() * 0.2 * 1000) + 650;
            Thread.sleep(nextMillisecond - MILLISECOND);
        }
    }

    /**
     * 事件1
     * @param interval 随机间隔
     */
    private void event1(long interval) {
        try {
            Thread.sleep(interval);
            delayForEvent1();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int random2 = new Random().nextInt(5) + 1;
        unicast(new Message(1, random2, dateFormat.format(new Date())), 999);
    }

    /**
     * 事件1推迟
     * @throws InterruptedException 由Thread.sleep抛出
     */
    private void delayForEvent1() throws InterruptedException {
        final int MILLISECOND = Calendar.getInstance().get(Calendar.MILLISECOND);
        if (MILLISECOND > 0 && MILLISECOND < 500) {
            Thread.sleep(500 - MILLISECOND);
        } else if (MILLISECOND > 500) {
            Thread.sleep(1000 - MILLISECOND);
        }
    }

    /**
     * 事件打印
     * @param message 信息
     */
    private void logLine(Message message) {
        System.out.printf("%-10d%-10d%-15s\n",
                message.getRandom1(),
                message.getRandom2(),
                message.getTimer());
    }

    /**
     * 随机间隔生成器
     * @return 随机间隔
     */
    private long getRandomInterval() {
        return Math.round(-6000 * Math.log(Math.random()));
    }
}
