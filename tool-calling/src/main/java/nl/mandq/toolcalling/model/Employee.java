package nl.mandq.toolcalling.model;

public class Employee {
    private String empId;
    private String name;
    private String email;

    public Employee() {
    }

    public Employee(String empId, String name, String email) {
        this.empId = empId;
        this.name = name;
        this.email = email;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}