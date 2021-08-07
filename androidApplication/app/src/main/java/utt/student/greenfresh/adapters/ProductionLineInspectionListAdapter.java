package utt.student.greenfresh.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.anychart.chart.common.dataentry.BoxDataEntry;
import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.anychart.palettes.RangeColors;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import utt.student.greenfresh.R;
import utt.student.greenfresh.classes.Inspection;
import utt.student.greenfresh.classes.ProductionLine;

public class ProductionLineInspectionListAdapter extends BaseAdapter {
    // attributes
    private Activity activity;
    private ArrayList<ProductionLine> productionLines;
    private static LayoutInflater inflater;


    // constructor
    public ProductionLineInspectionListAdapter(Activity activity, ArrayList<ProductionLine> productionLines) {
        this.activity = activity;
        this.productionLines = productionLines;
        inflater = (LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // methods
    @Override
    public int getCount() { return productionLines.size(); }
    @Override
    public Object getItem(int position) { return position; }
    @Override
    public long getItemId(int position) { return position; }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // create view
        View v = convertView;

        // assign adapter layout
        if(v == null) v = inflater.inflate(R.layout.production_line_inspection_layout, parent, false);

        // reference layout controls
        TextView tvProductionLineName = (TextView)v.findViewById(R.id.tvProductionLineName);
        TextView tvCurrentFruit = (TextView)v.findViewById(R.id.tvCurrentFruitName);
        TextView tvRGBColor = (TextView)v.findViewById(R.id.tvRGBValue);
        TextView tvAccepted = (TextView)v.findViewById(R.id.tvAcceptedResults);
        TextView tvRejected = (TextView)v.findViewById(R.id.tvRejectedResults);
       /* ImageView ivFruitImage = (ImageView)v.findViewById(R.id.ivCurrentFruitImage);
        ImageView ivFruitColor = (ImageView)v.findViewById(R.id.ivInspectionFruitColor);*/

        // read item
        ProductionLine pl = productionLines.get(position);

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
       /* ivFruitColor.setBackgroundColor(Color.rgb(r, g, b));

        Picasso.get().load(pl.getCurrentFruit().getImage()).into(ivFruitImage);*/

        // Inspection Results Chart
        AnyChartView inspectionChart = v.findViewById(R.id.inspectionResultsChart);
        inspectionChart.setProgressBar(v.findViewById(R.id.inspectionResultsProgressBar));
        // create pie chart
        Pie pie = AnyChart.pie();
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(activity, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });
        // set data
        List<DataEntry> data = new ArrayList<>();
        for(int x = 0; x < pl.getInspectionResults().size(); x++) {
            Inspection i = pl.getInspectionResults().get(x);

            if (pl.getCurrentFruit().getCode().equals(i.getFruit().getCode())) {

                Log.d("Testing", pl.getCurrentFruit().getCode() +" "+ i.getFruit().getCode() );
                data.add(new ValueDataEntry(i.getFruit().getName() + " Accepted", i.getAccepted()));
                data.add(new ValueDataEntry(i.getFruit().getName() + " Rejected", i.getRejected()));

                tvAccepted.setText("Accepted: "+i.getAccepted());
                tvRejected.setText("Rejected: "+i.getRejected());

            }


        }
        pie.data(data);
        // set labels and title

        pie.title("Fruit inspection results (in pc)");
        pie.labels().position("outside");
        pie.legend().title().enabled(true);
        pie.legend().title().text("Retail channels").padding(0d, 0d, 10d, 0d);
        pie.legend().position("center-bottom").itemsLayout(LegendLayout.HORIZONTAL).align(Align.CENTER);
        // set pie chart to inspection chart


        inspectionChart.setChart(pie);

        // Weight chart stuff
        /*AnyChartView weightChart = v.findViewById(R.id.chartWeightResults);
        weightChart.setProgressBar(v.findViewById(R.id.weightProgressBar));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair().
        yLabel(true).
        yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Fruit weight - relation");

        cartesian.yAxis(0).title("Number of bottles sold");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<DataEntry>();
        seriesData.add(new CustomDataEntry("1997", 3.6, 2.3,2.7 ));
        seriesData.add(new CustomDataEntry("1998", 4.6, 3.3,4.7 ));
        seriesData.add(new CustomDataEntry("1999", 5.6, 1.3,3.7 ));
        seriesData.add(new CustomDataEntry("2001", 6.6, 4.3,6.7 ));
        seriesData.add(new CustomDataEntry("2002", 7.6, 2.3,5.7 ));
        seriesData.add(new CustomDataEntry("2003", 8.6, 5.3,8.7 ));
        seriesData.add(new CustomDataEntry("2004", 9.6, 7.3,7.7 ));
        seriesData.add(new CustomDataEntry("2005", 10.6, 8.3,9.7 ));



        Set set = Set.instantiate();
        set.data(seriesData);

        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Brandy");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);

        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);


        Line series2 = cartesian.line(series2Mapping);
        series2.name("Whiskey");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);

        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);


        Line series3 = cartesian.line(series3Mapping);
        series3.name("Tequila");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);

        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        weightChart.setChart(cartesian);*/

        return v;
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }

    }

}
