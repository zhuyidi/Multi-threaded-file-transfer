package multhreadfiletransport.view;


import multhreadfiletransport.client.reciever.RecieverCenter;
import multhreadfiletransport.client.reciever.RecieverMap;
import multhreadfiletransport.client.reciever.RecieverThread;
import multhreadfiletransport.observer.IFileJoinListener;
import multhreadfiletransport.observer.IFileReceiverCenterListener;
import multhreadfiletransport.observer.IFileReceiverCenterSpeaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dela on 1/29/18.
 */
public class ReceiveProgressDialog extends JDialog implements IFileJoinListener,
        IFileReceiverCenterSpeaker {
    private static final long serialVersionUID = 1578361299790200727L;

    private int parentWidth;
    private int parentHeight;
    private int width;
    private int height;
    private int top;
    private int btnHeight;
    private int paddingX;
    private int paddingY;
    private Dimension freeArea;
    private int unitHeight;

    private JFrame mfrmParent;
    private JLabel jlblTopic;
    private JLabel jlblActionReport;

    // 顶部进度条, 用于总控
    private JProgressBar jpgbTopProgress;
    // 设置所请求的文件的总个数(targetFile)
    private int requestFileCount;
    // 设置已经接收到的文件的总个数(targetFile)
    private int receivedFileCount;
    // 请求的section的个数
    private int requestFileSectionCount;
    private int receivedFileSectionCount;
    // 接收到的section的个数
    private int requestJoinedFileCount;
    // 合并的targetFile的个数
    private int joinedFileCount;

    // 停止接收的按钮
    private JButton jbtnStopReceive;

    private IFileReceiverCenterListener centerListener;

    // 一个接收文件的线程对应一个进度条
    private Map<RecieverThread, FileReceiveProgress> receiveProgressMap;
    private RecieverMap recieverMap;


    public ReceiveProgressDialog() {
    }

    public ReceiveProgressDialog(Frame owner) {
        super(owner);
        mfrmParent = (JFrame) owner;
    }

    public ReceiveProgressDialog(Dialog owner) {
        super(owner);
    }

    public ReceiveProgressDialog(Window owner) {
        super(owner);
    }

    public ReceiveProgressDialog(Frame owner, boolean modal) {
        super(owner, modal);
    }

    public ReceiveProgressDialog(Frame owner, String title) {
        super(owner, title);
        mfrmParent = (JFrame) owner;
    }

    public ReceiveProgressDialog(Dialog owner, boolean modal) {
        super(owner, modal);
    }

    public ReceiveProgressDialog(Dialog owner, String title) {
        super(owner, title);
    }

    public ReceiveProgressDialog(Window owner, ModalityType modalityType) {
        super(owner, modalityType);
    }

    public ReceiveProgressDialog(Window owner, String title) {
        super(owner, title);
    }

    public ReceiveProgressDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
    }

    public ReceiveProgressDialog(Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
    }

    public ReceiveProgressDialog(Window owner, String title, ModalityType modalityType) {
        super(owner, title, modalityType);
    }

    public ReceiveProgressDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
    }

    public ReceiveProgressDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
    }

    public ReceiveProgressDialog(Window owner, String title, ModalityType modalityType,
                                 GraphicsConfiguration gc) {
        super(owner, title, modalityType, gc);
    }

    public void setReceiveFileCount(int fileCount) {
        this.requestFileCount = fileCount;
        jlblActionReport.setText("�����贫��" + fileCount + "���ļ�");
        jpgbTopProgress.setMaximum(100);
        jpgbTopProgress.setValue(0);
        jpgbTopProgress.setVisible(true);
    }

    public Dimension getFreeArea() {
        return freeArea;
    }

    public void setTopicContext(String context) {
        jlblTopic.setText(context);
    }

    public int getUnitHeight() {
        return unitHeight;
    }

    public void setBtnStopReceive(boolean value) {
        jbtnStopReceive.setEnabled(value);
    }

    // JDialog初始化
    public void initView(RecieverCenter receiverCenter, RecieverMap recieverMap) {
        this.addFileReceiverCenterListener(receiverCenter);
        this.recieverMap = recieverMap;
//        receiverCenter.getFileReceiverServer().addFileReceiverListener(this);
        receiverCenter.addFileJoinListener(this);
        this.receiveProgressMap = new HashMap<>();
        requestFileSectionCount = requestFileCount =
                receivedFileCount = receivedFileSectionCount = 0;

        // 设置JDialog的相关参数
        width = 400;
        height = 100;
        parentWidth = mfrmParent.getWidth();
        parentHeight = mfrmParent.getHeight();
        paddingX = 5;
        paddingY = 5;

        Font topicFont = new Font("微软雅黑", Font.BOLD, 30);
        Font normalFont = new Font("宋体", Font.PLAIN, 16);
        Font btnFont = new Font("宋体", Font.PLAIN, 14);
        Color topicColor = new Color(5, 62, 131);
        Color reportColor = Color.red;
        int topicFontSize = topicFont.getSize();
        unitHeight = 3 * normalFont.getSize();
        freeArea = new Dimension();

        setSize(width, height);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModalityType(ModalityType.TOOLKIT_MODAL);

        jlblTopic = new JLabel("", JLabel.CENTER);
        jlblTopic.setSize(width, topicFontSize + 4);
        jlblTopic.setFont(topicFont);
        jlblTopic.setForeground(topicColor);
        jlblTopic.setLocation(0, 0);
        add(jlblTopic);
        top = jlblTopic.getHeight() + 3 * paddingY;

        jlblActionReport = new JLabel("", JLabel.CENTER);
        jlblActionReport.setFont(normalFont);
        jlblActionReport.setForeground(reportColor);
        jlblActionReport.setSize(width, normalFont.getSize() + 2);
        jlblActionReport.setLocation(0, top);
        add(jlblActionReport);
        top += jlblActionReport.getHeight() + paddingY;

        jpgbTopProgress = new JProgressBar();
        jpgbTopProgress.setSize(width - 10 - 2 * paddingX, normalFont.getSize() + 2);
        jpgbTopProgress.setLocation(paddingX, top);
        jpgbTopProgress.setStringPainted(true);
        add(jpgbTopProgress);
        jpgbTopProgress.setVisible(false);
        top += jpgbTopProgress.getHeight() + paddingY;

        btnHeight = 2 * btnFont.getSize();

        jbtnStopReceive = new JButton("ֹͣ停止接收");
        jbtnStopReceive.setFont(btnFont);
        jbtnStopReceive.setSize(btnFont.getSize() * 7, btnHeight);
        add(jbtnStopReceive);

        redrawFreeArea();

        dealAction();
    }

    private void redrawFreeArea() {
        int receiveProgressCount = receiveProgressMap.size();
        freeArea.setSize(width-2*paddingX, receiveProgressCount * unitHeight);
        height = 30 + top + btnHeight + 2*paddingY + freeArea.height;
        setSize(width, height);
        setLocation((parentWidth-15-width)/2, (parentHeight-30-height)/2);

        int i = 0;
        for(RecieverThread recieverThread : receiveProgressMap.keySet()) {
            FileReceiveProgress receiveProgress = receiveProgressMap.get(recieverThread);
            receiveProgress.setSize(freeArea.width, unitHeight);
            receiveProgress.setLocation(paddingX, top + i * unitHeight);
            ++i;
        }

        int stopReceiveButtonTop = height - 30
                - jbtnStopReceive.getHeight() - paddingY;
        jbtnStopReceive.setLocation((width - jbtnStopReceive.getWidth()) / 2,
                stopReceiveButtonTop);
    }

    private void exitDialog() {
        dispose();
    }

    private void dealAction() {
        jbtnStopReceive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitDialog();
            }
        });
    }

    private void addFileReceiveProgress(RecieverThread recieverThread,
                                        FileReceiveProgress fileReceiveProgress) {
        add(fileReceiveProgress);
        receiveProgressMap.put(recieverThread, fileReceiveProgress);
        redrawFreeArea();
        recieverThread.addSectionReceiverListener(fileReceiveProgress);
        fileReceiveProgress.setVisible(true);
    }

    @Override
    public void onBeginJoin() {

    }

    @Override
    public void onGetJoinCount(int count) {

    }

    @Override
    public void onJoinOne() {

    }

    @Override
    public void onAllDone() {

    }

    @Override
    public void addFileReceiverCenterListener(IFileReceiverCenterListener listner) {

    }

    @Override
    public void removeFileReceiverCenterListener(IFileReceiverCenterListener listner) {

    }

    @Override
    public void stopReceive() {

    }
}


