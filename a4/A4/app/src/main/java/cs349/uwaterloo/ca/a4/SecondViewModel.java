package cs349.uwaterloo.ca.a4;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Observable;
import java.util.Observer;

/**
 * MVP1
 * <p>
 * Created by J. J. Hartmann on 11/19/2017.
 * Email: j3hartma@uwaterloo.ca
 * Copyright 2017
 */

public class SecondViewModel extends ViewModel implements Observer
{

    // Private Vars
    private MutableLiveData<String > mCounterString;
    private Model mModel;

    // Initialize persistent data
    public void init(Integer i){
        if (mCounterString == null){
            mCounterString = new MutableLiveData<String>();
        }

        if (mModel == null){
            mModel = Model.getInstance();
            mModel.addObserver(this);
        }
        mModel.initObservers();
    }

    public boolean verifyButton(int x){
        return mModel.verifyButton(x);
    }

    public int getState() {
        return mModel.state;
    }

    public int nextButton() {
        return mModel.nextButton();
    }

    public void setState(int x) {
        if (x != 1) mModel.setState(x);
        else {
            mModel.newRound();
        }
    }

    public void draw() {
        mModel.draw();
    }

    // Get Counter Value
    public  MutableLiveData<String> getCounter() {
        return mCounterString;
    }

    // Increment Values
    public void incrementCounter(){
        mModel.incrementCounter();
    }


    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg)
    {
        // Build String based on mModel state
        StringBuilder s = new StringBuilder();
        if (mModel.state == 0) {
            // start
            s = s.append("Click any button to continue! Score: "+mModel.score);
        } else if (mModel.state == 1) {
            s = s.append("Watch what I do... Score: "+mModel.score);
        } else if (mModel.state == 2) {
            s = s.append("Your turn... Score: "+mModel.score);
        } else if (mModel.state == 3) {
            s = s.append("You lose. Click any button to continue. Score: "+mModel.score);
        } else {
            s = s.append("You win! Click any button to continue! Score: "+mModel.score);
        }

        // Update textView
        mCounterString.setValue(s.toString());
    }
}
