package sockettests.mulvsmul.common;

/**
 * Created by dela on 1/20/18.
 */
public interface ICommunicationSpeaker {
    void addListener(ICommunicationListener listener);
    void postMessage(String message);
}
