package Driver;

import java.sql.*;


public class Driver {

private static final String URL="yourMysqlURL";
    private static final String USER="yourusername";
    private static final String PASSWORD="yourpassword";
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
