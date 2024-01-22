package dto;

import com.sonderben.trust.db.CrudRepositoryImpl;
import entity.EmployeeEntity;
import entity.InvoiceEntity;
import entity.ProductEntity;
import org.hibernate.Session;

import javax.annotation.Nonnull;
import java.util.List;

public class ProductDto extends CrudRepositoryImpl<ProductEntity,Long> {
    public ProductEntity findById(@Nonnull Long id)  {

        Session session = this.getSessionFactory().openSession();

        ProductEntity e= session.get(ProductEntity.class, id);

        session.close();
        return e;
    }
    public List<ProductEntity> findAll() throws Exception {
        var session = getSessionFactory().openSession();
        var a = session.createQuery("select e from ProductEntity e", ProductEntity.class).getResultList();
        session.close();
        return a;
    }

    public ProductEntity findByCode(@Nonnull String code1){
        Session session = this.getSessionFactory().openSession();
        var a= session.createQuery("select p from ProductEntity p where p.code =:code", ProductEntity.class)
                .setParameter("code",code1).getSingleResult();
        session.close();
        return a;
    }
}
