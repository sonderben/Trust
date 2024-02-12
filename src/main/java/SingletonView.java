import com.sonderben.trust.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class SingletonView {

    private static Node employee, configuration, sale, user, product,
            invoice, businessInfo, inventory, role, queries, productSold,
            productRemaining, bestEmployee, bestSellingProduct, frequentCustomer,
            productExpired, returned,spendingCustomer,admin;


    private SingletonView(){

    }
    public static Node get(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(path));
        switch (path) {
            case "view/sale.fxml" -> {
                if (sale == null) {
                    sale = fxmlLoader.load();
                }
                return sale;
            }
            case "view/user.fxml" -> {
                if (user == null) {
                    user = fxmlLoader.load();
                }
                return user;
            }
            case "view/product.fxml" -> {
                if (product == null) {
                    product = fxmlLoader.load();
                }
                return product;
            }
            case "view/employee.fxml" -> {
                if (employee == null) {
                    employee = fxmlLoader.load();
                }
                return employee;
            }
            case "view/config/admin.fxml"->{
                if (admin == null) {
                    admin = fxmlLoader.load();
                }
                return admin;
            }
            case "view/configuration.fxml" -> {
                if (configuration == null) {
                    configuration = fxmlLoader.load();
                }
                return configuration;
            }
            case "view/config/businessInfo.fxml" -> {
                if (businessInfo == null) {
                    businessInfo = fxmlLoader.load();
                }
                return businessInfo;
            }
            case "view/config/invoice.fxml" -> {
                if (invoice == null) {
                    invoice = fxmlLoader.load();
                }
                return invoice;
            }
            case "view/inventory.fxml"->{
                if (inventory == null){
                    inventory = fxmlLoader.load();
                }
                return inventory;
            }
            case "view/role.fxml"->{
                if (role ==null){
                    role = fxmlLoader.load();
                }
                return role;
            }
            case "view/queries/queries.fxml"->{
                if (queries == null){
                    queries = fxmlLoader.load();
                }
                return queries;
            }
            case "view/queries/productSold.fxml"->{
                if (product ==null){
                    productSold = fxmlLoader.load();
                }
                return productSold;
            }
            case "view/queries/bestEmployee.fxml"->{
                if (bestEmployee ==null){
                    bestEmployee = fxmlLoader.load();
                }
                return bestEmployee;
            }


            case "view/queries/bestSellingProduct.fxml"->{
                if (bestSellingProduct ==null){
                    bestSellingProduct = fxmlLoader.load();
                }
                return bestSellingProduct;
            }
            case "view/queries/frequentCustomers.fxml"->{
                if (frequentCustomer ==null){
                    frequentCustomer = fxmlLoader.load();
                }
                return frequentCustomer;
            }
            case "view/queries/productExpired.fxml"->{
                if (productExpired ==null){
                    productExpired = fxmlLoader.load();
                }
                return productExpired;
            }
            case "view/queries/productRemaining.fxml"->{
                if (productRemaining ==null){
                    productRemaining = fxmlLoader.load();
                }
                return productRemaining;
            }
            case "view/queries/productReturned.fxml"->{
                if (returned ==null){
                    returned = fxmlLoader.load();
                }
                return returned;
            }
            case "view/queries/spendingCustomers.fxml"->{
                if (spendingCustomer==null){
                    spendingCustomer = fxmlLoader.load();
                }
                return spendingCustomer;
            }



            default -> {
                System.err.println("Error: Unknown view path");
                return null;
            }
        }
    }

}
