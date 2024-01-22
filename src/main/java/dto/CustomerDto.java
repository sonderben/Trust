package dto;

import com.sonderben.trust.db.CrudRepositoryImpl;
import entity.CustomerEntity;
import entity.EmployeeEntity;
import org.hibernate.Session;

import javax.annotation.Nonnull;
import java.util.List;

public class CustomerDto extends CrudRepositoryImpl<CustomerEntity,Long> {

    public CustomerEntity findById(@Nonnull Long id)  {

        Session session = this.getSessionFactory().openSession();

        CustomerEntity e= session.get(CustomerEntity.class, id);

        session.close();
        return e;
    }
    public List<CustomerEntity> findAll() throws Exception {
        var session = getSessionFactory().openSession();
        var a = session.createQuery("select e from CustomerEntity e", CustomerEntity.class).getResultList();
        session.close();
        return a;
    }

}
