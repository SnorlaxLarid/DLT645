import Driver.Driver;
import Server.Server;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Test
{
    public static void main(String[] args)
    {
        HashMap<ChannelHandlerContext,Integer> ammeterNumMap = new HashMap<>();
        HashMap<ChannelHandlerContext,List<HashMap>> ctxMap = new HashMap();

        HashSet<String> loginCodeSet = new HashSet<>();
        loginCodeSet.add("12345678");
        loginCodeSet.add("text01");
        loginCodeSet.add("text02");
        for(String loginCode :loginCodeSet)
        {
        Connection connection= Driver.getConnection();
        String sql="SELECT *FROM ammeter_info WHERE loginCode ="+" '"+loginCode + "'";
        PreparedStatement preparedStatement= null;
        try{
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet=preparedStatement.executeQuery();

            List<HashMap> ammeterList_ctx = new ArrayList<>();
            int num =0;
            while (resultSet.next())
            {
                String address = resultSet.getString("Address");
                String type    = resultSet.getString("AmmeterType");
                HashMap ammeterInfo = new HashMap();
                ammeterInfo.put("address",address);
                ammeterInfo.put("type",type);

                ammeterList_ctx.add(ammeterInfo);
                num++;
            }
            ChannelHandlerContext ctx =  Server.loginCodeMap.get(loginCode);
            ctxMap.put(ctx,ammeterList_ctx);
            ammeterNumMap.put(ctx,num);
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println(ctxMap.values());
        System.out.println(ammeterNumMap.values());

        System.out.println("next");
        System.out.println("012345".charAt(4));

        HashMap map = new HashMap();
        map.put("1",1);
        map.put("2",2);
        Set set1 = map.keySet();
        Set set2 = null;
        set2.addAll(set1);
        set2.remove("2");
        System.out.println("这里"+map.values());
    }
}
}

