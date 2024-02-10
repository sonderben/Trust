import com.sonderben.trust.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class SingletonView {

    private static Node nodeEmployee,nodeConfiguration, nodeSale,nodeUser,nodeProduct,
            nodeInvoice,nodeBusinessInfo,nodeInventory,nodeRole,nodeQueries,nodeProductSold;

    private SingletonView(){

    }
    public static Node get(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(path));
        switch (path) {
            case "view/sale.fxml" -> {
                if (nodeSale == null) {
                    nodeSale = fxmlLoader.load();
                    return nodeSale;
                }
                return nodeSale;
            }
            case "view/user.fxml" -> {
                if (nodeUser == null) {
                    nodeUser = fxmlLoader.load();
                    return nodeUser;
                }
                return nodeUser;
            }
            case "view/product.fxml" -> {
                if (nodeProduct == null) {
                    nodeProduct = fxmlLoader.load();
                    return nodeProduct;
                }
                return nodeProduct;
            }
            case "view/employee.fxml" -> {
                if (nodeEmployee == null) {
                    nodeEmployee = fxmlLoader.load();
                    return nodeEmployee;
                }
                return nodeEmployee;
            }
            case "view/configuration.fxml" -> {
                if (nodeConfiguration == null) {
                    nodeConfiguration = fxmlLoader.load();
                    return nodeConfiguration;
                }
                return nodeConfiguration;
            }
            case "view/config/businessInfo.fxml" -> {
                if (nodeBusinessInfo == null) {
                    nodeBusinessInfo = fxmlLoader.load();
                    return nodeBusinessInfo;
                }
                return nodeBusinessInfo;
            }
            case "view/config/invoice.fxml" -> {
                if (nodeInvoice == null) {
                    nodeInvoice = fxmlLoader.load();
                    return nodeInvoice;
                }
                return nodeInvoice;
            }
            case "view/inventory.fxml"->{
                if (nodeInventory == null){
                    nodeInventory = fxmlLoader.load();
                    return nodeInventory;
                }
                return nodeInventory;
            }
            case "view/role.fxml"->{
                if (nodeRole==null){
                    nodeRole = fxmlLoader.load();
                    return nodeRole;
                }
                return nodeRole;
            }
            case "view/queries/queries.fxml"->{
                if (nodeQueries == null){
                    return nodeQueries = fxmlLoader.load();
                }
                return nodeQueries;
            }
            case "view/queries/productSold.fxml"->{
                if (nodeProduct==null){
                    return nodeProductSold = fxmlLoader.load();
                }
                return nodeProductSold;
            }
            default -> {
                System.err.println("Error: Unknown view path");
                return null;
            }
        }
    }
    /*public static Node get( String path ) throws IOException {
        var fxmlLoader = new FXMLLoader(HelloApplication.class.getResource( path ) );
        switch (path) {
            case "view/sale.fxml" -> {
                if (nodeSale == null) {

                    nodeSale = fxmlLoader.load();
                    node = nodeSale;

                    return node;
                }
            }
            case "view/user.fxml" -> {
                if (nodeUser == null) {
                    nodeUser = fxmlLoader.load();
                    node = nodeUser;
                    return node;
                }
            }
            case "view/product.fxml" -> {
                if (nodeProduct == null) {
                    nodeProduct = fxmlLoader.load();
                    node = nodeProduct;
                    return node;
                }
            }default -> {
                System.err.println(" errror");
            }

        }

        return node;
    }*/
}
