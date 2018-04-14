package seu;

import java.util.Random;
import java.util.TreeMap;

public class ConfigUtil {
    // Resource of node x
    public static int RESOURCE_I = 100;
    public static int RESOURCE_J = 100;
    public static int RESOURCE_K = 100;
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
    public final static char NODE = 'c';
    public static int TRANSMIT_TIMES;
    public static int SNAPSHOT_TIMES;
    public static Random RANDOM;
    public static TreeMap<Integer, Snapshot> SNAPSHOT_TABLE = new TreeMap<>();

    public static String getIP() {
        return "127.0.0.1";
    }

    public static String getIP(char node) throws Exception {
        switch (node) {
            case 'i':
            case 'j':
                return OPPOSITE_IP;
            case 'k':
            case 'c':
                return "127.0.0.1";
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
        return Math.round(second * -1000 * Math.log(RANDOM.nextDouble()));
    }
}
