package seu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

            if (counter0 >= 15) {
                event1(i, interval);
                counter1++;
            } else if (counter1 >= 5) {
                event0(i, interval);
                counter0++;
            } else {
                int random = (this.random.nextFloat() >= 0.25f) ? 0 : 1;
                if (random == 0) {
                    event0(i, interval);
                    counter0++;
                } else {
                    event1(i, interval);
                    counter1++;
                }
            }
        }
        System.out.println("结束时间：" + dateFormat.format(end = new Date()));
        System.out.println("运行时间(ms)：" + (end.getTime() - start.getTime()));
    }

    /**
     * 事件0
     * @param line 行号
     * @param interval 随机间隔
     * @throws InterruptedException 由Thread.sleep抛出
     */
    private void event0(int line, long interval) throws InterruptedException {
        Thread.sleep(interval);
        interval += delayForEvent0();
        logLine(line, 0, dateFormat.format(new Date()), interval);
    }

    /**
     * 事件0推迟
     * @return 推迟时间
     * @throws InterruptedException 由Thread.sleep抛出
     */
    private long delayForEvent0() throws InterruptedException {
        final int MILLISECOND = Calendar.getInstance().get(Calendar.MILLISECOND);
        long delay = 0;
        if (MILLISECOND >= 150 && MILLISECOND <= 350 ||
                MILLISECOND >= 650 && MILLISECOND <= 850) {
            return delay;
        } else if (MILLISECOND < 150) {
            long nextMillisecond = Math.round(Math.random() * 0.2 * 1000) + 150;
            Thread.sleep(delay = (nextMillisecond - MILLISECOND));
        } else if (MILLISECOND > 850) {
            long nextMillisecond = Math.round(Math.random() * 0.2 * 1000) + 150;
            Thread.sleep(delay = (1000 - MILLISECOND + nextMillisecond));
        } else {
            long nextMillisecond = Math.round(Math.random() * 0.2 * 1000) + 650;
            Thread.sleep(delay = (nextMillisecond - MILLISECOND));
        }
        return delay;
    }

    /**
     * 事件1
     * @param line 行号
     * @param interval 随机间隔
     * @throws InterruptedException 由Thread.sleep抛出
     */
    private void event1(int line, long interval) throws InterruptedException {
        Thread.sleep(interval);
        interval += delayForEvent1();
        logLine(line, 1, dateFormat.format(new Date()), interval);
    }

    /**
     * 事件1推迟
     * @return 推迟时间
     * @throws InterruptedException 由Thread.sleep抛出
     */
    private long delayForEvent1() throws InterruptedException {
        final int MILLISECOND = Calendar.getInstance().get(Calendar.MILLISECOND);
        long delay = 0;
        if (MILLISECOND > 0 && MILLISECOND < 500) {
            Thread.sleep(delay = (500 - MILLISECOND));
        } else if (MILLISECOND > 500) {
            Thread.sleep(delay = (1000 - MILLISECOND));
        }
        return delay;
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
