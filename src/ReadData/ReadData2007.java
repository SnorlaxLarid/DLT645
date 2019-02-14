package ReadData;


import DataIdentify.DataIdentify2007;
import OtherFuction.GetTime;
import OtherFuction.ToHex;
import OtherFuction.check;
import WindowsFrame.WindowsFrame;
import WriteDataToDatabase.WriteDataToDatabase;

import java.util.Arrays;


/**
 * Created by Administrator on 2017/5/28 0028.
 */
public class ReadData2007 {
    private  int  lengthOfData;
    private  int doc;//确定小数点
    private  String Ammeter_Address;
    private WriteDataToDatabase writeToDataBase=new WriteDataToDatabase();
    public  String Read_data(byte[] bytes)
    {
        Ammeter_Address= check.GetBCDAddress(bytes);
        if (bytes[0]==0x00)
        {
            WindowsFrame.textArea2007.append("Client "+" logout"+"\n");
            return "logout";
        }
        if (bytes[0]==0x11)
        {
            WindowsFrame.textArea2007.append("This Is Heart bit"+"\n");
            return "bit";
        }
        System.out.println(Arrays.toString(bytes));
        boolean check= OtherFuction.check.checkData(bytes);
        System.out.println(check);
        if (!check)
        {
            return "checking_error";
        }
        else {
            int[] read_ints = new int[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                if ((int) bytes[i] > 0) {
                    read_ints[i] = (int) bytes[i];
                } else {
                    read_ints[i] = (int) bytes[i] + 256;
                }
            }
            int j = 0;
            if (read_ints[j] == 0x68) {
                System.out.print("start_byte  ");
            }
            switch (read_ints[8])//read_int[8]为控制码
            {
                case 0xD1:
                    return "error";
                case 0xD2:
                    return "error";
                //读数据且无后续帧
                case 0x91:
                    System.out.print("normally "+"\n");
                    lengthOfData = read_ints[9];
                    byte[] type = new byte[4];
                    byte[] data = new byte[lengthOfData - 4];
                    for (int t = 0; t < 4; t++) {
                        type[t] = (byte) (read_ints[10 + t] - 0x33);
                    }
                    for (int d = 0; d < lengthOfData - 4; d++) {
                        data[d] = (byte) (read_ints[14 + d] - 0x33);
                    }

                    for(int t=0;t<type.length;t++) {
                        System.out.println(type[t]);
                    }

                    DataIdentify2007 dataIdentify = new DataIdentify2007();
                    String datatpye = dataIdentify.identifyname.get(Arrays.toString(type));
                    doc =dataIdentify.doc.get(Arrays.toString(type));
                    int value = 0;
                    for (int v = 0; v < lengthOfData - 4; v++) {
                        value = v + (data[v] *10/ 16 + (data[v]) % 16) * 100 ^ v;
                    }
                    double dataresult= ((double)value)/(10^doc);
                    String str_addr= ToHex.ToHex(bytes);
                    WindowsFrame.textArea2007.append(GetTime.NowTime()+" Received  "+datatpye+" from "+Ammeter_Address+"  Client : "+str_addr+"\n");
                    WindowsFrame.textArea2007.append("*******************"+datatpye+" : "+dataresult+"***********************\n");
                   // WriteDataToDatabase writeToDataBase=new WriteDataToDatabase();
                    writeToDataBase.write(datatpye,dataresult,Ammeter_Address);
                    System.out.println("client "+Ammeter_Address+" write "+datatpye+" "+dataresult);
                    return Ammeter_Address;
            }
        }
        return "complete";
    }
}

