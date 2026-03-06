package model;

public class LoginDetails {

    private int id;
    private int employeeId;
    private String username;
    private String passwordHash;
    public LoginDetails(){}
    public LoginDetails(int id,int employeeId,String username,String passwordHash){
        this.id=id;
        this.employeeId=employeeId;
        this.username=username;
        this.passwordHash=passwordHash;
    }
    public int getEmployeeId(){
        return employeeId;
    }

    public String getUsername(){
        return username;
    }

    public String getPasswordHash(){
        return passwordHash;
    }
}