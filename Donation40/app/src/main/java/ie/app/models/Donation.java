package ie.app.models;

public class Donation
{
    public int id; public int amount;
    public String method;
    public Donation (int amount, String method)
    {
        this.amount = amount;
        this.method = method;
    }
    public Donation ()
    {
        this.amount = 0;
        this.method = "";
    }
    public String toString()
    {
        return id + ", " + amount + ", " + method;
    }
}
