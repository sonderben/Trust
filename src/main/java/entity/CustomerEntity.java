package entity;


import java.util.Calendar;
import java.util.Objects;

public class CustomerEntity extends PersonEntity{
    private Long point;
    private String code;
    public CustomerEntity(){}

    public CustomerEntity( String code, String firstName, String passport, String lastName, String genre, String direction,
                           String email, String telephone, Calendar birthDay, Long point) {
        super(null, firstName, passport, lastName, genre, direction, email, telephone, birthDay);
        this.point = point;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "CustomerEntity{point=%d, code='%s', passport='%s', firstName='%s', lastName='%s', genre='%s', direction='%s', email='%s', telephone='%s', birthDay=%s}".formatted(point, code, passport, firstName, lastName, genre, direction, email, telephone, birthDay.getTime().getTime());
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerEntity that = (CustomerEntity) o;
        return Objects.equals(id, that.id);
    }


}
