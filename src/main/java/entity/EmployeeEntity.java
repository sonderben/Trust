package entity;

import com.sonderben.trust.model.Role;


import java.util.Calendar;
import java.util.List;


public class EmployeeEntity extends PersonEntity {
    String bankAccount,userName, password ;

    Role role;

    List<ScheduleEntity> schedules;

    public EmployeeEntity( String firstName, String passport, String lastName, String genre, String direction, String email, String telephone, Calendar birthDay, String bankAccount, String userName, String password, Role role, List<ScheduleEntity> schedule) {
        super(null,firstName,passport, lastName, genre, direction, email, telephone, birthDay);
        this.bankAccount = bankAccount;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.schedules = schedule;
    }




    public EmployeeEntity() {

    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setSchedules(List<ScheduleEntity> schedules) {
        this.schedules = schedules;
    }

    public List<ScheduleEntity> getSchedules() {
        return schedules;
    }

    @Override
    public String toString() {
        Long bd = 0L;
        if (birthDay == null){
            bd = birthDay.getTime().getTime();
        }
        return "EmployeeEntity{bankAccount='%s', userName='%s', password='%s', passport='%s', roleList=%s, schedules=%s, passport='%s', firstName='%s', lastName='%s', genre='%s', direction='%s', email='%s', telephone='%s', birthDay=%s, id=%d}".formatted(bankAccount, userName, password, passport, role, schedules, passport, firstName, lastName, genre, direction, email, telephone, bd, id);
    }

    public void setSchedule(List<ScheduleEntity> schedule) {
        this.schedules = schedule;
    }

    public String getFullName(){
        return firstName+" "+lastName;
    }
}
