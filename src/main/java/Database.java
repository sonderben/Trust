import com.sonderben.trust.constant.Action;
import com.sonderben.trust.constant.ScreenEnum;
import com.sonderben.trust.db.SqlCreateTables;
import com.sonderben.trust.model.Screen;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Database {
    private static Connection connection;

    private Database(){}

    public static synchronized Connection connect(){
        try {
            if (connection==null || connection.isClosed()){
                Class.forName( "org.sqlite.JDBC" );
                connection = DriverManager.getConnection("jdbc:sqlite:data/midb.db"/*,"",""*/);
                System.out.println(" connection open");
                //deleteTables();
                createTable();
                return connection;
            }
             return connection;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private static void createTable() throws SQLException {
        Statement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();

            List<String> tables = SqlCreateTables.INSTANCE.getTables();
            for (String table : tables) {
                statement.execute(table);
            }
            connection.commit();
            System.out.println("Tables create successfully");
        } catch (SQLException e) {
            connection.rollback();
            System.out.println("can not create tables");
            throw new RuntimeException(e);
        }finally {
            connection.setAutoCommit(true);
        }

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
            Statement statement = Database.connect().createStatement();
            ResultSet resultSet = statement.executeQuery(" SELECT last_insert_rowid() as last_id ");
            return resultSet.getLong( "last_id" );

    }

    public static List<Screen> findScreenByIdRole(  Long roleId)  {

        Connection connection1 = Database.connect();
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
}
