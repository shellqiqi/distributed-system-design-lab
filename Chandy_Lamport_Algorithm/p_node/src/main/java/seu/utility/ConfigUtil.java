package seu.utility;

import seu.pojo.Snapshot;

import java.util.TreeMap;

public class ConfigUtil {
    // Channel delay from x to y
    private final static int DELAY_IJ = 1000;
    private final static int DELAY_JI = 1300;
    private final static int DELAY_IK = 1600;
    private final static int DELAY_KI = 1900;
    private final static int DELAY_JK = 2100;
    private final static int DELAY_KJ = 2400;
    // Port of node x
    private final static int PORT_I = 777;
    private final static int PORT_J = 888;
    private final static int PORT_K = 999;
    private final static int PORT_C = 666;
    // IP of opposite nodes
    public static String OPPOSITE_IP;
    // Node info
    public static char NODE;
    public static int RESOURCE = 100;
    public static TreeMap<Integer, Snapshot> SNAPSHOT_TABLE = new TreeMap<>();

    public static char[] getOtherNodes() throws Exception {
        switch (NODE) {
            case 'i': return new char[] {'j', 'k'};
            case 'j': return new char[] {'i', 'k'};
            case 'k': return new char[] {'i', 'j'};
            default: throw new Exception("Unsupported node name");
        }
    }

    public static String getIP() {
        return "127.0.0.1";
    }

    public static String getIP(char node) throws Exception {
        switch (node) {
            case 'i':
            case 'j':
                if (NODE == 'i' || NODE == 'j') return "127.0.0.1";
                else return OPPOSITE_IP;
            case 'k':
            case 'c':
                if (NODE == 'k' || NODE == 'c') return "127.0.0.1";
                else return OPPOSITE_IP;
            default: throw new Exception("Unsupported node name");
        }
    }

    public static int getPort() throws Exception {
        return getPort(NODE);
    }

    public static int getPort(char node) throws Exception {
        switch (node) {
            case 'i': return PORT_I;
            case 'j': return PORT_J;
            case 'k': return PORT_K;
            case 'c': return PORT_C;
            default: throw new Exception("Unsupported node name");
        }
    }

    public static int getDelay(char to) {
        return getDelay(NODE, to);
    }

    public static int getDelay(char from, char to) {
        if (from == 'i' && to == 'j') return DELAY_IJ;
        else if (from == 'i' && to == 'k') return DELAY_IK;
        else if (from == 'j' && to == 'i') return DELAY_JI;
        else if (from == 'j' && to == 'k') return DELAY_JK;
        else if (from == 'k' && to == 'i') return DELAY_KI;
        else if (from == 'k' && to == 'j') return DELAY_KJ;
        else return 0;
    }

    public static long getRandomInterval(int second) {
        return Math.round(second * -1000 * Math.log(Math.random()));
    }
}
