package seu;

import java.util.TreeMap;

public class StaticData {
    public final static int DELAY_IJ = 1000;
    public final static int DELAY_JI = 1300;
    public final static int DELAY_IK = 1600;
    public final static int DELAY_KI = 1900;
    public final static int DELAY_JK = 2100;
    public final static int DELAY_KJ = 2400;
    public final static int PORT_C = 666;
    public final static int PORT_I = 777;
    public final static int PORT_J = 888;
    public final static int PORT_K = 999;

    public static char node;
    public static int resource = 300;
    public static TreeMap<Integer, Snapshot> snapshotTable = new TreeMap<>();
}
