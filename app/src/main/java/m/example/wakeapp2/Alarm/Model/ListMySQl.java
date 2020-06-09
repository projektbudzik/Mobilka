package m.example.wakeapp2.Alarm.Model;

public class ListMySQl {

    String AlarmId, DateStart, Sequence, DateEnd, Time, DeviceId, Comment, UserName, DeviceName, Create_by;

        public ListMySQl(String alarmId, String dateStart, String sequence, String dateEnd, String time, String deviceId, String comment, String userName, String create_by, String deviceName){

            AlarmId= alarmId;
            DateStart= dateStart;
            Sequence= sequence;
            DateEnd= dateEnd;
            Time= time;
            DeviceId= deviceId;
            Comment= comment;
            UserName = userName;
            DeviceName = deviceName;
            Create_by = create_by;

        }

    public String getAlarmId() {
        return AlarmId;
    }

    public String getDateStart() {
        return DateStart;
    }

    public String getSequence() {
        return Sequence;
    }

    public String getDateEnd() {
        return DateEnd;
    }

    public String getTime() {
        return Time;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public String getComment() {
        return Comment;
    }

    public String getUserName() {
        return UserName;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public String getCreateBy() {return Create_by;
    }
}
