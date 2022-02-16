package Model;

import android.graphics.Bitmap;

import java.time.LocalDate;

public class CityItems {

    public String CityName;
    public String CityTemperature;
    private String dateItemAdded;
    public String ImageName;
    private Bitmap Image;
    private int id;


    public CityItems() {
    }


    public CityItems(String cityName, String cityTemperature, String dateItemAdded, String imageName, Bitmap image, int id) {
        this.CityName = cityName;
        this.CityTemperature = cityTemperature;
        this.ImageName = imageName;
        this.Image = image;
        this.dateItemAdded = dateItemAdded;
        this.id = id;
    }


    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        this.CityName = cityName;
    }

    public String getCityTemperature() {
        return CityTemperature;
    }

    public void setCityTemperature(String cityTemperature) { this.CityTemperature = cityTemperature; }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        this.ImageName = imageName;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public Bitmap getImage() { return Image; }

    public void setImage(Bitmap image) { this.Image = image; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
