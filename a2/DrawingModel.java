import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.*;
import javax.vecmath.*;
import java.util.Iterator;
//import javax.vecmath.*;


// HelloMVC: a simple MVC example
// the model is just a counter 
// inspired by code by Joseph Mack, http://www.austintek.com/mvc/

// View interface
interface IView {
	public void updateView();
}

class Shape {

    private int sid;
    public int focus;

    // shape points
    ArrayList<Point2d> points;

    public void highlight() {
	focus = 1;
    }

    public void unhighlight() {
	focus = 0;
    }

    public void setSid(int x) {
	sid = x;
    }

    public int getSid() {
	return sid;
    }

    public void clearPoints() {
        points = new ArrayList<Point2d>();
        pointsChanged = true;
    }
  
    // add a point to end of shape
    public void addPoint(Point2d p) {
        if (points == null) clearPoints();
        points.add(p);
        pointsChanged = true;
    }    

    // add a point to end of shape
    public void addPoint(double x, double y) {
        addPoint(new Point2d(x, y));  
    }

    public int npoints() {
        return points.size();
    }

    // shape is polyline or polygon
    Boolean isClosed = false; 

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }    

    // if polygon is filled or not
    Boolean isFilled = false; 

    public Boolean getIsFilled() {
        return isFilled;
    }

    public void setIsFilled(Boolean isFilled) {
        this.isFilled = isFilled;
    }    

    // drawing attributes
    Color colour = Color.BLACK;
    float strokeThickness = 1.0f;

    public Color getColour() {
		return colour;
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}

    public float getStrokeThickness() {
		return strokeThickness;
	}

	public void setStrokeThickness(float strokeThickness) {
		this.strokeThickness = strokeThickness;
	}

    // shape's transform

    // quick hack, get and set would be better
    public float scale = 1.0f;
    public int rotate = 0;
    public double xxx = 0;
    public double yyy = 0;

    // some optimization to cache points for drawing
    Boolean pointsChanged = false; // dirty bit
    int[] xpoints, ypoints;
    int npoints = 0;

    void cachePointsArray() {
        xpoints = new int[points.size()];
        ypoints = new int[points.size()];
        for (int i=0; i < points.size(); i++) {
            xpoints[i] = (int)points.get(i).x;
            ypoints[i] = (int)points.get(i).y;
        }
        npoints = points.size();
        pointsChanged = false;
    }

    public int centerx() {
	int x1 = Integer.MAX_VALUE;
	int x2 = Integer.MIN_VALUE;
	for (int i=0;i< points.size();i++) {
		int tmp = (int)points.get(i).x;
		if (tmp < x1)
			x1 = tmp;
		if (tmp > x2)
			x2 = tmp;
	}
	return (x1+x2)/2;
    }	

    public int centery() {
	int y1 = Integer.MAX_VALUE;
	int y2 = Integer.MIN_VALUE;
	for (int i=0;i< points.size();i++) {
		int tmp = (int)points.get(i).y;
		if (tmp < y1)
			y1 = tmp;
		if (tmp > y2)
			y2 = tmp;
	}
	return (y1+y2)/2;
    }

    public void translate(double x,double y) {
	for (int i=0;i< points.size();i++) {
		points.set(i, new Point2d(points.get(i).x + x,points.get(i).y + y));
	}
    }

    public void scale(float x) {
	for (int i=0;i< points.size();i++) {
		points.set(i, new Point2d(points.get(i).x * x,points.get(i).y * x));
	}
    }
    
    // let the shape draw itself
    // (note this isn't good separation of shape View from shape Model)
    public void draw(Graphics2D g2) {

        // don't draw if points are empty (not shape)
        if (points == null) return;

        // see if we need to update the cache
        if (pointsChanged) cachePointsArray();

        // save the current g2 transform matrix 
        AffineTransform M = g2.getTransform();

        // multiply in this shape's transform
        // (uniform scale)

	g2.translate(centerx(),centery());
        //g2.scale(scalenew/scale, scalenew/scale);
        g2.scale(scale, scale);
	double r = Math.toRadians((double)rotate);
	g2.rotate(r);
	g2.translate(-centerx(),-centery());

        // call drawing functions
        g2.setColor(colour);            
        if (isFilled) {
            g2.fillPolygon(xpoints, ypoints, npoints);
        } else {
            // can adjust stroke size using scale
        	g2.setStroke(new BasicStroke(strokeThickness / scale)); 
        	if (isClosed)
                g2.drawPolygon(xpoints, ypoints, npoints);
            else
                g2.drawPolyline(xpoints, ypoints, npoints);
        }
        // reset the transform to what it was before we drew the shape
        g2.setTransform(M);  
    }
       
    // let shape handle its own hit testing
    // (x,y) is the point to test against
    // (x,y) needs to be in same coordinate frame as shape, you could add
    // a panel-to-shape transform as an extra parameter to this function
    // (note this isn't good separation of shape Controller from shape Model)    
    public boolean hittest(double x, double y)
    {   
    	if (points != null) {

            // TODO Implement
		double p0x,p0y,p1x,p1y;
		double d;
		double dt = 5;
		if (npoints() < 2) return false;
	        for (int i=0; i < points.size()-1; i++) {
		    double r = Math.toRadians((double)rotate);
		    double a = points.get(i).x-(double)centerx();
		    double b = points.get(i).y-(double)centery();
	            p0x = (a*Math.cos(r)-b*Math.sin(r))*scale+centerx();
	            p0y = (a*Math.sin(r)+b*Math.cos(r))*scale+centery();
		    a = points.get(i+1).x-(double)centerx();
		    b = points.get(i+1).y-(double)centery();
	            p1x = (a*Math.cos(r)-b*Math.sin(r))*scale+centerx();
	            p1y = (a*Math.sin(r)+b*Math.cos(r))*scale+centery();
		    //p0x = points.get(i).x;
		    //p0y = points.get(i).y;
		    //p1x = points.get(i+1).x;
		    //p1y = points.get(i+1).y; 
		    d = Line2D.ptSegDist(p0x,p0y,p1x,p1y,x,y);
		    if (d<=dt) return true;
	        }
		
    	}
    	
    	return false;
    }
}


public class DrawingModel {	

	// the data in the model, just a counter
	private int counter;	

	public int currentsid;

	public ArrayList<Shape> shapelst = new ArrayList<Shape>();

	public ArrayList<Integer> sidlst = new ArrayList<Integer>();
	
	public int getShapeSize() {
		return shapelst.size();
	}

	public Shape getShape(int x) {
		for (int i=0;i<shapelst.size();i++) {
			if (shapelst.get(i).getSid() == x) {
				return shapelst.get(i);
			}
		}
		return(null);
	}

	public void addShape(Shape x) {
		shapelst.add(x);
		updateAllViews();
	}

	public void removeShape() {
		Iterator<Shape> it = shapelst.iterator();
		while (it.hasNext()) {
			Shape s = it.next();
			if (s.focus == 1) {
				it.remove();
			}
		}
		updateAllViews();
		return;
	}

	public void changeScale(float f) {
		for (int i=0;i<shapelst.size();i++) {
			if (shapelst.get(i).focus == 1) {
				shapelst.get(i).scale = f;
			}
		}
		updateAllViews();
		return;
	}

	public void changeRotate(int d) {
		for (int i=0;i<shapelst.size();i++) {
			if (shapelst.get(i).focus == 1) {
				shapelst.get(i).rotate = d;
			}
		}
		updateAllViews();
		return;
	}

	// all views of this model
	private ArrayList<IView> views = new ArrayList<IView>();
	
	// set the view observer
	public void addView(IView view) {
		views.add(view);
		view.updateView();
	}
	
	public void updateAllViews() {
		for (IView view : this.views) {
			view.updateView();
		}
	}
}
