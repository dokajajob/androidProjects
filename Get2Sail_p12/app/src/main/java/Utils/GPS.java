package Utils;

import com.google.android.gms.maps.model.LatLng;

public class GPS {

    public double Lat;
    public double Lng;

    public GPS() {
    }

    public GPS(double lat, double lng) {
        Lat = lat;
        Lng = lng;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

}
