/*  This file is part of PDFCleaner.
 *  The class displays Contact us Screen.
 *
 *  Copyright (C) 2012  Sourabh Gandhe
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

import java.awt.*;

import javax.swing.*;
/*
 * Created by JFormDesigner on Mon Aug 09 14:02:43 EDT 2010
 */



/**
 * @author sourabh gandhe
 */
public class s extends JFrame {
	public s() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - sourabh gandhe
		label1 = new JLabel();
		label2 = new JLabel();
		label3 = new JLabel();
		this.setIconImage
		  (Toolkit.getDefaultToolkit()
		     .getImage(getClass().
		         getResource("index1.gif")));
		//======== this ========
		setTitle("About US");
		this.setResizable(false);
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		//---- label1 ----
		label1.setText("Developed By");
		label1.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 14));
		label1.setForeground(Color.magenta);
		contentPane.add(label1);
		label1.setBounds(50, 30, 200, label1.getPreferredSize().height);
        
		//---- label2 ----
		label2.setText("Sourabh Gandhe");
		contentPane.add(label2);
		label2.setBounds(30, 70, 200, label2.getPreferredSize().height);

		//---- label3 ----
		label3.setText("Binghamton University");
		contentPane.add(label3);
		label3.setBounds(50, 95, 200, label3.getPreferredSize().height);

		{ // compute preferred size
			Dimension preferredSize = new Dimension();
			for(int i = 0; i < contentPane.getComponentCount(); i++) {
				Rectangle bounds = contentPane.getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
			}
			Insets insets = contentPane.getInsets();
			preferredSize.width += insets.right;
			preferredSize.height += insets.bottom;
			contentPane.setMinimumSize(preferredSize);
			contentPane.setPreferredSize(preferredSize);
		}
		setSize(230, 210);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - sourabh gandhe
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
