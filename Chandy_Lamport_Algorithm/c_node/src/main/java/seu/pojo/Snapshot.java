package seu.pojo;

public class Snapshot {
    public int id;
    public int I;
    public int J;
    public int K;
    public int IJ;
    public int JI;
    public int IK;
    public int KI;
    public int JK;
    public int KJ;
    public boolean isCheckedI;
    public boolean isCheckedJ;
    public boolean isCheckedK;
    private boolean isListenIJ;
    private boolean isListenJI;
    private boolean isListenIK;
    private boolean isListenKI;
    private boolean isListenJK;
    private boolean isListenKJ;

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

    public void setChecked(char node, boolean status) {
        switch (node) {
            case 'i': isCheckedI = status; break;
            case 'j': isCheckedJ = status; break;
            case 'k': isCheckedK = status; break;
            default: break;
        }
    }

    public boolean getChecked(char node) {
        switch (node) {
            case 'i': return isCheckedI;
            case 'j': return isCheckedJ;
            case 'k': return isCheckedK;
            default: return false;
        }
    }

    public void setNodeResource(char node, int resource) {
        switch (node) {
            case 'i': I = resource; break;
            case 'j': J = resource; break;
            case 'k': K = resource; break;
            default: break;
        }
    }

    public void addChannelResource(char from, char to, int resource) {
        if (from == 'i' && to == 'j') IJ += resource;
        else if (from == 'i' && to == 'k') IK += resource;
        else if (from == 'j' && to == 'i') JI += resource;
        else if (from == 'j' && to == 'k') JK += resource;
        else if (from == 'k' && to == 'i') KI += resource;
        else if (from == 'k' && to == 'j') KJ += resource;
    }

    public boolean isListen(char from, char to) {
        if (from == 'i' && to == 'j') return isListenIJ;
        else if (from == 'i' && to == 'k') return isListenIK;
        else if (from == 'j' && to == 'i') return isListenJI;
        else if (from == 'j' && to == 'k') return isListenJK;
        else if (from == 'k' && to == 'i') return isListenKI;
        else if (from == 'k' && to == 'j') return isListenKJ;
        return false;
    }

    public void setListen(char from, char to) {
        if (from == 'i' && to == 'j') isListenIJ = true;
        else if (from == 'i' && to == 'k') isListenIK = true;
        else if (from == 'j' && to == 'i') isListenJI = true;
        else if (from == 'j' && to == 'k') isListenJK = true;
        else if (from == 'k' && to == 'i') isListenKI = true;
        else if (from == 'k' && to == 'j') isListenKJ = true;
    }

    public void cancelListen(char from, char to) {
        if (from == 'i' && to == 'j') isListenIJ = false;
        else if (from == 'i' && to == 'k') isListenIK = false;
        else if (from == 'j' && to == 'i') isListenJI = false;
        else if (from == 'j' && to == 'k') isListenJK = false;
        else if (from == 'k' && to == 'i') isListenKI = false;
        else if (from == 'k' && to == 'j') isListenKJ = false;
    }

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
