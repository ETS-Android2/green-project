package utt.student.greenfresh;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import utt.student.greenfresh.adapters.FruitsListAdapter;
import utt.student.greenfresh.adapters.ProductionLineInspectionListAdapter;
import utt.student.greenfresh.classes.Color;
import utt.student.greenfresh.classes.Fruit;
import utt.student.greenfresh.classes.Inspection;
import utt.student.greenfresh.classes.ProductionLine;
import utt.student.greenfresh.classes.Status;


public class DashboardFragment extends Fragment {
    // attributes
    private String baseURL = "http://192.168.1.66:5000/";
    private RequestQueue queue;
    private ArrayList<ProductionLine> productionLines = new ArrayList<>();

    // Constructor
    public DashboardFragment() {
        // Required empty public constructor
    }

    // Methods
    public static DashboardFragment newInstance(String param1, String param2){
        DashboardFragment dashboard = new DashboardFragment();
        return dashboard;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());      // queue request
        productionLines.clear();            // clear array list
        getData(view);                      // get date from the API
    }

    public void getData(View view){
        // request URL
        String url = baseURL + "get-all";
        // JSON request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            // handle response from API
            try {
                JSONArray data = response.getJSONArray("productionLines");
                for(int i = 0; i < data.length(); i++){
                    JSONObject pl = data.getJSONObject(i);
                    // getting the status of the production line
                    JSONObject status = pl.getJSONObject("status");
                    // getting the current fruit
                    JSONObject currentFruit = pl.getJSONObject("currentFruit");
                    // getting color from the current fruit
                    JSONObject cfColor = currentFruit.getJSONObject("requirements");
                    // create a new production line based on the JSON object
                    ProductionLine productionLine = new ProductionLine( pl.getString("code"),
                            pl.getString("description"),
                            pl.getString("ip"),
                            new Status( status.getString("lastConnection"),
                                        status.getBoolean("res"),
                                        status.getString("value")));
                    // set current fruit
                    productionLine.setCurrentFruit(new Fruit(currentFruit.getString("code"),
                                                             currentFruit.getString("name"),
                                                             currentFruit.getString("description"),
                                                             currentFruit.getString("image"),
                                                             new Color(cfColor.getInt("R"),
                                                                        cfColor.getInt("G"),
                                                                        cfColor.getInt("B"))));
                    // getting the inspection results
                    JSONArray inspectionResults = pl.getJSONArray("inspectionResults");
                    for(int x = 0; x < inspectionResults.length(); x++){
                        JSONObject results = inspectionResults.getJSONObject(x);
                        // getting the fruit
                        JSONObject fruit = results.getJSONObject("fruit");
                        // getting the fruit's color
                        JSONObject color = fruit.getJSONObject("requirements");
                        // getting the values
                        JSONObject values = results.getJSONObject("results");
                        // create and add a new inspection
                        Inspection inspection = new Inspection( results.getString("date"),
                                                                new Fruit(  fruit.getString("code"),
                                                                            fruit.getString("name"),
                                                                            fruit.getString("description"),
                                                                            fruit.getString("image"),
                                                                            new Color(  color.getInt("R"),
                                                                                        color.getInt("G"),
                                                                                        color.getInt("B"))),
                                                                values.getInt("accepted"),
                                                                values.getInt("rejected"));
                        // add inspection array
                        productionLine.addInspectionResult(inspection);
                    }
                    // add production line to the array list
                    productionLines.add(productionLine);
                }
                Log.d("Request", "Successful");

                // bind production lines to list view
                ListView lvProductionLine = (ListView)view.findViewById(R.id.lvProductionLineInspection);
                ListAdapter adapter = new ProductionLineInspectionListAdapter(getActivity(), productionLines);
                lvProductionLine.setAdapter(adapter);

            } catch (JSONException e){
                e.printStackTrace();
            }
        }, error -> Log.e("Request Error:", error.toString()));
        // add to request queue
        queue.add(request);
    }
}