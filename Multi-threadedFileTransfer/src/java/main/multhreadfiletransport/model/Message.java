package multhreadfiletransport.model;

/**
 * Created by dela on 2/5/18.
 */
public class Message {
    public static final String REQUEST_FILE = "request file"; // 请求文件
    public static final String UPDATE_RESOURCE = "update resource"; // 更新资源表

    private int from;
    private int to;
    private String action;
    private String message;

    public Message() { }

    public Message(int from, int to, String action, String message) {
        this.from = from;
        this.to = to;
        this.action = action;
        this.message = message;
    }

    public static String getRequestFile() {
        return REQUEST_FILE;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", to=" + to +
                ", action='" + action + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
