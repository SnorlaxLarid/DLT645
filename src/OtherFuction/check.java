package OtherFuction;

import static java.lang.System.arraycopy;

/**
 * Created by Administrator on 2017/7/1 0001.
 */
public class check {
    public static String GetBCDAddress(byte[] address) {
        byte[] byteAddress = new byte[6];
        arraycopy(address, 1, byteAddress, 0, 6);
        String addr = "";
        String EachByteAddress;
        int bitdata;
        for (byte addr_b : byteAddress) {
            if (addr_b<0)
            {
                bitdata=addr_b+256;
            }
            else
            {
                bitdata=addr_b;
            }
            EachByteAddress = bitdata / 16+""+ bitdata % 16 ;
            addr = EachByteAddress + addr;
        }
        return addr;
    }
    public static boolean checkData(byte[] data)
    {
        byte s=0;
        int j;
        for (j=data.length-1;j>0;j--)
        {
            if(data[j]==22)
            {
                break;
            }
        }

        for (int i=0;i<j-1;i++)
        {
            s += (data[i] & 0xff) % 256;
        }
        //System.out.println(s);
        if (s==data[j-1])
        {
            return true;
        }else {
            return  false;
        }
    }
}
