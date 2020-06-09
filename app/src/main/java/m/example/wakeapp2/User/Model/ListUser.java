package m.example.wakeapp2.User.Model;

public class ListUser {

    String UserId, Name, Email, UserRole, Create_on;

    public ListUser(String userId, String name, String email, String userRole, String create_on){
        UserId= userId;
        Name= name;
        UserRole= userRole;
        Create_on= create_on;
        Email = email;
    }

    public String getUserId() {
        return UserId;
    }

    public String getName() {
        return Name;
    }

    public String getUserRole() {
        return UserRole;
    }

    public String getCreate_on() {
        return Create_on;
    }

    public String getEmail() {
        return Email;
    }

}
