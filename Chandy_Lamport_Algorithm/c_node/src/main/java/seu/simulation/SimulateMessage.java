package seu.simulation;

public class SimulateMessage implements Comparable<SimulateMessage> {
    int command;
    char from;
    char to;
    int resource;
    int snapshotId;
    int time;

    SimulateMessage(int command, int time) {
        this.command = command;
        from = 0;
        to = 0;
        resource = 0;
        snapshotId = 0;
        this.time = time;
    }

    void setFromTo(char from, char to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public int compareTo(SimulateMessage o) {
        return time - o.time;
    }
}
