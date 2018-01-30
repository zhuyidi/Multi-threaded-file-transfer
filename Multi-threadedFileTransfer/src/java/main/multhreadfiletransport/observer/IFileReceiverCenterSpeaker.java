package multhreadfiletransport.observer;

/**
 * Created by dela on 1/29/18.
 */

// JDialog与RC通信的一组Listener和Speaker
// JDialog提供暂停接收的信号, RC进行处理
public interface IFileReceiverCenterSpeaker {
    void addFileReceiverCenterListener(IFileReceiverCenterListener listner);
    void removeFileReceiverCenterListener(IFileReceiverCenterListener listner);
    void stopReceive();
}
