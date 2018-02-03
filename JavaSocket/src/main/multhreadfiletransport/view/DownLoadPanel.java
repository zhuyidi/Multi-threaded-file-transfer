package multhreadfiletransport.view;


import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


/**
 * Created by dela on 1/26/18.
 */


public class DownLoadPanel extends JPanel {
    private static final long serialVersionUID = -5955191208512952432L;
    // 显示的是文件的基本信息（文件名+文件大小），例test.txt(52KB)
    private JLabel jlabelFileMessage;
    private JProgressBar jprogressbarDownLoad;

    public DownLoadPanel() {
// 固定其大小
        this.setLayout(null);
        this.setSize(200, 47); // 测试
        this.setBorder(BorderFactory.createLineBorder(Color.black, 1)); // 测试
        initDownLoadJPanel();
    }

    private void initDownLoadJPanel() {
        jlabelFileMessage = new JLabel();

        jlabelFileMessage.setBounds(5, 5, this.getWidth() - 10, 20);
        jlabelFileMessage.setBorder(BorderFactory.createLineBorder(Color.black, 1)); // 测试
        this.add(jlabelFileMessage);

        jprogressbarDownLoad = new JProgressBar();

        jprogressbarDownLoad.setBounds(jlabelFileMessage.getX(),
                jlabelFileMessage.getY() + jlabelFileMessage.getHeight() + 1,
                jlabelFileMessage.getWidth(), 16);
        jprogressbarDownLoad.setStringPainted(true);
        jprogressbarDownLoad.setForeground(Color.green);

        this.add(jprogressbarDownLoad);
    }

    public void updateFileMesssage(String fileMessage) {
        jlabelFileMessage.setText(fileMessage);
    }

    public void updateProgressBar(int updateValue) {
        jprogressbarDownLoad.setValue(updateValue);
    }
}
