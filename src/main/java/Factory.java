import com.sonderben.trust.HelloApplication;
import javafx.scene.image.Image;

public class Factory {

    public static Image createImage(String path){
        return new Image(HelloApplication.class.getResource(path).toString());
    }
}
