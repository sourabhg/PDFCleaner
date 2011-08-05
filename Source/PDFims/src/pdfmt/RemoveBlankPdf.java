/*  This file is part of PDFCleaner.
 *  Application automatically removes the blank pages from *.pdf files.This is main application file.
 *  Only PDF files supported.
 *
 *  Copyright (C) 2010  Sourabh Gandhe
 *                      Ashwini Kadam
 *	                    Siddharth Chitambar
 *  PDFCleaner is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  PDFCleaner is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PDFCleaner. Check for file named COPYING.
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package pdfmt;

/****************************************************************/
/*                      RemoveBlankPdf	                        */
/*                                                              */
/****************************************************************/
//import RemoveBlankPdf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageOutputStream;
import javax.media.jai.PlanarImage;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.text.PageText;
import org.icepdf.core.util.GraphicsRenderingHints;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.sun.media.imageio.plugins.tiff.BaselineTIFFTagSet;
import com.sun.media.imageio.plugins.tiff.TIFFDirectory;
import com.sun.media.imageio.plugins.tiff.TIFFField;
import com.sun.media.imageio.plugins.tiff.TIFFTag;
import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

// TODO: Auto-generated Javadoc
/**
 * Summary description for RemoveBlankPdf.
 */

public class RemoveBlankPdf extends JFrame implements Runnable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	// Variables declaration
	/** The j label1. */
	private JLabel jLabel1;

	/** The j label2. */
	private JLabel jLabel2;

	/** The Source field. */
	private JTextField SourceField;

	/** The Dest field. */
	private JTextField DestField;

	/** The S button. */
	private JButton SButton;

	/** The d button. */
	private JButton dButton;

	/** The c button. */
	private JButton cButton;

	/** The e button. */
	private JButton eButton;

	/** The msg. */
	private JLabel msg;

	/** The content pane. */
	private JPanel contentPane;

	/** The menu bar1. */
	private JMenuBar menuBar1;

	/** The menu1. */
	private JMenu menu1;

	/** The menu item1. */
	private JMenuItem menuItem1;

	/** The menu item2. */
	private JMenuItem menuItem2;

	/** The menu item3. */
	private JMenuItem menuItem3;

	/** The menu2. */
	private JMenu menu2;

	/** The menu item4. */
	private JMenuItem menuItem4;

	/** The menu item5. */
	private JMenuItem menuItem5;

	/** The menu item6. */
	private JMenuItem menuItem6;

	/** The suspendflag. */
	volatile boolean suspendflag;

	/** The exitflag. */
	volatile boolean exitflag = false;

	/** The worker. */
	private Thread worker;

	/** The flag. */
	boolean flag = false;
	// -------- NEW CHANGE
	// private JDialog dlg;
	// -------- NEW CHANGE
	// -------- CHANGE
	/** The jprogress. */
	private JProgressBar jprogress;
	// -------- CHANGE
	// End of variables declaration

	// End of variables declaration
	/** The Constant FAILURE. */
	public static final int FAILURE = 2;

	/** The Constant BLANK. */
	public static final int BLANK = 0;

	/** The Constant NOTBLANK. */
	public static final int NOTBLANK = 1;
	// value where we can consider that this is a blank image
	// can be much higher depending of the TIF source
	// (ex. scanner or fax)
	/** The Constant BLANK_THRESHOLD. */
	public static final int BLANK_THRESHOLD = 6100;

	/** The Constant INCH_RESOLUTION_UNIT. */
	private static final char[] INCH_RESOLUTION_UNIT = new char[] { 2 };

	/** The Constant X_DPI_RESOLUTION. */
	private static final long[][] X_DPI_RESOLUTION = new long[][] { { 150, 1 } };

	/** The Constant Y_DPI_RESOLUTION. */
	private static final long[][] Y_DPI_RESOLUTION = new long[][] { { 150, 1 } };

	/** The Constant BITS_PER_SAMPLE. */
	private static final char[] BITS_PER_SAMPLE = new char[] { 1 };

	/** The Constant COMPRESSION. */
	private static final char[] COMPRESSION = new char[] { BaselineTIFFTagSet.COMPRESSION_LZW };

	/** The Constant HEIGHT. */
	private static final int HEIGHT = 1650;

	/** The logger. */
	private static Logger logger = Logger.getLogger(RemoveBlankPdf.class);

	/** The start time. */
	private long startTime = 0;

	/** The stop time. */
	private long stopTime = 0;

	/** The SELECTIO n_ flag. */
	private boolean SELECTION_FLAG = false;

	/** The TEM p_ dir. */
	private static String TEMP_DIR = ".PDF_CLEANER_TEMP//";

	/** The TEM p_ tiff. */
	private static String TEMP_TIFF = "3.tiff";

	/** The TEM p_ ext r_ text. */
	private static String TEMP_EXTR_TEXT = "extractedtext.txt";
	
	private static String FILES_OF_TYPE = "Portable Document Format(PDF)";
	
	private static String ALLOWED_FILE_EXTENSION = "pdf";
	

	/**
	 * Instantiates a new removes the blank pdf.
	 */
	public RemoveBlankPdf() {// change
		startTime = System.currentTimeMillis();

		logger.info("In the constructor");
		/*
		 * super(); initializeComponent();
		 * 
		 * this.setVisible(true);
		 * 
		 * //change
		 */
		stopTime = System.currentTimeMillis();
		logger.info("Exit constructor with time: "
				+ ((stopTime - startTime) / 1000));
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Windows Form Designer. Otherwise, retrieving design
	 * might not work properly. Tip: If you must revise this method, please
	 * backup this GUI file for JFrameBuilder to retrieve your design properly
	 * in future, before revising this method.
	 * 
	 * @param e
	 *            the e
	 */
	public void menuItem1ActionPerformed(ActionEvent e) {
		startTime = System.currentTimeMillis();
		logger.info("Entered menuItem1ActionPerformed");

		String wd = null;

		JFileChooser fc = new JFileChooser(wd);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		/*FileNameExtensionFilter filter = new FileNameExtensionFilter(null,
				"pdf");*/
		FileFilter filter1 = new ExtensionFileFilter(FILES_OF_TYPE, ALLOWED_FILE_EXTENSION);
	   	fc.setFileFilter(filter1);
	   	fc.setAcceptAllFileFilterUsed(false);
	   	fc.addChoosableFileFilter(filter1);
	   	fc.setFileFilter(filter1);
	   	
		int rc = fc.showDialog(null, "Select Data File");
		if (rc == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String pdfSourceFile = file.getAbsolutePath();
			String append = "(converted).pdf";
			int length = pdfSourceFile.length();
			int point = (length - 4);
			String temp = pdfSourceFile.substring(0, point);
			String finaldes = temp + append;

			SourceField.setText(pdfSourceFile);
			DestField.setText(finaldes);
		}
		stopTime = System.currentTimeMillis();
		logger.info("Exit menuItem1ActionPerformed with time: "
				+ ((stopTime - startTime) / 1000));
	}

	/**
	 * Menu item2 action performed.
	 * 
	 * @param e
	 *            the e
	 */
	public void menuItem2ActionPerformed(ActionEvent e) {
		startTime = System.currentTimeMillis();
		logger.info("In the menuItem1ActionPerformed");

		String wd = null;
		String pdfSourceFile = SourceField.getText();
		if (pdfSourceFile.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Source File not specified",
					"Error", JOptionPane.ERROR_MESSAGE);
		} else {
			String append = "(converted).pdf";
			int length = pdfSourceFile.length();
			int point = (length - 4);
			String temp = pdfSourceFile.substring(0, point);
			String finaldes = temp + append;
			String pdfDestinationFile = finaldes;
			JFileChooser fc = new JFileChooser(wd);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			FileFilter filter1 = new ExtensionFileFilter(FILES_OF_TYPE, ALLOWED_FILE_EXTENSION);
		   	fc.setFileFilter(filter1);
		   	fc.setAcceptAllFileFilterUsed(false);
		   	fc.addChoosableFileFilter(filter1);
		   	fc.setFileFilter(filter1);

			int rc = fc.showDialog(null, "Select Data File");
			if (rc == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				pdfDestinationFile = file.getAbsolutePath();
				DestField.setText(pdfDestinationFile);
			}
		}

		stopTime = System.currentTimeMillis();
		logger.info("Exit menuItem2ActionPerformed with time: "
				+ ((stopTime - startTime) / 1000));

	}

	/**
	 * Menu item3 action performed.
	 * 
	 * @param e
	 *            the e
	 */
	public void menuItem3ActionPerformed(ActionEvent e) {
		startTime = System.currentTimeMillis();
		logger.info("In the menuItem3ActionPerformed");

		String pdfSourceFile = SourceField.getText();
		String pdfDestinationFile = DestField.getText();
		File f = new File(pdfDestinationFile);
		flag = true;
		msg.setVisible(flag);

		if (pdfSourceFile.isEmpty() && pdfDestinationFile.isEmpty()) {
			JOptionPane.showMessageDialog(this,
					"Please Select Source and Destination files", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		else if (pdfSourceFile.equals(pdfDestinationFile)) {

			JOptionPane.showMessageDialog(this,
					"Select Different Destination File", "Error",
					JOptionPane.ERROR_MESSAGE);

		} else if (pdfSourceFile.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Source File not specified",
					"Error", JOptionPane.ERROR_MESSAGE);
		
		} else if (checkForExistingFileInCurrDir(pdfDestinationFile))  {
			int  answer = JOptionPane.showConfirmDialog(this,
					"Destination file already exists in the current directory. \n Do you want to replace it?.", "Error",
					JOptionPane.YES_NO_OPTION);
			logger.info("answer:	 "+answer);
			if(answer==0){
				Thread t = new Thread(new Runnable() {
					public void run() {
					}
				});
				t.start();
				synchronized (this) {
					notifyAll();
				}
			} 
		}
		else if (pdfDestinationFile.isEmpty()) {
			JOptionPane.showMessageDialog(this,
					"Destination File not Specified", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else if (f.exists()) {
			JOptionPane.showMessageDialog(this,
					"File Already Exists. Choose Different Destination File",
					"Error", JOptionPane.ERROR_MESSAGE);
		}

		else {
			/*
			 * this.convertPDF(pdfSourceFile, pdfDestinationFile); }
			 */

			worker = new Thread();
			/*
			 * worker= new Thread(new Runnable() {
			 * 
			 * public void run() { }
			 * 
			 * });
			 */
			worker.start();
			// -------- NEW CHANGE

			// System.out.println("Thread Name : "+t.getName());

			// -------- CHANGE
			synchronized (this) {
				notifyAll();
			}
			// -------- CHANGE

			// System.out.println("Thread exiting: "+t.getName());
		}
		stopTime = System.currentTimeMillis();
		logger.info("Exit menuItem3ActionPerformed with time:	 "
				+ ((stopTime - startTime) / 1000));
	}

	/**
	 * Menu item4 action performed.
	 * 
	 * @param e
	 *            the e
	 */
	private void menuItem4ActionPerformed(ActionEvent e) {
		logger.info("In the menuItem4ActionPerformed");
		startTime = System.currentTimeMillis();
		HowTo t = new HowTo();
		// t.show();
		stopTime = System.currentTimeMillis();
		logger.info("Exit menuItem4ActionPerformed with time:	 "
				+ ((stopTime - startTime) / 1000));
	}

	/**
	 * Menu item5 action performed.
	 * 
	 * @param e
	 *            the e
	 */
	private void menuItem5ActionPerformed(ActionEvent e) {
		startTime = System.currentTimeMillis();
		logger.info("In the menuItem5ActionPerformed");
		s abus = new s();
		abus.show();
		stopTime = System.currentTimeMillis();
		logger.info("Exit menuItem5ActionPerformed with time:	 "
				+ ((stopTime - startTime) / 1000));
	}

	/**
	 * Menu item6 action performed.
	 * 
	 * @param e
	 *            the e
	 */
	private void menuItem6ActionPerformed(ActionEvent e) {
		startTime = System.currentTimeMillis();
		logger.info("In the menuItem6ActionPerformed");
		// System.out.println("Exit flag Value : "+exitflag);
		System.exit(0);
		// exitflag=true;
		// System.out.println("Exit flag Value : "+exitflag);
		stopTime = System.currentTimeMillis();
		logger.info("Exit menuItem6ActionPerformed with time:	 "
				+ ((stopTime - startTime) / 1000));
	}

	/**
	 * Initialize component.
	 */
	private void initializeComponent() {
		startTime = System.currentTimeMillis();
		logger.info("In the initializeComponent");
		// start of intialise component

		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		msg = new JLabel();
		SourceField = new JTextField();
		DestField = new JTextField();
		SButton = new JButton();
		dButton = new JButton();
		cButton = new JButton();
		eButton = new JButton();
		menuBar1 = new JMenuBar();
		menu1 = new JMenu();
		menuItem1 = new JMenuItem();
		menuItem2 = new JMenuItem();
		menuItem3 = new JMenuItem();
		menu2 = new JMenu();
		menuItem4 = new JMenuItem();
		menuItem5 = new JMenuItem();
		menuItem6 = new JMenuItem();
		flag = false;
		contentPane = (JPanel) this.getContentPane();
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("index1.gif")));

		// -------- NEW CHANGE
		// dlg = new JDialog(this, "Converting PDF.Please Wait...............",
		// true);

		jprogress = new JProgressBar();
		jprogress.setValue(0);
		jprogress.setLocation(new Point(500, 300));

		jprogress.setStringPainted(true);
		jprogress.setVisible(true);

		if (!SELECTION_FLAG) {
			Dimension dimension = new Dimension();
			dimension.setSize(500, 150);
			msg.setText(".....::::: Please select file to convert :::::.....");
			msg.setMaximumSize(dimension);
			msg.setVisible(true);
		}

		/*
		 * if(!flag) { Dimension dimension = new Dimension();
		 * dimension.setSize(500, 150);
		 * msg.setText("Converting PDF.Please be wait .................");
		 * msg.setMaximumSize(dimension); msg.setVisible(true); } else {
		 * msg.setText("....:::: PDF Conversion has completed ::::...");
		 * msg.setSize(1000, 150); msg.setVisible(true); }
		 */
		// dlg.setResizable(false);
		// dlg.setIconImage
		// (Toolkit.getDefaultToolkit()
		// .getImage(getClass().
		// getResource("index1.gif")));
		// dlg.add(BorderLayout.CENTER, jprogress);
		// dlg.add(BorderLayout.NORTH, new JLabel("Progress..."));
		// dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		// dlg.setMaximumSize(new Dimension(300,75));
		// dlg.setLocation(new Point(500,300));
		// dlg.setSize(300, 75);
		// dlg.setLocationRelativeTo(this);
		// -------- NEW CHANGE

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		//
		// jLabel1
		//
		jLabel1.setText("Source");
		//
		// jLabel2
		//
		jLabel2.setText("Destination");
		//
		// SourceField
		//
		// ======== menuBar1 ========
		{

			// ======== menu1 ========
			{
				menu1.setText("File");

				// ---- menuItem1 ----
				menuItem1.setText("SelectSource");
				menuItem1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						menuItem1ActionPerformed(e);
					}
				});
				menu1.add(menuItem1);

				// ---- menuItem2 ----
				menuItem2.setText("Select Destination");
				menuItem2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						menuItem2ActionPerformed(e);
					}
				});
				menu1.add(menuItem2);
				menu1.addSeparator();

				// ---- menuItem3 ----
				menuItem3.setText("Convert");
				menuItem3.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						menuItem3ActionPerformed(e);
					}
				});
				// ---- menuItem6 ----
				
				menuItem6.setText("Exit");
				menuItem6.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						menuItem6ActionPerformed(e);
					}
				});
				menu1.add(menuItem3);
				menu1.addSeparator();
				menu1.add(menuItem6);
			}
			menuBar1.add(menu1);

			// ======== menu2 ========
			{
				menu2.setText("Help");

				// ---- menuItem4 ----
				menuItem4.setText("HowTo");
				menuItem4.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						menuItem4ActionPerformed(e);
					}
				});
				menu2.add(menuItem4);
				menu2.addSeparator();
				// ---- menuItem5 ----
				menuItem5.setText("About US");
				menuItem5.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						menuItem5ActionPerformed(e);
					}
				});
				menu2.add(menuItem5);

				// menu2.add(menuItem6);
			}
			menuBar1.add(menu2);
		}
		setJMenuBar(menuBar1);
		pack();
		SourceField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SourceField_actionPerformed(e);
			}

		});
		//
		// DestField
		//
		DestField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DestField_actionPerformed(e);
			}

		});
		//
		// SButton
		//
		SButton.setText("Select");
		SButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SButton_actionPerformed(e);
			}

		});
		//
		// dButton
		//
		dButton.setText("Select");
		dButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dButton_actionPerformed(e);
			}

		});
		//
		// cButton
		//

		cButton.setText("Convert");
		cButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cButton_actionPerformed(e);
			}

		});

		//
		// eButton
		//

		eButton.setText("Exit");
		eButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eButton_actionPerformed(e);
			}

		});

		//
		// contentPane
		//
		contentPane.setLayout(null);
		// addComponent(contentPane, menuBar, 7,31,60,18);
		addComponent(contentPane, jLabel1, 7, 31, 60, 18);
		addComponent(contentPane, jLabel2, 6, 73, 71, 18);
		addComponent(contentPane, SourceField, 84, 31, 193, 22);
		addComponent(contentPane, DestField, 83, 70, 193, 22);
		addComponent(contentPane, SButton, 289, 30, 81, 25);
		addComponent(contentPane, dButton, 289, 68, 80, 25);
		addComponent(contentPane, cButton, 90, 180, 92, 25);
		addComponent(contentPane, eButton, 230, 180, 100, 25);
		addComponent(contentPane, msg, 50, 110, 300, 18);
		addComponent(contentPane, jprogress, 23, 133, 340, 26);

		//
		// RemoveBlankPdf
		//

		this.setTitle("PDFCleaner");
		this.setLocation(new Point(400, 300));
		this.setSize(new Dimension(400, 300));

		// end of initialise component
		stopTime = System.currentTimeMillis();
		logger.info("Exit initializeComponent with time:	 "
				+ ((stopTime - startTime) / 1000));
	}

	/**
	 * Add Component Without a Layout Manager (Absolute Positioning).
	 * 
	 * @param container
	 *            the container
	 * @param c
	 *            the c
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	private void addComponent(Container container, Component c, int x, int y,
			int width, int height) {
		c.setBounds(x, y, width, height);
		container.add(c);
	}

	//
	// TODO: Add any appropriate code in the following Event Handling Methods
	//
	/**
	 * Source field_action performed.
	 * 
	 * @param e
	 *            the e
	 */
	private void SourceField_actionPerformed(ActionEvent e) {
		// System.out.println("\nSourceField_actionPerformed(ActionEvent e) called.");
		// TODO: Add any handling code here

	}

	/**
	 * Dest field_action performed.
	 * 
	 * @param e
	 *            the e
	 */
	private void DestField_actionPerformed(ActionEvent e) {
		// System.out.println("\nDestField_actionPerformed(ActionEvent e) called.");
		// TODO: Add any handling code here

	}

	/**
	 * S button_action performed.
	 * 
	 * @param e
	 *            the e
	 */
	private void SButton_actionPerformed(ActionEvent e) {
		startTime = System.currentTimeMillis();
		logger.info("Enter SButton_actionPerformed");

		String wd = null;

		JFileChooser fc = new JFileChooser(wd);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileFilter filter1 = new ExtensionFileFilter(FILES_OF_TYPE, ALLOWED_FILE_EXTENSION);
	   	fc.setFileFilter(filter1);
	   	fc.setAcceptAllFileFilterUsed(false);
	   	fc.addChoosableFileFilter(filter1);
	   	fc.setFileFilter(filter1);

		int rc = fc.showDialog(null, "Select Data File");
		if (rc == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String pdfSourceFile = file.getAbsolutePath();
			String append = "(converted).pdf";
			int length = pdfSourceFile.length();
			int point = (length - 4);
			String temp = pdfSourceFile.substring(0, point);
			String finaldes = temp + append;

			SourceField.setText(pdfSourceFile);
			DestField.setText(finaldes);
		}
		// System.out.println("\nSButton_actionPerformed(ActionEvent e) called.");
		// TODO: Add any handling code here

		SELECTION_FLAG = true;
		stopTime = System.currentTimeMillis();
		logger.info("Exit SButton_actionPerformed with time:	 "
				+ ((stopTime - startTime) / 1000));
	}

	/**
	 * D button_action performed.
	 * 
	 * @param e
	 *            the e
	 */
	private void dButton_actionPerformed(ActionEvent e) {

		startTime = System.currentTimeMillis();
		logger.info("Enter dButton_actionPerformed");

		String wd = null;
		String pdfSourceFile = SourceField.getText();

		if (pdfSourceFile.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Source File not specified",
					"Error", JOptionPane.ERROR_MESSAGE);
		} else {

			String append = "(converted).pdf";
			int length = pdfSourceFile.length();
			int point = (length - 4);
			String temp = pdfSourceFile.substring(0, point);
			String finaldes = temp + append;
			String pdfDestinationFile = finaldes;
			JFileChooser fc = new JFileChooser(wd);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			FileFilter filter1 = new ExtensionFileFilter(FILES_OF_TYPE, ALLOWED_FILE_EXTENSION);
		   	fc.setFileFilter(filter1);
		   	fc.setAcceptAllFileFilterUsed(false);
		   	fc.addChoosableFileFilter(filter1);
		   	fc.setFileFilter(filter1);

			int rc = fc.showDialog(null, "Select Data File");
			if (rc == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				pdfDestinationFile = file.getAbsolutePath();
				DestField.setText(pdfDestinationFile);
			}
		}

		// System.out.println("\ndButton_actionPerformed(ActionEvent e) called.");
		// TODO: Add any handling code here
		stopTime = System.currentTimeMillis();
		logger.info("Exit dButton_actionPerformed with time:	 "
				+ ((stopTime - startTime) / 1000));
	}

	/**
	 * C button_action performed.
	 * Modified @SiD 11:15 AM 9/23/2010
	 * @param e
	 *            the e
	 */
	private void cButton_actionPerformed(ActionEvent e) {

		startTime = System.currentTimeMillis();
		logger.info("Enter cButton_actionPerformed");

		// -------- NEW CHANGE
		String pdfSourceFile = SourceField.getText();
		String pdfDestinationFile = DestField.getText();
		flag = true;
		msg.setVisible(flag);
		logger.info("pdfSourceFile --" + pdfSourceFile);
		logger.info("pdfDestinationFile --" + pdfDestinationFile);
		if (pdfSourceFile.equals("") &&
				 pdfDestinationFile.equals("")) {
			JOptionPane.showMessageDialog(this,
					"Please Select Source and Destination files.", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else if (pdfSourceFile.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Source File not specified",
					"Error", JOptionPane.ERROR_MESSAGE);
		
		} else if (checkForExistingFileInCurrDir(pdfDestinationFile))  {
			int  answer = JOptionPane.showConfirmDialog(this,
					"Destination file already exists in the current directory. \n Do you want to replace it?.", "Error",
					JOptionPane.YES_NO_OPTION);
			logger.info("answer:	 "+answer);
			if(answer==0){
				Thread t = new Thread(new Runnable() {
					public void run() {
					}
				});
				t.start();
				synchronized (this) {
					notifyAll();
				}
			} 
		}	
		else if (pdfDestinationFile.isEmpty()) {
			JOptionPane.showMessageDialog(this,
					"Destination File not Specified", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else if (checkForExistingFileInCurrDir(pdfDestinationFile))  {
			JOptionPane.showMessageDialog(this,
					"Destination file already exists in the current directory. \n Please choose another file name.", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else if (pdfSourceFile.equals(pdfDestinationFile)) {
			// System.out.println("Here");
			JOptionPane.showMessageDialog(this,
					"Select Different Destination File", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			Thread t = new Thread(new Runnable() {
				public void run() {
				}
			});
			t.start();
			// -------- CHANGE
			synchronized (this) {
				notifyAll();
			}
			// -------- CHANGE

		}
		stopTime = System.currentTimeMillis();
		logger.info("Exit dButton_actionPerformed with time:	 "
				+ ((stopTime - startTime) / 1000));
	}

	// removeBlankPdfPages(pdfSourceFile,pdfDestinationFile);
	// System.out.println("\ncButton_actionPerformed(ActionEvent e) called.");
	// TODO: Add any handling code here

	// }

	/**
	 * E button_action performed.
	 * 
	 * @param e
	 *            the e
	 */
	private void eButton_actionPerformed(ActionEvent e) {

		// System.out.println("\neButton_actionPerformed(ActionEvent e) called.");
		System.exit(0);
		// TODO: Add any handling code here

	}

	/**
	 * P button_action performed.
	 * 
	 * @param e
	 *            the e
	 */
	private void pButton_actionPerformed(ActionEvent e) {

		// System.out.println("\neButton_actionPerformed(ActionEvent e) called.");

		// TODO: Add any handling code here

		try {

			String wd = null;
			JFileChooser fc = new JFileChooser(wd);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			FileFilter filter1 = new ExtensionFileFilter(FILES_OF_TYPE, ALLOWED_FILE_EXTENSION);
		   	fc.setFileFilter(filter1);
		   	fc.setAcceptAllFileFilterUsed(false);
		   	fc.addChoosableFileFilter(filter1);
		   	fc.setFileFilter(filter1);

			int rc = fc.showOpenDialog(this);

			if (rc == JFileChooser.APPROVE_OPTION) {

				File file = fc.getSelectedFile();
				Runtime.getRuntime().exec(
						"rundll32 SHELL32.DLL,ShellExec_RunDLL \"" + file
								+ "\"");

				// System.out.println("THE FILE IS "+ file);

			}

		} catch (Exception e1) {
			System.out.println("Exception is" + e1);
		}

	}

	/**
	 * Convert pdf.
	 * 
	 * @param pdf
	 *            the pdf
	 * @param destPdf
	 *            the dest pdf
	 * @return true, if successful
	 */
	public boolean convertPDF(String pdf, String destPdf) {
		logger.info("In the boolean convertPDF");
		startTime = System.currentTimeMillis();
		/*
		 * FOLLOWING LINE CREATES A TEMP DIR FOR TIFF PURPOSES.
		 */
		createTempDir(TEMP_DIR);

		try {
			convert(pdf, TEMP_DIR + TEMP_TIFF, destPdf);
			stopTime = System.currentTimeMillis();
			logger.info("Exit boolean convertPDF with time:	 "
					+ ((stopTime - startTime) / 1000));
			return true;
		} catch (IOException e) {
			stopTime = System.currentTimeMillis();
			logger.info("Exit boolean convertPDF with time:	 "
					+ ((stopTime - startTime) / 1000));
			return false;
		}

	}

	/**
	 * Convert a PDF document to a TIF file.
	 * 
	 * @param pdf
	 *            the pdf
	 * @param tif
	 *            the tif
	 * @param destPdf
	 *            the dest pdf
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void convert(String pdf, String tif, String destPdf)
			throws IOException {

		startTime = System.currentTimeMillis();
		logger
				.info("In the boolean convert(String pdf, String tif, String destPdf)");
		org.icepdf.core.pobjects.Document pdffile = new org.icepdf.core.pobjects.Document();

		try {

			pdffile.setFile(pdf);

		} catch (PDFException ex) {
			// System.out.println("Error parsing PDF document " + ex);
		} catch (PDFSecurityException ex) {
			// System.out.println("Error encryption not supported " + ex);
		} catch (FileNotFoundException ex) {
			// System.out.println("Error file not found " + ex);
		} catch (IOException ex) {
			// System.out.println("Error handling PDF document " + ex);
		}

		int numPgs = pdffile.getNumberOfPages();
		msg.setText(".....::::: Converting pages please wait :::::.....");
		addComponent(contentPane, msg, 10, 110, 200, 18);

		try {
			// step 1: create new reader
			PdfReader r = new PdfReader(pdf);
			// System.out.println("File Lenght:" + r.getFileLength());
			RandomAccessFileOrArray raf = new RandomAccessFileOrArray(pdf);
			// System.out.println("Raf:" + raf);
			Document document = new Document(r.getPageSizeWithRotation(1));
			// // step 2: create a writer that listens to the document
			PdfCopy writer = new PdfCopy(document,
					new FileOutputStream(destPdf));
			//	        
			// // step 3: we open the document
			document.open();
			// // step 4: we add content
			PdfImportedPage page = null;

			float scale = 2.084f;
			float rotation = 0f;

			BufferedImage image[] = new BufferedImage[numPgs];

			// -------- CHANGE
			jprogress.setMaximum(numPgs);
			// -------- CHANGE

			for (int i = 0; i < numPgs; i++) {

				// -------- CHANGE
				jprogress.setValue(i + 1);
				// -------- CHANGE

				byte bContent[] = r.getPageContent(i + 1, raf);
				// System.out.println(bContent.toString());

				ByteArrayOutputStream bs = new ByteArrayOutputStream();
				// write the content to an output stream
				bs.write(bContent);

				// System.out.println("page content length of page " + i+1 +
				// " = "
				// + bs.size());

				/*
				 * Generate the image: Notes: 1275x1650 = 8.5 x 11 @ 150dpi ???
				 */
				image[i] = (BufferedImage) pdffile.getPageImage(i,
						GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX,
						rotation, scale);
				Iterator writers = ImageIO.getImageWritersByFormatName("TIFF");
				if (writers == null || !writers.hasNext()) {
					throw new RuntimeException("No writers for available.");

				}
				ImageWriter myWriter = (ImageWriter) writers.next();
				myWriter.setOutput(new FileImageOutputStream(new File(tif)));
				myWriter.prepareWriteSequence(null);
				ImageTypeSpecifier imageType = ImageTypeSpecifier
						.createFromRenderedImage(image[i]);
				IIOMetadata imageMetadata = myWriter.getDefaultImageMetadata(
						imageType, null);
				imageMetadata = createImageMetadata(imageMetadata);
				myWriter.writeToSequence(new IIOImage(image[i], null,
						imageMetadata), null);

				myWriter.dispose();
				image[i] = null;
				myWriter = null;

				FileInputStream in = new FileInputStream(tif);
				FileChannel channel = in.getChannel();
				ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
				channel.read(buffer);
				Image imageBlank;

				imageBlank = load(buffer.array());

				BufferedImage bufferedImage = imageToBufferedImage(imageBlank);
				boolean isBlank;
				isBlank = isBlank(bufferedImage);
				// System.out.println("isblank "+ isBlank);
				boolean hasContent = false;
				File file = new File(TEMP_DIR + TEMP_EXTR_TEXT);

				try {
					FileWriter fileWriter = new FileWriter(file);

					PageText pageText = pdffile.getPageText(i);
					if (pageText != null && pageText.getPageLines() != null) {
						fileWriter.write(pageText.toString());
					}

					// close the writer
					fileWriter.close();
					System.out.println(file.length());
					if (file.length() > 20) {
						hasContent = true;

					}
					file.delete();

					System.out.println(TEMP_TIFF + " deleted");
				} catch (IOException e) {

					e.printStackTrace();
				}

				if (isBlank == false && hasContent == true) {

					page = writer.getImportedPage(r, i + 1);
					writer.addPage(page);

				}
				bs.close();
				in.close();
				File ft = new File(TEMP_DIR + TEMP_TIFF);
				boolean check = ft.delete();
				if (check == true) {
					System.out.println("Deleted");
				} else {
					System.out.println("Stuck");
				}
				System.gc();

			}

			document.close();
			writer.close();
			raf.close();
			r.close();
			stopTime = System.currentTimeMillis();
			logger
					.info("Exit boolean convert(String pdf, String tif, String destPdf) with time:	 "
							+ ((stopTime - startTime) / 1000));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Load.
	 * 
	 * @param data
	 *            the data
	 * @return the image
	 * @throws Exception
	 *             the exception
	 */
	public static Image load(byte[] data) throws Exception {
		long startTime = System.currentTimeMillis();
		logger
				.info("In the boolean convert(String pdf, String tif, String destPdf)");

		Image image = null;
		SeekableStream stream = new ByteArraySeekableStream(data);
		String[] names = ImageCodec.getDecoderNames(stream);
		ImageDecoder dec = ImageCodec
				.createImageDecoder(names[0], stream, null);
		RenderedImage im = dec.decodeAsRenderedImage();
		image = PlanarImage.wrapRenderedImage(im).getAsBufferedImage();
		// scale-down the image , maximum width : 500 px
		// to preserve memory
		Image imageScaled = image
				.getScaledInstance(500, -1, Image.SCALE_SMOOTH);

		long stopTime = System.currentTimeMillis();
		logger.info("Exit Image load(byte[] data) with time:	 "
				+ ((stopTime - startTime) / 1000));
		return imageScaled;
	}

	/**
	 * Image to buffered image.
	 * 
	 * @param im
	 *            the im
	 * @return the buffered image
	 */
	public static BufferedImage imageToBufferedImage(Image im) {
		long startTime = System.currentTimeMillis();
		logger.info("In the BufferedImage imageToBufferedImage(Image im)");

		BufferedImage bi = new BufferedImage(im.getWidth(null), im
				.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics bg = bi.getGraphics();
		bg.drawImage(im, 0, 0, null);
		bg.dispose();
		bg = null;

		long stopTime = System.currentTimeMillis();
		logger
				.info("Exit BufferedImage imageToBufferedImage(Image im) with time:	 "
						+ ((stopTime - startTime) / 1000));
		return bi;
	}

	/**
	 * Checks if is blank.
	 * 
	 * @param bi
	 *            the bi
	 * @return true, if is blank
	 * @throws Exception
	 *             the exception
	 */
	public static boolean isBlank(BufferedImage bi) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("In the boolean isBlank(BufferedImage bi)");

		long count = 0;
		long total = 0;
		double totalVariance = 0;
		double stdDev = 0;
		int height = bi.getHeight();
		int width = bi.getWidth();

		int[] pixels = new int[width * height];
		PixelGrabber pg = new PixelGrabber(bi, 0, 0, width, height, pixels, 0,
				width);
		pg.grabPixels();
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				count++;
				int pixel = pixels[j * width + i];
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				int pixelValue = new Color(red, green, blue, 0).getRGB();
				total += pixelValue;
				double avg = total / count;
				totalVariance += Math.pow(pixelValue - avg, 2);
				stdDev = Math.sqrt(totalVariance / count);
			}
		}

		long stopTime = System.currentTimeMillis();
		logger.info("Exit isBlank(BufferedImage bi) with time:	 "
				+ ((stopTime - startTime) / 1000));

		return (stdDev < BLANK_THRESHOLD);
	}

	/**
	 * Save tiff.
	 * 
	 * @param imageMetadata
	 *            the image metadata
	 * @return the iIO metadata
	 * @throws IIOInvalidTreeException
	 *             the iIO invalid tree exception
	 */
	// @SuppressWarnings("unchecked")
	// private static void save(BufferedImage[] b, String tif) throws
	// IOException {
	//
	// //Get a TIFF writer and set its output.
	// Iterator writers = ImageIO.getImageWritersByFormatName("TIFF");
	//
	// if (writers == null || !writers.hasNext()) {
	// throw new RuntimeException("No writers for available.");
	// }
	//
	// ImageWriter writer = (ImageWriter) writers.next();
	// writer.setOutput(new FileImageOutputStream(new File(tif)));
	// writer.prepareWriteSequence(null);
	//
	// for (int i = 0; i < b.length; i++) {
	// ImageTypeSpecifier imageType =
	// ImageTypeSpecifier.createFromRenderedImage(b[i]);
	// IIOMetadata imageMetadata = writer.getDefaultImageMetadata(imageType,
	// null);
	// imageMetadata = createImageMetadata(imageMetadata);
	// writer.writeToSequence(new IIOImage(b[i], null, imageMetadata), null);
	// }
	//
	// writer.dispose();
	// writer = null;
	//		
	//
	// }

	/**
	 * Return the metadata for the new TIF image
	 */
	private static IIOMetadata createImageMetadata(IIOMetadata imageMetadata)
			throws IIOInvalidTreeException {
		long startTime = System.currentTimeMillis();
		logger
				.info("In the IIOMetadata createImageMetadata(IIOMetadata imageMetadata)");
		// Get the IFD (Image File Directory) which is the root of all the tags
		// for this image. From here we can get all the tags in the image.
		TIFFDirectory ifd = TIFFDirectory.createFromMetadata(imageMetadata);

		// Create the necessary TIFF tags that we want to add to the image
		// metadata
		BaselineTIFFTagSet base = BaselineTIFFTagSet.getInstance();

		// Resolution tags...
		TIFFTag tagResUnit = base
				.getTag(BaselineTIFFTagSet.TAG_RESOLUTION_UNIT);
		TIFFTag tagXRes = base.getTag(BaselineTIFFTagSet.TAG_X_RESOLUTION);
		TIFFTag tagYRes = base.getTag(BaselineTIFFTagSet.TAG_Y_RESOLUTION);

		// BitsPerSample tag
		TIFFTag tagBitSample = base
				.getTag(BaselineTIFFTagSet.TAG_BITS_PER_SAMPLE);

		// Row and Strip tags...
		TIFFTag tagRowStrips = base
				.getTag(BaselineTIFFTagSet.TAG_ROWS_PER_STRIP);

		// Compression tag
		TIFFTag tagCompression = base
				.getTag(BaselineTIFFTagSet.TAG_COMPRESSION);

		// Set the tag values
		TIFFField fieldResUnit = new TIFFField(tagResUnit, TIFFTag.TIFF_SHORT,
				1, INCH_RESOLUTION_UNIT);
		TIFFField fieldXRes = new TIFFField(tagXRes, TIFFTag.TIFF_RATIONAL, 1,
				X_DPI_RESOLUTION);
		TIFFField fieldYRes = new TIFFField(tagYRes, TIFFTag.TIFF_RATIONAL, 1,
				Y_DPI_RESOLUTION);
		TIFFField fieldBitSample = new TIFFField(tagBitSample,
				TIFFTag.TIFF_SHORT, 1, BITS_PER_SAMPLE);
		TIFFField fieldRowStrips = new TIFFField(tagRowStrips,
				TIFFTag.TIFF_LONG, 1, new long[] { HEIGHT });
		TIFFField fieldCompression = new TIFFField(tagCompression,
				TIFFTag.TIFF_SHORT, 1, COMPRESSION);

		// Cleanup the fields
		// ifd.removeTIFFFields();

		// Add the new tag/value sets to the image metadata
		ifd.addTIFFField(fieldResUnit);
		ifd.addTIFFField(fieldXRes);
		ifd.addTIFFField(fieldYRes);
		ifd.addTIFFField(fieldBitSample);
		ifd.addTIFFField(fieldRowStrips);
		ifd.addTIFFField(fieldCompression);

		long stopTime = System.currentTimeMillis();
		logger
				.info("Exit IIOMetadata createImageMetadata(IIOMetadata imageMetadata) with time:	 "
						+ ((stopTime - startTime) / 1000));

		return ifd.getAsMetadata();

	}

	// ============================= Testing ================================//
	// = =//
	// = The following main method is just for testing this class you built.=//
	// = After testing,you may simply delete it. =//
	// ======================================================================//
	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		logger.info("In the main");
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		try {

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (Exception ex) {
			// System.out.println("Failed loading L&F: ");
			// System.out.println(ex);
		}
		// new RemoveBlankPdf();

		// -------- CHANGE
		final RemoveBlankPdf t = new RemoveBlankPdf();

		new Thread(t).start();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				t.initializeComponent();
				t.setVisible(true);
				t.setResizable(false); /* Modified by SiD */
				// System.out.println("Exit flag Value : "+exitflag);
				/*
				 * if(exitflag==true) {
				 * 
				 * Thread.currentThread().stop(); }
				 */

			}
		});

		// -------- CHANGE

		long stopTime = System.currentTimeMillis();
		logger.info("Exit void main(String[] args) with time:	 "
				+ ((stopTime - startTime) / 1000));
	}

	// = End of Testing =

	// -------- CHANGE
	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		long startTime = System.currentTimeMillis();
		logger.info("In the run()");

		while (true) {
			try {
				synchronized (this) {
					wait();
				}
			} catch (InterruptedException e) {
			}
			String pdfSourceFile = SourceField.getText();
			String pdfDestinationFile = DestField.getText();

			System.out.println("Thread running : "
					+ Thread.currentThread().getName());
			System.out.println("Exit flag Value" + exitflag);

			exitflag = this.convertPDF(pdfSourceFile, pdfDestinationFile);
			System.out.println("Exit flag value " + exitflag);

			msg.setText(".....::::: PDF Conversion Complete :::::.....");
			addComponent(contentPane, msg, 25, 110, 400, 18);

			jprogress.setValue(0);
			jprogress.setLocation(new Point(500, 300));
			jprogress.setStringPainted(true);
			jprogress.setVisible(true);
			addComponent(contentPane, jprogress, 23, 133, 340, 26);
			JOptionPane.showMessageDialog(this,
					"Converted The Document SuccessFully", "Success",
					JOptionPane.INFORMATION_MESSAGE);

			// -------- NEW CHANGE
			// dlg.setVisible(false);
			// -------- NEW CHANGE
			try {

				Runtime.getRuntime().exec(
						"rundll32 SHELL32.DLL,ShellExec_RunDLL \""
								+ pdfDestinationFile + "\"");
				long stopTime = System.currentTimeMillis();
				logger.info("Exit try run()  with time:	 "
						+ ((stopTime - startTime) / 1000));
			} catch (Exception e) {
				System.out.println("exception :" + e);
			}

			SourceField.setText(" ");
			DestField.setText(" ");
			// cButton.enable();
			// TODO Auto-generated method stub
			System.out.println("Thread exiting : "
					+ Thread.currentThread().getName());
			// Thread.currentThread().

			if (exitflag) {
				System.out.println("Killing the thread here");
				long stopTime = System.currentTimeMillis();
				logger.info("Exit if (exitflag) run()  with time:	 "
						+ ((stopTime - startTime) / 1000));
				stopthread();
				// t=null;
			}
		}

	}

	// -------- CHANGE*/

	/**
	 * Stopthread.
	 */
	public void stopthread() {
		long startTime = System.currentTimeMillis();
		logger.info("In the stopthread()");
		System.out.println("In Stopthread method.Stopping threads");
		System.out.println("Currenlty Running thread is :"
				+ Thread.currentThread().getName());
		String current = Thread.currentThread().getName();

		try {
			String current1 = Thread.currentThread().getName();
			// currentthread=null;

			System.out.println("Thread stopped succesfully.;");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// System.gc();
			deleteDirectory(new File(TEMP_DIR));
			Runtime.getRuntime().gc();
		}
		long stopTime = System.currentTimeMillis();
		logger.info("Exit stopthread()  with time:	 "
				+ ((stopTime - startTime) / 1000));
	}

	/** 
	 * @SiD
	 * This function creates the temp directory created for TIFF PURPOSES.
	 * Creates the temp dir.
	 * 
	 * @param dirname
	 *            the dirname
	 */
	public static void createTempDir(String dirname) {
		try {
			String strDirectoy = dirname;
			// Create one directory
			boolean success = (new File(strDirectoy)).mkdir();
			if (success) {
				logger.info("Directory: " + strDirectoy + " created");
			}
		} catch (Exception e) {// Catch exception if any
			logger.info("createTempDir Error: " + e.getMessage());
		}
	}

	/**
	 * @SiD This function has been written to 
	 * delete the temp directory created for TIFF PURPOSES.
	 * Delete directory.
	 * 
	 * @param path
	 *            the path
	 */
	public static void deleteDirectory(File path) {
		try {
			if (path.exists()) {
				File[] files = path.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
			path.delete();
			logger.info("deleteDirectory Success: " + path.getAbsolutePath());
		} catch (Exception e) {// Catch exception if any
			logger.info("deleteDirectory Error: " + e.getMessage());
		}

	}


	/**
	 * @SiD This function has been written to 
	 * CHECK WHETHER THE DESTINATION FILE ALREADY EXISTS IN RESIDING DIRECTORY.
	 * Check for existing file in curr dir.
	 *
	 * @param destFile the dest file
	 * @return true, if successful
	 */
	public static boolean checkForExistingFileInCurrDir(String destFile) {
		File destnFile = new File(destFile);
		logger.info("destnFile is " + destnFile.getName());
		File file = new File(destnFile.getParent());
		logger.info("destnFile.getParent() is " + destnFile.getParent());
        boolean exists = false;
		// does the file exist
		if (destnFile.exists()) {
			logger.info(destnFile.getName() + " )--file exists");
		}
		logger.info("file is " + file.length() + " bytes long");
		String[] files = file.list();
		for (String fileName : files) {
			if(destnFile.getName().equals(fileName))
				exists = true;
		}
		return exists;
	}
	
	
}
