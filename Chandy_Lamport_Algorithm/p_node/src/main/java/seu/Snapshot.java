package seu;

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

    public int receivedCount;

    public Snapshot() {
        id = 0;
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
