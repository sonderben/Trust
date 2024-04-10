
import com.sonderben.trust.constant.ScreenEnum;
import com.sonderben.trust.db.SqlDdl;
import com.sonderben.trust.db.dao.*;
import com.sonderben.trust.db.service.ProductService;
import com.sonderben.trust.db.service.RoleService;
import com.sonderben.trust.model.Role;
import entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Database {
    private static Connection connection;

    public static String TRUST_DB = "trust.db";
    //midb.db

    private Database(){}

    public static synchronized Connection connect(){
        try {

            if (connection==null || connection.isClosed()){




                Class.forName( "org.sqlite.JDBC" );
                connection = DriverManager.getConnection("jdbc:sqlite:data/midb.db"/*,"",""*/);
                return connection;


            }

             return connection;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static void createTable() {


        Statement statement;
        String tableError = "";
        try(Connection connection1 = Database.connect()) {
            connection1.setAutoCommit(false);
            statement = connection1.createStatement();

            List<String> tables = SqlDdl.INSTANCE.getTables();
            for (String table : tables) {
                tableError = table;
                statement.execute(table);
            }
            connection1.commit();
            connection1.setAutoCommit(true);
            statement.close();


        } catch (SQLException e) {

            System.out.println("can not create tables");
            System.out.println("error on "+tableError);
            System.err.println( e.getMessage() );
            throw new RuntimeException(e);
        }


    }




    public static Long getLastId( Connection connection ) throws SQLException {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(" SELECT last_insert_rowid() as last_id ");
            return resultSet.getLong( "last_id" );

    }



    public static List<ScheduleEntity> findScheduleByIdEmployee(  Long employeeId)  {


        List<ScheduleEntity> schedules = new ArrayList<>();
        try(PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM "+ SqlDdl.schedules+" WHERE id_employee = ?")){
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


        }catch (Exception e){
            System.err.println(e.getMessage());
        }

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
            PreparedStatement ps = Database.connect().prepareStatement(selectAllIdRole);
            ps.setLong(1,roleId);
            ResultSet resultSet =  ps.executeQuery();


                String roleName = resultSet.getString("name");


            List<ScreenEnum> screensList = Arrays.stream(resultSet.getString("screens").split(","))
                    //.map(String::trim)
                    .map(ScreenEnum::valueOf)
                    .toList();


            Role role = new Role(roleName, screensList );
                role.setId( roleId );
                roleList.add( role );

            return role;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
