package multhreadfiletransport.view;

import multhreadfiletransport.client.reciever.RecieverThread;
import multhreadfiletransport.model.RecieverSectionInfo;
import multhreadfiletransport.observer.filetransport.ISectionReceiverListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by dela on 1/29/18.
 */
public class FileReceiveProgress extends JPanel implements ISectionReceiverListener {
    private static final long serialVersionUID = -3413056747937138747L;

    // 发送端的名字
    private JLabel jlblSenderName;
    // 发送的文件名字
    private JLabel jlblFileName;
    // 进度条
    private JProgressBar jpgbFile;
    // 文件总大小
    private int fileSize;
    // 目前接收到的大小
    private int receivedSize;

    public FileReceiveProgress() {
    }

    public FileReceiveProgress(LayoutManager layout) {
        super(layout);
    }

    public FileReceiveProgress(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public FileReceiveProgress(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public void setFileName(String fileName) {
        jlblFileName.setText(fileName);
    }

    public void setSenderName(String senderName) {
        jlblSenderName.setText(senderName);
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
        jpgbFile.setMaximum(fileSize);
        jpgbFile.setValue(0);
    }

    public void initPanel(int unitWidth, int unitHeight) {
        Font normalFont = new Font("宋体", Font.PLAIN, 14);
        Font smallFont = new Font("宋体", Font.PLAIN, 12);
        int normalFontSize = normalFont.getSize();
        int smallFontSize = smallFont.getSize();
        int paddingX = 5;
        int paddingY = 5;

        // 设置这个JPanel的长宽/布局等
        int width = unitWidth;
        int height = unitHeight;
        setSize(width, height);
        setLayout(null);

        // sender信息的JLabel
        jlblSenderName = new JLabel("");
        jlblSenderName.setFont(smallFont);
        jlblSenderName.setSize(smallFontSize*4, normalFontSize+2);
        jlblSenderName.setForeground(Color.red);
        jlblSenderName.setLocation(0, 0);
        add(jlblSenderName);

        // 文件信息的JLabel
        jlblFileName = new JLabel("", JLabel.CENTER);
        jlblFileName.setFont(normalFont);
        jlblFileName.setSize(width - jlblSenderName.getWidth(), normalFontSize+2);
        jlblFileName.setLocation(jlblSenderName.getWidth(), 0);
        add(jlblFileName);

        // 进度条的JLabel
        jpgbFile = new JProgressBar();
        jpgbFile.setSize(width - 2*paddingX, normalFontSize + 2);
        jpgbFile.setLocation(0, jlblFileName.getHeight() + paddingY);
        jpgbFile.setStringPainted(true);
        add(jpgbFile);
    }

    @Override
    public void onGetSectionList(RecieverThread recieverThread) {

    }

    // 开始接收一个section片段
    @Override
    public void onBeginReceiveOneSection(RecieverSectionInfo sectionInfo) {
        // 得到这个分片文件的名字和长度, 并设置进这个面板里
        String fileName = sectionInfo.getTempFileName();
        jlblFileName.setText(fileName);
        fileSize = (int) sectionInfo.getSectionLen();
        receivedSize = 0;
        jpgbFile.setMaximum(100);
        jpgbFile.setValue(0);
    }

    // 正在接收文件
    @Override
    public void onReceiving(int receiveLen) {
        // 每接收一点, 就给receiveLen上加一点, 并求出百分比
        receivedSize += receiveLen;
        int value = (int) ((double) receivedSize / fileSize * 100);
        jpgbFile.setValue(value);
    }

    @Override
    public void endReceiveOneSection(RecieverThread recieverThread) {

    }

    @Override
    public void onReceiveOver(RecieverThread recieverThread) {

    }

    @Override
    public void onReceiveFailure(RecieverThread recieverThread) {

    }
}
