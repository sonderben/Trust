package entity;

import com.sonderben.trust.model.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.Calendar;
import java.util.List;

@Entity
public class EmployeeEntity extends PersonEntity {
    String bankAccount,userName, password,passport ;
    @OneToMany(cascade = CascadeType.ALL)
    List<Role> roleList;
    @OneToMany(cascade = CascadeType.ALL)
    List<ScheduleEntity> schedules;

    public EmployeeEntity( String firstName, String passport, String lastName, String genre, String direction, String email, String telephone, Calendar birthDay, String bankAccount, String userName, String password, List<Role> roleList, List<ScheduleEntity> schedule) {
        super(null, firstName,passport, lastName, genre, direction, email, telephone, birthDay);
        this.bankAccount = bankAccount;
        this.userName = userName;
        this.password = password;
        this.roleList = roleList;
        this.schedules = schedule;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
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

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public List<ScheduleEntity> getSchedules() {
        return schedules;
    }

    @Override
    public String toString() {
        return "EmployeeEntity{bankAccount='%s', userName='%s', password='%s', passport='%s', roleList=%s, schedules=%s, passport='%s', firstName='%s', lastName='%s', genre='%s', direction='%s', email='%s', telephone='%s', birthDay=%s, id=%d}".formatted(bankAccount, userName, password, passport, roleList, schedules, passport, firstName, lastName, genre, direction, email, telephone, birthDay.getTime().getTime(), id);
    }

    public void setSchedule(List<ScheduleEntity> schedule) {
        this.schedules = schedule;
    }

    public String getFullName(){
        return firstName+" "+lastName;
    }
}
