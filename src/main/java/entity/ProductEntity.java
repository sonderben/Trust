package entity;



import java.util.Calendar;


public class ProductEntity extends BaseEntity {


    private  int quantityRemaining;

    public int getQuantityRemaining() {
        return quantityRemaining;
    }

    public void setQuantityRemaining(int quantityRemaining) {
        this.quantityRemaining = quantityRemaining;
    }

    private String code;
    private String description;
    private double sellingPrice,purchasePrice, discount, itbis;
    private int quantity;
    private Calendar dateAdded,expirationDate;

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Calendar getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Calendar dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Calendar getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }


    private CategoryEntity category;
    

    private EmployeeEntity employee;

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public ProductEntity() {
    }

    @Override
    public String toString() {
        return "ProductEntity{quantityRemaining=%d, code='%s', description='%s', sellingPrice=%s, purchasePrice=%s, discount=%s, itbis=%s, quantity=%d, dateAdded=%s, expirationDate=%s, category=%s, employee=%s, id=%d}".formatted(quantityRemaining, code, description, sellingPrice, purchasePrice, discount, itbis, quantity, dateAdded, expirationDate, category, employee.getFullName(), id);
    }

    public ProductEntity(String code, String description, double sellingPrice, double purchasePrice, double discount, double itbis, int quantity, int quantityRemaining, Calendar dateAdded, Calendar expirationDate, CategoryEntity category, EmployeeEntity employee) {
        this.code = code;
        this.description = description;
        this.sellingPrice = sellingPrice;
        this.purchasePrice = purchasePrice;
        this.discount = discount;
        this.itbis = itbis;
        this.quantity = quantity;
        this.quantityRemaining = quantityRemaining;
        this.dateAdded = dateAdded;
        this.expirationDate = expirationDate;
        this.category = category;
        this.employee = employee;
    }

    public String getCode() {
        return code;
    }



    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getItbis() {
        return itbis;
    }

    public void setItbis(double itbis) {
        this.itbis = itbis;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double total() {
        double normalPrice = (itbis + sellingPrice) * quantity;
        if (discount <= 0){
            return normalPrice;
        } else if (category!=null && category.getDiscount()>0) {
            return normalPrice-(sellingPrice * (quantity/100.0) * category.getDiscount());
        }
        return normalPrice-(sellingPrice * (quantity/100.0) * discount);
    }
}
