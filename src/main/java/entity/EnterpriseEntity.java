package entity;

import com.sonderben.trust.CategoryEnum;

import java.util.Calendar;

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



}
