package WriteDataToDatabase;

import Driver.Driver;
import WindowsFrame.WindowsFrame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class WriteDataToDatabase {
    Connection connection= Driver.getConnection();
    PreparedStatement preparedStatement= null;
    ResultSet resultSet = null;
    public void write(String type, double value, String address){

        String sql="SELECT *FROM ammeter_info WHERE Address ="+" "+address ;

        //PreparedStatement preparedStatement= null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            String s;
            List<String> userList = new ArrayList<>();
            while (resultSet.next())
            {
                s=resultSet.getString("Address");
                userList.add(s);
            }
            if(userList.isEmpty())
            {
                sql="INSERT INTO ammeter_info (Address) VALUES"+" ("+address+")";
                preparedStatement=connection.prepareStatement(sql);
                preparedStatement.execute();
            }
            System.out.println("check Address");
            userList.clear();
            //在保证用户已在数据库中，在进行数据添加

            int count;
            Calendar calendar = Calendar.getInstance();
            int min = calendar.get(Calendar.MINUTE);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            count = hour * 4 + (min / 15);
            sql = "SELECT *FROM" + " " + type + " " + "WHERE Date=CURRENT_DATE AND Address = " + address;
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                s = resultSet.getString("Address");
                userList.add(s);
            }
            if (userList.isEmpty()) {
                System.out.println("new a record");
                sql = "INSERT INTO" + " " + type + " " + "(ADDRESS,DATE )" + " " + "VALUES"
                        + " " + "(?,CURRENT_DATE)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, address);
                preparedStatement.execute();
            }
            System.out.println(count);
            userList.clear();
            sql = "UPDATE" + " " + type + " " + "SET " + " " + "Data" + count + " " + "=" + " " + value + " " + "WHERE DATE= CURRENT_DATE AND ADDRESS=" + " " + address;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            preparedStatement.close();
            resultSet.close();
            preparedStatement = null;
            resultSet = null;
            WindowsFrame.Message.setText("Write Data to Database successful !!!");
        } catch (SQLException e) {
            WindowsFrame.textArea1997.append(e.toString());
            e.printStackTrace();
            WindowsFrame.Message.setText("Write Data to Database failed !!!");
        }

    }
    /*
    * test the WriteDataToDatabase Programmer
    * */
    public static void main(String[] Args)
    {
        WriteDataToDatabase writeToDataBase=new WriteDataToDatabase();
        writeToDataBase.write("pos_positive_power",600,"000000000006");
    }
}
