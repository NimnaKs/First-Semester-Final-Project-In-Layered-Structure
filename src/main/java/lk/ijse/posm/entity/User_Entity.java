package lk.ijse.posm.entity;

public class User_Entity {
    private String user_id;
    private String user_name;
    private String password;
    private String type;
    private String employee_id;

    public User_Entity(String user_id, String user_name, String password, String type, String employee_id) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.password = password;
        this.type = type;
        this.employee_id = employee_id;
    }

    public User_Entity(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;
    }

    public User_Entity() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }
}
