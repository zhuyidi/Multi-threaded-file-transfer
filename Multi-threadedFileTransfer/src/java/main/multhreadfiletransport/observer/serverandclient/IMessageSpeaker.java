package multhreadfiletransport.observer.serverandclient;

/**
 * Created by dela on 2/5/18.
 */
public interface IMessageSpeaker {
    void addListener(IMessageListener listener);
    void removeListener(IMessageListener listener);
    void sendMessage(String strMessage);
}
