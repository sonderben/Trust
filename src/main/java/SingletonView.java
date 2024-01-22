import com.sonderben.trust.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class SingletonView {

    private static Node nodeEmployee, nodeSale,nodeUser,nodeProduct;
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
                return nodeSale;//new FXMLLoader(HelloApplication.class.getResource(path)).load();
            }
            case "view/user.fxml" -> {
                if (nodeUser == null) {
                    nodeUser = fxmlLoader.load();
                    return nodeUser;
                }
                return nodeUser;//new FXMLLoader(HelloApplication.class.getResource(path)).load();
            }
            case "view/product.fxml" -> {
                if (nodeProduct == null) {
                    nodeProduct = fxmlLoader.load();
                    return nodeProduct;
                }
                return nodeProduct;//new FXMLLoader(HelloApplication.class.getResource(path)).load();
            }
            case "view/employee.fxml" -> {
                if (nodeEmployee == null) {
                    nodeEmployee = fxmlLoader.load();
                    return nodeEmployee;
                }
                return nodeEmployee;//new FXMLLoader(HelloApplication.class.getResource(path)).load();
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
