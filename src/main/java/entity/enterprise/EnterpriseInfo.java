package entity.enterprise;

import entity.BaseEntity;

public class EnterpriseInfo extends BaseEntity {

    public EnterpriseInfo(){}
    private String path,name;

    @Override
    public String toString() {
        return "EnterpriseInfo{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnterpriseInfo(String path, String name) {
        this.path = path;
        this.name = name;
    }
}
