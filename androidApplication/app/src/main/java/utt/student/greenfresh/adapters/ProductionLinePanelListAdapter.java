package utt.student.greenfresh.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.media.RatingCompat;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;

import java.util.ArrayList;

import utt.student.greenfresh.R;
import utt.student.greenfresh.classes.AreaSensor;
import utt.student.greenfresh.classes.ProductionLine;

public class ProductionLinePanelListAdapter extends BaseAdapter {
    // attributes
    private Activity activity;
    private ArrayList<ProductionLine> productionLines;
    private static LayoutInflater inflater;

    // constructor
    public ProductionLinePanelListAdapter(Activity activity, ArrayList<ProductionLine> productionLines) {
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
        // view
        View v = convertView;

        // assign adapter layout
        if(v == null) v = inflater.inflate(R.layout.production_line_panel_layout, parent, false);

        // reference layout controls
        RelativeLayout panel = (RelativeLayout)v.findViewById(R.id.rlProductionLinePanel);
        RelativeLayout moreContent = (RelativeLayout)v.findViewById(R.id.rlMoreContent);
        ImageView moreContentIcon = (ImageView)v.findViewById(R.id.ivArrowIcon);
        TextView tvProductionLine = (TextView)v.findViewById(R.id.tvProductionLineName);
        TextView tvIpAddress = (TextView)v.findViewById(R.id.tvIpAddress);
        TextView tvLastUpdate = (TextView)v.findViewById(R.id.tvLastUpdate);
        ImageView ivStatus = (ImageView)v.findViewById(R.id.ivStatusIcon);
        TextView tvStatus = (TextView)v.findViewById(R.id.tvStatus);

        // read item
        ProductionLine pl = this.productionLines.get(position);


        // bind data controls
        tvProductionLine.setText(pl.getName());
        tvIpAddress.setText(pl.getIp());
        tvLastUpdate.setText(pl.getStatus().getLastConnection());
        tvStatus.setText(pl.getStatus().getName());

        // set listener to the panel
        moreContentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moreContent.getVisibility() == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(panel, new AutoTransition());
                    moreContent.setVisibility(View.GONE);
                    moreContentIcon.setImageResource(R.drawable.arrow_down);
                }else{
                    TransitionManager.beginDelayedTransition(panel, new AutoTransition());
                    moreContent.setVisibility(View.VISIBLE);
                    moreContentIcon.setImageResource(R.drawable.arrow_up);
                }
            }
        });

        if(pl.getStatus().getValue() == true){ ivStatus.setImageResource(R.drawable.online); }
        else{ ivStatus.setImageResource(R.drawable.offline); }

        // adding the temperature gauge
        HalfGauge temperatureGauge = (HalfGauge)v.findViewById(R.id.temperatureGauge);
        // get ranges to the sensor
        AreaSensor temperatureSensor = pl.getAreaSensors().get(0);
        for(int x = 0; x < temperatureSensor.getRanges().size(); x++){
            // establish ranges
            Range range = new Range();
            range.setColor(Color.parseColor(temperatureSensor.getRanges().get(x).getColor()));
            range.setFrom(temperatureSensor.getRanges().get(x).getMinimum());
            range.setTo(temperatureSensor.getRanges().get(x).getMaximum());
            // add range
            temperatureGauge.addRange(range);
        }
        // set Values
        temperatureGauge.setMinValue(0);
        temperatureGauge.setMaxValue(50);
        temperatureGauge.setValue(temperatureSensor.getCurrent());
        // set colors
        temperatureGauge.setNeedleColor(Color.DKGRAY);
        temperatureGauge.setValueColor(Color.parseColor("#616161"));
        temperatureGauge.setMinValueTextColor(Color.BLUE);
        temperatureGauge.setMaxValueTextColor(Color.RED);

        // adding the humidity gauge
        ArcGauge humidityGauge = (ArcGauge)v.findViewById(R.id.humidityGauge);
        // get ranges to the sensor
        AreaSensor humiditySensor = pl.getAreaSensors().get(1);
        for(int x = 0; x < humiditySensor.getRanges().size(); x++){
            // establish ranges
            Range range = new Range();
            range.setColor(Color.parseColor(humiditySensor.getRanges().get(x).getColor()));
            range.setFrom(humiditySensor.getRanges().get(x).getMinimum());
            range.setTo(humiditySensor.getRanges().get(x).getMaximum());
            // add range
            humidityGauge.addRange(range);
        }
        // set values
        humidityGauge.setMinValue(0);
        humidityGauge.setMaxValue(100);
        humidityGauge.setValue(humiditySensor.getCurrent());
        // set colors
        humidityGauge.isUseRangeBGColor();
        humidityGauge.setValueColor(Color.parseColor("#616161"));

        return v;
    }
}
