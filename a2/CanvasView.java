// HelloMVC: a simple MVC example
// the model is just a counter
// inspired by code by Joseph Mack, http://www.austintek.com/mvc/

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import javax.vecmath.*;
import java.awt.geom.*;
import javax.swing.event.*;
import java.awt.*;

class CanvasView extends JPanel implements IView {

	// the model that this view is showing
	private DrawingModel model;
	private JLabel label = new JLabel();

	Shape shape;

	CanvasView(DrawingModel model) {


		// set the model
		this.model = model;
		this.registerControllers();
		this.model.addView(new IView() {
			public void updateView() {
				repaint();
			}
		});
	}

	private void registerControllers() {
		MouseInputListener mc = new MController();
		this.addMouseListener(mc);
		this.addMouseMotionListener(mc);

	}

	public void paintComponent(Graphics g) {
        	super.paintComponent(g);
      		Graphics2D g2 = (Graphics2D) g; // cast to get 2D drawing methods
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  // antialiasing look nicer
	                            RenderingHints.VALUE_ANTIALIAS_ON);
		for (int i=0;i<model.getShapeSize();i++) {
			if (model.shapelst.get(i).focus == 1) {
				model.shapelst.get(i).setColour(Color.YELLOW);
				model.shapelst.get(i).setStrokeThickness(6);
				model.shapelst.get(i).draw(g2);
				model.shapelst.get(i).setColour(Color.BLACK);
				model.shapelst.get(i).setStrokeThickness(1);
			}
			model.shapelst.get(i).draw(g2);
		}
		if (shape != null) {
			shape.draw(g2);
		}
	}


	// IView interface
	public void updateView() {
		//System.out.println("updateCView");
		// just displays an 'X' for each counter value
		repaint();
	}

	//private class MController implements MouseListener, MouseMotionListener {
	private class MController extends MouseInputAdapter {
		private int flag1 = 0;
		private int flag2 = 0;
		private int drag = 0;
		private double xx = 0;
		private double yy = 0;
	        public void mouseClicked(MouseEvent e) { 
			for (int i=model.getShapeSize()-1;i>=0;i--) {
				model.shapelst.get(i).unhighlight();
			}
			repaint();
			for (int i=model.getShapeSize()-1;i>=0;i--) {
				if (model.shapelst.get(i).hittest(e.getX(),e.getY()) == true) {
				model.shapelst.get(i).highlight();
				break;
				}
			}
			model.updateAllViews();
			repaint();
		}

	        public void mousePressed(MouseEvent e) {
		/*	for (int i=0;i<model.getShapeSize();i++) {
				if ((model.shapelst.get(i).focus == 1) && (model.shapelst.get(i).hittest(e.getX(),e.getY()))) {
					drag = 1;
					xx = e.getX();
					yy = e.getY();
					return;
				}
			}*/
			flag1 = 1;
	                shape = new Shape();
			shape.unhighlight();
	                // change shape type
	                // shape.setIsClosed(true);
	                // shape.setIsFilled(true);
	                shape.setColour(Color.BLACK);
	
	                // try setting scale to something other than 1 
	                shape.scale = 1.0f;
			shape.rotate = 0;
			shape.xxx = 0;
			shape.yyy = 0;
	        }

	        public void mouseDragged(MouseEvent e) {
		/*	
			if (drag == 1) {
				for (int i=0;i<model.getShapeSize();i++) {
					Shape s = model.shapelst.get(i);
					if (s.focus == 1) {
						s.xxx = s.xxx+e.getX()-xx;
						s.yyy = s.yyy+e.getY()-yy;
						xx = e.getX();
						yy = e.getY();
						break;
					}
				}
			model.updateAllViews();
				repaint();
				return;
			}
		*/
			flag2 = 1;
			shape.addPoint(e.getX(), e.getY());
	                repaint();
	        }

	        public void mouseReleased(MouseEvent e) { 
			drag = 0;
			if (flag1 * flag2 == 0)
				return;
			flag1 = 0;
			flag2 = 0;
			int x = model.sidlst.remove(0);
			if (model.sidlst.size() == 0) {
				model.sidlst.add(x+1);
			}
			shape.setSid(x);
			model.currentsid = x;	
			model.addShape(shape);
			shape = null;
			model.updateAllViews();
			repaint();
		}

	        public void mouseEntered(MouseEvent e) { }
	        public void mouseExited(MouseEvent e) { }
	        public void mouseMoved(MouseEvent e) { }

	}
}
