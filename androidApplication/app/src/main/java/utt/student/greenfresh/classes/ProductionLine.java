package utt.student.greenfresh.classes;

import java.util.ArrayList;
import java.util.Date;

public class ProductionLine {

    //attributes
    private String id;
    private String name;
    private String ipAddress;
    private Status status;

    private Date lastConnection;
    private ArrayList<Sensor> sensors;
    private ArrayList<InspectionResult> inspectionResults;




    //getters and setters.
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public Date getLastConnection() { return lastConnection; }
    public void setLastConnection(Date lastConnection) { this.lastConnection = lastConnection; }


    public ArrayList<Sensor> getSensors() { return sensors; }
    public void setSensor(ArrayList<Sensor> sensors) { this.sensors = sensors; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public void setInspectionResults(ArrayList<InspectionResult> inspectionResults) {
        this.inspectionResults = inspectionResults;
    }
    public ArrayList<InspectionResult> getInspectionResults() {
        return inspectionResults;
    }

    //Constructors

    public ProductionLine() {
        this.id = "";
        this.name = "";
        this.ipAddress = "";
        this.lastConnection = new Date();
        this.sensors = new ArrayList<Sensor>();
        this.inspectionResults = new ArrayList<InspectionResult>();
        this.status = new Status();
    }

    public ProductionLine(String id, String name, String ipAddress, Date lastConnection, ArrayList<Sensor> sensors, ArrayList<InspectionResult> inspectionResults, Status status) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
        this.lastConnection = lastConnection;
        this.sensors = sensors;
        this.inspectionResults = inspectionResults;
        this.status = status;
    }



}
