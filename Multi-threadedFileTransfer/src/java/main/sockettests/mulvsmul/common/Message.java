package sockettests.mulvsmul.common;

/**
 * Created by dela on 1/20/18.
 */
public class Message {
    private String from;
    private String to;
    private String command;
    private String message;

    public Message() { }

    public Message(String from, String to, String command, String message) {
        this.from = from;
        this.to = to;
        this.command = command;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
