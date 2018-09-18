#include <cstdlib>
#include <iostream>
#include <vector>
#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include <list>
#include <sstream>
#include <unistd.h>
#include <sys/time.h>
#include <math.h>
#include <unistd.h>
#include "simon.h"

unsigned long now() {
        timeval tv;
        gettimeofday(&tv, NULL);
        return tv.tv_sec * 1000000 + tv.tv_usec;
}


using namespace std;


// - - - X globals - - -

struct XInfo {
        Display* display;
        Window window;
        int screen;
	int width;
	int height;
        GC gc;
};

class Displayable {
	public:
		virtual void paint() = 0;
};


list<Displayable *> dList;
XInfo xinfo;
// frames per second to run animation loop
int FPS = 60;

// helper function to set X foreground colour
enum Colour {BLACK, WHITE};
void setForeground(Colour c) {
        if (c == BLACK) {
                XSetForeground(xinfo.display, xinfo.gc, BlackPixel(xinfo.display, xinfo.screen));
        } else {
                XSetForeground(xinfo.display, xinfo.gc, WhitePixel(xinfo.display, xinfo.screen));
        }
}
void setBackground(Colour c) {
        if (c == BLACK) {
                XSetBackground(xinfo.display, xinfo.gc, BlackPixel(xinfo.display, xinfo.screen));
        } else {
                XSetBackground(xinfo.display, xinfo.gc, WhitePixel(xinfo.display, xinfo.screen));
        }
}


// isPaused functionality
bool isPaused = false;
// isPaused callback (a simple event handler)
void togglePause(bool isOn) {
        isPaused = isOn;
}

// A toggle button widget
class ToggleButton {

public:


        // the MODEL
        bool isOn;
	bool isHl;
        int x;
        int y;
	int i;
	int ani;
	double count;
        ToggleButton(int _x, int _y, int ra, int _num, void (*_toggleEvent)(bool)) {
                x = _x;
                y = _y;
		num = _num;
                toggleEvent = _toggleEvent;
                isOn = false;
                diameter = ra;
		isHl = false;
		ani = 1;
		count = 0;
        }

        // the CONTROLLER
        void mouseClick(int mx, int my) {
                float dist = sqrt(pow(mx - x, 2) + pow(my - y, 2));
                if (dist < diameter / 2) {
                        toggle();
                }
        }

        void mouseMove(int mx, int my) {
                float dist = sqrt(pow(mx - x, 2) + pow(my - y, 2));
                if (dist < diameter / 2) {
                        highlight();
                } else isHl = false;
        }

        // the VIEW
        void draw() {
	  if (ani == 0) {
		if (isOn) {
                        setForeground(BLACK);
                        XFillArc(xinfo.display, xinfo.window, xinfo.gc,
                                 x - diameter / 2,
                                 y - diameter / 2,
                                 diameter, diameter,
                                 0, 360 * 64);
                        setForeground(WHITE);
                        XDrawArc(xinfo.display, xinfo.window, xinfo.gc,
                                 x - i / 2,
                                 y - i / 2,
                                 i, i,
                                 0, 360 * 64);
			if (i>=5) i-=4;
			else isOn = !isOn;
                } else {
	                setForeground(WHITE);
	                XFillArc(xinfo.display, xinfo.window, xinfo.gc,
	                        x - diameter / 2,
	                        y - diameter / 2,
	                        diameter, diameter,
	                        0, 360 * 64);
	                setForeground(BLACK);
			if (isHl) {
				XSetLineAttributes(xinfo.display, xinfo.gc,
                           		4, LineSolid, CapButt, JoinMiter);
	                	XDrawArc(xinfo.display, xinfo.window, xinfo.gc,
		        	        x - diameter / 2,
		        	        y - diameter / 2,
		        	        diameter, diameter,
		        	        0, 360 * 64);
				XSetLineAttributes(xinfo.display, xinfo.gc,
                        	   		1, LineSolid, CapButt, JoinMiter);
				
			} else {
	                	XDrawArc(xinfo.display, xinfo.window, xinfo.gc,
		        	        x - diameter / 2,
		        	        y - diameter / 2,
		        	        diameter, diameter,
		        	        0, 360 * 64);
			}
		  	//load a larger font
			setForeground(BLACK);
			setBackground(WHITE);

			XFontStruct * font;
			font = XLoadQueryFont (xinfo.display, "12x24");
			XSetFont (xinfo.display, xinfo.gc, font->fid);
			
			// draw text
			stringstream ss;
			ss << num;
			string text(ss.str());
			ss.str("");
			XDrawImageString( xinfo.display, xinfo.window, xinfo.gc,
		                x-5,y+11, text.c_str(), text.length());

		}
	  } else {
		if (isOn) {
                        setForeground(BLACK);
                        XFillArc(xinfo.display, xinfo.window, xinfo.gc,
                                 x - diameter / 2,
                                 y - diameter / 2 + 10 * sin(count),
                                 diameter, diameter,
                                 0, 360 * 64);
                        setForeground(WHITE);
                        XDrawArc(xinfo.display, xinfo.window, xinfo.gc,
                                 x - i / 2,
                                 y - i / 2 + 10 * sin(count),
                                 i, i,
                                 0, 360 * 64);
			if (i>=5) i-=4;
			else isOn = !isOn;
                } else {
	                setForeground(WHITE);
	                XFillArc(xinfo.display, xinfo.window, xinfo.gc,
	                        x - diameter / 2,
	                        y - diameter / 2 + 10 * sin(count),
	                        diameter, diameter,
	                        0, 360 * 64);
	                setForeground(BLACK);
			if (isHl) {
				XSetLineAttributes(xinfo.display, xinfo.gc,
                           		4, LineSolid, CapButt, JoinMiter);
	                	XDrawArc(xinfo.display, xinfo.window, xinfo.gc,
		        	        x - diameter / 2,
		        	        y - diameter / 2 + 10 * sin(count),
		        	        diameter, diameter,
		        	        0, 360 * 64);
				XSetLineAttributes(xinfo.display, xinfo.gc,
                        	   		1, LineSolid, CapButt, JoinMiter);
				
			} else {
	                	XDrawArc(xinfo.display, xinfo.window, xinfo.gc,
		        	        x - diameter / 2,
		        	        y - diameter / 2 + 10 * sin(count),
		        	        diameter, diameter,
		        	        0, 360 * 64);
			}
		  	//load a larger font
			setForeground(BLACK);
			setBackground(WHITE);

			XFontStruct * font;
			font = XLoadQueryFont (xinfo.display, "12x24");
			XSetFont (xinfo.display, xinfo.gc, font->fid);
			
			// draw text
			stringstream ss;
			ss << num;
			string text(ss.str());
			ss.str("");
			XDrawImageString( xinfo.display, xinfo.window, xinfo.gc,
		                x-5,y+11+10*sin(count), text.c_str(), text.length());

		}
		count+=0.1;
		if (count >= 85) count = 0;

	  }
        }

private:

        // VIEW "essential geometry"
	int num;
        int diameter;

        // toggle event callback
        void (*toggleEvent)(bool);


        void toggle() {
                isOn = true;
                toggleEvent(isOn);
		i = diameter;
        }
	void highlight() {
		isHl = true;
	}
		
};


void repaint() {
	list<Displayable *>::const_iterator begin = dList.begin();
	list<Displayable *>::const_iterator end = dList.end();
        // draw display list
        while( begin != end ) {
                Displayable *d = *begin;
                d->paint();
                begin++;
        }
        XFlush( xinfo.display );
};

int main ( int argc, char* argv[] ) {

	// get the number of buttons from args
	// (default to 4 if no args)
	int n = 4;
    if (argc > 1) {
        n = atoi(argv[1]);
    }
    n = max(1, min(n, 9));

    int wi = 800;
    int hi = 400;

    
        xinfo.display = XOpenDisplay("");
        if (xinfo.display == NULL) exit (-1);
        int screennum = DefaultScreen(xinfo.display);
        long background = WhitePixel(xinfo.display, screennum);
        long foreground = BlackPixel(xinfo.display, screennum);
	XSizeHints hints;
        xinfo.window = XCreateSimpleWindow(xinfo.display, DefaultRootWindow(xinfo.display),
                                           10, 10, wi, hi, 2, foreground, background);
	XSetStandardProperties(
		xinfo.display, xinfo.window, "a1", "a1", None, argv,argc, &hints);
        XSelectInput(xinfo.display, xinfo.window,
                     ButtonPressMask |PointerMotionMask |StructureNotifyMask | KeyPressMask); // select events
        XMapRaised(xinfo.display, xinfo.window);
        XFlush(xinfo.display);

        XEvent event; // save the event here


        // create gc for drawing
        xinfo.gc = XCreateGC(xinfo.display, xinfo.window, 0, 0);
	XSetLineAttributes(xinfo.display, xinfo.gc,
              		1, LineSolid, CapButt, JoinMiter);

        // time of last xinfo.window paint
        unsigned long lastRepaint = 0;

        XWindowAttributes w;
        XGetWindowAttributes(xinfo.display, xinfo.window, &w);
			XSetLineAttributes(xinfo.display, xinfo.gc,
                           		1, LineSolid, CapButt, JoinMiter);

	vector<ToggleButton> toggleButton;

	for (int i = 0;i<n;i++) {
		toggleButton.push_back(ToggleButton((wi-n*100)/(n+1)*(i+1)+i*100+50, hi/2, 100, i+1, &togglePause));
		toggleButton[i].count = i*0.4;
	}


//        ToggleButton toggleButton(800/5, 200, &togglePause);




    // create the Simon game object
	Simon simon = Simon(n, true);

	cout << "Playing with " << simon.getNumButtons() << " buttons." << endl;
	string text1("Press SPACE to play");
	string text2("You won! Press SPACE to continue.");
	string text3("You lose. Press SPACE to play again.");

	while (true) {
		// leave some space between rounds
		cout << endl;

		for (int i = 0;i<n;i++)
			toggleButton[i].ani = 1;
		int flag = 1;
	        while (flag) {

	                // TRY THIS
	                // comment out this conditional to see what happens when
	                // events block (run the program and keep pressing the mouse)
			//load a larger font
			setForeground(BLACK);
			setBackground(WHITE);

			XFontStruct * font;
			font = XLoadQueryFont (xinfo.display, "10x20");
			XSetFont (xinfo.display, xinfo.gc, font->fid);
			
			stringstream ss;
			ss << simon.getScore();
			string t = ss.str();
			XDrawImageString( xinfo.display, xinfo.window, xinfo.gc,
				40,40, t.c_str(), t.length());
			ss.str("");
			// draw text
			switch (simon.getState()) {

			// will only be in this state right after Simon object is contructed
			case Simon::START:
				XDrawImageString( xinfo.display, xinfo.window, xinfo.gc,
			                40,80, text1.c_str(), text1.length());
				break;
			// they won last round
			// score is increased by 1, sequence length is increased by 1
			case Simon::WIN:
				XDrawImageString( xinfo.display, xinfo.window, xinfo.gc,
			                40,80, text2.c_str(), text2.length());
				break;
			// they lost last round
			// score is reset to 0, sequence length is reset to 1
			case Simon::LOSE:
				XDrawImageString( xinfo.display, xinfo.window, xinfo.gc,
			                40,80, text3.c_str(), text3.length());
				break;
			default:
				// should never be any other state at this point ...
				break;
			}
	                if (XPending(xinfo.display) > 0) {
	                        XNextEvent( xinfo.display, &event );

	
	                        switch ( event.type ) {
	
	                        // mouse button press
	                        case ButtonPress:
	                                // cout << "ButtonPress" << endl;
					for (int i = 0;i<n;i++)
	                        		toggleButton[i].mouseClick(event.xbutton.x, event.xbutton.y);
	                                break;
				
				case MotionNotify:
					for (int i = 0;i<n;i++){
						toggleButton[i].mouseMove(event.xmotion.x, event.xmotion.y);
					}
					break;
				case ConfigureNotify:
					for (int i = 0;i<n;i++){
						toggleButton[i].x = (event.xconfigure.width-n*100)/(n+1)*(i+1)+i*100+50;
						toggleButton[i].y = event.xconfigure.height/2;
					}
					break;

	                        case KeyPress: // any keypress
	                                KeySym key;
	                                char text[10];
	                                int i = XLookupString( (XKeyEvent*)&event, text, 10, &key, 0 );
	                                if ( i == 1 && text[0] == 'q' ) {
	                                        XCloseDisplay(xinfo.display);
	                                        exit(0);
	                                }
					if ( i == 1 && text[0] == ' ' ) {
						flag = 0;
					}
					break;
	                        }
	                }
			if (!flag) break;
	                unsigned long end = now();

	                if (end - lastRepaint > 1000000 / FPS) {

				for (int i=0;i<n;i++) {
				        toggleButton[i].draw();
				}
	        		XFlush(xinfo.display);
	                	lastRepaint = now(); // remember when the paint happened
	                }
		        // IMPORTANT: sleep for a bit to let other processes work
	                if (XPending(xinfo.display) == 0) {
	                        usleep(1000000 / FPS - (end - lastRepaint));
	        		XClearWindow(xinfo.display, xinfo.window);
				
	                }
        	}

		// start new round with a new sequence
		simon.newRound();
		
		for (int i=0;i<n;i++) {
			toggleButton[i].isOn = false;
			toggleButton[i].isHl = false;
			toggleButton[i].ani = 0;
		}
		usleep(500);
		while (simon.getState() == Simon::COMPUTER) {
		int tmp = simon.nextButton();
		toggleButton[tmp].mouseClick(toggleButton[tmp].x, toggleButton[tmp].y);
		while (true) {
			setForeground(BLACK);
			setBackground(WHITE);

			XFontStruct * font;
			font = XLoadQueryFont (xinfo.display, "10x20");
			XSetFont (xinfo.display, xinfo.gc, font->fid);
				
			stringstream ss;
			ss << simon.getScore();
			string t = ss.str();
			XDrawImageString( xinfo.display, xinfo.window, xinfo.gc,
				40,40, t.c_str(), t.length());
			ss.str("");
			// computer plays
			string text4("Watch what I do ...");
			XDrawImageString( xinfo.display, xinfo.window, xinfo.gc,
				40,80, text4.c_str(), text4.length());
			
	                //if (XPending(xinfo.display) > 0) {
	                //        XNextEvent( xinfo.display, &event );

	
	                        switch ( event.type ) {
				case ConfigureNotify:
					for (int i = 0;i<n;i++){
						toggleButton[i].x = (event.xconfigure.width-n*100)/(n+1)*(i+1)+i*100+50;
						toggleButton[i].y = event.xconfigure.height/2;
					}
					break;

	                        case KeyPress: // any keypress
	                                KeySym key;
	                                char text[10];
	                                int i = XLookupString( (XKeyEvent*)&event, text, 10, &key, 0 );
	                                if ( i == 1 && text[0] == 'q' ) {
	                                        XCloseDisplay(xinfo.display);
	                                        exit(0);
	                                }
					break;
	                        }
			//}
			if (toggleButton[tmp].isOn == false) {
				usleep(250);
				break;
			}
	                unsigned long end = now();

	                if (end - lastRepaint > 1000000 / FPS) {

				for (int i=0;i<n;i++) {
				        toggleButton[i].draw();
				}
	        		XFlush(xinfo.display);
	                	lastRepaint = now(); // remember when the paint happened
	                }
		        // IMPORTANT: sleep for a bit to let other processes work
	                if (XPending(xinfo.display) == 0) {
	                        usleep(1000000 / FPS - (end - lastRepaint));
	        		XClearWindow(xinfo.display, xinfo.window);	
	                }
		}
		}

		
	        //XClearWindow(xinfo.display, xinfo.window);
		for (int i=0;i<n;i++) {
			toggleButton[i].isOn = false;
			toggleButton[i].isHl = false;
		}

		// now human plays
		while (simon.getState() == Simon::HUMAN) {
			int ans;
			int pick = 0;
			// see if guess was correct
	        	while (true) {

			setForeground(BLACK);
			setBackground(WHITE);

			XFontStruct * font;
			font = XLoadQueryFont (xinfo.display, "10x20");
			XSetFont (xinfo.display, xinfo.gc, font->fid);
			
			stringstream ss;
			ss << simon.getScore();
			string t = ss.str();
			XDrawImageString( xinfo.display, xinfo.window, xinfo.gc,
				40,40, t.c_str(), t.length());
			ss.str("");

			string text5("Your turn ...");
			XDrawImageString( xinfo.display, xinfo.window, xinfo.gc,
		                40,80, text5.c_str(), text5.length());
	                if (XPending(xinfo.display) > 0) {
	                        XNextEvent( xinfo.display, &event );

	
	                        switch ( event.type ) {
	
	                        // mouse button press
	                        case ButtonPress:
	                                // cout << "ButtonPress" << endl;
					for (int i = 0;i<n;i++){
	                        		toggleButton[i].mouseClick(event.xbutton.x, event.xbutton.y);
						if (toggleButton[i].isOn == true) {
							ans = i;
							pick = 1;
						}
					}
	                                break;
				
				case MotionNotify:
					for (int i = 0;i<n;i++){
						toggleButton[i].mouseMove(event.xmotion.x, event.xmotion.y);
					}
					break;
				case ConfigureNotify:
					for (int i = 0;i<n;i++){
						toggleButton[i].x = (event.xconfigure.width-n*100)/(n+1)*(i+1)+i*100+50;
						toggleButton[i].y = event.xconfigure.height/2;
					}
					break;

	                        case KeyPress: // any keypress
	                                KeySym key;
	                                char text[10];
	                                int i = XLookupString( (XKeyEvent*)&event, text, 10, &key, 0 );
	                                if ( i == 1 && text[0] == 'q' ) {
	                                        XCloseDisplay(xinfo.display);
	                                        exit(0);
	                                }
					break;
	                        }
	                }
			if ((pick == 1) and (toggleButton[ans-1].isOn == false)){
				pick = 0;
				break;
			}
	                unsigned long end = now();

	                if (end - lastRepaint > 1000000 / FPS) {

				for (int i=0;i<n;i++) {
				        toggleButton[i].draw();
				}
	        		XFlush(xinfo.display);
	                	lastRepaint = now(); // remember when the paint happened
	                }
		        // IMPORTANT: sleep for a bit to let other processes work
	                if (XPending(xinfo.display) == 0) {
	                        usleep(1000000 / FPS - (end - lastRepaint));
	        		XClearWindow(xinfo.display, xinfo.window);
				
	                }
        		}
		simon.verifyButton(ans);
	}
}
}
