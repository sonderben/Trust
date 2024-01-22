package dto;

import com.sonderben.trust.db.CrudRepositoryImpl;
import entity.CustomerEntity;
import entity.InvoiceEntity;
import org.hibernate.Session;

import javax.annotation.Nonnull;
import java.util.List;

public class InvoiceDto extends CrudRepositoryImpl<InvoiceEntity,Long> {
    public InvoiceEntity findById(@Nonnull Long id)  {

        Session session = this.getSessionFactory().openSession();

        InvoiceEntity e= session.get(InvoiceEntity.class, id);

        session.close();
        return e;
    }
    public List<InvoiceEntity> findAll() throws Exception {
        var session = getSessionFactory().openSession();
        var a = session.createQuery("select e from InvoiceEntity e", InvoiceEntity.class).getResultList();
        session.close();
        return a;
    }
}
