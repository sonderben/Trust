package dto;

import com.sonderben.trust.db.CrudRepositoryImpl;
import entity.CategoryEntity;
import entity.ProductEntity;
import org.hibernate.Session;

import javax.annotation.Nonnull;
import java.util.List;

public class CategoryDto extends CrudRepositoryImpl<CategoryEntity,Long> {
    public CategoryEntity findById(@Nonnull Long id)  {

        Session session = this.getSessionFactory().openSession();

        CategoryEntity e= session.get(CategoryEntity.class, id);

        session.close();
        return e;
    }
    public List<CategoryEntity> findAll() throws Exception {
        var session = getSessionFactory().openSession();
        var a = session.createQuery("select e from CategoryEntity e", CategoryEntity.class).getResultList();
        session.close();
        return a;
    }

}
