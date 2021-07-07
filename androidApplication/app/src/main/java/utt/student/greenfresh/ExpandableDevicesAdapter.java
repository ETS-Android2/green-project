package utt.student.greenfresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;

import utt.student.greenfresh.classes.Device;
import utt.student.greenfresh.classes.Sensor;


public class ExpandableDevicesAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Device> devices;

    public ExpandableDevicesAdapter(Context context, ArrayList<Device> devices) {
        this.context = context;
        this.devices = devices;
    }

    @Override
    public int getGroupCount() { return this.devices.size(); }

    @Override
    public int getChildrenCount(int groupPosition) { return this.devices.get(groupPosition).getSensors().size(); }

    @Override
    public Object getGroup(int groupPosition) { return this.devices.get(groupPosition); }

    @Override
    public Object getChild(int groupPosition, int childPosition) { return this.devices.get(groupPosition).getSensors().get(childPosition); }

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

        Device d = this.devices.get(groupPosition);

        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;

        LayoutInflater inflater = LayoutInflater.from(this.context);

        //if (v == null) v =inflater.inflate(R.id.SOMETHING_HERE);

        Sensor s = this.devices.get(groupPosition).getSensors().get(childPosition);

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
