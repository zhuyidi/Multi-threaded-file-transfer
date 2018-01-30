package multhreadfiletransport.observer;

/**
 * Created by dela on 1/29/18.
 */

// JDialog和RC通信的一组Listener和Speaker
// 用于RC通知JDialog文件合并情况
public interface IFileJoinSpeaker {
    void addFileJoinListener(IFileJoinListener listener);
    void removeFileJoinListener(IFileJoinListener listener);
}
