package utt.student.greenfresh;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import utt.student.greenfresh.classes.Fruit;

/*
public class FruitListAdapter extends BaseAdapter {


    private Activity activity;
    private ArrayList<Fruit> data;
    private LayoutInflater inflater;

    public FruitListAdapter(ArrayList<Fruit> data, Activity activity) {
        this.data = data;
        this.activity = activity;
        this.inflater = (LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
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
        // View
        View v = convertView;

        // Inflater stuff
        if (v == null) v = inflater.inflate(R.layout.inspection_result_layout, null);

        // Reference Layout

        ImageView ivIcon = (ImageView)v.findViewById(R.id.ivFruitIcon);
        TextView tvRejected = (TextView)v.findViewById(R.id.tvRejectedFruitsValue);
        TextView tvAccepted = (TextView)v.findViewById(R.id.tvAcceptedFruitsValue);
        TextView tvName = (TextView)v.findViewById(R.id.tvFruitName);
        AnyChartView chart = v.findViewById(R.id.chRadial);

        AnyChartView gaugeChart = v.findViewById(R.id.chGauge);

        // GAUGE


        // PIE!!!!

        Fruit f = this.data.get(position);

        // any charts stuff
        Pie pie = AnyChart.pie(); // pie data
        ArrayList <DataEntry> entries = new ArrayList<DataEntry>(); // array list with the data

        // data
        entries.add(new ValueDataEntry("Accepted",14 ));
        entries.add(new ValueDataEntry("Rejected",8 ));

        // binding data with the chart

        pie.setData(entries);

        chart.setChart(pie);
        Picasso.get().load(f.getImage()).into(ivIcon);
        tvAccepted.setText("14");
        tvRejected.setText("8");
        tvName.setText(f.getName());



        return v;


    }
}


 */