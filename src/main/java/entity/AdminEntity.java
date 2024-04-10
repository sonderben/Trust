package entity;

import com.sonderben.trust.constant.ScreenEnum;
import com.sonderben.trust.model.Role;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AdminEntity extends EmployeeEntity{
    public AdminEntity( String firstName,String lastName,   String genre,  String email, String telephone, Calendar birthDay,  String password ){
        super(  firstName,  "",  lastName,  genre,  "",  email,  telephone,  birthDay,  "",  "root",  password,  new Role("admin", Arrays.stream(ScreenEnum.values()).toList()),  List.of() );
    }

    public AdminEntity(){
        role = new Role("admin", Arrays.stream(ScreenEnum.values()).toList());
        userName = "root";
        schedules = List.of();
    }

    @Override
    public void setUserName(String userName) {
        super.setUserName("root");
    }

    @Override
    public void setSchedule(List<ScheduleEntity> schedule) {
        super.setSchedule(List.of());
    }

    @Override
    public void setRole(Role role) {
        super.setRole( new Role("admin", Arrays.stream(ScreenEnum.values()).toList()) );
    }
}
