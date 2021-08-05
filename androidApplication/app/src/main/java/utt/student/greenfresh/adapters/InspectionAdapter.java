package utt.student.greenfresh.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import utt.student.greenfresh.R;
import utt.student.greenfresh.classes.Inspection;
import utt.student.greenfresh.classes.ProductionLine;

public class InspectionAdapter extends BaseExpandableListAdapter {
    // attributes
    private ArrayList<ProductionLine> productionLines;
    private Context context;
    private Activity activity;
    private RequestQueue queue;
    private String baseURL = "http://192.168.1.65:5000/";

    // constructor
    public InspectionAdapter(ArrayList<ProductionLine> productionLines, Context context, Activity activity) {
        this.productionLines = productionLines;
        this.context = context;
        this.activity = activity;
    }

    // override instance methods
    @Override
    public int getGroupCount() { return this.productionLines.size(); }
    @Override
    public int getChildrenCount(int groupPosition) { return this.productionLines.get(groupPosition).getInspectionResults().size(); }
    @Override
    public Object getGroup(int groupPosition) { return this.productionLines.get(groupPosition); }
    @Override
    public Object getChild(int groupPosition, int childPosition) { return this.productionLines.get(groupPosition).getInspectionResults().get(childPosition); }
    @Override
    public long getGroupId(int groupPosition) { return groupPosition; }
    @Override
    public long getChildId(int groupPosition, int childPosition) { return childPosition; }
    @Override
    public boolean hasStableIds() { return false; }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = LayoutInflater.from(this.context);

        // assign adapter layout
        if(v == null) v = inflater.inflate(R.layout.production_line_inspection_layout, parent, false);

        // reference layout controls
        TextView tvProductionLineName = (TextView)v.findViewById(R.id.tvProductionLineName);
        TextView tvCurrentFruit = (TextView)v.findViewById(R.id.tvCurrentFruitName);
        TextView tvRGBColor = (TextView)v.findViewById(R.id.tvRGBValue);
        ImageView ivFruitImage = (ImageView)v.findViewById(R.id.ivCurrentFruitImage);
        ImageView ivFruitColor = (ImageView)v.findViewById(R.id.ivFruitColor);

        // read item
        ProductionLine pl = productionLines.get(groupPosition);

        // bind data to controls
        tvProductionLineName.setText(pl.getName());
        tvCurrentFruit.setText(pl.getCurrentFruit().getName());
        tvRGBColor.setText( Integer.toString(pl.getCurrentFruit().getColor().getRed()) + ", " +
                            Integer.toString(pl.getCurrentFruit().getColor().getGreen()) + ", " +
                            Integer.toString(pl.getCurrentFruit().getColor().getBlue()));
        // getting RGB colors
        int r = pl.getCurrentFruit().getColor().getRed();
        int g = pl.getCurrentFruit().getColor().getGreen();
        int b = pl.getCurrentFruit().getColor().getBlue();
        ivFruitColor.setBackgroundColor(Color.rgb(r, g, b));
        // new request
        Picasso.get().load(pl.getCurrentFruit().getImage()).into(ivFruitImage);
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = LayoutInflater.from(this.context);

        // assign adapter layout
        if(v == null) v = inflater.inflate(R.layout.fruit_inspection_layout, parent, false);

        // reference layout controls
        ImageView ivFruitImage = (ImageView)v.findViewById(R.id.ivFruitImage);
        TextView tvFruitName = (TextView)v.findViewById(R.id.tvFruitName);

        // read item
        Inspection i = productionLines.get(groupPosition).getInspectionResults().get(childPosition);

        // bind data to controls
        tvFruitName.setText(i.getFruit().getName());
        // new request


        // Testing chart
        AnyChartView anyChartView = v.findViewById(R.id.inspectionResultsChart);
        anyChartView.setProgressBar(v.findViewById(R.id.inspectionResultsProgressBar));

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(activity, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Apples", 6371664));
        data.add(new ValueDataEntry("Pears", 789622));
        data.add(new ValueDataEntry("Bananas", 7216301));
        data.add(new ValueDataEntry("Grapes", 1486621));
        data.add(new ValueDataEntry("Oranges", 1200000));

        pie.data(data);

        pie.title("Fruits imported in 2015 (in kg)");

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Retail channels")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return false; }
}
