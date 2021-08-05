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
    private RequestQueue queue;
    private String baseURL = "http://192.168.1.65:5000/";

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
        ImageView ivFruitImage = (ImageView)v.findViewById(R.id.ivCurrentFruitImage);
        ImageView ivFruitColor = (ImageView)v.findViewById(R.id.ivFruitColor);

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
        ivFruitColor.setBackgroundColor(Color.rgb(r, g, b));
        // new request
        queue = Volley.newRequestQueue(this.activity);
        // image request
        String url = baseURL + "image/" + pl.getCurrentFruit().getImage();
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Log.d("Image Request:", "Successful");
                ivFruitImage.setImageBitmap(response);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Request Error:", error.toString());
                ivFruitImage.setImageResource(R.drawable.fruits);
            }
        });
        // add queue request
        queue.add(request);

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
            data.add(new ValueDataEntry(i.getFruit().getName() + " Accepted", i.getAccepted()));
            data.add(new ValueDataEntry(i.getFruit().getName() + " Rejected", i.getRejected()));
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



        return v;
    }
}
