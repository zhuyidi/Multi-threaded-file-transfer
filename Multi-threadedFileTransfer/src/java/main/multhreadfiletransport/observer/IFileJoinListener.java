package multhreadfiletransport.observer;

/**
 * Created by dela on 1/29/18.
 */

// JDialog和RC通信的一组Listener和Speaker
// 用于RC通知JDialog文件合并情况
public interface IFileJoinListener {
    // 开始合并
    void onBeginJoin();
    // 得到合并的数量
    void onGetJoinCount(int count);
    // 一个targetFile已经合并完
    void onJoinOne();
    // 全部targetFile已经合并完
    void onAllDone();
}
