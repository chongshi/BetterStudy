package comp5216.sydney.edu.au.betterstudy.model;

public class User {

    private String userId;
    private String userName;
    private String userPassword;
    private String email;
    private String phone;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
