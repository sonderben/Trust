package entity;

import com.sonderben.trust.constant.ScreenEnum;

import java.util.List;

public class Role extends BaseEntity {
    private String name; private List<ScreenEnum> screens;
    public Role (String name, List<ScreenEnum> screens){
        this.name = name;
        this.screens = screens;
    }
    public Role(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ScreenEnum> getScreens() {
        return screens;
    }

    public void setScreens(List<ScreenEnum> screens) {
        this.screens = screens;
    }
}
