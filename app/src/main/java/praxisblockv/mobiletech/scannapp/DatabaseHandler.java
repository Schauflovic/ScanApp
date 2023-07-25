package praxisblockv.mobiletech.scannapp;

import java.util.ArrayList;
import java.util.List;

public interface DatabaseHandler {

    void writeData(String type, String name, String address, Long lat, Long lon);

    void updateData();

    Device readData();

    void deleteData();

    List<String> getAllData();
    void deleteAllData();

}
