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

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.charts.CircularGauge;
import com.anychart.core.axes.Circular;
import com.anychart.core.gauge.pointers.Bar;
import com.anychart.enums.Anchor;
import com.anychart.graphics.vector.Fill;
import com.anychart.graphics.vector.SolidFill;
import com.anychart.graphics.vector.text.HAlign;
import com.anychart.graphics.vector.text.VAlign;

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
        AnyChartView chTemperature = v.findViewById(R.id.any_chart_temperature);
        AnyChartView chHumidity = v.findViewById(R.id.any_chart_humidity);

        chHumidity.setProgressBar(v.findViewById(R.id.progress_barHumidity));
        chTemperature.setProgressBar(v.findViewById(R.id.progress_barTemperature));

        AreaReadings ar = this.areaReadings.get(position);

        tvName.setText(ar.getProductionLine().getDescription());
        tvIp.setText(ar.getProductionLine().getIp());
        tvLastUpdate.setText(ar.getProductionLine().getStatus().getLastConnection());
        tvStatus.setText(ar.getProductionLine().getStatus().getValue());

        if (ar.getProductionLine().getStatus().getValue() != "Online") ivStatus.getResources().getDrawable(R.drawable.offline);

        // Any charts stuff

        CircularGauge circularGauge = AnyChart.circular();
        circularGauge.data(new SingleValueDataSet(new String[] {String.valueOf(ar.getTemperature())}));
        circularGauge.
                fill("#fff"). //background color
                stroke(null). // stroke
                padding(0d, 0d, 0d,0d). // padding
                margin(100d, 100d, 100d, 100d); // the margin

        circularGauge.startAngle(0d); // initial angle for the chart
        circularGauge.sweepAngle(270d); // the end of the angle

        Circular xAxis = circularGauge.axis(0).
                radius(100d). // The size of the bar?
                width(1d). // the width
                fill((Fill)null); // the fill?

        xAxis.scale().
                minimum(0d). // the range?
                maximum(100d); // the range

        xAxis.ticks("{ interval: 1} ") // the gap between the gauges, if there more than one?
               .minorTicks("{ interval: 1} ");

        xAxis.labels().enabled(false);
        xAxis.ticks().enabled(false);
        xAxis.minorTicks().enabled(false);

        circularGauge.label(0d). // the title into the gauge?
                text("Temperature, <span style=\"\">"+String.valueOf(ar.getTemperature())+"</span>").
                useHtml(true).
                hAlign(HAlign.CENTER). // allocation of the text
                vAlign(VAlign.MIDDLE);

        circularGauge.label(0d). // what the heck is this?
                anchor(Anchor.CENTER).
                padding(0d, 10d, 0d, 0d). // what the heck is this?
                height(17d / 2d + "%").
                offsetY(100d + "%").
                offsetX(0d); // what the heck is this?

        // color bar
        Bar bar0 = circularGauge.bar(0d);
        bar0.dataIndex(0d);
        bar0.radius(100d);
        bar0.width(17d);
        bar0.fill(new SolidFill("#64b5f6", 1d)); // the color of the bar
        bar0.stroke(null);
        bar0.zIndex(5d); // what the heck is this

        // shadow bar

        Bar bar100 = circularGauge.bar(100);
        bar100.dataIndex(5d);
        bar100.radius(100d);
        bar100.width(17d);
        bar100.fill(new SolidFill("#F5F4F4", 1d)); // the color of the bar
        bar100.stroke("1 #e5e4e4");
        bar100.zIndex(4d); // what the heck is this

        circularGauge.margin(50d, 50d, 50d, 50d);

        chTemperature.setChart(circularGauge);
        return v;    }
}
