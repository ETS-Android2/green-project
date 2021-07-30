package utt.student.greenfresh.classes;

public class Inspection {
    // attributes
    private String date;
    private Fruit fruit;
    private int accepted;
    private int rejected;

    // getters and setters
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public Fruit getFruit() { return fruit; }
    public void setFruit(Fruit fruit) { this.fruit = fruit; }
    public int getAccepted() { return accepted; }
    public void setAccepted(int accepted) { this.accepted = accepted; }
    public int getRejected() { return rejected; }
    public void setRejected(int rejected) { this.rejected = rejected; }

    // constructors
    public Inspection(String date, Fruit fruit, int accepted, int rejected) {
        this.date = date;
        this.fruit = fruit;
        this.accepted = accepted;
        this.rejected = rejected;
    }

    public Inspection() {
        this.date = "";
        this.fruit = new Fruit();
        this.accepted = 0;
        this.rejected = 0;
    }
}
