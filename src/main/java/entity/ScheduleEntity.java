package entity;


public class ScheduleEntity extends BaseEntity{
    private Integer workDay; private String startHour, endHour;
    private Long idEmployee;

    public ScheduleEntity() {

    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public ScheduleEntity(Long idEmployee, Long id, Integer workDay, String startHour, String endHour) {
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

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    @Override
    public String toString() {
        return "ScheduleEntity{" +
                "workDay=" + workDay +
                ", startHour='" + startHour + '\'' +
                ", endHour='" + endHour + '\'' +
                ", idEmployee=" + idEmployee +
                ", id=" + id +
                '}';
    }
}
