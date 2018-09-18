package cs349.uwaterloo.ca.a4;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;

/**
 * Created by Hanzhang Chen, UWID:20574275
 * Start from the example code:
 * MVC2 Model
 * <p>
 * Created by J. J. Hartmann on 11/19/2017.
 * Email: j3hartma@uwaterloo.ca
 * Copyright 2017
 */

class Model extends Observable
{
    // Create static instance of this mModel
    private static final Model ourInstance = new Model();
    static Model getInstance()
    {
        return ourInstance;
    }

    // Private Variables
    int score;
    int state;
    int speed;
    int length;
    int buttons;
    int index;
    ArrayList<Integer> sequence = new ArrayList<>();


    /**
     * Model Constructor:
     * - Init member variables
     */
    Model() {
        score = 0;
        speed = 1;
        length = 1;
        state = 0;
        buttons = 4;
    }

    /**
     * Get mCounter Values
     * @return Current value mCounter
     */
    public int getCounter()
    {
        return score;
    }

    /**
     * Set mCounter Value
     * @param i
     * -- Value to set Counter
     */
    public void setCounter(int i)
    {
        Log.d("SIMON", "Model: set counter to " + score);
        this.score = i;
    }

    /**
     * Increment mCounter by 1
     */
    public void incrementCounter()
    {
        score++;
        Log.d("SIMON", "Model: increment counter to " + score);

        // Observable API
        setChanged();
        notifyObservers();
    }

    public void draw() {
        setChanged();
        notifyObservers();
    }

    public void setState(int x) {
        state = x;
        setChanged();
        notifyObservers();
    }

    public void newRound() {
        if (state == 3) {
            length = 1;
            score = 0;
        }
        sequence.clear();
        for (int i=0;i<length;i++) {
            int random = (int)(Math.random() * buttons);
            sequence.add(random);
        }
        index = 0;
        state = 1;
        setChanged();
        notifyObservers();
    }

    public int nextButton() {
        if (state != 1) {
            return -1;
        }

        int button = sequence.get(index);
        index++;
        if (index >= sequence.size()) {
            index = 0;
            state = 2;
        }
        return button;
    }

    public boolean verifyButton(int button) {
        if (state != 2) {
            return false;
        }

        boolean correct = (button == sequence.get(index));
        index++;
        if (!correct) {
            state = 3;
        } else {
            if (index == sequence.size()) {
                state = 4;
                score++;
                length++;
            }
        }
        setChanged();
        notifyObservers();
        return correct;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Observable Methods
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Helper method to make it easier to initialize all observers
     */
    public void initObservers()
    {
        setChanged();
        notifyObservers();
    }

    /**
     * Deletes an observer from the set of observers of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     *
     * @param o the observer to be deleted.
     */
    @Override
    public synchronized void deleteObserver(Observer o)
    {
        super.deleteObserver(o);
    }

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    @Override
    public synchronized void addObserver(Observer o)
    {
        super.addObserver(o);
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    @Override
    public synchronized void deleteObservers()
    {
        super.deleteObservers();
    }

    /**
     * If this object has changed, as indicated by the
     * <code>hasChanged</code> method, then notify all of its observers
     * and then call the <code>clearChanged</code> method to
     * indicate that this object has no longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and <code>null</code>. In other
     * words, this method is equivalent to:
     * <blockquote><tt>
     * notifyObservers(null)</tt></blockquote>
     *
     * @see Observable#clearChanged()
     * @see Observable#hasChanged()
     * @see Observer#update(Observable, Object)
     */
    @Override
    public void notifyObservers()
    {
        super.notifyObservers();
    }
}
