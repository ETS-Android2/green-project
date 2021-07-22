package utt.student.greenfresh.classes;

public class FruitReadings {
    // attributes
    private ProductionLine productionLine;
    private String date;
    private Fruit fruit;
    private double weight;
    private int r;
    private int g;
    private int b;

    // getters and setters
    public ProductionLine getProductionLine() { return productionLine; }
    public void setProductionLine(ProductionLine productionLine) { this.productionLine = productionLine; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public Fruit getFruit() { return fruit; }
    public void setFruit(Fruit fruit) { this.fruit = fruit; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public int getR() { return r; }
    public void setR(int r) { this.r = r; }
    public int getG() { return g; }
    public void setG(int g) { this.g = g; }
    public int getB() { return b; }
    public void setB(int b) { this.b = b; }

    // constructors
    public FruitReadings(ProductionLine productionLine, String date, Fruit fruit, double weight, int r, int g, int b) {
        this.productionLine = productionLine;
        this.date = date;
        this.fruit = fruit;
        this.weight = weight;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public FruitReadings() {
        this.productionLine = productionLine;
        this.date = date;
        this.fruit = fruit;
        this.weight = weight;
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
