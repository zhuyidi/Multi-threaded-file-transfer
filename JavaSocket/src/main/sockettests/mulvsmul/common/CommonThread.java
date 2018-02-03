package sockettests.mulvsmul.common;

/**
 * Created by dela on 1/22/18.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by dela on 1/20/18.
 */

// 两端(服对客/客对服)之间的接收数据和发送数据
public class CommonThread implements Runnable {
    // 定义当前线程所要处理的客户端的Socket
    private Socket socket;
    // 定义当前线程所处理的Socket对应的输入流
    private DataInputStream inputStream;
    // 定义当前线程所处理的socket的对应的输出流, 以便将消息发送给对端
    private DataOutputStream outputStream;
    // 上层的Listener
    private ICommunicationListener listener;

    // 一些状态值
    private boolean connectionOk = true;
    private boolean goon = true;
    private final static String OFF_LINE = "0";
    private final static String FAIL = "-1";
    private final static String SEND = "1";

    // 在构造方法中注入客户端的socket, 并初始化该Socket所对应的输入流
    public CommonThread(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream());
    }

    // CommonThread处理发送数据
    public void sendMessage(String message) throws IOException {
        connectionOk = false;
        outputStream.writeUTF(message);
        connectionOk = true;
    }

    private void stop() {
        goon = false;
        try {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String sendMessage = null;

        // 处理线程被动的不断从Socket中读取对端发送过来的消息
        while (goon) {
            try {
                sendMessage = inputStream.readUTF();
//                JSONObject
            } catch (IOException e) {
                // 如果发生了异常, 那么说对方在发送数据的时候发生异常,
                // 要根据connectionOk的值进行分类向上抛出信息
                if (connectionOk == false) {
                    // connectionOk为false, 说明对方是异常下线
                    Message message = new Message();
                    message.setFrom("#");
                    message.setTo("#");
                    message.setCommand(FAIL);
                    message.setMessage("异常下线");


                } else {
                    // connectionOk为true, 说明对方是正常下线
                    Message message = new Message();
                    message.setFrom("#");
                    message.setTo("#");
                    message.setCommand(OFF_LINE);
                    message.setMessage("正常下线");
                }
            }
        }

    }
}

