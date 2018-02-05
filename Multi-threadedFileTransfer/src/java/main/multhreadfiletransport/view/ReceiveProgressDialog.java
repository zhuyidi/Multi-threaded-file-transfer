package multhreadfiletransport.view;

import multhreadfiletransport.client.reciever.RecieverCenter;
import multhreadfiletransport.client.reciever.RecieverMap;
import multhreadfiletransport.client.reciever.RecieverThread;
import multhreadfiletransport.model.RecieverSectionInfo;
import multhreadfiletransport.observer.filetransport.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Created by dela on 1/29/18.
 */
public class ReceiveProgressDialog extends JDialog implements IFileJoinListener,
        IFileReceiverCenterSpeaker, IReceiverServerListener, ISectionReceiverListener {
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

    private JLabel jlblTopic;
    private JLabel jlblActionReport;
    private JFrame mfrmParent;


    // 顶部进度条, 用于总控
    private JProgressBar jpgbTopProgress;
    // 设置所请求的文件的总个数(targetFile)(这里到底是targetFile还是所有的section总和)
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

    // 接收中心(RC)的Listener
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
        jlblActionReport.setText("本次需传输" + fileCount + "个文件");
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
        receiverCenter.getRecieverServer().addFileReceiverListener(this);
        receiverCenter.addFileJoinListener(this);
        this.receiveProgressMap = new HashMap<>();
        requestFileSectionCount = requestFileCount =
                receivedFileCount = receivedFileSectionCount = 0;

        // 设置JDialog的相关参数
        width = 400;
        height = 100;
        parentWidth = mfrmParent.getWidth();
        parentHeight = mfrmParent.getHeight();
//        parentWidth = 800;
//        parentHeight = 400;
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

    // 处理停止接收
    private void dealAction() {
        jbtnStopReceive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitDialog();
            }
        });
    }

    // 增加一组线程和进度条
    private void addFileReceiveProgress(RecieverThread recieverThread,
                                        FileReceiveProgress fileReceiveProgress) {
        add(fileReceiveProgress);
        receiveProgressMap.put(recieverThread, fileReceiveProgress);
        redrawFreeArea();
        recieverThread.addSectionReceiverListener(fileReceiveProgress);
        fileReceiveProgress.setVisible(true);
    }

    // 1. IReceiverServerListener接口的处理方法, 用于产生的新线程和新进度条合并的工作
    // 当RS端连接上一个sender之后新产生一个线程, 并将这个线程的信息发送给JDialog
    // synchronized
    @Override
    public void dealNewReceiverThread(RecieverThread recieverThread) {
        String ip = recieverThread.getSocket().getInetAddress().getHostAddress();
        String hostName = recieverThread.getSocket().getInetAddress().getHostName();
        // 将JDialog加到该线程的监听队列中
        recieverThread.addSectionReceiverListener(this);
        jlblActionReport.setText("接收到一个发送源" + ip);

        // new出一个进度条面板
        FileReceiveProgress fileReceiveProgress = new FileReceiveProgress();
        // 将该线程所对应的进度条也加到该线程的监听队列中
        recieverThread.addSectionReceiverListener(fileReceiveProgress);
        // 初始化进度条面板
        fileReceiveProgress.initPanel(freeArea.width, unitHeight);
        // 设置发送端name
        fileReceiveProgress.setSenderName(hostName);
        // 将这一对线程/进度条加入到map中
        addFileReceiveProgress(recieverThread, fileReceiveProgress);
        // 接收线程开始接收
        recieverThread.run();
    }

    // 初始化targetFile的数量
    @Override
    public void initTragetFileCount(int targetFileCount) {
        setReceiveFileCount(targetFileCount);
    }

    // 2. IFileReceiverCenterSpeaker接口的方法, 用于RC和JDialog之间的通信,
    //     比如JDialog停止接收文件
    // 添加一个RC的监听者
    @Override
    public void addFileReceiverCenterListener(IFileReceiverCenterListener listner) {
        centerListener = listner;
    }

    // 删除一个RC的监听者
    @Override
    public void removeFileReceiverCenterListener(IFileReceiverCenterListener listner) {
        if(listner == centerListener) {
            centerListener = null;
        }
    }

    // 停止接收文件(这里应该给一个参数, 用于通知RC暂停接收时应该要做的事, 为断点续传做准备)
    // 停止接收文件, 这里调用RC的dealStopReceive
    @Override
    public void stopReceive() {
        centerListener.dealStopReceive();
    }

    // 3. ISectionReceiverListener的接口, 主要用于RT与JDialog之间的通信
    // 这里关于接收某一section的进度的相关方法不做处理
    // synchronized
    @Override
    public void onGetSectionList(RecieverThread recieverThread) {
        List<RecieverSectionInfo> sectionInfoList = recieverThread.getSectionFileInfoList();
        int sectionCount = sectionInfoList.size();
        requestFileSectionCount += sectionCount;
        jlblActionReport.setText(receivedFileInfo());
    }

    private String receivedFileInfo() {
        StringBuffer str = new StringBuffer("应接收文件");
        str.append("(");
        str.append(receivedFileCount);
        str.append("/");
        str.append(requestFileCount);
        str.append("已接收文件或片段");

        str.append(receivedFileSectionCount);
        str.append("/");
        str.append(requestFileSectionCount);
        str.append(")");

        return str.toString();
    }

    // 在JDialog中不做处理
    @Override
    public void onBeginReceiveOneSection(RecieverSectionInfo sectionInfo) { }

    // 在JDialog中不做处理
    @Override
    public void onReceiving(int receiveLen) { }

    // 接收完一个section
    // 每接收完一个section, 就计算一下当前是否有所有section都接收完的targetFile, 保存在receiveedFileCount中
    // synchronized
    @Override
    public void endReceiveOneSection(RecieverThread recieverThread) {
        ++receivedFileSectionCount;
        // 在map中得到标记完全的targetFile的数量
        receivedFileCount = recieverMap.getMarkTragetFileCount();
        int value = (int ) ((double) receivedFileCount
                / requestFileCount * 100);
        jpgbTopProgress.setValue(value);
        jlblActionReport.setText(receivedFileInfo());
    }

    // 当一个sender发送文件结束之后, 删除这个sender的线程和进度条所对应的进度条, 重新绘制JDialog
    // synchronized
    @Override
    public void onReceiveOver(RecieverThread recieverThread) {
        FileReceiveProgress fileReceiveProgress = receiveProgressMap.get(recieverThread);
        fileReceiveProgress.setVisible(false);
        receiveProgressMap.remove(recieverThread);
        redrawFreeArea();
    }

    @Override
    public void onReceiveFailure(RecieverThread recieverThread) {
        // TODO 异常处理
    }

    // 4. IFileJoinListener接口的方法
    // 开始合并文件
    // synchronized
    @Override
    public void onBeginJoin() {
        jlblActionReport.setText("开始文件合并");
    }

    // 得到已经合并完成的targetFile的个数
    // synchronized
    @Override
    public void onGetJoinCount(int count) {
        joinedFileCount = 0;
        requestJoinedFileCount = count;
        jpgbTopProgress.setValue(0);
        jpgbTopProgress.setMaximum(100);
    }

    // 合并完一个targetFile
    // synchronized
    @Override
    public void onJoinOne() {
        ++joinedFileCount;
        int joinedValue = (int) ((double) joinedFileCount
                / requestJoinedFileCount * 100);
        jpgbTopProgress.setValue(joinedValue);
        jlblActionReport.setText("已完成合并" + joinedFileCount + "/"
                + requestJoinedFileCount);
    }

    // 全部合并完, 释放资源
    @Override
    public void onAllDone() {
        // 释放资源
        dispose();
    }
}