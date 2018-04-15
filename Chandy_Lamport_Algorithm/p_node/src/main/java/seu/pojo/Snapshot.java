package seu.pojo;

import static seu.utility.ConfigUtil.*;

public class Snapshot {
    public int id;
    private int I;
    private int J;
    private int K;
    private int IJ;
    private int JI;
    private int IK;
    private int KI;
    private int JK;
    private int KJ;
    private boolean isListenIJ;
    private boolean isListenJI;
    private boolean isListenIK;
    private boolean isListenKI;
    private boolean isListenJK;
    private boolean isListenKJ;

    public int receivedCount;

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

    public boolean isComplete() {
        return receivedCount >= 2;
    }

    public void addChannelResource(char from, int resource) {
        addChannelResource(from, NODE, resource);
    }

    private void addChannelResource(char from, char to, int resource) {
        if (from == 'i' && to == 'j') IJ += resource;
        else if (from == 'i' && to == 'k') IK += resource;
        else if (from == 'j' && to == 'i') JI += resource;
        else if (from == 'j' && to == 'k') JK += resource;
        else if (from == 'k' && to == 'i') KI += resource;
        else if (from == 'k' && to == 'j') KJ += resource;
    }

    public boolean isListen(char from) {
        return isListen(from, NODE);
    }

    private boolean isListen(char from, char to) {
        if (from == 'i' && to == 'j') return isListenIJ;
        else if (from == 'i' && to == 'k') return isListenIK;
        else if (from == 'j' && to == 'i') return isListenJI;
        else if (from == 'j' && to == 'k') return isListenJK;
        else if (from == 'k' && to == 'i') return isListenKI;
        else if (from == 'k' && to == 'j') return isListenKJ;
        return false;
    }

    public void setListen(char from) {
        setListen(from, NODE);
    }

    private void setListen(char from, char to) {
        if (from == 'i' && to == 'j') isListenIJ = true;
        else if (from == 'i' && to == 'k') isListenIK = true;
        else if (from == 'j' && to == 'i') isListenJI = true;
        else if (from == 'j' && to == 'k') isListenJK = true;
        else if (from == 'k' && to == 'i') isListenKI = true;
        else if (from == 'k' && to == 'j') isListenKJ = true;
    }

    public void cancelListen(char from) {
        cancelListen(from, NODE);
    }

    private void cancelListen(char from, char to) {
        if (from == 'i' && to == 'j') isListenIJ = false;
        else if (from == 'i' && to == 'k') isListenIK = false;
        else if (from == 'j' && to == 'i') isListenJI = false;
        else if (from == 'j' && to == 'k') isListenJK = false;
        else if (from == 'k' && to == 'i') isListenKI = false;
        else if (from == 'k' && to == 'j') isListenKJ = false;
    }

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
            default: break;
        }
        return snapshot;
    }

    public static Snapshot getInstanceOfGetSnapshot(char sourceNode, int snapshotId) {
        Snapshot snapshot = getInstanceOfStartSnapshot(snapshotId);
        snapshot.cancelListen(sourceNode);
        return snapshot;
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
