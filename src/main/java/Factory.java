import com.sonderben.trust.HelloApplication;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.web.HTMLEditor;

import java.util.function.UnaryOperator;

public class Factory {

    public static Image createImage(String path){
        return new Image(HelloApplication.class.getResource(path).toString());
    }

    public static TextFormatter<String> createFilterTextField(){
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("\\d?")){
                return change;
            }
            return null;
        };


        return new TextFormatter<String>(filter);
    }


}
