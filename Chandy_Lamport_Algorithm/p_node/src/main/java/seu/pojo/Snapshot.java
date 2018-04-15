package seu.pojo;

import static seu.utility.ConfigUtil.NODE;
import static seu.utility.ConfigUtil.RESOURCE;

/**
 * Snapshot.
 */
public class Snapshot {
    // Snapshot id
    public int id;
    // Received snapshot count
    public int receivedCount;
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
        receivedCount = 0;
        isListenIJ = false;
        isListenJI = false;
        isListenIK = false;
        isListenKI = false;
        isListenJK = false;
        isListenKJ = false;
    }

    /**
     * Get an instance of snapshot for the node start a snapshot.
     *
     * @param snapshotId snapshot id.
     * @return an instance of snapshot.
     */
    public static Snapshot getInstanceOfStartSnapshot(int snapshotId) {
        Snapshot snapshot = new Snapshot(snapshotId);
        switch (NODE) {
            case 'i':
                snapshot.I = RESOURCE;
                snapshot.setListen('j');
                snapshot.setListen('k');
                break;
            case 'j':
                snapshot.J = RESOURCE;
                snapshot.setListen('i');
                snapshot.setListen('k');
                break;
            case 'k':
                snapshot.K = RESOURCE;
                snapshot.setListen('i');
                snapshot.setListen('j');
                break;
            default:
                break;
        }
        return snapshot;
    }

    /**
     * Get an instance of snapshot for the node get a snapshot.
     *
     * @param sourceNode node the snapshot from.
     * @param snapshotId id of the snapshot.
     * @return an instance of snapshot.
     */
    public static Snapshot getInstanceOfGetSnapshot(char sourceNode, int snapshotId) {
        Snapshot snapshot = getInstanceOfStartSnapshot(snapshotId);
        snapshot.cancelListen(sourceNode);
        return snapshot;
    }

    /**
     * Whether snapshot is complete.
     *
     * @return complete status.
     */
    public boolean isComplete() {
        return receivedCount >= 2;
    }

    /**
     * Add channel resource of which channel ends with local node.
     *
     * @param from     node channel from.
     * @param resource resource in the channel.
     */
    public void addChannelResource(char from, int resource) {
        addChannelResource(from, NODE, resource);
    }

    /**
     * Add channel resource.
     *
     * @param from     node channel from.
     * @param to       node channel to.
     * @param resource resource in the channel.
     */
    private void addChannelResource(char from, char to, int resource) {
        if (from == 'i' && to == 'j') IJ += resource;
        else if (from == 'i' && to == 'k') IK += resource;
        else if (from == 'j' && to == 'i') JI += resource;
        else if (from == 'j' && to == 'k') JK += resource;
        else if (from == 'k' && to == 'i') KI += resource;
        else if (from == 'k' && to == 'j') KJ += resource;
    }

    /**
     * Get channel listen status of which channel ends with local node.
     *
     * @param from node channel from.
     * @return listen status of the channel.
     */
    public boolean isListen(char from) {
        return isListen(from, NODE);
    }

    /**
     * Get channel listen status.
     *
     * @param from node channel from.
     * @param to   node channel to.
     * @return listen status of the channel.
     */
    private boolean isListen(char from, char to) {
        if (from == 'i' && to == 'j') return isListenIJ;
        else if (from == 'i' && to == 'k') return isListenIK;
        else if (from == 'j' && to == 'i') return isListenJI;
        else if (from == 'j' && to == 'k') return isListenJK;
        else if (from == 'k' && to == 'i') return isListenKI;
        else if (from == 'k' && to == 'j') return isListenKJ;
        return false;
    }

    /**
     * Set channel listen status to listening of which channel ends with local node.
     *
     * @param from node channel from.
     */
    public void setListen(char from) {
        setListen(from, NODE);
    }

    /**
     * Set channel listen status to listening.
     *
     * @param from node channel from.
     * @param to   node channel to.
     */
    private void setListen(char from, char to) {
        if (from == 'i' && to == 'j') isListenIJ = true;
        else if (from == 'i' && to == 'k') isListenIK = true;
        else if (from == 'j' && to == 'i') isListenJI = true;
        else if (from == 'j' && to == 'k') isListenJK = true;
        else if (from == 'k' && to == 'i') isListenKI = true;
        else if (from == 'k' && to == 'j') isListenKJ = true;
    }

    /**
     * Cancel listen status of the channel that ends with local node.
     *
     * @param from node channel from.
     */
    public void cancelListen(char from) {
        cancelListen(from, NODE);
    }

    /**
     * Cancel listen status of the channel.
     *
     * @param from node channel from.
     * @param to   node channel to.
     */
    private void cancelListen(char from, char to) {
        if (from == 'i' && to == 'j') isListenIJ = false;
        else if (from == 'i' && to == 'k') isListenIK = false;
        else if (from == 'j' && to == 'i') isListenJI = false;
        else if (from == 'j' && to == 'k') isListenJK = false;
        else if (from == 'k' && to == 'i') isListenKI = false;
        else if (from == 'k' && to == 'j') isListenKJ = false;
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
