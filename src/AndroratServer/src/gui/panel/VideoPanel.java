package gui.panel;


import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;


import gui.UserGUI;


import javax.swing.JPanel;


import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;


import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;


import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

public class VideoPanel extends JPanel
{

	/**
	 * The standard aspect ratios.
	 */
	private static final String[][] ASPECTS =
	{
	{ "<choose...>", null },
	{ "16:10", "16:10" },
	{ "16:9", "16:9" },
	{ "1.85:1", "185:100" },
	{ "2.21:1", "221:100" },
	{ "2.35:1", "235:100" },
	{ "2.39:1", "239:100" },
	{ "5:3", "5:3" },
	{ "4:3", "4:3" },
	{ "5:4", "5:4" },
	{ "1:1", "1:1" } };

	private MediaPlayerFactory factory;
	private EmbeddedMediaPlayer mediaPlayer;
	private CanvasVideoSurface videoSurface;

	private JFrame frame;
	private JPanel contentPane;
	private JPanel videoPane;
	private Canvas videoCanvas;
	private JPanel controlsPane;
	private JPanel allPanel ;
	private JLabel standardAspectLabel;
	private JComboBox standardAspectComboBox;
	private JLabel lblStart;
	private JLabel lblPause;
	private JLabel lblStop;
	private JButton btnStartStream;
	private boolean streaming = false;
	private boolean playing = false;
	FileOutputStream fout;
	private UserGUI gui;
	String filename;

	/**
	 * Create the panel.
	 */
	public VideoPanel(UserGUI gui)
	{
		 NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "D://Util/VLC");
			Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		this.gui = gui ;
		factory = new MediaPlayerFactory("--no-video-title-show");
	    mediaPlayer = factory.newEmbeddedMediaPlayer();
	    
	    videoPane = new JPanel();
	    videoPane.setBorder(new CompoundBorder(new LineBorder(Color.black, 1), new EmptyBorder(0, 0, 0, 0)));
	    videoPane.setLayout(new BorderLayout());
	    videoPane.setBackground(Color.white);
	    
	    videoCanvas = new Canvas();
	    videoCanvas.setBackground(Color.white);
	    videoCanvas.setSize(720, 350);


	    videoPane.add(videoCanvas, BorderLayout.CENTER);
	    
	    videoSurface = factory.newVideoSurface(videoCanvas);
	    
	    mediaPlayer.setVideoSurface(videoSurface);
	    
	    standardAspectLabel = new JLabel("Standard Aspect:");
	    standardAspectLabel.setDisplayedMnemonic('s');
	    
	    standardAspectComboBox = new JComboBox(ASPECTS);
	    standardAspectComboBox.setEditable(false);
	    standardAspectComboBox.setRenderer(new DefaultListCellRenderer() {
	      @Override
	      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	        JLabel l = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	        String[] val = (String[])value;
	        l.setText(val[0]);
	        return l;
	      }
	    });
	   
	    standardAspectLabel.setLabelFor(standardAspectComboBox);
	    
		lblPause = new JLabel(reziseImage(("/gui/res/gtk-media-pause.png")));
	    
		lblStart = new JLabel(reziseImage("/gui/res/gtk-media-play-ltr.png"));
		
		
		lblStop = new JLabel(reziseImage("/gui/res/gtk-media-stop.png"));
		lblStop.setEnabled(false);
		 btnStartStream = new JButton("Start stream");
		
	    controlsPane = new JPanel();
	    controlsPane.setLayout(new BoxLayout(controlsPane, BoxLayout.X_AXIS));
	    controlsPane.add(standardAspectLabel);
	    controlsPane.add(Box.createHorizontalStrut(4));
	    controlsPane.add(standardAspectComboBox);
	    controlsPane.add(Box.createHorizontalStrut(5));
	    controlsPane.add(btnStartStream);
	    controlsPane.add(lblStart);
	    controlsPane.add(Box.createHorizontalStrut(4));
	    controlsPane.add(lblStop);
	    controlsPane.add(Box.createHorizontalStrut(4));
	    controlsPane.add(lblPause);
	 
	    
	    contentPane = new JPanel();
	    contentPane.setBorder(new EmptyBorder(16, 16, 16, 16));
	    contentPane.setLayout(new BorderLayout(16, 16));
	    contentPane.add(videoPane, BorderLayout.CENTER);
	    contentPane.add(controlsPane, BorderLayout.SOUTH);
	    //*
	    frame = new JFrame("Video streaming");
	    frame.setContentPane(contentPane);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    
	    frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
	    
	    frame.pack();
	    frame.setVisible(true);
	    //*/
	    //mediaPlayer.playMedia("tbbt.mp4");
	    
//	    contentPane.setVisible(true);
	    //allPanel.add(frame);
	  //  gui.add(allPanel);
	    
	    standardAspectComboBox.addActionListener(new ActionListener() {
	      @Override
	      public void actionPerformed(ActionEvent e) {
	        Object selectedItem = standardAspectComboBox.getSelectedItem();
	        if(selectedItem != null) {
	          String[] value = (String[])selectedItem;
	          mediaPlayer.setAspectRatio(value[1]);
	        }
	      }
	    });

	    btnStartStream.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                    fireButtonStartStreaming();
            }
	    });
	    
	    lblStart.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				fireButtonPlay();
			}
		});
	    
	    lblStop.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				fireButtonPlay();
			}
		});
	    
	    lblPause.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				mediaPlayer.pause();
			}
		});
	    
	}

	private void formWindowClosed(java.awt.event.WindowEvent evt){
		//System.out.println("coucou");
		frame = null;
		gui.fireButtonCloseTab();
	}
	
	/*
	 * mediaPlayer = new EmbeddedMediaPlayerComponent(); scrollPane.setViewportView(mediaPlayer); setLayout(groupLayout);
	 */
	private ImageIcon reziseImage(String path)
	{
		ImageIcon getImg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(UserGUI.class.getResource(path)));
		Image img = getImg.getImage();
		Image newimg = img.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH);  
		return new ImageIcon(newimg); 
	}
	public boolean getStreaming()
	{
		return streaming;
	}

	public void fireButtonStartStreaming()
	{
		if (!streaming)
		{
			filename = new Date(System.currentTimeMillis()).toString().replaceAll(" ", "_") + ".mp4";
			try
			{
				fout = new FileOutputStream(new File(filename));
				gui.fireStartVideoStream();
				btnStartStream.setText("Stop Streaming");
				streaming = true;
			} catch (FileNotFoundException e)
			{
				gui.getGUI().logErrTxt("Cannot create output file for video streaming");
			}
		} else
		{
			gui.fireStopVideoStream();
			streaming = false;
		}
	}
	
 
	public void fireButtonPlay() {
		if(!playing) {
			mediaPlayer.playMedia(filename);
			lblStart.setEnabled(false);
			lblStop.setEnabled(true);
			playing = true;
		}
		else {
			mediaPlayer.stop();
			lblStart.setEnabled(true);
			lblStop.setEnabled(false);
			playing = false;
		}
	}
	

	public void addVideoBytes(byte[] data)
	{
		try
		{
			fout.write(data);
		} catch (IOException e)
		{
			gui.getGUI().logErrTxt("Error while writing in video file");
		}
	}
}