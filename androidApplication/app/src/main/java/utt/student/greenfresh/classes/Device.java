package utt.student.greenfresh.classes;

import java.util.ArrayList;

public class Device {
    //attributes
    private String id;
    private String name;
    private String ipAddress;
    private String lastConnection;
    private ArrayList<Sensor> sensors;

    //getters and setters.
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getLastConnection() { return lastConnection; }
    public void setLastConnection(String lastConnection) { this.lastConnection = lastConnection; }
    public ArrayList<Sensor> getSensors() { return sensors; }
    public void setSensor(ArrayList<Sensor> sensors) { this.sensors = sensors; }

    //Constructors

    public Device() {
        this.id = "";
        this.name = "";
        this.ipAddress = "";
        this.lastConnection = "";
        this.sensors = new ArrayList<Sensor>();
    }

    public Device(String id, String name, String ipAddress, String lastConnection, ArrayList<Sensor> sensor) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
        this.lastConnection = lastConnection;
        this.sensors = sensor;
    }
}
