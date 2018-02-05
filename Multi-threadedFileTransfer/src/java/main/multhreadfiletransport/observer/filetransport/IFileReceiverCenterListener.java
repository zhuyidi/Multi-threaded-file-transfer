package multhreadfiletransport.observer.filetransport;

/**
 * Created by dela on 1/29/18.
 */
// JDialog与RC通信的一组Listener和Speaker
// JDialog提供暂停接收的信号, RC进行处理
public interface IFileReceiverCenterListener {
    // 处理接收端(框口)的停止接收文件
    void dealStopReceive();
}
