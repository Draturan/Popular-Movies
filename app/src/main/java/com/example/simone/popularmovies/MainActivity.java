package com.example.simone.popularmovies;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.simone.popularmovies.fragments.FavoritesFragment;
import com.example.simone.popularmovies.fragments.MostRatedFragment;
import com.example.simone.popularmovies.fragments.PopularFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.pb_api_request_indicator) ProgressBar mProgressBar;
    @BindView(R.id.tv_internet_message) TextView mInternetMessage;

    private static final String LIFECYCLE_LAST_MAIN_FRAGMENT_VIEWED = "last_main_fragment";
    public static final String FRAGMENT_BACKSTACK = "ReturnToCorrectFragmentLessie";
    private int mLastMainFragmentId = 0;
    private Fragment selectedFragment = null;
    private static final String SELECTED_FRAGMENT_TAG = "myfragmentTag";

    @BindView(R.id.bottom_navigation_view) BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // checking for savedInstantStates
        if (savedInstanceState != null){
            mLastMainFragmentId = savedInstanceState.getInt(LIFECYCLE_LAST_MAIN_FRAGMENT_VIEWED);
            // solution found here https://github.com/codepath/android_guides/wiki/Handling-Configuration-Changes
            selectedFragment = getSupportFragmentManager().findFragmentByTag(SELECTED_FRAGMENT_TAG);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /*
            Bottom Navigator View
            The guide to implement the Bottom Navigator View, with usage of Fragments, was given by
            my reviewer as a suggestion to improve the usability of the app, followed at this link:
            http://www.truiton.com/2017/01/android-bottom-navigation-bar-example/
         */
        // setting up Bottom Navigator View
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        mLastMainFragmentId = item.getItemId();
                        switch (mLastMainFragmentId){
                            case R.id.action_popular:
                                selectedFragment = PopularFragment.newInstance();
                                break;
                            case R.id.action_most_rated:
                                selectedFragment = MostRatedFragment.newInstance();
                                break;
                            case R.id.action_favorites:
                                selectedFragment = FavoritesFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment, SELECTED_FRAGMENT_TAG).addToBackStack(FRAGMENT_BACKSTACK).commit();
                        return true;
                    }
        });

        if (mLastMainFragmentId != 0){
            if (selectedFragment == null){
                switch (mLastMainFragmentId) {
                    case R.id.action_popular:
                        selectedFragment = PopularFragment.newInstance();
                        break;
                    case R.id.action_most_rated:
                            selectedFragment = MostRatedFragment.newInstance();
                        break;
                    case R.id.action_favorites:
                            selectedFragment = FavoritesFragment.newInstance();
                        break;
                }
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment,SELECTED_FRAGMENT_TAG).addToBackStack(FRAGMENT_BACKSTACK).commit();
        }else {
            // Start with the first fragment automatically
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //checking if the selectedFragment is not already created
            selectedFragment = selectedFragment == null ? PopularFragment.newInstance() : selectedFragment;
            transaction.replace(R.id.frame_layout, selectedFragment, SELECTED_FRAGMENT_TAG).addToBackStack(FRAGMENT_BACKSTACK).commit();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LIFECYCLE_LAST_MAIN_FRAGMENT_VIEWED, mLastMainFragmentId);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mainmenu, menu);

        /* --- initialize Spinner for sorting ---
            Find out how to put a Spinner inside a menu here:
            https://stackoverflow.com/questions/37250397/how-to-add-a-spinner-next-to-a-menu-in-the-toolbar
            and on Android Guide:
            https://developer.android.com/guide/topics/ui/controls/spinner.html
        */
//        MenuItem item = menu.findItem(R.id.action_sorting);
//        final Spinner spinner = (Spinner) item.getActionView();
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.sorting_modes, R.layout.spinner_item);
//        adapter.setDropDownViewResource(R.layout.spinner_dropdown_menu);
//        spinner.setAdapter(adapter);
//        // set last Spinner item Position
//        spinner.setSelection(mSpinnerItemPosition,true);
//        // onClickListener of the Spinner
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (spinner.getSelectedItem().toString()) {
//                    case "Popularity":
//                        mSelectedSort = "popular";
//                        break;
//                    case "Average Vote":
//                        mSelectedSort = "top rated";
//                        break;
//                }
//                //
//                // startAsyncRetrievingMoviesInfo(mSelectedSort,null);
//                mSpinnerItemPosition = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        return true;
    }


}
