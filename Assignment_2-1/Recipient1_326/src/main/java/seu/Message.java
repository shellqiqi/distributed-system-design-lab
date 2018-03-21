package seu;

import java.io.Serializable;

public class Message implements Serializable {
    private int random1;
    private int random2;
    private String timer;

    public Message(int random1, int random2, String timer) {
        this.random1 = random1;
        this.random2 = random2;
        this.timer = timer;
    }

    public int getRandom1() {
        return random1;
    }

    public void setRandom1(int random1) {
        this.random1 = random1;
    }

    public int getRandom2() {
        return random2;
    }

    public void setRandom2(int random2) {
        this.random2 = random2;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    @Override
    public String toString() {
        return "Message{" +
                "random1=" + random1 +
                ", random2=" + random2 +
                ", timer='" + timer + '\'' +
                '}';
    }
}
