/*  This file is part of PDFCleaner.
 *  The class displays the help screen.
 *
 *  Copyright (C) 2010  Sourabh Gandhe
 *                      
 *	
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
 /*                      HowTo	                            */ 
 /*                                                              */ 
 /****************************************************************/ 
 import java.awt.*; 

import javax.swing.*; 
 /** 
  * Summary description for HowTo 
  * 
  */ 
 public class HowTo extends JFrame 
 { 
 	// Variables declaration 
 	private JLabel jLabel1; 
 	private JLabel jLabel2; 
 	private JLabel jLabel3; 
 	private JLabel jLabel4; 
 	private JLabel jLabel5; 
 	private JLabel jLabel6; 
 	private JPanel contentPane; 
 	// End of variables declaration 
  
  
 	public HowTo() 
 	{ 
 		super(); 
 		initializeComponent(); 
 		// 
 		// TODO: Add any constructor code after initializeComponent call 
 		// 
  
 		this.setVisible(true); 
 	} 
  
 	/** 
 	 * This method is called from within the constructor to initialize the form. 
 	 * WARNING: Do NOT modify this code. The content of this method is always regenerated 
 	 * by the Windows Form Designer. Otherwise, retrieving design might not work properly. 
 	 * Tip: If you must revise this method, please backup this GUI file for JFrameBuilder 
 	 * to retrieve your design properly in future, before revising this method. 
 	 */ 
 	private void initializeComponent() 
 	{ 
 		jLabel1 = new JLabel(); 
 		jLabel2 = new JLabel(); 
 		jLabel3 = new JLabel(); 
 		jLabel4 = new JLabel(); 
 		jLabel5 = new JLabel(); 
 		jLabel6 = new JLabel(); 
 		contentPane = (JPanel)this.getContentPane(); 
 		this.setIconImage
		  (Toolkit.getDefaultToolkit()
		     .getImage(getClass().
		         getResource("index1.gif"))); 
 		// 
 		// jLabel1 
 		// 
 		this.setResizable(false);
 		jLabel1.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 14));
		jLabel1.setForeground(Color.magenta);

 		jLabel1.setText("           Converting a PDF "); 
 		// 
 		// jLabel2 
 		// 
 		jLabel2.setText("1.Choose Select Data File to select the source PDF."); 
 		// 
 		// jLabel3 
 		// 
 		jLabel3.setText("2.Choose Select Data File to choose location of converted PDF."); 
 		// 
 		// jLabel4 
 		// 
 		jLabel4.setText("3.Click on Convert Button to obtain the modified PDF."); 
 		// 
 		// jLabel5 
 		// 
 		jLabel5.setText("4.To obtain the preview of the converted PDF click on Preview button."); 
 		// 
 		// jLabel6 
 		// 
 		jLabel6.setText("5.To Exit from application choose the Exit button."); 
 		// 
 		// contentPane 
 		// 
 		contentPane.setLayout(null); 
 		addComponent(contentPane, jLabel1, 144,3,346,70); 
 		addComponent(contentPane, jLabel2, 22,76,342,29); 
 		addComponent(contentPane, jLabel3, 21,107,310,18); 
 		addComponent(contentPane, jLabel4, 22,135,311,18); 
 		addComponent(contentPane, jLabel5, 22,163,465,18); 
 		addComponent(contentPane, jLabel6, 23,194,365,18); 
 		// 
 		// HowTo 
 		// 
 		this.setTitle("Help"); 
 	//	this.setLocation(new Point(0, 0)); 
 		//this.setSize(new Dimension(598, 303));
 		this.setSize(598, 303);
		this.setLocationRelativeTo(getOwner());
	
 	} 
  
 	/** Add Component Without a Layout Manager (Absolute Positioning) */ 
 	private void addComponent(Container container,Component c,int x,int y,int width,int height) 
 	{ 
 		c.setBounds(x,y,width,height); 
 		container.add(c); 
 	} 
  
 	// 
 	// TODO: Add any method code to meet your needs in the following area 
 	// 
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
   
  
 //============================= Testing ================================// 
 //=                                                                    =// 
 //= The following main method is just for testing this class you built.=// 
 //= After testing,you may simply delete it.                            =// 
 //======================================================================// 
 	public static void main(String[] args) 
 	{ 
 		JFrame.setDefaultLookAndFeelDecorated(true); 
 		JDialog.setDefaultLookAndFeelDecorated(true); 
 		try 
 		{ 
 			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); 
 		} 
 		catch (Exception ex) 
 		{ 
 			System.out.println("Failed loading L&F: "); 
 			System.out.println(ex); 
 		} 
 		new HowTo(); 
 	} 
 //= End of Testing = 
  
  
 } 
  
 