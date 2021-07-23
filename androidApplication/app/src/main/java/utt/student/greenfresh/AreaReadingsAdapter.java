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

import com.anychart.anychart.AnyChartView;

import java.util.ArrayList;

import utt.student.greenfresh.classes.AreaReadings;
import utt.student.greenfresh.classes.ProductionLine;

public class AreaReadingsAdapter extends BaseAdapter {


    // attributes
    private ArrayList<AreaReadings> areaReadings;
    private Activity activity;
    private LayoutInflater inflater;

    // constructor
    public AreaReadingsAdapter(ArrayList<AreaReadings> areaReadings, Activity activity) {
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
        if (v == null) inflater.inflate(R.layout.production_line_panel_layout, null);

        // binding the element views

        TextView tvName = (TextView)v.findViewById(R.id.tvProductionLine);
        TextView tvIp = (TextView)v.findViewById(R.id.tvIpAddress);
        TextView tvLastUpdate = (TextView)v.findViewById(R.id.tvLastUpdate);
        TextView tvStatus = (TextView)v.findViewById(R.id.tvStatus);

        ImageView ivStatus = (ImageView)v.findViewById(R.id.ivStatusIcon);
        AnyChartView chTemperature ;

        AreaReadings ar = this.areaReadings.get(position);

        tvName.setText(ar.getProductionLine().getDescription());
        tvIp.setText(ar.getProductionLine().getIp());
        tvLastUpdate.setText(ar.getProductionLine().getStatus().getLastConnection());
        tvStatus.setText(ar.getProductionLine().getStatus().getValue());



        if (ar.getProductionLine().getStatus().getValue() != "Online") ivStatus.getResources().getDrawable(R.drawable.offline);


        return v;
    }
}
