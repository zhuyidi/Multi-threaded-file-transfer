package multhreadfiletransport.observer;

/**
 * Created by dela on 1/29/18.
 */
public interface ISectionReceiverSpeaker {
    void addSectionReceiverListener(ISectionReceiverListener listener);
    void removeSectionReceiverListener(ISectionReceiverListener listener);
}
