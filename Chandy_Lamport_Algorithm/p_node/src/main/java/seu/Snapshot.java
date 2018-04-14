package seu;

import static seu.ConfigUtil.*;

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
    public boolean isListenij;
    public boolean isListenji;
    public boolean isListenik;
    public boolean isListenki;
    public boolean isListenjk;
    public boolean isListenkj;

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

    public void setListen(char from) {
        setListen(from, NODE);
    }

    public void setListen(char from, char to) {
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

    public void cancelListen(char from, char to) {
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
