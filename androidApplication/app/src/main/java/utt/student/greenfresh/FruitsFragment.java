package utt.student.greenfresh;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.jar.JarException;

import utt.student.greenfresh.adapters.FruitsListAdapter;
import utt.student.greenfresh.classes.AreaSensor;
import utt.student.greenfresh.classes.Color;
import utt.student.greenfresh.classes.Fruit;
import utt.student.greenfresh.classes.ProductionLine;
import utt.student.greenfresh.classes.Range;
import utt.student.greenfresh.classes.Status;
import utt.student.greenfresh.classes.Type;
import utt.student.greenfresh.classes.UnitOfMeasurement;


public class FruitsFragment extends Fragment {
    // attributes
    private String baseURL = "http://192.168.1.65:5000/";
    private RequestQueue queue;
    private ArrayList<Fruit> fruits = new ArrayList<>();

    // Constructor
    public FruitsFragment() {
        // Required empty public constructor
    }

    // Methods
    public static FruitsFragment newInstance(String param1, String param2) {
        FruitsFragment fragment = new FruitsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fruits, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());      // queue request
        fruits.clear();     // clear array list
        getData(view);          // get data from the API
    }

    public void getData(View view){
        // request URL
        String url = baseURL + "get-fruits";
        // JSON request
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            // handle response from API
            try {
                JSONArray data = response;
                for(int x = 0; x < data.length(); x++){
                    // getting the fruit
                    JSONObject fruit = response.getJSONObject(x);
                    // getting color of the fruit
                    JSONObject color = fruit.getJSONObject("requirements");
                    // add and create a new fruit
                    fruits.add(new Fruit(fruit.getString("code"),
                                         fruit.getString("name"),
                                         fruit.getString("description"),
                                         fruit.getString("image"),
                                         new Color(color.getInt("R"),
                                                   color.getInt("G"),
                                                   color.getInt("B"))));
                }
                Log.d("Request", "Successful");

                // bind production lines to list view
                ListView lvFruits = (ListView)view.findViewById(R.id.lvFruitsList);
                ListAdapter adapter = new FruitsListAdapter(getActivity(), fruits);
                lvFruits.setAdapter(adapter);

            } catch (Exception e){
                e.printStackTrace();
            }
        }, error -> Log.e("Request Error:", error.toString()));
        // add to request queue
        queue.add(request);
    }
}