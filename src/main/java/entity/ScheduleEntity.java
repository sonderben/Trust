package entity;


public class ScheduleEntity extends BaseEntity{
    private Integer workDay; private Float startHour, endHour;

    public ScheduleEntity() {

    }

    public ScheduleEntity(Long id,Integer workDay, Float startHour, Float endHour) {
        super(id);
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
