package utt.student.greenfresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import utt.student.greenfresh.classes.InspectionResult;
import utt.student.greenfresh.classes.ProductionLine;
import utt.student.greenfresh.classes.Sensor;


public class ProductionLine_InspectionResultAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<ProductionLine> productionLines;

    public ProductionLine_InspectionResultAdapter(Context context, ArrayList<ProductionLine> productionLines) {
        this.context = context;
        this.productionLines = productionLines;
    }

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
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;

        LayoutInflater inflater = LayoutInflater.from(this.context);

        if (v == null) v =inflater.inflate(R.layout.production_line_layout, null);

        ProductionLine pl = this.productionLines.get(groupPosition);

        TextView tvProductionLineName = (TextView)v.findViewById(R.id.tvProductionLine);
        TextView tvIpAddress = (TextView)v.findViewById(R.id.tvIpAddress);
        TextView tvLastUpdate = (TextView)v.findViewById(R.id.tvLastUpdate);
        TextView tvStatus = (TextView)v.findViewById(R.id.tvStatus);

        ImageView ivStatusIcon = (ImageView)v.findViewById(R.id.ivStatusIcon);


        SimpleDateFormat fs = new SimpleDateFormat("E 'at' hh:mm a");

        tvProductionLineName.setText(pl.getName());
        tvIpAddress.setText(pl.getIpAddress());
        tvLastUpdate.setText(fs.format(pl.getLastConnection()));
        tvStatus.setText(pl.getStatus().getName());
        ivStatusIcon.setImageDrawable(pl.getStatus().getIcon());

        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;

        LayoutInflater inflater = LayoutInflater.from(this.context);

        //if (v == null) v =inflater.inflate(R.id.SOMETHING_HERE);

        InspectionResult ir = this.productionLines.get(groupPosition).getInspectionResults().get(childPosition);



        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
