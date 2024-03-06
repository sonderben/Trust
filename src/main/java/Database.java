import com.sonderben.trust.constant.Action;
import com.sonderben.trust.constant.ScreenEnum;
import com.sonderben.trust.db.SqlCreateTables;
import com.sonderben.trust.db.dao.*;
import com.sonderben.trust.model.Role;
import com.sonderben.trust.model.Screen;
import entity.*;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class Database {
    private static Connection connection;
    public static String DATABASE_NAME = "trust.db";
    public static String TRUST_DB = "trust.db";
    //midb.db

    private Database(){}

    public static synchronized Connection connect(String dbName){
        try {

            if (connection==null || connection.isClosed()){

                DATABASE_NAME = dbName;


                Class.forName( "org.sqlite.JDBC" );
                connection = DriverManager.getConnection("jdbc:sqlite:data/midb.db"/*,"",""*/);
                return connection;


            }

             return connection;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static void createTable() throws SQLException {

        DATABASE_NAME = "midb.db";
        Statement statement;
        String tableError = "";
        try(Connection connection1 = Database.connect(DATABASE_NAME)) {
            connection1.setAutoCommit(false);
            statement = connection1.createStatement();

            List<String> tables = SqlCreateTables.INSTANCE.getTables();
            for (String table : tables) {
                tableError = table;
                statement.execute(table);
            }
            connection1.commit();
            connection1.setAutoCommit(true);
            statement.close();
            System.out.println("Tables create successfully");

        } catch (SQLException e) {

            System.out.println("can not create tables");
            System.out.println("error on "+tableError);
            System.err.println( e.getMessage() );
            throw new RuntimeException(e);
        }


    }
    public static void createTrustTables()throws SQLException{
        String tableError = "";
        try(Connection conn = Database.connect(TRUST_DB)) {
            conn.setAutoCommit(false);
            Statement st = conn.createStatement();
            List<String> tables = SqlCreateTables.INSTANCE.getTrustTables();
            for (String table : tables){
                tableError = table;
                st.execute( table );
            }
            conn.commit();
            conn.setAutoCommit(true);
            st.close();
        }catch (SQLException e){
            System.out.println("can not create trust tables");
            System.out.println("error on "+tableError);
            System.err.println( e.getMessage() );
            throw new RuntimeException(e);
        }
    }
    public static void prepopulate(){
        CategoryDao.INSTANCE.save(
                new CategoryEntity("0000","General",0)
        );



        RoleDao.INSTANCE.save(
                new Role(
                        "Admin",List.of(
                                new Screen( ScreenEnum.LOGIN,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                        new Screen( ScreenEnum.LOGIN,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                        //new Screen( ScreenEnum.INVENTORY,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                        new Screen( ScreenEnum.ROLE,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                        new Screen( ScreenEnum.USER,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                        new Screen( ScreenEnum.SALE,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                        new Screen( ScreenEnum.PRODUCT,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                        new Screen( ScreenEnum.EMPLOYEE,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                        new Screen( ScreenEnum.CONFIGURATION,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                        new Screen( ScreenEnum.QUERIES,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) ),
                        new Screen( ScreenEnum.CUSTOMER_SERVICE,List.of(Action.ADD,Action.READ,Action.UPDATE,Action.DELETE) )

                )
                )
        );
        Role role = new Role();
        role.setId(1L);


        RoleDao.INSTANCE.save(
                new Role("Saler",List.of(
                        new Screen(ScreenEnum.SALE,List.of(
                                Action.DELETE,Action.ADD,Action.UPDATE,Action.READ
                        ))
                ))
        );
        Role role2 = new Role();
        role2.setId(2L);
        EmployeeDao.INSTANCE.save(
                new EmployeeEntity("Sale","1234551","Pierre","Female","PV","pierresophie@gmail.com","8293045678", Calendar.getInstance(),"123-2342-3423","sofi","1234",role2,List.of())
        );

        CustomerDao.INSTANCE.save(
                new CustomerEntity("000000000001","Jean","1234","Pierre","male","PV","jean@gmail.com","34543454",Calendar.getInstance(),0L)
        );
        CategoryEntity category = new CategoryEntity();
        category.setId(1L);
        EmployeeEntity employee = new EmployeeEntity();
        employee.setId(1L);
        ProductDao.INSTANCE.save(
                new ProductEntity("1","Pan",10,12,0,0,15,15,Calendar.getInstance(),Calendar.getInstance(),category,employee)
        );

        ProductDao.INSTANCE.save(
                new ProductEntity("2","Hard disk 2GB",3000,3601,0,21,5,5,Calendar.getInstance(),Calendar.getInstance(),category,employee)
        );
    }

    private static void deleteTables()  {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(SqlCreateTables.INSTANCE.getDeleteCategoryTable());
            System.out.println("Tables delete successfully");
        } catch (SQLException e) {
            System.out.println("can not delete tables");
            throw new RuntimeException(e);
        }

    }
    public static Long getLastId() throws SQLException {
            Statement statement = Database.connect(DATABASE_NAME).createStatement();
            ResultSet resultSet = statement.executeQuery(" SELECT last_insert_rowid() as last_id ");
            return resultSet.getLong( "last_id" );

    }

    public static List<Screen> findScreenByIdRole(  Long roleId)  {

        Connection connection1 = Database.connect(DATABASE_NAME);
        List<Screen> screens = new ArrayList<>();
        try(PreparedStatement ps = connection1.prepareStatement("SELECT * FROM "+SqlCreateTables.screen+" WHERE id_role = ?")){
            ps.setLong(1, roleId);
            ResultSet resultSet = ps.executeQuery();

           while (resultSet.next()){
               Screen screen = new Screen();
               screen.setId( resultSet.getLong("id") );
               screen.setScreen(ScreenEnum.valueOf(resultSet.getString("screenEnum")));
               List<Action> actionList = Arrays.stream(resultSet.getString("actions").split(","))
                       .map(Action::valueOf)
                       .collect(Collectors.toList());
               screen.setActions( actionList );

               screens.add( screen );

           }


        }catch (Exception e){}

        return screens;
    }

    public static List<ScheduleEntity> findScheduleByIdEmployee(  Long employeeId)  {


        List<ScheduleEntity> schedules = new ArrayList<>();
        try(PreparedStatement ps = Database.connect(DATABASE_NAME).prepareStatement("SELECT * FROM "+SqlCreateTables.schedules+" WHERE id_employee = ?")){
            ps.setLong(1, employeeId);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()){

                ScheduleEntity schedule = new ScheduleEntity();
                schedule.setId( resultSet.getLong("id") );
                schedule.setWorkDay( resultSet.getInt("workDay") );
                schedule.setStartHour( resultSet.getFloat("start_hour") );
                schedule.setEndHour( resultSet.getFloat("end_hour") );
                schedules.add( schedule );

            }


        }catch (Exception e){}

        return schedules;
    }

    public static Role findRolesByIdEmployee(Long roleId){
        if (roleId ==null){
            return new Role();
        }
        List<Role>roleList = new ArrayList<>();
        final var selectAllIdRole = """
                SELECT * from Roles 
                where id = ? 
                """;
        try {
            PreparedStatement ps = Database.connect(DATABASE_NAME).prepareStatement(selectAllIdRole);
            ps.setLong(1,roleId);
            ResultSet resultSet =  ps.executeQuery();


                String roleName = resultSet.getString("name");
                List<Screen> screens = findScreenByIdRole(roleId);
                Role role = new Role(roleName,screens);
                role.setId( roleId );
                roleList.add( role );

            return role;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
