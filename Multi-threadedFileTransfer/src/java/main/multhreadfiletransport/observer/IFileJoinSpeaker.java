package multhreadfiletransport.observer;

/**
 * Created by dela on 1/29/18.
 */
public interface IFileJoinSpeaker {
    void addFileJoinListener(IFileJoinListener listener);
    void removeFileJoinListener(IFileJoinListener listener);
}
