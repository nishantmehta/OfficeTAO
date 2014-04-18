package com.tao.ui;



import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tao.R;
import com.tao.database.LogsDataSource;
import com.tao.unitClass.Logs;
import com.tao.utility.HelperFunctions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nishantmehta.n on 3/30/14.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainListFragment extends Fragment {
    Bundle args;
    int flag;
    long formID;
    List<Logs> logs;
    EnhancedListView elv;
    EnahncedListViewAdapter lAdapter;
    LayoutInflater inflater;
    Context ctx;
    public MainListFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater=inflater;
        ctx=getActivity();
        View rootView = inflater.inflate(R.layout.fragment_taomain_screen, container, false);
        args=this.getArguments();
        flag=args.getInt("flag");
        formID =args.getLong("formID");



        elv = (EnhancedListView) rootView.findViewById(R.id.ListViewFragment);
        lAdapter= new EnahncedListViewAdapter();

        lAdapter.resetItems();
        elv.setAdapter(lAdapter);
        this.setUpList();
        elv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Logs temp= (Logs)lAdapter.getItem(position);
                Intent itn = new Intent(ctx,DataFormActivity.class);

                itn.putExtra("formID",temp.getFormID()+"");
                itn.putExtra("logID",temp.getLogID()+"");
                itn.putExtra("newForm",0+"");

                ctx.startActivity(itn);
            }
        });

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        lAdapter.resetItems();
        lAdapter.notifyDataSetChanged();
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
                        Log.d("TAO", "size of log is "+logs.size());
                        LogsDataSource LDS = new LogsDataSource(ctx);
                        LDS.open();
                        LDS.deleteLog(/*logs.get(pos)*/temp.getLogID());
                        LDS.close();
                    }
                };
            }});

        elv.setUndoStyle(EnhancedListView.UndoStyle.MULTILEVEL_POPUP);
        elv.enableSwipeToDismiss();
        elv.setSwipeDirection(EnhancedListView.SwipeDirection.START);
        elv.setRequireTouchBeforeDismiss(false);
        elv.setUndoHideDelay(3000);

    }


    class EnahncedListViewAdapter extends BaseAdapter {


        private List<Logs> listLogs= new ArrayList<Logs>();

        void resetItems()
        {
            LogsDataSource LDS = new LogsDataSource(ctx);
            LDS.open();
            List<Logs> templist= LDS.getAllLogs();

            //


            listLogs.clear();
            if(flag==0 )
            {
            for(Logs log : templist){
                if(log.getFormID()==formID)
                    listLogs.add(new Logs(log));
            }

            }
            else
            {
                listLogs= LDS.getAllLogs();
            }

            logs=listLogs;


            for (Logs g : logs)
            {
                Log.d("TAO",HelperFunctions.dateFormatter(g.getTimeStamp()));
            }

            LDS.close();
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
                view = inflater.inflate(R.layout.list_element,parent,false);
            }

            Logs currentLog = listLogs.get(position);
            TextView heading = (TextView) view.findViewById(R.id.heading);
            TextView summary = (TextView) view.findViewById(R.id.summary);
            summary.setVisibility(View.INVISIBLE);
            heading.setText(HelperFunctions.timeFormater(currentLog.getTimeStamp())+"");
            //summary.setText(currentLog.getData());


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
}

