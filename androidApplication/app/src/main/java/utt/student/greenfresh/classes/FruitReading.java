package utt.student.greenfresh.classes;

import java.util.ArrayList;

public class FruitReading {
    // attributes
    private String date;
    private Fruit fruit;
    private ArrayList<FruitSensor> results;

    // getters and setters
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public Fruit getFruit() { return fruit; }
    public void setFruit(Fruit fruit) { this.fruit = fruit; }
    public ArrayList<FruitSensor> getResults() { return results; }
    public void setResults(ArrayList<FruitSensor> results) { this.results = results; }

    // constructors
    public FruitReading(String date, Fruit fruit, ArrayList<FruitSensor> results) {
        this.date = date;
        this.fruit = fruit;
        this.results = results;
    }

    public FruitReading(String date, Fruit fruit) {
        this.date = date;
        this.fruit = fruit;
        this.results = new ArrayList<>();
    }

    public FruitReading() {
        this.date = date;
        this.fruit = new Fruit();
        this.results = new ArrayList<>();
    }

    // methods
    public void addResult(FruitSensor fruitSensor){
        this.results.add(fruitSensor);
    }
}
