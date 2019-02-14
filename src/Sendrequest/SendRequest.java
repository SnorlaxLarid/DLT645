package Sendrequest;

import CollectData.*;
import Driver.Driver;
import Server.Server;
import WindowsFrame.WindowsFrame;
import com.mysql.jdbc.log.NullLogger;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/** 这个线程用来给所有ctx发送数据请求**/
public class SendRequest extends Thread
{
    private Connection connection = Driver.getConnection();
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet  = null;
    private CopyOnWriteArraySet<String> loginCodeSet =  new  CopyOnWriteArraySet ();
    private   HashMap<ChannelHandlerContext, List<HashMap>> ctxInfoMap = new HashMap();
    private   HashMap<ChannelHandlerContext, Integer> ammeterNumMap = new HashMap<>();
    private   ArrayList<ChannelHandlerContext> ctxList = new ArrayList<>();


    public void  run() {
        while (true) {
            if (Server.loginCodeMap.keySet().size() == 0) {
                System.out.println("Wait for connecting");
                //若当前没有连接，暂停10s
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {


                //获取ctxMap中的所有loginCode
//                Set<String> loginCodeSet = Server.loginCodeMap.keySet();

                //将要进行数据请求的ctx与电表具体信息
//                HashMap<ChannelHandlerContext, List<HashMap>> ctxInfoMap = new HashMap();
                //存放每个ctx对应的电表数目的信息
//                HashMap<ChannelHandlerContext, Integer> ammeterNumMap = new HashMap<>();

                //根据每个ctx对应的电表数目从小到大排序的List
//                ArrayList<ChannelHandlerContext> ctxList = new ArrayList<>();

                refresh();
                //通过loginCode从数据库里找到电表的信息
                for (String loginCode : loginCodeSet) {

                    String sql = "SELECT *FROM ammeter_info WHERE loginCode =" + " '" + loginCode + "'";
                    // PreparedStatement preparedStatement = null;
                    try {
                        preparedStatement = connection.prepareStatement(sql);
                        resultSet = preparedStatement.executeQuery();

                        List<HashMap> ammeterList_ctx = new ArrayList<>();
                        int num = 0;
                        while (resultSet.next()) {
                            String address = resultSet.getString("Address");
                            String type = resultSet.getString("AmmeterType");
                            HashMap ammeterInfo = new HashMap();
                            ammeterInfo.put("address", address);
                            ammeterInfo.put("type", type);

                            ammeterList_ctx.add(ammeterInfo);
                            num++;
                        }
                        ChannelHandlerContext ctx = Server.loginCodeMap.get(loginCode);
                        ctxInfoMap.put(ctx, ammeterList_ctx);
                        ammeterNumMap.put(ctx, num);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }


                /**根据ctx中对应的最大电表数确定传输的顺序与延时**/
                //需要知道：每个ctx中所对应的表的数量
                //          每个表的类型，因为97与07所发送的命令帧的数量是不同的,以命令数多的类型为准

                /**首先根据ctx对应的电表数目由多到少排序,并去除掉电表数为0的ctx**/

                orderByValue(ctxList, ammeterNumMap);
                sendRequestByOrder(ctxInfoMap, ammeterNumMap, ctxList);
            }

        }
    }

    /**
     * //流程：给第一个ctx发送25中的第一个 →给第二个ctx发送25中的第一个 。。。。
     *
     *       给第一个ctx发送25中的第二个 →给第二个ctx发送25中的第二个。。。→第一台表完成，删掉所有ctx的第一台表
     *       例：ctx1--3台，ctx2--7台，ctx3--1台，ctx4--6台
     *             step1：首先根据电表数量从大到小排序 7，6，3，1（ctx2，ctx4，ctx2，ctx3）
     *             step2：7，6，3，1→  6，6，3，1 → 3，3，3，1 → 1，1，1，1 → 0，0，0，0**/
    private void  sendRequestByOrder(HashMap<ChannelHandlerContext,List<HashMap>> ctxInfoMap,HashMap<ChannelHandlerContext,Integer> ammeterNumMap,ArrayList<ChannelHandlerContext> ctxList)
    {
        int ctxNum = ctxList.size();
        for(int i=0; i<ctxNum;i++)
        {
            //senNum是第i次应该发送请求帧的电表数量
            int sendNum = 0;
            if(i == ctxNum -1)
            {
                ChannelHandlerContext ctxLast = ctxList.get(i);
                sendNum = ammeterNumMap.get(ctxLast);
            }else {
                ChannelHandlerContext ctxMore = ctxList.get(i);
                ChannelHandlerContext ctxLess = ctxList.get(i + 1);
                sendNum = ammeterNumMap.get(ctxMore) - ammeterNumMap.get(ctxLess);
            }


            for(int j=0; j<sendNum; j++)
            {


                //一次完整的请求命令帧数量25在外循环
                for(int k=0; k<25; k++)
                {
                    //第i次只对i+1个ctx发送请求
                    for(int p=0; p<i+1; p++)
                    {
                        //获取所要发送请求的ctx的电表信息
                        ChannelHandlerContext ctxToSend = ctxList.get(p);
                        HashMap <String,String>ammeterToSendInfoMap = ctxInfoMap.get(ctxToSend).get(0);
                        String address = ammeterToSendInfoMap.get("address");
                        String type = ammeterToSendInfoMap.get("type");

                        if(type.equals("2007"))
                        {
                            CollectDataByFrame2007 c2007 = new CollectDataByFrame2007(ctxToSend,address,k);
                            c2007.work();
                        }
                        else if(type.equals("1997"))
                        {
                            CollectDataByFrame1997 c1997 = new CollectDataByFrame1997(ctxToSend,address,k);
                            c1997.work();
                        }
                        //最后一条发完了，移除该电表
                        if(k>=24)
                        {
                            ctxInfoMap.get(ctxToSend).remove(0);
                        }

                        try {
                            Thread.sleep(24/(i+1));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void orderByValue(ArrayList<ChannelHandlerContext> ctxList ,HashMap<ChannelHandlerContext,Integer> ammeterNumMap) {
        Set<ChannelHandlerContext> ctxSet = ammeterNumMap.keySet();
        HashSet<ChannelHandlerContext> keySet = new HashSet<ChannelHandlerContext>();

        //先把ammeterNumMap中的ctxSet复制过来
        for(ChannelHandlerContext key: ctxSet)
        {
            keySet.add(key);
        }
        int ctxNum = keySet.size();


        for (int i = 0; i < ctxNum; i++) {
            int max = 0;
            ChannelHandlerContext maxKey = null;
            for (ChannelHandlerContext key : keySet) {

//                if(ammeterNumMap.get(key) == 0)
//                {
//                   continue;
//                }
                if( ammeterNumMap.get(key) >= max)
                {
                    max =  ammeterNumMap.get(key);
                    maxKey = key;
                }
            }
            ctxList.add(maxKey);
            keySet.remove(maxKey);

        }
    }

    public void refresh()
    {

        loginCodeSet.clear();
        loginCodeSet = null;
        loginCodeSet = new  CopyOnWriteArraySet (Server.loginCodeMap.keySet());

        ctxInfoMap.clear();
        ammeterNumMap.clear();
        ctxList.clear();

    }
}
