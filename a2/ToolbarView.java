// HelloMVC: a simple MVC example
// the model is just a counter
// inspired by code by Joseph Mack, http://www.austintek.com/mvc/

import javax.swing.*;
import javax.swing.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

class ToolbarView extends JPanel implements IView {

	// the view's main user interface
	private JButton button;
	private JSlider scale;
	private JSlider rotate;
	private JLabel la1;
	private JLabel la2;
	private JLabel la3;
	private JLabel la4;

	// the model that this view is showing
	private DrawingModel model;

	ToolbarView(DrawingModel model) {
		
		button = new JButton("Delete");
		button.setFocusPainted(false);
		this.add(button);

		la1 = new JLabel("       Scale");
		this.add(la1);

		scale = new JSlider(5,20,10);
		this.add(scale);

		la2 = new JLabel(""+scale.getValue()/10f);
		this.add(la2);

		la3 = new JLabel("       Rotate");
		this.add(la3);

		rotate = new JSlider(-180,180,0);
		this.add(rotate);

		la4 = new JLabel(""+rotate.getValue());
		this.add(la4);
		
		// set the model
		this.model = model;
		// setup the event to go to the controller
		this.registerControllers();
		
	}

	private void greyOut() {
		la1.setEnabled(false);
		la2.setEnabled(false);
		la3.setEnabled(false);
		la4.setEnabled(false);
		button.setEnabled(false);
		scale.setEnabled(false);
		rotate.setEnabled(false);
	}

	private void notGreyOut() {
		la1.setEnabled(true);
		la2.setEnabled(true);
		la3.setEnabled(true);
		la4.setEnabled(true);
		button.setEnabled(true);
		scale.setEnabled(true);
		rotate.setEnabled(true);
	}

        private void registerControllers() {
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.removeShape();
			}	
		});
		scale.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				model.changeScale(scale.getValue()/10f);
			}
		});
		rotate.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				model.changeRotate(rotate.getValue());
			}
		});
	}

	// IView interface
	public void updateView() {
		//System.out.println("ToolView: updateView");
		int flag = 0;
		for (int i=0;i<model.getShapeSize();i++) {
			Shape s = model.shapelst.get(i);
			if (s.focus == 1) {
				notGreyOut();
				flag = 1;
				float f = s.scale * 10;
				scale.setValue((int)f);
				la2.setText(""+s.scale);
				int d = s.rotate;
				rotate.setValue(d);
				la4.setText(""+d);	
			}
		}
		if (flag == 0) {
			scale.setValue(10); 
			la2.setText(""+scale.getValue()/10f);
			rotate.setValue(0);
			la4.setText(""+rotate.getValue());
			greyOut();
		}
	}
}
