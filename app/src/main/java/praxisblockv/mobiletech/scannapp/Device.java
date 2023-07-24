package praxisblockv.mobiletech.scannapp;

public class Device {

    private String type;
    private String address;
    private String name;
    private long lat;
    private long lon;

    public Device(String type, String address, String name, long lat, long lon) {
        this.type = type;
        this.address = address;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nMAC: " + address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }
}
