package utt.student.greenfresh.classes;

import android.graphics.drawable.Drawable;

public class InspectionResult {
    // Attributes

    private int id;
    private String name;
    private Drawable image;
    private int rejectedResults;
    private int acceptedResults;


    // Getters and Setters

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Drawable getImage() { return image; }

    public void setImage(Drawable image) { this.image = image; }

    public int getRejectedResults() { return rejectedResults; }

    public void setRejectedResults(int rejectedResults) { this.rejectedResults = rejectedResults; }

    public int getAcceptedResults() { return acceptedResults; }

    public void setAcceptedResults(int acceptedResults) { this.acceptedResults = acceptedResults; }

    // Constructor

    public InspectionResult(int id, String name, Drawable image, int rejectedResults, int acceptedResults) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.rejectedResults = rejectedResults;
        this.acceptedResults = acceptedResults;
    }

    public InspectionResult() {
        this.id = 0;
        this.name = "";
        this.image = null;
        this.rejectedResults = 0;
        this.acceptedResults = 0;
    }

}