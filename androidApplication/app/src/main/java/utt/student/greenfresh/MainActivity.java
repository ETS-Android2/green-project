package utt.student.greenfresh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Date;

import utt.student.greenfresh.classes.InspectionResult;
import utt.student.greenfresh.classes.ProductionLine;
import utt.student.greenfresh.classes.Sensor;
import utt.student.greenfresh.classes.Status;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Status Objects

        Status online = new Status("Online", getResources().getDrawable(R.drawable.online));
        Status offline = new Status("Offline", getResources().getDrawable(R.drawable.offline));

        // ArrayList InspectionResults

        ArrayList<InspectionResult> inspectionResults = new ArrayList<InspectionResult>();

        InspectionResult appleResults = new InspectionResult(
            1, "Apple", getResources().getDrawable(R.drawable.apple), 13, 15
        );

        InspectionResult bananaResults = new InspectionResult(
            2, "Banana", getResources().getDrawable(R.drawable.bananas), 7, 21
        );

        InspectionResult orangeResults = new InspectionResult(
            3, "Orange", getResources().getDrawable(R.drawable.orange), 3, 11
        );

        inspectionResults.add(appleResults);
        inspectionResults.add(bananaResults);
        inspectionResults.add(orangeResults);


        // ArrayList Sensors

        ArrayList<Sensor> sensors = new ArrayList<Sensor>();

        Sensor humidity = new Sensor(
            "Humidity", getResources().getDrawable(R.drawable.humidity), "33%"
        );

        Sensor temperature = new Sensor(
            "Temperature", getResources().getDrawable(R.drawable.temperature), "27°C"
        );

        sensors.add(humidity);
        sensors.add(temperature);

        // ArrayList ProductionLine
        ArrayList<ProductionLine> productionLines = new ArrayList<ProductionLine>();

        productionLines.add(
            new ProductionLine(
                "1",
                "Production Line #1",
                "192.168.1.5",
                new Date(),
                sensors,
                inspectionResults,
                online
            )
        );

        productionLines.add(
            new ProductionLine(
                "2",
                "Production Line #2",
                "192.168.1.3",
                new Date(),
                sensors,
                inspectionResults,
                offline
            )
        );

        productionLines.add(
            new ProductionLine(
                    "3",
                    "Production Line #3",
                    "192.168.1.7",
                    new Date(),
                    sensors,
                    inspectionResults,
                    online
            )
        );

        // ExpandableListAdapters

        ExpandableListView elvLineProductions = (ExpandableListView)findViewById(R.id.elvLineProductions);


        ProductionLine_InspectionResultAdapter adapter = new ProductionLine_InspectionResultAdapter(
                this, productionLines
        );

        /*ProductionLine_SensorAdapter adapter = new ProductionLine_SensorAdapter(
            this, productionLines
        ); */

        elvLineProductions.setAdapter(adapter);

    }

}