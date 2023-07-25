package praxisblockv.mobiletech.scannapp;

import java.util.ArrayList;

public interface DatabaseHandler {

    void writeData(String type, String name, String address, Long lat, Long lon);

    void updateData();

    Device readData();

    void deleteData();

}
