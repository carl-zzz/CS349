// HelloMVC: a simple MVC example
// the model is just a counter
// inspired by code by Joseph Mack, http://www.austintek.com/mvc/

/**
 * Two views coordinated with the observer pattern.  Separate controller.
 * The mechanics of a separate controller are starting to break down.
 */

import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.Color;
import java.awt.FlowLayout;

public class A2Basic {

	public static void main(String[] args){
		JFrame frame = new JFrame("A2Basic");

		// create Model and initialize it
		DrawingModel model = new DrawingModel();
		model.sidlst.add(0);
		frame.setPreferredSize(new Dimension(800,600));
                JPanel p = new JPanel();

		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		// create View, tell it about model and controller
		//cview.setPreferredSize(new Dimension(760,480));

		ToolbarView tview = new ToolbarView(model);
		model.addView(tview);
		CanvasView cview = new CanvasView(model);
		model.addView(cview);
		StatusbarView sview = new StatusbarView(model);
		model.addView(sview);

		// add panels, since need flowlayout left align.
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		p.add(p1);
		p1.setMaximumSize(new Dimension(Integer.MAX_VALUE,50));
		p1.add(tview);

		JScrollPane cviewscroll = new JScrollPane(cview);
		cviewscroll.setViewportView(cview);
		cviewscroll.setBorder(BorderFactory.createLineBorder(Color.black));
		p.add(cviewscroll);
		//cview.setBorder(BorderFactory.createLineBorder(Color.black));
		//p.add(cview);

		p.add(p3);
		p3.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
                p3.add(sview);

		// setup the window
                frame.getContentPane().add(p);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
