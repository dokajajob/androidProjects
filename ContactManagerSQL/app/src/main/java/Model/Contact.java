package Model;

public class Contact {
    private int id;
    private String Name;
    private String Phone;

    public Contact() {
    }

    public Contact(int id, String name, String phone) {
        this.id = id;
        this.Name = name;
        this.Phone = phone;
    }

    public Contact(int id) {
        this.id = id;
    }

    public Contact(String name, String phone) {
        Name = name;
        Phone = phone;
    }

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

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}


