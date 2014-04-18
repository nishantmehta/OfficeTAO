package com.tao.ui;


import android.annotation.TargetApi;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.Toast;
import android.util.Base64;

import com.formtry.FormActivity;
import com.tao.R;
import com.tao.database.FormTemplateDataSource;
import com.tao.database.LogsDataSource;
import com.tao.unitClass.Form;
import com.tao.unitClass.Logs;
import com.tao.utility.HelperFunctions;

import org.json.JSONObject;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.tao.R;
import java.util.Calendar;
import java.util.Date;

public class DataFormActivity  extends FormActivity implements OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    public static final int OPTION_SAVE = 0;
    public static final int OPTION_POPULATE = 1;
    public static final int OPTION_CANCEL = 2;
    private long formID;
    private long logID;
    private int newForm;
    Form form;
    Logs log;
    Calendar calendar;
    Button dateButton,timeButton;


    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );

       Intent extra=getIntent();

        //get extra from string
        if(extra!=null){
            newForm=Integer.parseInt(extra.getExtras().getString("newForm"));
            formID=Long.parseLong(extra.getExtras().getString("formID"));

        }

        setContentView(R.layout.activity_data_form);

        timeButton=(Button) findViewById(R.id.timeDataButton);
        dateButton=(Button)findViewById(R.id.dateDataButton);


       calendar  = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);

        findViewById(R.id.dateDataButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        findViewById(R.id.dateDataButton).setBackgroundResource(R.drawable.abc_spinner_ab_default_holo_light);
        findViewById(R.id.timeDataButton).setBackgroundResource(R.drawable.abc_spinner_ab_default_holo_light);
        findViewById(R.id.timeDataButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });



        FormTemplateDataSource FTDS = new FormTemplateDataSource(this);
        FTDS.open();
        form = FTDS.getForm(formID);
        FTDS.close();
        generateForm(HelperFunctions.makeAppCompact(form.getFormTemplate()));
        if(newForm==0)
        {
            logID=Long.parseLong(extra.getExtras().getString("logID"));
            LogsDataSource LDS = new LogsDataSource(this);
            LDS.open();
            log=LDS.getLog(logID);
            LDS.close();
            timeButton.setText(HelperFunctions.onlyTimeFormatter(log.getTimeStamp()));
            dateButton.setText(HelperFunctions.dateFormatter(log.getTimeStamp()));
            populate(HelperFunctions.makeAppCompact(log.getData()));
        }
        else{
            timeButton.setText(HelperFunctions.onlyTimeFormatter(calendar.getTimeInMillis()));
            dateButton.setText(HelperFunctions.dateFormatter(calendar.getTimeInMillis()));
        }


        getActionBar().setTitle(form.getFormName());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        save();
    }



    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate(R.menu.data_form, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_save_form:
            {
                JSONObject obj =save();
                String data=obj.toString();
                long time=calendar.getTimeInMillis();
                data= HelperFunctions.makeDBCompact(data);
                if(newForm==0)
                {
                    log.setData(HelperFunctions.makeDBCompact(data));
                    log.setTimeStamp(time);
                }
                else
                {

                    log = new Logs(formID,data);
                    log.setLogID(time);
                    log.setTimeStamp(time);

                }

                LogsDataSource LDS = new LogsDataSource(this);
                LDS.open();




                LDS.addOrUpdateLog(log);
                LDS.close();
                Toast.makeText(this,"log  saved",Toast.LENGTH_SHORT);
                Log.d("TAO", data);
                break;

            }
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onMenuItemSelected( int id, MenuItem item )
    {

        switch( item.getItemId() )
        {
            case OPTION_SAVE:

                break;

            case OPTION_CANCEL:

                break;
        }

        finish();
        return super.onMenuItemSelected( id, item );
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        Log.d("TAO","month "+month);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DATE,day);
        calendar.set(Calendar.YEAR,year);
        Log.d("TAO","month "+calendar.MONTH);
        dateButton.setText(HelperFunctions.dateFormatter(calendar.getTimeInMillis()));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Date dt=calendar.getTime();
        calendar.set(dt.getYear(),dt.getMonth(),dt.getDay(),hourOfDay,minute);
        timeButton.setText(HelperFunctions.onlyTimeFormatter(calendar.getTimeInMillis()));

    }
}