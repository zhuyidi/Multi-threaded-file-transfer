package multhreadfiletransport.view;

import java.awt.Container;

import javax.swing.JFrame;

/**
 * Created by dela on 1/26/18.
 */


public class TestJFrame {
    private DownLoadPanel downLoadPanel;

    public TestJFrame() {
        initView();
    }

    private void initView() {
        JFrame jframe = new JFrame();

        jframe.setLayout(null);
        jframe.setTitle("测试窗口");
        jframe.setSize(300, 150);
        jframe.setLocationRelativeTo(null);

        Container container = jframe.getContentPane();

        downLoadPanel = new DownLoadPanel();
        downLoadPanel.setLocation(40, 20);
        container.add(downLoadPanel);

        // JDialog jdialog = new JDialog(jframe, "对话框？", false); // 与jframe同为窗口不能添加到jframe
        // jdialog.setSize(150, 75);
        // jdialog.setLocationRelativeTo(null);
        // jdialog.setVisible(true);

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
    }

    public void updateFileMesssage(String fileMessage) {
        downLoadPanel.updateFileMesssage(fileMessage);
    }

    public void updateProgressBar(int updateValue) {
        downLoadPanel.updateProgressBar(updateValue);
    }

    public static void main(String[] args) {
        new TestJFrame();
    }
}