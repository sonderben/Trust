package dto;

import com.sonderben.trust.db.CrudRepositoryImpl;
import com.sonderben.trust.model.Role;
import entity.ProductEntity;
import org.hibernate.Session;

import javax.annotation.Nonnull;
import java.util.List;

public class RoleDto extends CrudRepositoryImpl<Role, Long>{
    public Role findById(@Nonnull Long id)  {

        Session session = this.getSessionFactory().openSession();

        Role e= session.get(Role.class, id);

        session.close();
        return e;
    }
    public List<Role> findAll() throws Exception {
        var session = getSessionFactory().openSession();
        var a = session.createQuery("select e from Role e", Role.class).getResultList();
        session.close();
        return a;
    }
}
