package utt.student.greenfresh.classes;

import java.util.ArrayList;
import java.util.Date;

public class ProductionLine {
    //atributtes
    private String code;
    private String ip;
    private String description;
    private Status status;

    //getters and setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status;}

    //constructor
    public ProductionLine() {
        this.code = "";
        this.ip = "";
        this.description = "";
        this.status = null;
    }

    public ProductionLine(String code, String ip, String description, Status status) {
        this.code = code;
        this.ip = ip;
        this.description = description;
        this.status = status;
    }
}
