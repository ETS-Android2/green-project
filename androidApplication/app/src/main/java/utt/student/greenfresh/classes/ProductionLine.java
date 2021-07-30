package utt.student.greenfresh.classes;

import java.util.ArrayList;
import java.util.Date;

public class ProductionLine {
    // attributes
    private String code;
    private String name;
    private String ip;
    private Status status;
    private Fruit currentFruit;
    private ArrayList<AreaSensor> areaSensors;
    private ArrayList<FruitReading> fruitReadings;
    private ArrayList<Inspection> inspectionResults;

    // getters and setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Fruit getCurrentFruit() { return currentFruit; }
    public void setCurrentFruit(Fruit currentFruit) { this.currentFruit = currentFruit; }
    public ArrayList<AreaSensor> getAreaSensors() { return areaSensors; }
    public void setAreaSensors(ArrayList<AreaSensor> areaSensors) { this.areaSensors = areaSensors; }
    public ArrayList<FruitReading> getFruitReadings() { return fruitReadings; }
    public void setFruitReadings(ArrayList<FruitReading> fruitReadings) { this.fruitReadings = fruitReadings; }
    public ArrayList<Inspection> getInspectionResults() { return inspectionResults; }
    public void setInspectionResults(ArrayList<Inspection> inspectionResults) { this.inspectionResults = inspectionResults; }

    // constructors

    public ProductionLine(String code, String name, String ip, Status status, Fruit currentFruit,
                          ArrayList<AreaSensor> areaSensors,
                          ArrayList<FruitReading> fruitReadings,
                          ArrayList<Inspection> inspectionResults) {
        this.code = code;
        this.name = name;
        this.ip = ip;
        this.status = status;
        this.currentFruit = currentFruit;
        this.areaSensors = areaSensors;
        this.fruitReadings = fruitReadings;
        this.inspectionResults = inspectionResults;
    }

    public ProductionLine(String code, String name, String ip, Status status, Fruit currentFruit) {
        this.code = code;
        this.name = name;
        this.ip = ip;
        this.status = status;
        this.currentFruit = currentFruit;
        this.areaSensors = new ArrayList<>();
        this.fruitReadings = new ArrayList<>();
        this.inspectionResults = new ArrayList<>();
    }

    public ProductionLine() {
        this.code = "";
        this.name = "";
        this.ip = "";
        this.status = new Status();
        this.currentFruit = new Fruit();
        this.areaSensors = new ArrayList<>();
        this.fruitReadings = new ArrayList<>();
        this.inspectionResults = new ArrayList<>();
    }

    // methods
    public void addAreaSensor(AreaSensor sensor){
        this.areaSensors.add(sensor);
    }

    public void addFruitReading(FruitReading reading){
        this.fruitReadings.add(reading);
    }

    public void addInspectionResult(Inspection inspection){
        this.inspectionResults.add(inspection);
    }
}
