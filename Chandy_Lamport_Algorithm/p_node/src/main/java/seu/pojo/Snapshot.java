package seu.pojo;

import static seu.utility.ConfigUtil.*;

public class Snapshot {
    public int id;
    public int i;
    public int j;
    public int k;
    public int ij;
    public int ji;
    public int ik;
    public int ki;
    public int jk;
    public int kj;
    private boolean isListenij;
    private boolean isListenji;
    private boolean isListenik;
    private boolean isListenki;
    private boolean isListenjk;
    private boolean isListenkj;

    public int receivedCount;

    public Snapshot(int id) {
        this.id = id;
        i = 0;
        j = 0;
        k = 0;
        ij = 0;
        ji = 0;
        ik = 0;
        ki = 0;
        jk = 0;
        kj = 0;
        receivedCount = 0;
        isListenij = false;
        isListenji = false;
        isListenik = false;
        isListenki = false;
        isListenjk = false;
        isListenkj = false;
    }

    public Snapshot(String snapshot) {
        String[] splits = snapshot.split("\\|");
        id = Integer.parseInt(splits[0]);
        i = Integer.parseInt(splits[1]);
        j = Integer.parseInt(splits[2]);
        k = Integer.parseInt(splits[3]);
        ij = Integer.parseInt(splits[4]);
        ji = Integer.parseInt(splits[5]);
        ik = Integer.parseInt(splits[6]);
        ki = Integer.parseInt(splits[7]);
        jk = Integer.parseInt(splits[8]);
        kj = Integer.parseInt(splits[9]);
    }

    public boolean isComplete() {
        return receivedCount >= 2;
    }

    public void addChannelResource(char from, int resource) {
        addChannelResource(from, NODE, resource);
    }

    private void addChannelResource(char from, char to, int resource) {
        if (from == 'i' && to == 'j') ij += resource;
        else if (from == 'i' && to == 'k') ik += resource;
        else if (from == 'j' && to == 'i') ji += resource;
        else if (from == 'j' && to == 'k') jk += resource;
        else if (from == 'k' && to == 'i') ki += resource;
        else if (from == 'k' && to == 'j') kj += resource;
    }

    public boolean isListen(char from) {
        return isListen(from, NODE);
    }

    private boolean isListen(char from, char to) {
        if (from == 'i' && to == 'j') return isListenij;
        else if (from == 'i' && to == 'k') return isListenik;
        else if (from == 'j' && to == 'i') return isListenji;
        else if (from == 'j' && to == 'k') return isListenjk;
        else if (from == 'k' && to == 'i') return isListenki;
        else if (from == 'k' && to == 'j') return isListenkj;
        return false;
    }

    public void setListen(char from) {
        setListen(from, NODE);
    }

    private void setListen(char from, char to) {
        if (from == 'i' && to == 'j') isListenij = true;
        else if (from == 'i' && to == 'k') isListenik = true;
        else if (from == 'j' && to == 'i') isListenji = true;
        else if (from == 'j' && to == 'k') isListenjk = true;
        else if (from == 'k' && to == 'i') isListenki = true;
        else if (from == 'k' && to == 'j') isListenkj = true;
    }

    public void cancelListen(char from) {
        cancelListen(from, NODE);
    }

    private void cancelListen(char from, char to) {
        if (from == 'i' && to == 'j') isListenij = false;
        else if (from == 'i' && to == 'k') isListenik = false;
        else if (from == 'j' && to == 'i') isListenji = false;
        else if (from == 'j' && to == 'k') isListenjk = false;
        else if (from == 'k' && to == 'i') isListenki = false;
        else if (from == 'k' && to == 'j') isListenkj = false;
    }

    public static Snapshot getInstanceOfStartSnapshot(int snapshotId) {
        Snapshot snapshot = new Snapshot(snapshotId);
        switch (NODE) {
            case 'i':
                snapshot.i = RESOURCE;
                snapshot.setListen('j');
                snapshot.setListen('k');
                break;
            case 'j':
                snapshot.j = RESOURCE;
                snapshot.setListen('i');
                snapshot.setListen('k');
                break;
            case 'k':
                snapshot.k = RESOURCE;
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
                "|" + i +
                "|" + j +
                "|" + k +
                "|" + ij +
                "|" + ji +
                "|" + ik +
                "|" + ki +
                "|" + jk +
                "|" + kj;
    }
}
