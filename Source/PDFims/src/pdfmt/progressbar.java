/*  This file is part of PDFCleaner.
 *  The class Disaplays the progressbar.
 *
 *  Copyright (C) 2010  Ashwini Kadam
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
/*                      progressbar	                            */
/*                                                              */
/****************************************************************/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Summary description for progressbar
 *
 */
public class progressbar extends JInternalFrame
{
	// Variables declaration
	private JLabel jLabel1;
	private JProgressBar jProgressBar1;
	private JButton jButton1;
	private JPanel contentPane;
	// End of variables declaration


	public progressbar()
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
		jProgressBar1 = new JProgressBar();
		jButton1 = new JButton();
		contentPane = (JPanel)this.getContentPane();

		//
		// jLabel1
		//
		//
		// jProgressBar1
		//
		jProgressBar1.setBorderPainted(false);
		//
		// jButton1
		//
		jButton1.setText("Ok");
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				jButton1_actionPerformed(e);
			}

		});
		//
		// contentPane
		//
		contentPane.setLayout(null);
		addComponent(contentPane, jLabel1, 90,48,111,18);
		addComponent(contentPane, jProgressBar1, 33,22,252,22);
		addComponent(contentPane, jButton1, 100,79,83,28);
		//
		// progressbar
		//
		this.setTitle("Removing Blank Pages..........");
		this.setLocation(new Point(0, 0));
		this.setSize(new Dimension(324, 152));
		this.setResizable(true);
	}

	/** Add Component Without a Layout Manager (Absolute Positioning) */
	private void addComponent(Container container,Component c,int x,int y,int width,int height)
	{
		c.setBounds(x,y,width,height);
		container.add(c);
	}

	//
	// TODO: Add any appropriate code in the following Event Handling Methods
	//
	private void jButton1_actionPerformed(ActionEvent e)
	{
		System.out.println("\njButton1_actionPerformed(ActionEvent e) called.");
		// TODO: Add any handling code here

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
		final JFrame w 		= new JFrame("Desktop Window");
		final JDesktopPane desktop = new JDesktopPane();
		JMenuBar menuBar 	= new JMenuBar();
		JMenu menu 			= new JMenu("Document");
		JMenuItem menuItem 	= new JMenuItem("New");
		w.setContentPane(desktop);
		w.setJMenuBar(menuBar);
		menuBar.add(menu);
		menu.add(menuItem);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
							public void actionPerformed(java.awt.event.ActionEvent e)
							{
								//-- Create a progressbar --
								progressbar JIF = new progressbar();
								desktop.add(JIF);
								try
								{
									JIF.setSelected(true);
								}
								catch (java.beans.PropertyVetoException ee) {}
								//---------------------
							}});
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		w.setSize(700,500);
		w.setLocation(50,50);
		w.setVisible(true);
	}
//= End of Testing =


}
