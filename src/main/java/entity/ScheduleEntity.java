package entity;


public class ScheduleEntity extends BaseEntity{
    private Integer workDay; private Float startHour, endHour;
    private Long idEmployee;

    public ScheduleEntity() {

    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public ScheduleEntity(Long idEmployee, Long id, Integer workDay, Float startHour, Float endHour) {
        super(id);
        this.idEmployee = idEmployee;
        this.workDay = workDay;
        this.startHour = startHour;
        this.endHour = endHour;
    }


    public Integer getWorkDay() {
        return workDay;
    }

    public void setWorkDay(Integer workDay) {
        this.workDay = workDay;
    }

    public Float getStartHour() {
        return startHour;
    }

    public void setStartHour(Float startHour) {
        this.startHour = startHour;
    }

    public Float getEndHour() {
        return endHour;
    }

    public void setEndHour(Float endHour) {
        this.endHour = endHour;
    }
}
