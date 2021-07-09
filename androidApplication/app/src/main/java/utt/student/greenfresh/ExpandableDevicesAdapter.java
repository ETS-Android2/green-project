package utt.student.greenfresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;

import utt.student.greenfresh.classes.ProductionLine;
import utt.student.greenfresh.classes.Sensor;


public class ExpandableDevicesAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<ProductionLine> productionLines;

    public ExpandableDevicesAdapter(Context context, ArrayList<ProductionLine> productionLines) {
        this.context = context;
        this.productionLines = productionLines;
    }

    @Override
    public int getGroupCount() { return this.productionLines.size(); }

    @Override
    public int getChildrenCount(int groupPosition) { return this.productionLines.get(groupPosition).getSensors().size(); }

    @Override
    public Object getGroup(int groupPosition) { return this.productionLines.get(groupPosition); }

    @Override
    public Object getChild(int groupPosition, int childPosition) { return this.productionLines.get(groupPosition).getSensors().get(childPosition); }

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

        //if (v == null) v =inflater.inflate(R.id.SOMETHING_HERE);

        ProductionLine pl = this.productionLines.get(groupPosition);

        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;

        LayoutInflater inflater = LayoutInflater.from(this.context);

        //if (v == null) v =inflater.inflate(R.id.SOMETHING_HERE);

        Sensor s = this.productionLines.get(groupPosition).getSensors().get(childPosition);

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
