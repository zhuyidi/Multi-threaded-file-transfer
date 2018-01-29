package multhreadfiletransport.observer;

/**
 * Created by dela on 1/29/18.
 */
public interface IFileReceiverCenterSpeaker {
    void addFileReceiverCenterListener(IFileReceiverCenterListener listner);
    void removeFileReceiverCenterListener(IFileReceiverCenterListener listner);
    void stopReceive();
}
