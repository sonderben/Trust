import com.sonderben.trust.HelloApplication;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;

import java.util.Objects;
import java.util.function.UnaryOperator;

public class Factory {

    public static Image createImage(String path){

        return new Image( Objects.requireNonNull( HelloApplication.class.getResource(path) ).toString() );
    }

    public static TextFormatter<String> intFilterTextField(){
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if ( text.matches("\\d*")  ){
                return change;
            }
            return null;
        };


        return new TextFormatter<String>(filter);
    }

    public static TextFormatter<String> doubleFilterTextField(){
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("-?\\d*\\.?\\d*")){//if (text.matches("^\\d*\\.\\d+$")){
                return change;
            }
            return null;
        };


        return new TextFormatter<String>(filter);
    }


}
