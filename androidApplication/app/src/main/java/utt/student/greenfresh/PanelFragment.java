package utt.student.greenfresh;

import android.app.VoiceInteractor;
import android.graphics.Color;
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
import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.HalfGauge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import utt.student.greenfresh.adapters.ProductionLinePanelListAdapter;
import utt.student.greenfresh.classes.AreaSensor;
import utt.student.greenfresh.classes.ProductionLine;
import utt.student.greenfresh.classes.Range;
import utt.student.greenfresh.classes.Status;
import utt.student.greenfresh.classes.Type;
import utt.student.greenfresh.classes.UnitOfMeasurement;

public class PanelFragment extends Fragment {
    // attributes
    private String baseURL = "http://192.168.1.66:5000/";
    private RequestQueue queue;
    private ArrayList<ProductionLine> productionLines = new ArrayList<>();

    // Constructor
    public PanelFragment() {
        // Required empty public constructor
    }

    // Methods
    public static PanelFragment newInstance(String param1, String param2) {
        PanelFragment fragment = new PanelFragment();
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
        return inflater.inflate(R.layout.fragment_panel, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());      // queue request
        productionLines.clear();    // clear array list
        getData(view);      // get data from the API
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
                    // create a new production line based on the JSON object
                    ProductionLine productionLine = new ProductionLine( pl.getString("code"),
                                                                        pl.getString("description"),
                                                                        pl.getString("ip"),
                                                                        new Status( status.getString("lastConnection"),
                                                                                    status.getBoolean("res"),
                                                                                    status.getString("value")));
                    // getting the area sensors from area readings
                    JSONArray as = pl.getJSONArray("areaReadings");
                    // array list for the area sensors
                    ArrayList<AreaSensor> areaSensors = new ArrayList<>();
                    for(int x = 0; x < as.length(); x++){
                        JSONObject sensor = as.getJSONObject(x);
                        // getting sensor type
                        JSONObject type = sensor.getJSONObject("type");
                        // getting the unit of measurement
                        JSONObject unitOfMeasurement = type.getJSONObject("unitOfMeasurement");
                        // create a new area sensor
                        AreaSensor areaSensor = new AreaSensor( sensor.getDouble("current"),
                                                        new Type(type.getString("name"),
                                                                 type.getString("icon"),
                                                                 new UnitOfMeasurement( unitOfMeasurement.getString("name"),
                                                                                        unitOfMeasurement.getString("symbol"))));
                        // getting the ranges
                        JSONArray rs = sensor.getJSONArray("ranges");
                        for(int y = 0; y < rs.length(); y++){
                            JSONObject r = rs.getJSONObject(y);
                            // getting values
                            JSONObject values = r.getJSONObject("values");
                            // create and add new range
                            Range range = new Range(r.getString("name"),
                                                    r.getString("color"),
                                                    values.getInt("maximum"),
                                                    values.getInt("minimum"));
                            // add ranges to the area sensor
                            areaSensor.addRange(range);
                        }
                        // add area sensor to the production line
                        productionLine.addAreaSensor(areaSensor);
                    }

                    // add production line to the array list
                    productionLines.add(productionLine);
                }
                Log.d("Request", "Successful");

                // bind production lines to list view
                ListView lvProductionLines = (ListView)view.findViewById(R.id.lvProductionLinePanel);
                ProductionLinePanelListAdapter adapter = new ProductionLinePanelListAdapter(getActivity(), productionLines);
                lvProductionLines.setAdapter(adapter);

            } catch (JSONException e){
                e.printStackTrace();
            }
        }, error -> Log.e("Request Error:", error.toString()));
        // add to request queue
        queue.add(request);
    }
}