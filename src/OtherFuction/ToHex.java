package OtherFuction;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/8/15 0015.
 */
public class ToHex {
    static char[] BCDCode=new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','0'};
    public static String ToHex(byte[] bytes)
    {
        int[] read_ints = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            if ((int) bytes[i] > 0) {
                read_ints[i] = (int) bytes[i];
            } else {
                read_ints[i] = (int) bytes[i] + 256;
            }
        }
        System.out.println(Arrays.toString(read_ints));
        String str_addr = "";
        String EachByteAddress;
        for (int addr_b : read_ints) {
            EachByteAddress = BCDCode[addr_b / 16]+""+ BCDCode[addr_b % 16] +" ";
            str_addr =str_addr + EachByteAddress;
        }
        return "[ "+str_addr+" ]";
    }
}
