import sockettests.simplefiletransport.file_transport.multi_receiver.IFileJoinListener;

/**
 * Created by dela on 2/2/18.
 */
public interface IFruitSpeaker {
    void addListener(IFileJoinListener listener);
    void removeListener(IFileJoinListener listener);
    void sendPrice();
}
