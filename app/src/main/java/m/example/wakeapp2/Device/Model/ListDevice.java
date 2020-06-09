package m.example.wakeapp2.Device.Model;

public class ListDevice {

    String DeviceId, Name , DeviceType, MAC, UserName;

    public ListDevice(String deviceId, String name, String deviceType, String xMAC, String userName){
        DeviceId= deviceId;
        Name= name;
        DeviceType= deviceType;
        MAC= xMAC;
        UserName = userName;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public String getName() {
        return Name;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public String getMAC() {
        return MAC;
    }

    public String getUserName() {
        return UserName;
    }
}
