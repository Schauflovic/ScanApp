package praxisblockv.mobiletech.scannapp;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Device {

    private Timestamp timestamp;
    private String type;
    private String address;
    private String name;
    private long lat;
    private long lon;


    public ArrayList<Device> deviceList;

    public Device(Timestamp timestamp, String type, String address, String name, long lat, long lon) {
        this.timestamp = timestamp;
        this.type = type;
        this.address = address;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public Device(){
        deviceList = new ArrayList<>();
    }

    public void addDevice(Device device) {
        deviceList.add(device);
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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
