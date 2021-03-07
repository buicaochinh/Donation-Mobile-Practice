package ie.app.models;

public class Donation {
    public int amount;
    public String method;
    public Donation (int amount, String method)
    {
        this.amount = amount;
        this.method = method;
    }
}
