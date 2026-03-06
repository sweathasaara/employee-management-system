package model;

public class Employee {

    private int id;
    private String name;
    private double salary;
    private int departmentId;

    public Employee(){}
    public Employee(int id,String name,double salary,int departmentId){
        this.id=id;
        this.name=name;
        this.salary=salary;
        this.departmentId=departmentId;
    }
    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public double getSalary(){
        return salary;
    }

    public int getDepartmentId(){
        return departmentId;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setSalary(double salary){
        this.salary=salary;
    }
    public void setDepartmentId(int departmentId){
        this.departmentId=departmentId;
    }
}