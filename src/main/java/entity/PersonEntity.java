package entity;



import java.util.Calendar;


public class PersonEntity extends BaseEntity{
    String passport, firstName, lastName, genre, direction, email, telephone;
    Calendar birthDay;

    public PersonEntity() {

    }

    public PersonEntity(Long id,/*String code,*/ String firstName,String passport, String lastName, String genre, String direction, String email, String telephone, Calendar birthDay) {
        super(id);
        this.passport = passport;
        //this.code = code;
        this.firstName = firstName;
        this.lastName = lastName;
        this.genre = genre;
        this.direction = direction;
        this.email = email;
        this.telephone = telephone;
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return "PersonEntity{ passport='%s', firstName='%s', lastName='%s', genre='%s', direction='%s', email='%s', telephone='%s', birthDay=%s, id=%d}".formatted( passport, firstName, lastName, genre, direction, email, telephone, birthDay, id);
    }



    public String getFullName(){
        return firstName +" "+lastName;
    }



    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Calendar getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Calendar birthDay) {
        this.birthDay = birthDay;
    }
}
