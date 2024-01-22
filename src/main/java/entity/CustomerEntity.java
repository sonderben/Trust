package entity;

import jakarta.persistence.Entity;

@Entity
public class CustomerEntity extends PersonEntity{
    private Long Point;
    public CustomerEntity(){}

    public Long getPoint() {
        return Point;
    }

    public void setPoint(Long point) {
        Point = point;
    }
}
