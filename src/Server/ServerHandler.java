package Server;



import OtherFuction.GetTime;
import OtherFuction.ToHex;
import ReadData.ReadData1997;
import ReadData.ReadData2007;
import WindowsFrame.WindowsFrame;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


import java.net.InetSocketAddress;
import java.util.Collection;
import org.apache.log4j.Logger;



public class ServerHandler extends ChannelInboundHandlerAdapter
{
    private static int connectNum;

    private static int getConnectNum()
    {
        return ServerHandler.connectNum;
    }

    private static void setConnectNum(int connectNum)
    {
        ServerHandler.connectNum = connectNum;
    }
    private static Logger logger = Logger.getLogger(ServerHandler.class);




    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /**建立连接后连接数+1**/

        System.out.println("--- A New Client ---");
        int connectNum = getConnectNum() + 1 ;

        setConnectNum(connectNum);
        //System.out.println(connectNum);
        WindowsFrame.count.setText("The Number of Client :" + connectNum+"");
        WindowsFrame.Message.setText("someone is connecting!!");

        /**给该从站发送请求电表地址的命令，判断电表类型**/

        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIP = insocket.getAddress().getHostAddress();
        int port = insocket.getPort();
        WindowsFrame.textArea2007.append("ip: "+clientIP + " port:" + port+"\n");


    }

    /**
     * 先解析数据，然后根据特征位判断返回的应答种类，之后进行相应的操作
     * **/
    @Override
    public void channelRead (ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println(Thread.currentThread().getName());

        ByteBuf buf  = (ByteBuf) msg;
        /** req里保存Byte数组*/
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);


        if(req.length >8) {


            /** 返回的为2007电表的各项数据，进行解析并写入数据库 **/
             if (req[8] == (byte) 0x91 || req[8] == (byte) 0xb1) {
                String result = null;
                 ReadData2007 readData2007 = new ReadData2007();
                 result = readData2007.Read_data(req);
                 readData2007 = null;
//            buffer[0]=0x11;
                switch (result) {
                    case "bit":
                        WindowsFrame.textArea2007.append("It is just bit!" + "\n");
                        break;
                    case "error":
                        WindowsFrame.textArea2007.append(GetTime.NowTime() + " " + "异常应答：" + ToHex.ToHex(req) + "\n");
                        break;
                    case "checking_error":
                        WindowsFrame.textArea2007.append(GetTime.NowTime() + " " + "验证异常：" + ToHex.ToHex(req) + "\n");
                        break;
                    default:
                        WindowsFrame.textArea2007.append("completed !!" + "\n");
                }
                result = null;
            }
            /**2007从站异常应答**/
            else if ((req[8] | 0xf0) == 0xd0) {
                WindowsFrame.textArea2007.append("Error2007 * 1 ");
            }


            /** 1997电表返回数据**/
            else if (req[8] == (byte) 0x81) {
                String result = null;
                ReadData1997 readData1997 = new ReadData1997();
                result = readData1997.Read_data(req);
                readData1997 = null;
                switch (result) {
                    case "bit":
                        WindowsFrame.textArea1997.append("It is just bit!" + "\n");
                        break;
                    case "error":
                        WindowsFrame.textArea1997.append(GetTime.NowTime() + " " + "异常应答：" + ToHex.ToHex(req) + "\n");
                        break;
                    case "checking_error":
                        WindowsFrame.textArea1997.append(GetTime.NowTime() + " " + "验证异常：" + ToHex.ToHex(req) + "\n");
                        break;
                    default:
                        WindowsFrame.textArea1997.append("completed !!" + "\n");
                }
                result = null;

            }
        }


        /**最后判断是不是登录检测码**/
        for(int i=0; i<req.length; i++) {
            if(req[i] < 32 || req[i]> 127){
                break;
            }
            //req里装的是登录检测码or heartbit
            if(i >= req.length-1) {
                //log
                this.logger.info(new String(req));

                String loginCode = new String(req);
                System.out.println(loginCode);
//                if( !  Server.loginCodeMap.containsKey(loginCode)){
//                //添加到通道键值对中
//                Server.loginCodeMap.put(loginCode,ctx);
//                }
                Server.loginCodeMap.put(loginCode,ctx);

            }
        }
        buf.release();
        buf = null;




    }

    /** 连接中断，去除ctxMap中相应的ctx**/
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        int connectNum = getConnectNum() - 1 ;
        setConnectNum(connectNum);




        if(connectNum == 0)
        {
            WindowsFrame.Message.setText("All Connections Are Closed!!");
        }
        //System.out.println(connectNum);
        WindowsFrame.count.setText("The Number of Client :" + connectNum+"");
        Collection values = Server.loginCodeMap.values();
        values.remove(ctx);
    }


    @Override
    public  void exceptionCaught(ChannelHandlerContext ctx, Throwable t)
    {
        ;
    }

}
