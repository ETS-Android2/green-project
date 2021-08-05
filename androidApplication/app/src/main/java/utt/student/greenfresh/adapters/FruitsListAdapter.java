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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Queue;

import utt.student.greenfresh.R;
import utt.student.greenfresh.classes.Fruit;

public class FruitsListAdapter extends BaseAdapter {
    // attributes
    private Activity activity;
    private ArrayList<Fruit> fruits;
    private static LayoutInflater inflater;

    // constructors
    public FruitsListAdapter(Activity activity, ArrayList<Fruit> fruits) {
        this.activity = activity;
        this.fruits = fruits;
        inflater = (LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // methods
    @Override
    public int getCount() { return fruits.size(); }
    @Override
    public Object getItem(int position) { return position; }
    @Override
    public long getItemId(int position) { return position; }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // create view
        View v = convertView;

        // assign adapter layout
        if(v == null) v = inflater.inflate(R.layout.fruit_card, parent, false);

        // reference layout controls
        ImageView ivFruit = (ImageView)v.findViewById(R.id.ivFruit);
        TextView tvFruitName = (TextView)v.findViewById(R.id.tvFruitName);
        TextView tvFruitDescription = (TextView)v.findViewById(R.id.tvFruitDescription);
        ImageView ivFruitColor = (ImageView)v.findViewById(R.id.ivFruitColor);
        TextView tvFruitRed = (TextView) v.findViewById(R.id.tvFruitRedValue);
        TextView tvFruitGreen = (TextView)v.findViewById(R.id.tvFruitGreenValue);
        TextView tvFruitBlue = (TextView)v.findViewById(R.id.tvFruitBlueValue);

        // read item
        Fruit f = this.fruits.get(position);

        // bind data controls
        tvFruitName.setText(f.getName());
        tvFruitDescription.setText(f.getDescription());
        tvFruitRed.setText(Integer.toString(f.getColor().getRed()));
        tvFruitGreen.setText(Integer.toString(f.getColor().getGreen()));
        tvFruitBlue.setText(Integer.toString(f.getColor().getBlue()));
        // getting RGB colors
        int r = f.getColor().getRed();
        int g = f.getColor().getGreen();
        int b = f.getColor().getBlue();
        ivFruitColor.setBackgroundColor(Color.rgb(r, g, b));
        Picasso.get().load(f.getImage()).into(ivFruit);

        return v;
    }
}
