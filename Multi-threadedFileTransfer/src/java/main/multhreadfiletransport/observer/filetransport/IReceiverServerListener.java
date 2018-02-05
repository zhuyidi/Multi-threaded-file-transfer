package multhreadfiletransport.observer.filetransport;

import multhreadfiletransport.client.reciever.RecieverThread;

/**
 * Created by dela on 1/29/18.
 */

// 一组RS和JDialog通信的Listener/Speaker
// 用于RS端产生一个线程, 对应的JDialog端就多一组RT和进度条
public interface IReceiverServerListener {
    // 初始化信息(targetfileCount)
    void initTragetFileCount(int targetFileCount);
    // 当RS端将新产生的这个线程传送给JDialog之后, JDialog端就进行处理
    void dealNewReceiverThread(RecieverThread recieverThread);
}
