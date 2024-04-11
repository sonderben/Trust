package entity;

import com.sonderben.trust.CategoryEnum;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EnterpriseEntity extends BaseEntity{


        private String name;
        private String direction;
        private String telephone;
        private Calendar foundation;
        private String website;
        private CategoryEnum category;
        AdminEntity adminEntity;
        private String invoiceTemplateHtml;

        public String getCategoryString(){
            return category.name();
        }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "EnterpriseEntity{name='%s', direction='%s', telephone='%s', foundation=%s, website='%s', category=%s, admin=%s, invoiceTemplateHtml='%s', id=%d}".formatted(name, direction, telephone, foundation, website, category, adminEntity, invoiceTemplateHtml, id);
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

    public AdminEntity getAdminEntity() {
        return adminEntity;
    }

    public void setAdminEntity(AdminEntity adminEntity) {
        this.adminEntity = adminEntity;
    }





    public String getInvoiceTemplateHtml() {

        return invoiceTemplateHtml;
    }

    public void setInvoiceTemplateHtml(String invoiceTemplateHtml) {
        this.invoiceTemplateHtml = invoiceTemplateHtml;
    }

    public EnterpriseEntity(String name, String direction, String telephone, Calendar foundation, String website, String category, AdminEntity adminEntity, String invoiceTemplateHtml) {
        this.name = name;
        this.direction = direction;
        this.telephone = telephone;
        this.foundation = foundation;
        this.website = website;
        this.category = CategoryEnum.valueOf(category);

        this.adminEntity = adminEntity;
        this.invoiceTemplateHtml = invoiceTemplateHtml;
    }

    public EnterpriseEntity(Long id,String name, String direction, String telephone, Calendar foundation, String website, String category, AdminEntity adminEntity , String invoiceTemplateHtml) {
        this.id = id;
        this.name = name;
        this.direction = direction;
        this.telephone = telephone;
        this.foundation = foundation;
        this.website = website;
        this.category = CategoryEnum.valueOf(category);
        this.adminEntity = adminEntity;
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

        this.adminEntity = createEmployee();
        this.invoiceTemplateHtml = "";
    }

    private AdminEntity createEmployee(){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,2006);

            return   new AdminEntity("","","",null,"",calendar,"");
    }


}
