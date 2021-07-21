package utt.student.greenfresh.classes;

public class AreaReadings {
    // attributes
    private ProductionLine productionLine;
    private Float temperature;
    private int humidity;

    // getters and setters
    public ProductionLine getProductionLine() { return productionLine; }
    public void setProductionLine(ProductionLine productionLine) { this.productionLine = productionLine; }
    public Float getTemperature() { return temperature; }
    public void setTemperature(Float temperature) { this.temperature = temperature; }
    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    // constructors
    public AreaReadings(ProductionLine productionLine, Float temperature, int humidity) {
        this.productionLine = productionLine;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public AreaReadings() {
        this.productionLine = productionLine;
        this.temperature = temperature;
        this.humidity = humidity;
    }
}
