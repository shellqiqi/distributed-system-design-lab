package seu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class EventCreator {

    private int counter0 = 0;
    private int counter1 = 0;
    private Random random;
    private SimpleDateFormat dateFormat;

    public EventCreator(int seed) {
        this.random = new Random(seed);
        this.dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
    }

    public void start() throws InterruptedException {
        Date start, end;
        System.out.println("开始时间：" + dateFormat.format(start = new Date()));
        System.out.printf("%-4s%-5s%-13s%-8s\n", "序号", "1/0", "时刻", "间隔(ms)");
        for (int i = 1; i <= 20; i++) {
            long interval = getRandomInterval();
            Thread.sleep(interval);

            String time = dateFormat.format(new Date());
            if (counter0 >= 15) {
                logLine(i, 1, time, interval);
                counter1++;
            } else if (counter1 >= 5) {
                logLine(i, 0, time, interval);
                counter0++;
            } else {
                int random = (this.random.nextFloat() >= 0.25f) ? 0 : 1;
                logLine(i, random, time, interval);
                if (random == 0) counter0++;
                else counter1++;
            }
        }
        System.out.println("结束时间：" + dateFormat.format(end = new Date()));
        System.out.println("运行时间(ms)：" + (end.getTime() - start.getTime()));
    }

    /**
     * 事件打印
     * @param line 行号
     * @param random 随机数
     * @param time 时间
     * @param interval 随机间隔
     */
    private void logLine(int line, int random, String time, long interval) {
        System.out.printf("%-6d%-5d%-15s%-10d\n", line, random, time, interval);
    }

    /**
     * 随机间隔生成器
     * @return 随机间隔
     */
    private long getRandomInterval() {
        return Math.round(-6000 * Math.log(Math.random()));
    }
}
