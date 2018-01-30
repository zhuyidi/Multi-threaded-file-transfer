package sockettests.simplefiletransport.file_transport.multi_receiver.view;

import sockettests.simplefiletransport.file_transport.multi_receiver.*;
import sockettests.simplefiletransport.file_transport.multi_receiver.model.FileSectionModel;
import sockettests.simplefiletransport.file_transport.multi_receiver.model.RequestFileMap;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;


public class ReceiveProgressDialog extends JDialog
		implements IFileReceiveControllerSpeaker, IFileReceiverListener,
		IFileReceiveServerListener, IFileJoinListener {
	private static final long serialVersionUID = 1578361299790200727L;
	
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
	
	private JProgressBar jpgbTopProgress;
	private int requestFileCount;
	private int receivedFileCount;
	private int requestFileSectionCount;
	private int receivedFileSectionCount;
	private int requestJoinedFileCount;
	private int joinedFileCount;
	
	private JButton jbtnStopReceive;
	
	private IFileReceiveControllerListener controllerListener;
	
	private Map<FileReceiver, FileReceiveProgress> receiveProgressMap;
	private RequestFileMap requestFileMap;

	public void initView(FileReceiverCenter receiverCenter, RequestFileMap requestFileMap) {
		this.setFileReceiveControllerListener(receiverCenter);
		this.requestFileMap = requestFileMap;
		receiverCenter.getFileReceiverServer().addFileReceiverListener(this);
		receiverCenter.addFileJoinListener(this);
		this.receiveProgressMap = new HashMap<>();
		requestFileSectionCount = requestFileCount = 
		receivedFileCount = receivedFileSectionCount= 0;
		
		width = 400;
		height = 100;
		parentWidth = mfrmParent.getWidth();
		parentHeight = mfrmParent.getHeight();
		paddingX = 5;
		paddingY = 5;
		
		Font topicFont = new Font("΢���ź�", Font.BOLD, 30);
		Font normalFont = new Font("����", Font.PLAIN, 16);
		Font btnFont = new Font("����", Font.PLAIN, 14);
		Color topicColor = new Color(5, 62, 131);
		Color reportColor = Color.red;
		int topicFontSize = topicFont.getSize();
		unitHeight = 3*normalFont.getSize();
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
		top = jlblTopic.getHeight() + 3*paddingY;
		
		jlblActionReport = new JLabel("", JLabel.CENTER);
		jlblActionReport.setFont(normalFont);
		jlblActionReport.setForeground(reportColor);
		jlblActionReport.setSize(width, normalFont.getSize() + 2);
		jlblActionReport.setLocation(0, top);
		add(jlblActionReport);
		top += jlblActionReport.getHeight() + paddingY;
		
		jpgbTopProgress = new JProgressBar();
		jpgbTopProgress.setSize(width - 10 - 2*paddingX, normalFont.getSize()+2);
		jpgbTopProgress.setLocation(paddingX, top);
		jpgbTopProgress.setStringPainted(true);
		add(jpgbTopProgress);
		jpgbTopProgress.setVisible(false);
		top += jpgbTopProgress.getHeight() + paddingY;
		
		btnHeight = 2 * btnFont.getSize();
		
		jbtnStopReceive = new JButton("ֹͣ����");
		jbtnStopReceive.setFont(btnFont);
		jbtnStopReceive.setSize(btnFont.getSize() * 7, btnHeight);
		add(jbtnStopReceive);
		
		redrawFreeArea();
		
		dealAction();
	}
	
	public void setBtnStopReceive(boolean value) {
		jbtnStopReceive.setEnabled(value);
	}
	
	private void redrawFreeArea() {
		int receiveProgressCount = receiveProgressMap.size();
		freeArea.setSize(width-2*paddingX, receiveProgressCount * unitHeight);
		height = 30 + top + btnHeight + 2*paddingY + freeArea.height;
		setSize(width, height);
		setLocation((parentWidth-15-width)/2, (parentHeight-30-height)/2);
		
		int i = 0;
		for(FileReceiver receiver : receiveProgressMap.keySet()) {
			FileReceiveProgress receiveProgress = receiveProgressMap.get(receiver);
			receiveProgress.setSize(freeArea.width, unitHeight);
			receiveProgress.setLocation(paddingX, top + i * unitHeight);
			++i;
		}
		
		int stopReceiveButtonTop = height - 30 
				- jbtnStopReceive.getHeight() - paddingY;
		jbtnStopReceive.setLocation((width - jbtnStopReceive.getWidth()) / 2, 
				stopReceiveButtonTop);
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
	
	private void addFileReceiveProgress(FileReceiver receiver, 
			FileReceiveProgress fileReceiveProgress) {
		add(fileReceiveProgress);
		receiveProgressMap.put(receiver, fileReceiveProgress);
		redrawFreeArea();
		receiver.addFileReceiverListener(fileReceiveProgress);
		fileReceiveProgress.setVisible(true);
	}
	
	@Override
	public synchronized void dealNewReceiver(FileReceiver receiver) {
		String ip = receiver.getIp();
		String hostName = receiver.getHostName();
		receiver.addFileReceiverListener(this);
		
		jlblActionReport.setText("���յ�һ������Դ:" + ip);
		FileReceiveProgress fileReceiveProgress = 
				new FileReceiveProgress();
		receiver.addFileReceiverListener(fileReceiveProgress);
		fileReceiveProgress.initPanel(freeArea.width, unitHeight);
		fileReceiveProgress.setSenderName(hostName);
		addFileReceiveProgress(receiver, fileReceiveProgress);
		receiver.startReceive();
	}
	
	@Override
	public void setFileReceiveControllerListener(IFileReceiveControllerListener listener) {
		this.controllerListener = listener;
	}

	@Override
	public void removeFileReceiveControllerListener(IFileReceiveControllerListener listener) {
		if(controllerListener == listener) {
			controllerListener = null;
		}
	}

	@Override
	public void stopReceive() {
		controllerListener.dealStopReceive();
	}

	@Override
	public synchronized void onReceiveOver(FileReceiver receiver) {
		FileReceiveProgress fileReceiveProgress = 
				receiveProgressMap.get(receiver);
		fileReceiveProgress.setVisible(false);
		receiveProgressMap.remove(receiver);
		redrawFreeArea();
	}

	private String receivedFileInfo() {
		StringBuffer str = new StringBuffer("Ӧ�����ļ�");
		str.append("(");
		str.append(receivedFileCount);
		str.append("/");
		str.append(requestFileCount);
		str.append(")���ѽ����ļ���Ƭ��(");
		
		str.append(receivedFileSectionCount);
		str.append("/");
		str.append(requestFileSectionCount);
		str.append(")");
		
		return str.toString();
	}
	
	@Override
	public synchronized void onGetFileList(FileReceiver receiver) {
		List<FileSectionModel> sectionList = receiver.getReceiveFileList();
		int sectionCount = sectionList.size();
		requestFileSectionCount += sectionCount;
		jlblActionReport.setText(receivedFileInfo());
	}

	@Override
	public synchronized void endReceiveOneFile(FileReceiver receiver) {
		++receivedFileSectionCount;
		receivedFileCount = requestFileMap.getReceivedFileCount();
		int value = (int ) ((double) receivedFileCount 
				/ requestFileCount * 100);
		jpgbTopProgress.setValue(value);
		jlblActionReport.setText(receivedFileInfo());
	}

	@Override
	public void onBeginReceiveOneFile(FileReceiver receiver) {}

	@Override
	public void onReceiving(int receiveLen) {}

	@Override
	public void onReceiveFailure(FileReceiver receiver) {
		// TODO ����ʧ�ܵĴ���
	}

	@Override
	public synchronized void onBeginJoin() {
		jlblActionReport.setText("��ʼ��Դ����");
	}

	@Override
	public synchronized void onGetJoinCount(int count) {
		joinedFileCount = 0;
		requestJoinedFileCount = count;
		jpgbTopProgress.setValue(0);
		jpgbTopProgress.setMaximum(100);
	}

	@Override
	public synchronized void onJoinOne() {
		++joinedFileCount;
		int joinedValue = (int) ((double) joinedFileCount
				/ requestJoinedFileCount * 100);
		jpgbTopProgress.setValue(joinedValue);
		jlblActionReport.setText("��������� " + joinedFileCount + "/"
				+ requestJoinedFileCount);
	}

	@Override
	public synchronized void onAllDone() {
		dispose();
	}
}
