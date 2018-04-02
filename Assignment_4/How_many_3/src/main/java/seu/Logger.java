package seu;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Date;

public class Logger {

    private BufferedOutputStream bufferedOutputStream;

    /**
     * Constructor of Logger.
     * @param id node id.
     * @throws FileNotFoundException FileNotFoundException.
     */
    public Logger(int id) throws FileNotFoundException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File("log" + id + ".txt"));
        bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
    }

    /**
     * Log one line to logfile.
     * @param io input = 1 and output = 0.
     * @param IP remote socket IP.
     * @param port remote socket port.
     * @param code message code.
     * @param parameter message parameter.
     * @param total total resource.
     * @param time time.
     */
    public void log(int io, String IP, int port, int code, int parameter, int total, Date time) {
        String s = String.format("%-2d %s %-3s%-5d%-5d%-14s\n",
                io,
                IP + ":" + port,
                Integer.toBinaryString(code),
                parameter,
                total,
                App.dateFormat.format(time));
        try {
            bufferedOutputStream.write(s.getBytes());
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Log one line to logfile.
     * @param io input = 1 and output = 0.
     * @param address remote socket address.
     * @param code message code.
     * @param parameter message parameter.
     * @param total total resource.
     * @param time time.
     */
    public void log(int io, InetSocketAddress address, int code, int parameter, int total, Date time) {
        String s = String.format("%-2d %s %-3s%-5d%-5d%-14s\n",
                io,
                address.getAddress().toString() + ":" + address.getPort(),
                Integer.toBinaryString(code),
                parameter,
                total,
                App.dateFormat.format(time));
        try {
            bufferedOutputStream.write(s.getBytes());
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close file.
     * @throws IOException IOException.
     */
    public void close() throws IOException {
        bufferedOutputStream.close();
    }
}
