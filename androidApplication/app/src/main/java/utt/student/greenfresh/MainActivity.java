package utt.student.greenfresh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.Volley;


import android.util.Log;
import android.widget.ListView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import utt.student.greenfresh.classes.AreaReadings;
import utt.student.greenfresh.classes.Fruit;
import utt.student.greenfresh.classes.FruitReadings;
import utt.student.greenfresh.classes.ProductionLine;
import utt.student.greenfresh.classes.Status;

public class MainActivity extends AppCompatActivity {
    // variables
    private RequestQueue queue;
    private static String baseURL = "http://192.168.1.65:5000/";
    // array List
    private ArrayList<Fruit> fruits = new ArrayList<>();
    private ArrayList<FruitReadings> fruitReadings = new ArrayList<>();
    private ArrayList<ProductionLine> productionLines = new ArrayList<>();
    private ArrayList<AreaReadings> areaReadings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // request queue
        queue = Volley.newRequestQueue(this);
        getAreaReadings();

        ListView lvList = (ListView)findViewById(R.id.lvList);
        AreaReadingsAdapter adapter = new AreaReadingsAdapter(this.areaReadings, this);
        lvList.setAdapter(adapter);


    }

    private void getFruit(){
        // request URL
        String url = baseURL + "api?action=get_fruits";
        // JSON request
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            // handle response from API
            try {
                for(int i = 0; i < response.length(); i++){
                    JSONObject fruit = response.getJSONObject(i);
                    // adding a fruit to the array list
                    fruits.add(new Fruit(
                            fruit.getString("code"),
                            fruit.getString("name"),
                            fruit.getString("description"),
                            baseURL+"image/" + fruit.getString("image")
                    ));
                    // Log.d("Request", fruit.getString("description"));
                }
                Log.d("Request", "Successful");
            } catch (JSONException e){
                e.printStackTrace();
            }
        }, error -> Log.e("Request Error:", error.toString()));
        // add to request queue
        queue.add(request);

    }

    private void getFruitReadings(){
        // request URL
        String url = baseURL + "api?action=get_readings";
        // JSON request
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            // handle response from API
            try {
                for(int i = 0; i < response.length(); i++){
                    JSONObject data = response.getJSONObject(i);
                    // get status object from data
                    JSONObject status = data.getJSONObject("status");
                    // get reading object from data
                    JSONObject reading = data.getJSONObject("reading");
                    // get color object from reading
                    JSONObject color = reading.getJSONObject("color");
                    // get weight object from reading
                    JSONObject weight = reading.getJSONObject("weight");
                    // adding a fruit to the array list
                    fruitReadings.add(new FruitReadings(
                            new ProductionLine( data.getString("code"),
                                                data.getString("ip"),
                                                data.getString("description"),
                                                new Status( status.getString("lastConnection"),
                                                            status.getString("value"))),
                            reading.getString("date"),
                            new Fruit(reading.getString("fruit")),
                            weight.getDouble("value"),
                            color.getInt("R"),
                            color.getInt("G"),
                            color.getInt("B")
                    ));
                    // Log.d("Date", reading.getString("date"));
                }
                Log.d("Request", "Successful");
            } catch (JSONException e){
                e.printStackTrace();
            }
        }, error -> Log.e("Request Error:", error.toString()));
        // add to request queue
        queue.add(request);
    }

    private void getProductionLines(){
        // request URL
        String url = baseURL + "api?action=get_productionLines";
        // JSON request
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            // handle response from API
            try {
                for(int i = 0; i < response.length(); i++){
                    JSONObject data = response.getJSONObject(i);
                    // get status object from data
                    JSONObject status = data.getJSONObject("status");
                    // adding a production line to the array list
                    productionLines.add(new ProductionLine(
                            data.getString("code"),
                            data.getString("ip"),
                            data.getString("description"),
                            new Status( status.getString("lastConnection"),
                                        status.getString("value"))
                    ));
                    Log.d("Request", data.getString("code"));
                }
                Log.d("Request", "Successful");
            } catch (JSONException e){
                e.printStackTrace();
            }
        }, error -> Log.e("Request Error:", error.toString()));
        // add to request queue
        queue.add(request);
    }

    private void getAreaReadings(){
        // request URL
        String url = baseURL + "api?action=get_enviromentVariables";
        // JSON request
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            // handle response from API
            try {
                for(int i = 0; i < response.length(); i++){
                    JSONObject data = response.getJSONObject(i);
                    // get status object from data
                    JSONObject status = data.getJSONObject("status");
                    // get values object from data
                    JSONObject values = data.getJSONObject("values");
                    // adding area readings to the array list
                    areaReadings.add(new AreaReadings(
                            new ProductionLine( data.getString("code"),
                                                data.getString("ip"),
                                                data.getString("description"),
                                                new Status( status.getString("lastConnection"),
                                                            status.getString("value"))),
                            values.getDouble("temperature"),
                            values.getInt("humidity")
                    ));
                    Log.d("Temperature", values.getString("temperature"));
                }
                Log.d("Request", "Successful");
            } catch (JSONException e){
                e.printStackTrace();
            }
        }, error -> Log.e("Request Error:", error.toString()));
        // add to request queue
        queue.add(request);
    }

}