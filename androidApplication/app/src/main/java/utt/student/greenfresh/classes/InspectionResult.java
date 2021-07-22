package utt.student.greenfresh.classes;

import android.graphics.drawable.Drawable;

public class InspectionResult {
    // Attributes

    private Fruit fruit;
    private int rejectedResults;
    private int acceptedResults;


    // Getters and Setters

    public Fruit getFruit() { return fruit; }

    public void setFruit(Fruit fruit) { this.fruit = fruit; }

    public int getRejectedResults() { return rejectedResults; }

    public void setRejectedResults(int rejectedResults) { this.rejectedResults = rejectedResults; }

    public int getAcceptedResults() { return acceptedResults; }

    public void setAcceptedResults(int acceptedResults) { this.acceptedResults = acceptedResults; }

    // Constructor

    public InspectionResult(Fruit fruit, int rejectedResults, int acceptedResults) {
        this.fruit = fruit;
        this.rejectedResults = rejectedResults;
        this.acceptedResults = acceptedResults;
    }

    public InspectionResult() {
        this.fruit = new Fruit();
        this.rejectedResults = 0;
        this.acceptedResults = 0;
    }

}