package seu;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;

import static seu.App.getRandomInterval;
import static seu.MessageUtil.*;

public class SummationRequestCreator implements Runnable {

    private String IP1;
    private int port1;
    private String IP2;
    private int port2;

    public SummationRequestCreator(String IP1, int port1, String IP2, int port2) {
        this.IP1 = IP1;
        this.port1 = port1;
        this.IP2 = IP2;
        this.port2 = port2;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(getRandomInterval(9));
                summation();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }

    private void summation() throws IOException {
        Date start = new Date();
        Socket socket1;
        Socket socket2;
        socket1 = new Socket(IP1, port1);
        socket2 = new Socket(IP2, port2);
        socket1.setSoTimeout(10000);
        socket2.setSoTimeout(10000);
        DataInputStream inputStream1 = new DataInputStream(socket1.getInputStream());
        DataInputStream inputStream2 = new DataInputStream(socket2.getInputStream());
        DataOutputStream outputStream1 = new DataOutputStream(new BufferedOutputStream(socket1.getOutputStream()));
        DataOutputStream outputStream2 = new DataOutputStream(new BufferedOutputStream(socket2.getOutputStream()));
        outputStream1.writeShort(getMessage(0b01));
        outputStream2.writeShort(getMessage(0b01));
        outputStream1.flush();
        outputStream2.flush();
        App.logger.log(0, (InetSocketAddress) socket1.getRemoteSocketAddress(),0b01, 0, 0, new Date());
        App.logger.log(0, (InetSocketAddress) socket2.getRemoteSocketAddress(),0b01, 0, 0, new Date());
        short response1 = getParameter(inputStream1.readShort());
        short response2 = getParameter(inputStream2.readShort());
        outputStream1.close();
        outputStream2.close();
        inputStream1.close();
        inputStream2.close();
        socket1.close();
        socket2.close();
        int localResource = App.resource;
        System.out.printf("Local:%-5d %s:%-5d %s:%-5dTotal:%-5dStartTime:%-12s EndTime:%-12s\n",
                localResource,
                IP1 + ":" + port1,
                response1,
                IP2 + ":" + port2,
                response2,
                localResource + response1 + response2,
                App.dateFormat.format(start),
                App.dateFormat.format(new Date()));
        App.logger.log(1, (InetSocketAddress) socket1.getRemoteSocketAddress(), 0b10, localResource, 0, new Date());
        App.logger.log(1, (InetSocketAddress) socket2.getRemoteSocketAddress(), 0b10, localResource, 0, new Date());
    }
}
