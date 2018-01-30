package sockettests.simplefiletransport.file_transport.multi_receiver.view;

import sockettests.simplefiletransport.file_transport.multi_receiver.FileReceiver;
import sockettests.simplefiletransport.file_transport.multi_receiver.IFileReceiverListener;
import sockettests.simplefiletransport.file_transport.multi_receiver.model.FileSectionModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


public class FileReceiveProgress extends JPanel 
		implements IFileReceiverListener {
	private static final long serialVersionUID = -3413056747937138747L;

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

	private JLabel jlblSenderName;
	private JLabel jlblFileName;
	private JProgressBar jpgbFile;
	
	private int fileSize;
	private int receivedSize;
	
	public void initPanel(int unitWidth, int unitHeight) {
		Font normalFont = new Font("����", Font.PLAIN, 14);
		Font smallFont = new Font("����", Font.PLAIN, 12);
		int normalFontSize = normalFont.getSize();
		int smallFontSize = smallFont.getSize();
		int paddingX = 5;
		int paddingY = 5;
		
		int width = unitWidth;
		int height = unitHeight;
		setSize(width, height);
		setLayout(null);
		
		jlblSenderName = new JLabel("");
		jlblSenderName.setFont(smallFont);
		jlblSenderName.setSize(smallFontSize*4, normalFontSize+2);
		jlblSenderName.setForeground(Color.red);
		jlblSenderName.setLocation(0, 0);
		add(jlblSenderName);
		
		jlblFileName = new JLabel("", JLabel.CENTER);
		jlblFileName.setFont(normalFont);
		jlblFileName.setSize(width - jlblSenderName.getWidth(), normalFontSize+2);
		jlblFileName.setLocation(jlblSenderName.getWidth(), 0);
		add(jlblFileName);
		
		jpgbFile = new JProgressBar();
		jpgbFile.setSize(width - 2*paddingX, normalFontSize + 2); 
		jpgbFile.setLocation(0, jlblFileName.getHeight() + paddingY);
		jpgbFile.setStringPainted(true);
		add(jpgbFile);
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
	
	@Override
	public void onGetFileList(FileReceiver receiver) {}

	@Override
	public synchronized void onBeginReceiveOneFile(FileReceiver receiver) {
		FileSectionModel file = receiver.getReceiveFile();
		String fileName = file.getTempFileName();
		jlblFileName.setText(fileName);
		fileSize = (int) file.getFileLen();
		receivedSize = 0;
		jpgbFile.setMaximum(100);
		jpgbFile.setValue(0);
	}

	@Override
	public synchronized void onReceiving(int receiveLen) {
		receivedSize += receiveLen;
		int value = (int) ((double) receivedSize
				/ fileSize * 100);
		jpgbFile.setValue(value);
	}

	@Override
	public void endReceiveOneFile(FileReceiver receiver) {}

	@Override
	public void onReceiveOver(FileReceiver receiver) {}

	@Override
	public void onReceiveFailure(FileReceiver receiver) {}
}
