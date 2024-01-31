package entity;


public class CategoryEntity extends BaseEntity{
    private String code;

    private String description;
    private double discount;

    public CategoryEntity(String code, String description, double discount) {
        this.code = code;
        this.description = description;
        this.discount = discount;
    }

    public CategoryEntity() {}

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

    public double getDiscount() {
        return discount;
    }

    @Override
    public String toString() {
        return "CategoryEntity{code='%s', description='%s', discount=%s, id=%d}".formatted(code, description, discount, id);
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}













