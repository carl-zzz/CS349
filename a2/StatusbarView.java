// HelloMVC: a simple MVC example
// the model is just a counter
// inspired by code by Joseph Mack, http://www.austintek.com/mvc/

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;

class StatusbarView extends JPanel implements IView {

	// the model that this view is showing
	private DrawingModel model;
	private JLabel la1;
	private JLabel la2;

	StatusbarView(DrawingModel model) {
		this.model = model;
		la1 = new JLabel("Stroke "+ model.getShapeSize());
		la2 = new JLabel("");

		this.add(la1);
		this.add(la2);

	}

	// IView interface
	public void updateView() {
		//System.out.println("StatusbarView: updateView");
		// just displays an 'X' for each counter value
		this.la1.setText("Stroke "+ model.getShapeSize());
		for (int i=0;i<model.getShapeSize();i++) {
			Shape s = model.shapelst.get(i);
			if (s.focus == 1) {
				this.la2.setText(", Selection ("+s.npoints()+" points, scale: "+s.scale+", rotation "+s.rotate+")");
			return;
			}
		}
		this.la2.setText("");
	}
}
