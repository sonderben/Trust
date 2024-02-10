package entity;


import java.util.Calendar;

public class CustomerEntity extends PersonEntity{
    private Long Point;
    public CustomerEntity(){}

    public CustomerEntity( String code, String firstName, String passport, String lastName, String genre, String direction, String email, String telephone, Calendar birthDay, Long point) {
        super(null, code, firstName, passport, lastName, genre, direction, email, telephone, birthDay);
        Point = point;
    }

    @Override
    public String toString() {
        return "CustomerEntity{Point=%d, code='%s', passport='%s', firstName='%s', lastName='%s', genre='%s', direction='%s', email='%s', telephone='%s', birthDay=%s}".formatted(Point, code, passport, firstName, lastName, genre, direction, email, telephone, birthDay.getTime().getTime());
    }

    public Long getPoint() {
        return Point;
    }

    public void setPoint(Long point) {
        Point = point;
    }
}
