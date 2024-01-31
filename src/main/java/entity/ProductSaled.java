package entity;

public class ProductSaled extends BaseEntity{
    private String code, description;
    private float price, qty, discount, itbis;
    private double total;
    private boolean wasDiscountCategory;

    public ProductSaled(){}
    public ProductSaled( ProductEntity p,boolean wasDiscountCategory ){
        this.id = p.id;
        this.code = p.getCode();
        this.description = p.getDescription();
        this.price = (float) p.getSellingPrice();
        this.qty = p.getQuantity();
        this.discount = (float) p.getDiscount();
        this.itbis = (float) p.getItbis();
        this.total = p.total();
        this.wasDiscountCategory = wasDiscountCategory;
    }
    public ProductSaled(String code, String description, float price, float qty, float discount, float itbis, double total,boolean wasDiscountCategory) {
        this.code = code;
        this.description = description;
        this.price = price;
        this.qty = qty;
        this.discount = discount;
        this.itbis = itbis;
        this.total = total;
        this.wasDiscountCategory = wasDiscountCategory;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getItbis() {
        return itbis;
    }

    public void setItbis(float itbis) {
        this.itbis = itbis;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isWasDiscountCategory() {
        return wasDiscountCategory;
    }

    public void setWasDiscountCategory(boolean wasDiscountCategory) {
        this.wasDiscountCategory = wasDiscountCategory;
    }

    @Override
    public String toString() {
        return "ProductSaled{code='%s', description='%s', price=%s, qty=%s, discount=%s, itbis=%s, total=%s, wasDiscountCategory=%s}".formatted(code, description, price, qty, discount, itbis, total, wasDiscountCategory);
    }
}