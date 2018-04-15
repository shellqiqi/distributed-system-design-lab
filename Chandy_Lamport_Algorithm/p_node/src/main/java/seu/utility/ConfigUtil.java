package seu.utility;

import seu.pojo.Snapshot;

import java.text.SimpleDateFormat;
import java.util.TreeMap;

/**
 * Configs and utilities.
 */
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
    // Node properties
    public static char NODE;
    public static int RESOURCE = 100;
    public static TreeMap<Integer, Snapshot> SNAPSHOT_TABLE = new TreeMap<>();
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");

    /**
     * Get other nodes besides the local node.
     *
     * @return other nodes.
     * @throws Exception throw when local node name is unsupported.
     */
    public static char[] getOtherNodes() throws Exception {
        switch (NODE) {
            case 'i':
                return new char[]{'j', 'k'};
            case 'j':
                return new char[]{'i', 'k'};
            case 'k':
                return new char[]{'i', 'j'};
            default:
                throw new Exception("Unsupported node name");
        }
    }

    /**
     * Get local IP.
     *
     * @return always 127.0.0.1
     */
    public static String getIP() {
        return "127.0.0.1";
    }

    /**
     * Get IP of the given node.
     *
     * @param node node.
     * @return IP of the given node.
     * @throws Exception throw when local node name is unsupported.
     */
    public static String getIP(char node) throws Exception {
        switch (node) {
            case 'i':
            case 'j':
                if (NODE == 'i' || NODE == 'j') return getIP();
                else return OPPOSITE_IP;
            case 'k':
            case 'c':
                if (NODE == 'k' || NODE == 'c') return getIP();
                else return OPPOSITE_IP;
            default:
                throw new Exception("Unsupported node name");
        }
    }

    /**
     * Get local port.
     *
     * @return port.
     * @throws Exception throw when local node is wrong.
     */
    public static int getPort() throws Exception {
        return getPort(NODE);
    }

    /**
     * Get port of the node.
     *
     * @param node node.
     * @return port of given node.
     * @throws Exception throw when gives an unsupported node.
     */
    public static int getPort(char node) throws Exception {
        switch (node) {
            case 'i':
                return PORT_I;
            case 'j':
                return PORT_J;
            case 'k':
                return PORT_K;
            case 'c':
                return PORT_C;
            default:
                throw new Exception("Unsupported node name");
        }
    }

    /**
     * Get channel delay from local to the given node.
     *
     * @param to target node.
     * @return delay millisecond.
     */
    public static int getDelay(char to) {
        return getDelay(NODE, to);
    }

    /**
     * Get channel delay from node to node.
     *
     * @param from node that channel from.
     * @param to   node that channel to.
     * @return delay millisecond.
     */
    public static int getDelay(char from, char to) {
        if (from == 'i' && to == 'j') return DELAY_IJ;
        else if (from == 'i' && to == 'k') return DELAY_IK;
        else if (from == 'j' && to == 'i') return DELAY_JI;
        else if (from == 'j' && to == 'k') return DELAY_JK;
        else if (from == 'k' && to == 'i') return DELAY_KI;
        else if (from == 'k' && to == 'j') return DELAY_KJ;
        else return 0;
    }
}
