package seu.simulation;

/**
 * Simulate message.
 */
public class SimulateMessage implements Comparable<SimulateMessage> {
    public int command;
    public char from;
    public char to;
    public int resource;
    public int snapshotId;
    public int time;

    /**
     * Construct initial message.
     *
     * @param command message command.
     * @param time    absolute time in simulation and
     *                interval time in sending.
     */
    public SimulateMessage(int command, int time) {
        this.command = command;
        from = 0;
        to = 0;
        resource = 0;
        snapshotId = 0;
        this.time = time;
    }

    /**
     * Set message sending from who to who.
     *
     * @param from node sending from.
     * @param to   node sending to.
     */
    public void setFromTo(char from, char to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public int compareTo(SimulateMessage o) {
        return time - o.time;
    }

    @Override
    public String toString() {
        switch (command) {
            case 1:
                return command + "|" + to + "|" + resource;
            case 2:
                return command + "|" + snapshotId;
            case 5:
                return String.valueOf(command);
            default:
                return null;
        }
    }
}
