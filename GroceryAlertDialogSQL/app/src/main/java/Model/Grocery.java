package Model;

public class Grocery {

    private int id;
    private String Name;
    private String Qty;

    public Grocery() {
    }

/*    public Grocery(int id) {
        this.id = id;
    }

    public Grocery(String name, String qty) {
        Name = name;
        Qty = qty;
    }

    public Grocery(int id, String name, String qty) {
        this.id = id;
        this.Name = name;
        this.Qty = qty;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }



}
