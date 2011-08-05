/*  This file is part of PDFCleaner.
 *  The class displays the progressbar.
 *
 *  Copyright (C) 2010  Ashwini Kadam.
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
public class Progress extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2982890026951228761L;
	
	JProgressBar current;
    JTextArea out;
    JButton find;
    Thread runner;
    int num = 0;

    public Progress() {
        super("Converting PDF.Please Wait");
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // JOptionPane pane1=new JOptionPane();
        
        JPanel pane = new JPanel();
        pane.setLayout(new FlowLayout());
        current = new JProgressBar(0, 2000);
        current.setValue(0);
        current.setStringPainted(true);
       
		current.setLocation(new Point(480, 300));
		current.setSize(new Dimension(400, 230));
        pane.add(current);
        pane.setLocation(new Point(500,300));
        pane.setSize(new Dimension(400,230));
        setContentPane(pane);
    }


    public void iterate() {
        while (num < 2000) {
            current.setValue(num);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { }
            num += 95;
        }
    }
    
    public static void main(String[] arguments) {
        Progress frame = new Progress();
        frame.setLocation(new Point(500,300));
        frame.setSize(new Dimension(800,200));
        frame.pack();
        frame.setVisible(true);
        frame.iterate();  
   }
}
