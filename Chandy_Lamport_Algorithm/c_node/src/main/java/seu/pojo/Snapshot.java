package seu.pojo;

/**
 * Snapshot.
 */
public class Snapshot {
    // Snapshot id
    public int id;
    // Merge count
    public int mergeCount = 0;
    // Node resource
    private int I;
    private int J;
    private int K;
    // Channel resource
    private int IJ;
    private int JI;
    private int IK;
    private int KI;
    private int JK;
    private int KJ;
    // Whether node received snapshot
    private boolean isCheckedI;
    private boolean isCheckedJ;
    private boolean isCheckedK;
    // Whether channel is listened
    private boolean isListenIJ;
    private boolean isListenJI;
    private boolean isListenIK;
    private boolean isListenKI;
    private boolean isListenJK;
    private boolean isListenKJ;

    /**
     * Construct a snapshot.
     *
     * @param id ID.
     */
    public Snapshot(int id) {
        this.id = id;
        I = 0;
        J = 0;
        K = 0;
        IJ = 0;
        JI = 0;
        IK = 0;
        KI = 0;
        JK = 0;
        KJ = 0;
        isCheckedI = false;
        isCheckedJ = false;
        isCheckedK = false;
        isListenIJ = false;
        isListenJI = false;
        isListenIK = false;
        isListenKI = false;
        isListenJK = false;
        isListenKJ = false;
    }

    /**
     * Construct from the string.
     *
     * @param snapshot the string.
     */
    public Snapshot(String snapshot) {
        String[] splits = snapshot.split("\\|");
        id = Integer.parseInt(splits[0]);
        I = Integer.parseInt(splits[1]);
        J = Integer.parseInt(splits[2]);
        K = Integer.parseInt(splits[3]);
        IJ = Integer.parseInt(splits[4]);
        JI = Integer.parseInt(splits[5]);
        IK = Integer.parseInt(splits[6]);
        KI = Integer.parseInt(splits[7]);
        JK = Integer.parseInt(splits[8]);
        KJ = Integer.parseInt(splits[9]);
    }

    /**
     * Merge with the given snapshot.
     *
     * @param snapshot the snapshot.
     */
    public void merge(Snapshot snapshot) {
        I += snapshot.I;
        J += snapshot.J;
        K += snapshot.K;
        IJ += snapshot.IJ;
        JI += snapshot.JI;
        IK += snapshot.IK;
        KI += snapshot.KI;
        JK += snapshot.JK;
        KJ += snapshot.KJ;
        mergeCount++;
    }

    /**
     * Whether snapshot is complete.
     *
     * @return complete status.
     */
    public boolean isComplete() {
        return mergeCount >= 3;
    }

    /**
     * Set node that have received a snapshot.
     *
     * @param node   the node.
     * @param status received status.
     */
    public void setChecked(char node, boolean status) {
        switch (node) {
            case 'i':
                isCheckedI = status;
                break;
            case 'j':
                isCheckedJ = status;
                break;
            case 'k':
                isCheckedK = status;
                break;
            default:
                break;
        }
    }

    /**
     * Get node status whether have received a snapshot.
     *
     * @param node the node.
     * @return received status.
     */
    public boolean getChecked(char node) {
        switch (node) {
            case 'i':
                return isCheckedI;
            case 'j':
                return isCheckedJ;
            case 'k':
                return isCheckedK;
            default:
                return false;
        }
    }

    /**
     * Set node resource.
     *
     * @param node     the node.
     * @param resource resource the node has.
     */
    public void setNodeResource(char node, int resource) {
        switch (node) {
            case 'i':
                I = resource;
                break;
            case 'j':
                J = resource;
                break;
            case 'k':
                K = resource;
                break;
            default:
                break;
        }
    }

    /**
     * Add channel resource.
     *
     * @param from     node channel from.
     * @param to       node channel to.
     * @param resource resource in the channel.
     */
    public void addChannelResource(char from, char to, int resource) {
        if (from == 'i' && to == 'j') IJ += resource;
        else if (from == 'i' && to == 'k') IK += resource;
        else if (from == 'j' && to == 'i') JI += resource;
        else if (from == 'j' && to == 'k') JK += resource;
        else if (from == 'k' && to == 'i') KI += resource;
        else if (from == 'k' && to == 'j') KJ += resource;
    }

    /**
     * Get channel listen status.
     *
     * @param from node channel from.
     * @param to   node channel to.
     * @return listen status of the channel.
     */
    public boolean isListen(char from, char to) {
        if (from == 'i' && to == 'j') return isListenIJ;
        else if (from == 'i' && to == 'k') return isListenIK;
        else if (from == 'j' && to == 'i') return isListenJI;
        else if (from == 'j' && to == 'k') return isListenJK;
        else if (from == 'k' && to == 'i') return isListenKI;
        else if (from == 'k' && to == 'j') return isListenKJ;
        return false;
    }

    /**
     * Set channel listen status to listening.
     *
     * @param from node channel from.
     * @param to   node channel to.
     */
    public void setListen(char from, char to) {
        if (from == 'i' && to == 'j') isListenIJ = true;
        else if (from == 'i' && to == 'k') isListenIK = true;
        else if (from == 'j' && to == 'i') isListenJI = true;
        else if (from == 'j' && to == 'k') isListenJK = true;
        else if (from == 'k' && to == 'i') isListenKI = true;
        else if (from == 'k' && to == 'j') isListenKJ = true;
    }

    /**
     * Cancel listen status of the channel.
     *
     * @param from node channel from.
     * @param to   node channel to.
     */
    public void cancelListen(char from, char to) {
        if (from == 'i' && to == 'j') isListenIJ = false;
        else if (from == 'i' && to == 'k') isListenIK = false;
        else if (from == 'j' && to == 'i') isListenJI = false;
        else if (from == 'j' && to == 'k') isListenJK = false;
        else if (from == 'k' && to == 'i') isListenKI = false;
        else if (from == 'k' && to == 'j') isListenKJ = false;
    }

    /**
     * Get total resource of a snapshot.
     *
     * @return total resource.
     */
    public int total() {
        return I + J + K
                + IJ + JI
                + IK + KI
                + JK + KJ;
    }

    @Override
    public String toString() {
        return id +
                "|" + I +
                "|" + J +
                "|" + K +
                "|" + IJ +
                "|" + JI +
                "|" + IK +
                "|" + KI +
                "|" + JK +
                "|" + KJ;
    }
}
