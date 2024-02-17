package entity;

import com.sonderben.trust.CategoryEnum;
import com.sonderben.trust.constant.Action;
import com.sonderben.trust.constant.ScreenEnum;
import com.sonderben.trust.model.Role;
import com.sonderben.trust.model.Screen;

import java.util.Calendar;
import java.util.List;

public class EnterpriseEntity extends BaseEntity{


        private String name;
        private String direction;
        private String telephone;
        private Calendar foundation;
        private String website;
        private CategoryEnum category;
        EmployeeEntity employee;
        private String invoiceTemplate;
        private String invoiceTemplateHtml;

        public String getCategoryString(){
            return category.name();
        }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "EnterpriseEntity{name='%s', direction='%s', telephone='%s', foundation=%s, website='%s', category=%s, employee=%s, invoiceTemplate='%s', invoiceTemplateHtml='%s', id=%d}".formatted(name, direction, telephone, foundation, website, category, employee, invoiceTemplate, invoiceTemplateHtml, id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Calendar getFoundation() {
        return foundation;
    }

    public void setFoundation(Calendar foundation) {
        this.foundation = foundation;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public String getInvoiceTemplateHtml() {

        return invoiceTemplateHtml;
    }

    public void setInvoiceTemplateHtml(String invoiceTemplateHtml) {
        this.invoiceTemplateHtml = invoiceTemplateHtml;
    }

    public EnterpriseEntity(String name, String direction, String telephone, Calendar foundation, String website, String category, EmployeeEntity employee, String invoiceTemplate, String invoiceTemplateHtml) {
        this.name = name;
        this.direction = direction;
        this.telephone = telephone;
        this.foundation = foundation;
        this.website = website;
        this.category = CategoryEnum.valueOf(category);

        this.employee = employee;
        this.invoiceTemplate = invoiceTemplate;
        this.invoiceTemplateHtml = invoiceTemplateHtml;
    }
    public EnterpriseEntity() {
        this.name = "";
        this.direction = "";
        this.telephone = "";
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,2018);
        this.foundation = calendar;
        this.website = "";
        this.category = null;

        this.employee = createEmployee();
        this.invoiceTemplate = "";
        this.invoiceTemplateHtml = "";
    }

    private EmployeeEntity createEmployee(){
        Role role =new Role(
                "Admin",List.of(
                new Screen( ScreenEnum.LOGIN,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                //new Screen( ScreenEnum.INVENTORY,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                new Screen( ScreenEnum.ROLE,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                new Screen( ScreenEnum.USER,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                new Screen( ScreenEnum.SALE,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                new Screen( ScreenEnum.PRODUCT,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                new Screen( ScreenEnum.EMPLOYEE,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                new Screen( ScreenEnum.CONFIGURATION,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                new Screen( ScreenEnum.QUERIES,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                new Screen( ScreenEnum.CUSTOMER_SERVICE,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) )

        ));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,2006);

            return   new EmployeeEntity("","","",null,"","","", calendar,"","root","",
                role, List.of( new ScheduleEntity(null,1,11.30f,1f) ));
    }


}
