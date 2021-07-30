package utt.student.greenfresh.classes;

public class UnitOfMeasurement {
    // attributes
    private String name;
    private String symbol;

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    // constructors
    public UnitOfMeasurement(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public UnitOfMeasurement() {
        this.name = "";
        this.symbol = "";
    }
}
