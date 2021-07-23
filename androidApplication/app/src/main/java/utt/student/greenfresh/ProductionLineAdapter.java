package utt.student.greenfresh;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;

import utt.student.greenfresh.classes.ProductionLine;

public class ProductionLineAdapter extends BaseExpandableListAdapter {
    // attributes
    private ArrayList<ProductionLine> productionLines;
    private Context context;

    // getters and setters
    public ArrayList<ProductionLine> getProductionLines() { return productionLines; }
    public void setProductionLines(ArrayList<ProductionLine> productionLines) { this.productionLines = productionLines; }
    public Context getContext() { return context; }
    public void setContext(Context context) { this.context = context; }

    // constructor
    public ProductionLineAdapter(ArrayList<ProductionLine> productionLines, Context context) {
        this.productionLines = productionLines;
        this.context = context;
    }

    // methods
    @Override
    public int getGroupCount() { return this.productionLines.size(); }
    @Override
    public int getChildrenCount(int groupPosition) { return 0; }
    @Override
    public Object getGroup(int groupPosition) { return null; }
    @Override
    public Object getChild(int groupPosition, int childPosition) { return null; }
    @Override
    public long getGroupId(int groupPosition) { return 0; }
    @Override
    public long getChildId(int groupPosition, int childPosition) { return 0; }
    @Override
    public boolean hasStableIds() { return false; }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) { return null; }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) { return null; }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return false; }
}
