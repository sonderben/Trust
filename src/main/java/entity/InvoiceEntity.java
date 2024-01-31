package entity;




import java.util.Calendar;
import java.util.List;


public class InvoiceEntity extends BaseEntity {

    private List<ProductSaled>products;

    private EmployeeEntity employee;

    private CustomerEntity customer;
    private String codeBar;
    private Calendar date;
    public InvoiceEntity(){}

    public InvoiceEntity(List<ProductSaled> products, EmployeeEntity employee, CustomerEntity customer, String codeBar, Calendar date) {

        this.products = products;
        this.employee = employee;
        this.customer = customer;
        this.codeBar = codeBar;
        this.date = date;
    }



    public List<ProductSaled> getProducts() {
        return products;
    }

    public void setProducts(List<ProductSaled> products) {
        this.products = products;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public String getCodeBar() {
        return codeBar;
    }

    public void setCodeBar(String codeBar) {
        this.codeBar = codeBar;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
