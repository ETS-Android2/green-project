package utt.student.greenfresh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import utt.student.greenfresh.DashboardFragment;
import utt.student.greenfresh.PanelFragment;
import utt.student.greenfresh.FruitsFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import utt.student.greenfresh.classes.Color;
import utt.student.greenfresh.classes.Fruit;


public class MainActivity extends AppCompatActivity {
    // variables
    // private RequestQueue queue;
    // private static String baseURL = "http://192.168.1.65:5000/";
    private BottomNavigationView bottomNavigationView;
    // fragments
    private Fragment dashboardFragment = new DashboardFragment();
    private Fragment panelFragment = new PanelFragment();
    private Fragment fruitsFragment = new FruitsFragment();
    // array List
    // private ArrayList<Fruit> fruits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // if there's no instance saved, by default replace using the dashboard
        if(savedInstanceState == null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, dashboardFragment).commit();
        }

        // Reference the bottom navigation on the main activity
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        // set listener for the menu option selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                if(item.getItemId() == R.id.dashboard){
                    fragmentTransaction.replace(R.id.fragmentContainer, dashboardFragment).commit();
                    item.setChecked(true);

                } else if (item.getItemId() == R.id.panel){
                    fragmentTransaction.replace(R.id.fragmentContainer, panelFragment).commit();
                    item.setChecked(true);

                } else if (item.getItemId() == R.id.fruits){
                    fragmentTransaction.replace(R.id.fragmentContainer, fruitsFragment).commit();
                    item.setChecked(true);

                }

                return true;
            }
        });
    }


    /*
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
                            baseURL+"image/" + fruit.getString("image"),
                            new Color(0,0,0)
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

<<<<<<< HEAD
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
                            color.getInt("r"),
                            color.getInt("g"),
                            color.getInt("b")
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
=======
    }*/


}