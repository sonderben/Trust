package dto;

import com.sonderben.trust.db.CrudRepositoryImpl;
import entity.CustomerEntity;
import entity.EmployeeEntity;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class EmployeeDto extends CrudRepositoryImpl<EmployeeEntity,Long> {
    public EmployeeEntity logIn(String userName,String password){
        try {
            EmployeeEntity employee = findByUsername( userName );
            if (employee.getPassword().equals(password)){
                return employee;
            }else
                return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public EmployeeEntity  findById( @Nonnull Long id)  {

        Session session = this.getSessionFactory().openSession();

        EmployeeEntity e= session.get(EmployeeEntity.class, id);

        session.close();
        return e;
    }
    public List<EmployeeEntity> findAll() throws Exception {
        var session = getSessionFactory().openSession();
        var a = session.createQuery("select e from EmployeeEntity e", EmployeeEntity.class).getResultList();
        session.close();
        return a;
    }

    public EmployeeEntity findByUsername(@Nonnull String username){
        Session session = this.getSessionFactory().openSession();
        var a= session.createQuery("select e from EmployeeEntity e where e.userName =:userName", EmployeeEntity.class)
                .setParameter(username,String.class).getSingleResult();
        session.close();
        return a;
    }
}













