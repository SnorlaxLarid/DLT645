package Driver;

import java.sql.*;

/**
 * Created by Administrator on 2017/6/28 0028.
 */
public class Driver {
//    private static final String URL="jdbc:mysql://47.93.235.197:3306/电力大数据?useSSL=false";
//    private static final String USER="Taylor";
//    private static final String PASSWORD="Qian";
private static final String URL="jdbc:mysql://localhost:3306/ebigdata?useSSL=false";
    private static final String USER="root";
    private static final String PASSWORD="icanfly";
    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection= DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
        }
    }
    public static Connection getConnection()
    {
        return connection;
    }
    /*
    * Test the Mysql connector
    * */
    public static void main(String[] args)
    {
        Connection connection= Driver.getConnection();
        String type = null;
        String sql="SELECT *FROM userlist WHERE AmmeterAddress = '000000000006'";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()) {
                type=resultSet.getString("AmmeterType");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.print(type);
    }
}
