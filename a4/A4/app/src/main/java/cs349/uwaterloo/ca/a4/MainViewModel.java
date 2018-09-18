package cs349.uwaterloo.ca.a4;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Hanzhang Chen, UWID:20574275
 * Start from the example code:
 * MVP1
 * <p>
 * Created by J. J. Hartmann on 11/19/2017.
 * Email: j3hartma@uwaterloo.ca
 * Copyright 2017
 */

public class MainViewModel extends ViewModel implements Observer
{

    // Private Vars
    private MutableLiveData<Integer> mCounter;
    private Model mModel;

    // Initialize persistent data
    public void init(Integer i){
        if (mCounter == null){
            mCounter = new MutableLiveData<Integer>();
        }

        if (mModel == null){
            mModel = Model.getInstance();
            mModel.addObserver(this);
        }

        mModel.initObservers();
    }

    // Get Counter Value
    public  MutableLiveData<Integer> getCounter() {
        return mCounter;
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
        // Update view mModel state
        mCounter.setValue(mModel.getCounter());
    }
}
