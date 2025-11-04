package dto;

import java.sql.Date;

public class Emp {
    private int empCode;
    private String empId;
    private String empPw;
    private String empName;
    private int active; // 상태 (1: 활성화, 0: 비활성화)
    private Date createDate;
    
    // Getters and Setters
    public int getEmpCode() {
        return empCode;
    }
    public void setEmpCode(int empCode) {
        this.empCode = empCode;
    }
    public String getEmpId() {
        return empId;
    }
    public void setEmpId(String empId) {
        this.empId = empId;
    }
    public String getEmpPw() {
        return empPw;
    }
    public void setEmpPw(String empPw) {
        this.empPw = empPw;
    }
    public String getEmpName() {
        return empName;
    }
    public void setEmpName(String empName) {
        this.empName = empName;
    }
    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}