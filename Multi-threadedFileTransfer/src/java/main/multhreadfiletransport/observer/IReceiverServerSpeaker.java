package multhreadfiletransport.observer;

import multhreadfiletransport.client.reciever.RecieverThread;

/**
 * Created by dela on 1/29/18.
 */

// 一组RS和JDialog通信的Listener/Speaker
// 用于RS端产生一个线程, 对应的JDialog端就多一组RT和进度条
public interface IReceiverServerSpeaker {
    void addFileReceiverListener(IReceiverServerListener serverListener);
    void removeFileReceiverListener(IReceiverServerListener serverListener);
    // 在RS中, 每产生一个新的线程, 就调用这个方法, 通知给JDialog
    void haveNewReceiverThread(RecieverThread recieverThread);
}
