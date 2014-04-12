package com.tao.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tao.R;
import com.tao.database.LogsDataSource;
import com.tao.unitClass.Logs;
import com.tao.utility.HelperFunctions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.List;


public class MainScreen extends Activity {
   List<Logs> logs;
    EnhancedListView elv;
    EnahncedListViewAdapter lAdapter;
    Context ctx ;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
        ctx=this;




        elv = (EnhancedListView) findViewById(R.id.viewEn);
    lAdapter= new EnahncedListViewAdapter();
        lAdapter.resetItems();
        elv.setAdapter(lAdapter);
        this.setUpList();
	}
    public void setUpList(){
        EnhancedListView enhancedListView = elv.setDismissCallback(new EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView listView, int position) {
                final Logs temp = logs.get(position);
                lAdapter.remove(position);
                final int pos = position;
                return new EnhancedListView.Undoable(){
                    @Override
                    public void undo() {
                        lAdapter.insert(pos, temp);


                }

                    @Override
                    public void discard() {
                        LogsDataSource LDS = new LogsDataSource(ctx);
                        LDS.open();
                        LDS.deleteLog(logs.get(pos).getLogID());
                        LDS.close();
                    }
                };
        }});

        elv.setUndoStyle(EnhancedListView.UndoStyle.MULTILEVEL_POPUP);
        elv.enableSwipeToDismiss();
        elv.setSwipeDirection(EnhancedListView.SwipeDirection.BOTH);
        elv.setRequireTouchBeforeDismiss(false);

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_screen, menu);
		return true;
	}


     class EnahncedListViewAdapter extends BaseAdapter{


         private List<Logs> listLogs;

         void resetItems()
         {
             LogsDataSource LDS = new LogsDataSource(ctx);
             LDS.open();
             Logs log = new Logs(1234234,"this is data");
             LDS.addOrUpdateLog(log);
             log.setLogID(21323331);

             LDS.addOrUpdateLog(log);
             log.setData("this is the updated data");
             LDS.addOrUpdateLog(log);
             listLogs= LDS.getAllLogs();
             LDS.close();
             logs=listLogs;
             notifyDataSetChanged();;
         }
         @Override
         public int getCount() {
             return listLogs.size();
         }

         @Override
         public Object getItem(int position) {
             return listLogs.get(position);
         }

         @Override
         public long getItemId(int position) {
             return position;
         }

         @Override
         public View getView(int position, View convertView, ViewGroup parent) {
             View view = convertView;
             if(view == null)
             {
                 view = getLayoutInflater().inflate(R.layout.list_element,parent,false);
             }

             Logs currentLog = listLogs.get(position);
             TextView heading = (TextView) view.findViewById(R.id.heading);
             TextView summary = (TextView) view.findViewById(R.id.summary);

             heading.setText(HelperFunctions.timeFormater(currentLog.getTimeStamp())+"");
             summary.setText(currentLog.getData());


             return view;
         }

         public void remove(int position) {
             listLogs.remove(position);
             notifyDataSetChanged();
         }

         public void insert(int position, Logs item) {

             listLogs.add(position, item);
             notifyDataSetChanged();
         }

         private class DialogPicker extends DialogFragment {
             @Override
             public Dialog onCreateDialog(Bundle savedInstanceState) {
                 return super.onCreateDialog(savedInstanceState);
             }
         }
    }
	{
	/*
	<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<extra code>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	/*
		FormTemplateDataSource FTDS = new FormTemplateDataSource(this);
		Form f1 = new Form("abc","hgjdf",1);
		f1.setFormID(1234556);
		f1.setTimeStamp(45689345);
		
		Form f3 = new Form("cde","hgjdf",1);
		f1.setFormID(1234556);
		f1.setTimeStamp(45689345);
		
		FTDS.open();
		FTDS.addOrUpdateForm(f1);
		f1.setFormName("nishant");
		FTDS.addOrUpdateForm(f1);
		FTDS.addOrUpdateForm(f3);
		
		List<Form> forms= FTDS.getAllForms();
		for(Form e : forms)
		Log.d("TAO",e.getFormName());
		FTDS.close();
		
		
		LogsDataSource LDS = new LogsDataSource(this);
		LDS.open();
		Logs log = new Logs(12344,"this is data");
		log.setLogID(2131);
		
		LDS.addOrUpdateLog(log);
		log.setData("this is the updated data");
		LDS.addOrUpdateLog(log);
		Logs log2 = new Logs(12344,"this is second data");
		log2.setLogID(21311);
		LDS.addOrUpdateLog(log2);
		Logs log3 = new Logs(12344,"this is third data");
		log3.setLogID(213111);
		LDS.addOrUpdateLog(log3);
		LDS.deleteLog(21311);
		 
		List<Logs> logs = LDS.getAllLogs();
		
		for (Logs l : logs)
		Log.d("TAO",l.getData());
		LDS.close();
		*/
	}
}
