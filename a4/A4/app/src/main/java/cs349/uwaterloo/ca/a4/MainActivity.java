package cs349.uwaterloo.ca.a4;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    // Private Vars
    private MainViewModel mainViewModel;
    private Button mButton;

    /**
     * OnCreate
     * -- Called when application is initially launched.
     *
     * @param savedInstanceState
     * @see <a href="https://developer.android.com/guide/components/activities/activity-lifecycle.html">Android LifeCycle</a>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Set View Content
        setContentView(R.layout.activity_main);

        // Set mButton ref
        mButton = (Button) findViewById(R.id.view1_button);

        // Set view model
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        // Init View Model
        mainViewModel.init(new Integer(0));

        // Set Observer to State in ViewModel
        mainViewModel.getCounter().observe(this, new Observer<Integer>()
        {
            @Override
            public void onChanged(@Nullable Integer i)
            {
                mButton.setText("Click to Start");
            }
        });

        // Create onClick Listener for Button
        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Update view model
                // mainViewModel.incrementCounter();

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);

                // Start activity
                startActivity(intent);
                finish();
            }
        });
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Options Menu
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view1, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle interaction based on item selection
        switch (item.getItemId())
        {
            case R.id.menu1_GoToView2:
                // Create Intent to launch second activity
                Intent intent = new Intent(this, SecondActivity.class);

                // Start activity
                startActivity(intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
