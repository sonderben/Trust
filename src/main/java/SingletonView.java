import com.sonderben.trust.HelloApplication;
import com.sonderben.trust.controller.BaseController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class SingletonView {

    /*private static Node node,employee, configuration, sale, user, product,
            invoice, businessInfo, inventory, role, queries, productSold,
            productRemaining, bestEmployee, bestSellingProduct, frequentCustomer,
            productExpired, returned,spendingCustomer,admin;*/
    private final static List<String> pathList =List.of("view/sale.fxml","view/user.fxml","view/product.fxml","view/employee.fxml",
            /*"view/config/admin.fxml",*/"view/configuration.fxml",/*"view/config/businessInfo.fxml","view/config/invoice.fxml",*/
            "view/inventory.fxml","view/role.fxml", "view/queries/queries.fxml","view/queries/productSold.fxml",
            "view/queries/bestEmployee.fxml","view/queries/bestSellingProduct.fxml","view/queries/frequentCustomers.fxml",
            "view/queries/productExpired.fxml","view/queries/productRemaining.fxml","view/queries/productReturned.fxml",
            "view/queries/spendingCustomers.fxml","view/customerService.fxml","view/technologies.fxml" );
    static BaseController controller = null;


    private SingletonView(){

    }

    public static Node get(String path) throws IOException {

        if (pathList.contains(path)){
            if (controller!=null){
                controller.onDestroy();
                controller = null;
            }

            ResourceBundle resourceBundle = ResourceBundle.getBundle("com.sonderben.trust.i18n.string");FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(path),resourceBundle);

            Node node= fxmlLoader.load();
            if (fxmlLoader.getController() instanceof BaseController){
                controller = fxmlLoader.getController();
            }

            return node;
        }


        System.err.println("Error: Unknown view path");
        return null;

    }

}
