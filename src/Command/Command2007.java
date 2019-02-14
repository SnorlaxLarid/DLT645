package Command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.arraycopy;

/**
 * Created by Administrator on 2017/5/28 0028.
 */
public class Command2007 {

    public static final String REQUEST_DATA = "request_data";
    public static final String READ_SUBSEQUENT_DATA = "read_subsequent_data";
    public static final String WRITE_DATA = "write_data";
    public static final String REQUEST_READ_AMM_ADDR = "reaquest_read_amm_addr";
    public static final String REQUEST_WRITE_AMM_ADDR = "reaquest_write_amm_addr";
    public static final String BROADCAST_SYNC_TIME = "broadcast_sync_time";
    public static final String FREEZE_DATA = "freeze_data";
    public static final String CHANGE_COMM_RATE = "change_comm_rate";
    public static final String CHANGE_PASSWORD = "change_password";

    public static final byte[] REQUEST_AMM_ADDR = {-86, -86, -86, -86, -86, -86};//0xaa,0xaa,0xaa
    public static final byte[] BROADCAST_ADDR = {-103, -103, -103, -103, -103, -103};//0x99,0x99,0x99
    private byte YY_byte;
    private byte MM_byte;
    private byte DD_byte;
    private byte hh_byte;
    private byte mm_byte;
    private byte ss_byte;
    private byte N_byte;
    private byte SEQ_byte;
    private byte Z_byte;//通信速率特征字
    private byte[] NN_Data_bytes;
    private byte[] passWord_bytes;//PAP0P1P2,P0P1P2为密码，PA表示该密码权限。
    private byte[] opCode_bytes;//C0C1C2C3是操作者代码，为要求记录操作人员信息的项目提供数据。


    public void setN_byte(int n_byte) {
        N_byte = getSigned(n_byte);
    }

    private boolean set_time_byte(String dateStr) {
        //先用正则表达式检验是否正确
        Pattern pattern = Pattern.compile("(\\d{4})-([0-1]\\d)-([0-3]\\d) ([0-2]\\d):([0-6]\\d):([0-6]\\d)");
        Matcher matcher = pattern.matcher(dateStr);
        if (!matcher.find()) {
            return false;
        }
        //java现推荐使用calendar
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse("2013-02-12 00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        YY_byte = (getByteBCD(cal.get(Calendar.YEAR) % 100));//年份的后两位
        MM_byte = getByteBCD(cal.get(Calendar.MONTH + 1));//月份加一
        DD_byte = getByteBCD(cal.get(Calendar.DAY_OF_MONTH));
        hh_byte = getByteBCD(cal.get(Calendar.HOUR_OF_DAY));
        mm_byte = getByteBCD(cal.get(Calendar.MINUTE));
        ss_byte = getByteBCD(cal.get(Calendar.SECOND));
        return true;
    }

    /**
     * @param addr           电表地址//包括查询电表地址的地址、广播地址和电表地址。根据请求类型而定
     * @param requestType    请求类型，最主要的参数
     * @param type           第一个请求里面还分三类 只有第一个类型的请求才需要此参数。否则设置为0就可以
     * @param dataIdentifier 请求的数据格式，请求数据的时候需要此参数其他时候不需要
     */
    public static byte[] generateRequest(byte[] addr, String requestType, int type, byte[] dataIdentifier) {
        if (dataIdentifier != null) {
            for (int i = 0; i < dataIdentifier.length; i++) {
                dataIdentifier[i] += 0x33;
            }
        }
        Command2007 command = new Command2007();
        command.set_time_byte("2013-02-12 00:00:00");
        command.setN_byte(99);
        return command.generateFrameData(addr, requestType, type, dataIdentifier);
    }

    private byte[] generateFrameData(byte[] addr, String type, int style, byte[] dataIdentifier) {
        byte[] bytesFrame = new byte[addr.length + 2];
        bytesFrame[0] = 0x68;
        arraycopy(addr, 0, bytesFrame, 1, addr.length);
        bytesFrame[addr.length + 1] = 0x68;
        switch (type) {
            case REQUEST_DATA:
                bytesFrame = generateRequestDataFrame(bytesFrame, style, dataIdentifier);
                break;
            case READ_SUBSEQUENT_DATA:
                bytesFrame = generateReadSubDataFrame(bytesFrame, dataIdentifier);
                break;
            case WRITE_DATA:
                bytesFrame = generateWriteDataFrame(bytesFrame, dataIdentifier);
                break;
            case REQUEST_READ_AMM_ADDR:
                bytesFrame = generateRequestReadAmmAddrDataFrame(bytesFrame);
                break;
            case REQUEST_WRITE_AMM_ADDR:
                bytesFrame = generateRequestWriteAmmAddrDataFrame(bytesFrame, dataIdentifier);
                break;
            case BROADCAST_SYNC_TIME:
                bytesFrame = generateBroadcastSyncTimeDataFrame(bytesFrame);
                break;
            case FREEZE_DATA:
                bytesFrame = generateFreezeDataDataFrame(bytesFrame);
                break;
            case CHANGE_COMM_RATE:
                bytesFrame = generateChangeCommRateDataFrame(bytesFrame);
                break;
            case CHANGE_PASSWORD:
                bytesFrame = generateChangePasswordDataFrame(bytesFrame, dataIdentifier);
                break;
            default:
                bytesFrame = new byte[0];
        }
        return bytesFrame;
    }

    private byte[] generateChangePasswordDataFrame(byte[] bytes, byte[] dataIdentifier) {

        return new byte[0];
    }

    private byte[] generateChangeCommRateDataFrame(byte[] bytes) {
        byte[] bytesFrame = new byte[bytes.length + 5];
        arraycopy(bytes, 0, bytesFrame, 0, bytes.length);
        bytesFrame[bytes.length] = 0x17;
        bytesFrame[bytes.length + 1] = 0x01;
        bytesFrame[bytes.length + 2] = Z_byte;
        bytesFrame[bytes.length + 3] = getSigned(getCSCode(bytesFrame, 0, bytes.length + 2));
        bytesFrame[bytes.length + 4] = 0x16;
        return bytesFrame;
    }

    /**
     * 前面的地址必須為廣播地址
     *
     * @param bytes
     * @return
     */
    private byte[] generateFreezeDataDataFrame(byte[] bytes) {
        byte[] bytesFrame = new byte[bytes.length + 8];
        arraycopy(bytes, 0, bytesFrame, 0, bytes.length);
        bytesFrame[bytes.length] = 0x16;
        bytesFrame[bytes.length + 1] = 0x04;
        bytesFrame[bytes.length + 2] = mm_byte;
        bytesFrame[bytes.length + 3] = hh_byte;
        bytesFrame[bytes.length + 4] = DD_byte;
        bytesFrame[bytes.length + 5] = MM_byte;
        bytesFrame[bytes.length + 6] = getSigned(getCSCode(bytesFrame, 0, bytes.length + 5));
        bytesFrame[bytes.length + 7] = 0x16;
        return bytesFrame;
    }

    private byte[] generateBroadcastSyncTimeDataFrame(byte[] bytes) {
        byte[] bytesFrame = new byte[bytes.length + 10];
        arraycopy(bytes, 0, bytesFrame, 0, bytes.length);
        bytesFrame[bytes.length] = 0x08;
        bytesFrame[bytes.length + 1] = 0x06;
        bytesFrame[bytes.length + 2] = ss_byte;
        bytesFrame[bytes.length + 3] = mm_byte;
        bytesFrame[bytes.length + 4] = hh_byte;
        bytesFrame[bytes.length + 5] = DD_byte;
        bytesFrame[bytes.length + 6] = MM_byte;
        bytesFrame[bytes.length + 7] = YY_byte;
        bytesFrame[bytes.length + 8] = getSigned(getCSCode(bytesFrame, 0, bytes.length + 7));
        bytesFrame[bytes.length + 9] = 0x16;
        return bytesFrame;
    }

    private byte[] generateRequestWriteAmmAddrDataFrame(byte[] bytes, byte[] dataIdentifier) {
        byte[] bytesFrame = new byte[bytes.length + 10];
        arraycopy(bytes, 0, bytesFrame, 0, bytes.length);
        bytesFrame[bytes.length] = 0x15;
        bytesFrame[bytes.length + 1] = 0x06;
        arraycopy(dataIdentifier, 0, bytesFrame, bytes.length + 2, dataIdentifier.length);
        bytesFrame[bytes.length + 8] = getSigned(getCSCode(bytesFrame, 0, bytes.length + 7));
        bytesFrame[bytes.length + 9] = 0x16;
        return bytesFrame;
    }


    /**
     * @param bytes
     * @return
     */

    //控制字为13，功能为请求读电表通信地址
    private byte[] generateRequestReadAmmAddrDataFrame(byte[] bytes) {
        byte[] bytesFrame = new byte[bytes.length + 4];
        arraycopy(bytes, 0, bytesFrame, 0, bytes.length);
        bytesFrame[bytes.length] = 0x13;
        bytesFrame[bytes.length + 1] = 0x00;
        bytesFrame[bytes.length + 2] = getSigned(getCSCode(bytesFrame, 0, bytes.length + 1));
        bytesFrame[bytes.length + 3] = 0x16;
        return bytesFrame;
    }

    private byte[] generateWriteDataFrame(byte[] bytes, byte[] dataIdentifier) {
        //检查NN_Data_bytes是否被赋值
        //....
        byte[] bytesFrame = new byte[bytes.length + NN_Data_bytes.length + 16];
        arraycopy(bytes, 0, bytesFrame, 0, bytes.length);
        bytesFrame[bytes.length] = 0x14;
        bytesFrame[bytes.length + 1] = getSigned(12 + NN_Data_bytes.length);
        arraycopy(dataIdentifier, 0, bytesFrame, bytes.length + 2, dataIdentifier.length);
        arraycopy(passWord_bytes, 0, bytesFrame, bytes.length + 6, passWord_bytes.length);
        arraycopy(opCode_bytes, 0, bytesFrame, bytes.length + 10, opCode_bytes.length);
        arraycopy(NN_Data_bytes, 0, bytesFrame, bytes.length + 14, NN_Data_bytes.length);
        bytesFrame[bytes.length + 14 + NN_Data_bytes.length] = getSigned(getCSCode(bytesFrame, 0, bytes.length + 13 + NN_Data_bytes.length));
        bytesFrame[bytes.length + 15 + NN_Data_bytes.length] = 0x16;
        return bytesFrame;
    }

    private byte[] generateReadSubDataFrame(byte[] bytes, byte[] dataIdentifier) {
        byte[] bytesFrame = new byte[bytes.length + 9];
        arraycopy(bytes, 0, bytesFrame, 0, bytes.length);
        bytesFrame[bytes.length] = 0x12;
        bytesFrame[bytes.length + 1] = 0x05;
        arraycopy(dataIdentifier, 0, bytesFrame, bytes.length + 2, dataIdentifier.length);
        bytesFrame[bytes.length + 6] = SEQ_byte;
        bytesFrame[bytes.length + 7] = getSigned(getCSCode(bytesFrame, 0, bytes.length + 6));
        bytesFrame[bytes.length + 8] = 0x16;
        return bytesFrame;
    }

    private byte[] generateRequestDataFrame(byte[] bytes, int style, byte[] dataIdentifier) {
        byte[] bytesFrame = new byte[bytes.length + 1];
        arraycopy(bytes, 0, bytesFrame, 0, bytes.length);
        bytesFrame[bytes.length] = 0x11;
        switch (style) {
            case 1:
                bytesFrame = generateRequestDataFrame_1(bytesFrame, dataIdentifier);
                break;
            case 2:
                bytesFrame = generateRequestDataFrame_2(bytesFrame, dataIdentifier);
                break;
            case 3:
                bytesFrame = generateRequestDataFrame_3(bytesFrame, dataIdentifier);
                break;
            default:
                bytesFrame = new byte[0];
        }
        return bytesFrame;
    }

    private byte[] generateRequestDataFrame_3(byte[] bytes, byte[] dataIdentifier) {
        byte[] byteFrame = new byte[bytes.length + 13];
        arraycopy(bytes, 0, byteFrame, 0, bytes.length);
        byteFrame[bytes.length] = 0x0a;
        arraycopy(dataIdentifier, 0, byteFrame, bytes.length + 1, dataIdentifier.length);
        byteFrame[bytes.length + 5] = this.N_byte;
        byteFrame[bytes.length + 6] = this.mm_byte;
        byteFrame[bytes.length + 7] = this.hh_byte;
        byteFrame[bytes.length + 8] = this.DD_byte;
        byteFrame[bytes.length + 9] = this.MM_byte;
        byteFrame[bytes.length + 10] = this.YY_byte;
        byteFrame[bytes.length + 11] = getSigned(getCSCode(byteFrame, 0, bytes.length + 10));
        byteFrame[bytes.length + 12] = 0x16;
        return byteFrame;
    }

    private byte[] generateRequestDataFrame_2(byte[] bytes, byte[] dataIdentifier) {
        byte[] byteFrame = new byte[bytes.length + 8];
        arraycopy(bytes, 0, byteFrame, 0, bytes.length);
        byteFrame[bytes.length] = 0x05;
        arraycopy(dataIdentifier, 0, byteFrame, bytes.length + 1, dataIdentifier.length);
        byteFrame[bytes.length + 5] = this.N_byte;
        byteFrame[bytes.length + 6] = getSigned(getCSCode(byteFrame, 0, bytes.length + 5));
        byteFrame[bytes.length + 7] = 0x16;
        return byteFrame;
    }

    private byte[] generateRequestDataFrame_1(byte[] bytes, byte[] dataIdentifier) {
        byte[] byteFrame = new byte[bytes.length + 7];
        arraycopy(bytes, 0, byteFrame, 0, bytes.length);
        byteFrame[bytes.length] = 0x04;
        arraycopy(dataIdentifier, 0, byteFrame, bytes.length + 1, dataIdentifier.length);
        byteFrame[bytes.length + 5] = getSigned(getCSCode(byteFrame, 0, bytes.length + 4));
        byteFrame[bytes.length + 6] = 0x16;
        return byteFrame;
    }

    /**
     * 检测码
     *
     * @param byteFrame
     * @param i
     * @param j
     * @return
     */
    private int getCSCode(byte[] byteFrame, int i, int j) {
        int s = 0;
        for (int k = i; k <= j; k++) {
            s += (byteFrame[k] & 0xff) % 256;
        }
        return s;
    }

    /**
     * 将无符号(形式上的)的数据转化成有符号的byte数据值
     *
     * @param a
     * @return
     */
    private byte getSigned(int a) {
        if (a > 0x7f)
            return (byte) (a - 256);
        else return (byte) (a);
    }

    /**
     * 将一个byte大小的int转化成BCD码的byte数据
     *
     * @param a
     * @return
     */
    private byte getByteBCD(int a) {
        return (byte) ((a / 10) * 16 + (a % 10));
    }


}
