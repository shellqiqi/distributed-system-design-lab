package seu;

import java.util.TreeMap;

public class ConfigUtil {
    // Channel delay from x to y
    public final static int DELAY_IJ = 1000;
    public final static int DELAY_JI = 1300;
    public final static int DELAY_IK = 1600;
    public final static int DELAY_KI = 1900;
    public final static int DELAY_JK = 2100;
    public final static int DELAY_KJ = 2400;
    // Port for node x
    public final static int PORT_C = 666;
    public final static int PORT_I = 777;
    public final static int PORT_J = 888;
    public final static int PORT_K = 999;
    // Node info
    public static char NODE;
    public static int RESOURCE = 300;
    public static TreeMap<Integer, Snapshot> SNAPSHOT_TABLE = new TreeMap<>();

    public static int getPort() {
        return getPort(NODE);
    }

    public static int getPort(char node) {
        switch (node) {
            case 'c': return PORT_C;
            case 'i': return PORT_I;
            case 'j': return PORT_J;
            case 'k': return PORT_K;
            default: return 0;
        }
    }
}
