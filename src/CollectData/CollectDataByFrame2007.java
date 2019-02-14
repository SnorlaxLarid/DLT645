package CollectData;


import Command.Command2007;
import DataIdentify.DataIdentify2007;
import OtherFuction.GetTime;
import OtherFuction.ToHex;
import WindowsFrame.WindowsFrame;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class CollectDataByFrame2007 {
    protected ChannelHandlerContext ctx;
    String address = "";
    int frameIndex;
    byte[] addressByte =new byte[6];
    public CollectDataByFrame2007(ChannelHandlerContext ctx,String address,int frameIndex){
        this.ctx = ctx;
        this.address =address;
        this.frameIndex = frameIndex;
        setAddrByte(address);
    }
    public  void setAddrByte(String addr)
    {
        for(int i=11; i>0; i-=2)
        {
            int a = addr.charAt(i);
            int b = addr.charAt(i-1) ;
            //由于地址是16进制，所以先进行判断
            //A - F
            if(a>=65 && a<=70)  {
                a -= 15;
            }else if(a>=97 && a<=102)
            {
                a -= 87;
            }else
            {
                a -= 48;
            }

            if(b>=65 && b<=70)  {
                b -= 15;
            }else if(b>=97 && b<=102)
            {
                b -= 87;
            }else
            {
                b -= 48;
            }

            this.addressByte[(11-i)/2] = (byte)(b*16+a);
        }
    }
    public void work()
    {
        DataIdentify2007 dataIdentify2007 = new DataIdentify2007();
        byte[] ID = dataIdentify2007.dataIdent.get(this.frameIndex);
        byte[] requestData = Command2007.generateRequest(this.addressByte, Command2007.REQUEST_DATA, 1, ID);
        try {
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer(requestData));
            String str_command = null;
            str_command = ToHex.ToHex(requestData);
            WindowsFrame.textArea2007.append(GetTime.NowTime() + " request data:" + str_command + "\n");

        } catch (Exception e) {
            try {
                ctx.close();
                WindowsFrame.textArea2007.append("Connection lost!!");

            } catch (Exception e1) {
                e1.getStackTrace();
            }

        }
    }
}


