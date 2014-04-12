package com.tao.ui;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.formtry.FormActivity;
import com.tao.R;
import com.tao.database.FormTemplateDataSource;
import com.tao.database.LogsDataSource;
import com.tao.unitClass.Form;
import com.tao.unitClass.Logs;
import com.tao.unitClass.NavListItem;
import com.tao.utility.HelperFunctions;

import java.util.ArrayList;
import java.util.List;

public class TAOMainScreen extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private NavAdapter menuAdapter;
    private ArrayList<Form> navFormArray;
    private ArrayList<NavListItem> navItemArray;
    private List<Form> forms;
    Context ctx;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taomain_screen);

        FormTemplateDataSource FTDS = new FormTemplateDataSource(this);
           ctx=this;
        String Monitoring =HelperFunctions.makeDBCompact(FormActivity.parseFileToString(this, "Anxiety.json"));

        Form f1 = new Form("Monitoring Log",Monitoring,1);

        f1.setFormID(1);
        f1.setTimeStamp(45689345);

        String Relaxation =HelperFunctions.makeDBCompact(FormActivity.parseFileToString(this, "Relaxation.json"));
        Form f3 = new Form("Relaxation Log",Relaxation,1);
        f3.setFormID(2);
        f3.setTimeStamp(45689345);

        String Challenge =HelperFunctions.makeDBCompact(FormActivity.parseFileToString(this, "AnxChlog2.json"));
        Form f2 = new Form("Challenge Log",Challenge,1);
        f2.setFormID(3);
        f2.setTimeStamp(454589345);

        String Exposure =HelperFunctions.makeDBCompact(FormActivity.parseFileToString(this, "Anxiety_challenge.json"));
        Form f4 = new Form("Exposure Log",Exposure,1);
        f4.setFormID(4);
        f4.setTimeStamp(454589345);


        FTDS.open();
        FTDS.addOrUpdateForm(f1);
        FTDS.addOrUpdateForm(f2);
        FTDS.addOrUpdateForm(f3);
        FTDS.addOrUpdateForm(f4);

        forms= FTDS.getAllForms();
        for(Form e : forms)
            Log.d("TAO", e.getFormName());
        FTDS.close();
        LogsDataSource LDS = new LogsDataSource(this);
/*
        LDS.open();

        for(int i=1;i<5;i++){
        Logs log = new Logs(i,"this has form "+ i +" data 1");
        log.setLogID(System.currentTimeMillis());
        LDS.addOrUpdateLog(log);

        log = new Logs(i,"this has form"+ i +" data 2");
        log.setLogID(System.currentTimeMillis());
        LDS.addOrUpdateLog(log);

        log = new Logs(i,"this has form "+ i +" data 3");
        log.setLogID(System.currentTimeMillis());
        LDS.addOrUpdateLog(log);

        log = new Logs(i,"this has form "+ i +" data 4");
        log.setLogID(System.currentTimeMillis());
        LDS.addOrUpdateLog(log);

        log = new Logs(i,"this has form "+ i +"  data 5");
        log.setLogID(System.currentTimeMillis());
        LDS.addOrUpdateLog(log);
        }
        LDS.close();
*/


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        mTitle = mDrawerTitle = getTitle();
        navFormArray=this.GetAllForms();
        navItemArray = new ArrayList<NavListItem>();
        this.setUpNavItems();
        menuAdapter = new NavAdapter(getApplicationContext(),
                navItemArray);
        mDrawerList.setAdapter(menuAdapter);





        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }


        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        if (savedInstanceState == null) {
            Fragment frag = new MainListFragment();
            Bundle args = new Bundle();
            args.putInt("flag",1);
            args.putLong("formID", 0);
            frag.setArguments(args);

            getSupportFragmentManager().beginTransaction().add(R.id.container, frag)
                    .commit();
        }


    }
    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    public void setUpNavItems(){
        navItemArray.add(new NavListItem(R.drawable.ic_launcher,"All forms", 0,1));
        for (Form f : navFormArray){
            navItemArray.add(new NavListItem(R.drawable.ic_launcher,f.getFormName(),f.getFormID(),0));
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.taomain_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_add_log:
            {
                View menuID = findViewById(R.id.action_add_log);
                PopupMenu popup = new PopupMenu(this,menuID);
                for(Form form : forms)
                {
                    popup.getMenu().add(Menu.NONE,(int)form.getFormID(),Menu.NONE,form.getFormName());
                }

                // popup.inflate(R.menu.taomain_screen);

                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent itn = new Intent(ctx,DataFormActivity.class);
                        int id=menuItem.getItemId();
                        itn.putExtra("formID",id+"");
                        itn.putExtra("newForm",1+"");
                        ctx.startActivity(itn);
                        Log.e("TAO", "got this one "+menuItem.getItemId());
                        return false;
                    }
                });


            }
        }

        if (id == R.id.action_settings) {


            return true;


        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Form> GetAllForms()
    {
        FormTemplateDataSource FTDS = new FormTemplateDataSource(this);
        FTDS.open();
        ArrayList<Form> data = (ArrayList<Form>) FTDS.getAllForms();
        FTDS.close();
        return data;

    }



    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new MainListFragment();
        NavListItem clicked= navItemArray.get(position);
        Bundle args = new Bundle();
        args.putLong("formID",clicked.getFormID());
        getActionBar().setTitle(clicked.getTitle());
        args.putInt("flag",clicked.getFlag());
        fragment.setArguments(args);
        if (fragment != null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                    .commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);

            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

}
