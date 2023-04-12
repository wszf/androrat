package gui.panel;

import gui.UserGUI;
import inout.Protocol;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;

import utils.wavIO;
import java.util.ResourceBundle;

public class SoundPanel extends JPanel {
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("gui.panel.messages"); //$NON-NLS-1$
	private UserGUI gui;
	private SourceDataLine dataLine;
	private boolean streaming;
	private JLabel lblCaptureSource;
	private JComboBox comboBox;
	private boolean mute = false ;
	private boolean isRecording = false;
	
	private JLabel lblMute ;
	private JLabel lblStop ;
	private JLabel lblStart ;
	private JLabel lblImage ;
	private JLabel lblSave ;
	
	private String nomRecord ;
	private FileOutputStream record;
	

	/**
	 * Create the panel.
	 */
	public SoundPanel(UserGUI gui) {
		streaming = false;
		this.gui = gui;
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null,BUNDLE.getString("Streaming-options"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, BUNDLE.getString("Informations"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_1, Alignment.LEADING, 0, 402, Short.MAX_VALUE)
						.addComponent(panel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 400, Short.MAX_VALUE))
					.addGap(8))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(26, Short.MAX_VALUE))
		);
		
		JLabel lblSampleRate = new JLabel(BUNDLE.getString("Sample-rate")); //$NON-NLS-1$
		
		JLabel lblSampleSizeBits = new JLabel(BUNDLE.getString("Sample-size-bits")); //$NON-NLS-1$
		
		JLabel lblChannels = new JLabel(BUNDLE.getString("Channels")); //$NON-NLS-1$
		
		JLabel lblSigned = new JLabel(BUNDLE.getString("Signed")); //$NON-NLS-1$
		
		JLabel lblValrate = new JLabel("val_rate");
		
		JLabel lblValsizebits = new JLabel("val_sizebits");
		
		JLabel lblValchannels = new JLabel("val_channels");
		
		JLabel lblValsigned = new JLabel("val_signed");
		
		JPanel panel_image = new JPanel();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblChannels)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblValchannels))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblSampleRate)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblValrate))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblSampleSizeBits)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblValsizebits))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblSigned)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblValsigned)))
					.addGap(18)
					.addComponent(panel_image, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblSampleRate)
								.addComponent(lblValrate))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblSampleSizeBits)
								.addComponent(lblValsizebits))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblChannels)
								.addComponent(lblValchannels))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblSigned)
								.addComponent(lblValsigned)))
						.addComponent(panel_image, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		ImageIcon getImg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(UserGUI.class.getResource("/gui/res/note.bmp")));
		Image img = getImg.getImage();
		Image newimg = img.getScaledInstance(250, 111,  java.awt.Image.SCALE_SMOOTH);  
		lblImage = new JLabel(new ImageIcon(newimg));
		lblImage.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				changeLblImage();
			}
		});
		//JLabel lblImage = new JLabel("image");
		panel_image.add(lblImage);
		panel_1.setLayout(gl_panel_1);
		setLayout(groupLayout);
		
		lblCaptureSource = new JLabel(BUNDLE.getString("Capture-source")); //$NON-NLS-1$
		
		Object[] items = {BUNDLE.getString("Microphone"), BUNDLE.getString("Voice-call"), BUNDLE.getString("Up-voice-call"), BUNDLE.getString("Down-voice-call")};
		comboBox = new JComboBox(items);
		
		getImg = reziseImage("/gui/res/gtk-media-play-ltr.png");
		lblStart = new JLabel(getImg);
		lblStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				fireButtonStartStream();
			}
		});
		
		getImg = reziseImage("/gui/res/gtk-media-stop.png");
		lblStop = new JLabel(getImg);
		lblStop.setEnabled(false);
		lblStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fireButtonStopStream();
			}
		});
		
		getImg = reziseImage("/gui/res/sound.png");
		lblMute = new JLabel(getImg);
		lblMute.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fireButtonMute();
			}
		});
		
		getImg = reziseImage("/gui/res/gtk-media-record.png");
		lblSave = new JLabel(getImg);
		lblSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fireButtonRecord();
			}
		});
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCaptureSource)
					.addGap(18)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblStart)
					.addGap(18)
					.addComponent(lblStop)
					.addGap(18)
					.addComponent(lblMute)
					.addGap(18)
					.addComponent(lblSave)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblSave)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addComponent(lblMute)
							.addComponent(lblStop)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblCaptureSource)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblStart))))
					.addContainerGap(12, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		AudioFormat format = new AudioFormat(11025, 16, 1, true, false);
		try {
			dataLine = AudioSystem.getSourceDataLine(format);
			dataLine.open(format);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		lblValchannels.setText("1");
		lblValrate.setText("11025");
		lblValsigned.setText("true");
		lblValsizebits.setText("16");
	}
	
	private ImageIcon reziseImage(String path)
	{
		ImageIcon getImg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(UserGUI.class.getResource(path)));
		Image img = getImg.getImage();
		Image newimg = img.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH);  
		return new ImageIcon(newimg); 
	}

	private void fireButtonStartStream() {
		streaming = true;
		int choice = Protocol.ARG_STREAM_AUDIO_MIC;
		if(comboBox.getSelectedItem().equals(BUNDLE.getString("Voice-call"))) choice = Protocol.ARG_STREAM_AUDIO_UPDOWN_CALL;
		else if(comboBox.getSelectedItem().equals(BUNDLE.getString("Up-voice-call"))) choice = Protocol.ARG_STREAM_AUDIO_UP_CALL;
		else if(comboBox.getSelectedItem().equals(BUNDLE.getString("Down-voice-call"))) choice = Protocol.ARG_STREAM_AUDIO_DOWN_CALL;
		
		//System.out.println("Envoi demande enregistrement, choix : "+((String)comboBox.getSelectedItem())+ " num="+choice);
		lblStart.setEnabled(false);
		lblStop.setEnabled(true);
		gui.fireStartSoundStreaming(choice);
		dataLine.start();
	}
	
	private void fireButtonStopStream() {
		streaming = false;
		lblStart.setEnabled(true);
		lblStop.setEnabled(false);
		gui.fireStopSoundStreaming();
	}
	
	private void fireButtonMute() {
		if(mute)
		{
			mute = false;
			dataLine.flush();
			ImageIcon getImg = reziseImage("/gui/res/sound.png");
			lblMute.setIcon(getImg);
			lblMute.validate();
		}
		else
		{
			mute = true;
			ImageIcon getImg = reziseImage("/gui/res/disable-sound.png");
			lblMute.setIcon(getImg) ;
			lblMute.validate();
		}
	}
	
	private void fireButtonRecord()
	{
		if(isRecording)
		{
			isRecording = false ;
			ImageIcon getImg = reziseImage("/gui/res/gtk-media-record.png");
			lblSave.setIcon(getImg);
			lblSave.validate();
			try
			{
				//Fin d'enregistrement, le fichier temporaire se voit rajout� une en tete et transform� en .wav
				wavIO wav = new wavIO(nomRecord+".pcm");
				wav.readRaw();
				wav.setHeaders();
				wav.setPath(nomRecord+".wav");
				wav.save();
				//le fichier temporaire est supprim�
				File f = new File(nomRecord+".pcm");
				f.delete();
				
				record.close();
				record = null;
				nomRecord = null ;
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			isRecording = true ;
			ImageIcon getImg = reziseImage("/gui/res/gtk-media-stop.png");
			lblSave.setIcon(getImg);
			lblSave.validate();
			
			String format = "dd_MM_yy_H_mm_ss"; 
			java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( format ); 
			java.util.Date date = new java.util.Date();
			nomRecord = "recordedSong_"+formater.format(date) ;
			try
			{
				//cr�ation du fichier temporaire qui va stocker l'audio jou� tant que record est on
				record = new FileOutputStream(nomRecord+".pcm");
				record.write("".getBytes()); 
				record = new FileOutputStream(nomRecord+".pcm",true);
				addSoundBytes("ma".getBytes());
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void changeLblImage()
	{
		ImageIcon getImg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(UserGUI.class.getResource("/gui/res/Jesus.jpeg")));
		Image img = getImg.getImage();
		Image newimg = img.getScaledInstance(250, 111,  java.awt.Image.SCALE_SMOOTH);
		lblImage.setIcon(new ImageIcon(newimg));
	}
	
	public void addSoundBytes(byte[] data) {
		if (!mute)
			dataLine.write(data, 0, data.length);
		if (isRecording)
		{
			try
			{
				record.write(data);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}		
	}
	
	
	
	public boolean getStreaming() {
		return streaming;
	}
}
