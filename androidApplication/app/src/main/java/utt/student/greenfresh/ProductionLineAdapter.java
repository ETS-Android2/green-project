package utt.student.greenfresh;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import utt.student.greenfresh.classes.AreaReadings;
import utt.student.greenfresh.classes.ProductionLine;

public class ProductionLineAdapter extends BaseAdapter {


    // attributes
    private ArrayList<AreaReadings> areaReadings;
    private Activity activity;
    private LayoutInflater inflater;

    // constructor
    public ProductionLineAdapter(ArrayList<AreaReadings> areaReadings, Activity activity) {
        this.areaReadings = areaReadings;
        this.activity = activity;
        this.inflater = (LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Methods
    @Override
    public int getCount() {
        return this.areaReadings.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // the view
        View v = convertView;

        // inflater stuff
        if (v == null) inflater.inflate(R.layout.production_line_panel, null);

        // binding the element views

        TextView tvIp = (TextView)v.findViewById(R.id.tvIpAddress);
        TextView tvDate = (TextView)v.findViewById(R.id.tvLastUpdate);
        TextView tvStatus = (TextView)v.findViewById(R.id.tvStatus);

        ImageView ivStatus = (ImageView)v.findViewById(R.id.ivStatusIcon);

        // 

        return v;
    }
}
