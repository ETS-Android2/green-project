package utt.student.greenfresh.classes;

public class AreaReadings {
    // attributes
    private ProductionLine productionLine;
    private double temperature;
    private int humidity;

    // getters and setters
    public ProductionLine getProductionLine() { return productionLine; }
    public void setProductionLine(ProductionLine productionLine) { this.productionLine = productionLine; }
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    // constructors
    public AreaReadings(ProductionLine productionLine, double temperature, int humidity) {
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
