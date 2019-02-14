package DataIdentify;

import java.util.*;

/**
 * Created by anyuan on and Taylor 2017/3/5.
 */
public class DataIdentify2007 {
    public List<byte[]> dataIdent = new ArrayList<>();
    public Map<String,String> identifyname=new HashMap<String,String>();
    public Map<String,Integer> doc=new HashMap<String,Integer>();
    public int length;
    public DataIdentify2007() {
        dataIdent.add(0,new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00});//有功总电能

        dataIdent.add(1,new byte[]{(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00});//正向有功总电能
        dataIdent.add(2,new byte[]{(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x00});//反向有功总电能

        dataIdent.add(3,new byte[]{(byte)0x00,(byte)0x01,(byte)0x01,(byte)0x02});//A相电压
        dataIdent.add(4,new byte[]{(byte)0x00,(byte)0x02,(byte)0x01,(byte)0x02});//B相电压
        dataIdent.add(5,new byte[]{(byte)0x00,(byte)0x03,(byte)0x01,(byte)0x02});//C相电压

        dataIdent.add(6,new byte[]{(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x02});//A相电流
        dataIdent.add(7,new byte[]{(byte)0x00,(byte)0x02,(byte)0x02,(byte)0x02});//B相电流
        dataIdent.add(8,new byte[]{(byte)0x00,(byte)0x03,(byte)0x02,(byte)0x02});//C相电流

        dataIdent.add(9,new byte[]{(byte)0x00,(byte)0x00,(byte)0x03,(byte)0x02});//瞬时有功功率
        dataIdent.add(10,new byte[]{(byte)0x00,(byte)0x01,(byte)0x03,(byte)0x02});//瞬时A相有功功率
        dataIdent.add(11,new byte[]{(byte)0x00,(byte)0x02,(byte)0x03,(byte)0x02});//瞬时B相有功功率
        dataIdent.add(12,new byte[]{(byte)0x00,(byte)0x03,(byte)0x03,(byte)0x02});//瞬时C相有功功率

        dataIdent.add(13,new byte[]{(byte)0x00,(byte)0x00,(byte)0x04,(byte)0x02});//瞬时无功功率
        dataIdent.add(14,new byte[]{(byte)0x00,(byte)0x01,(byte)0x04,(byte)0x02});//瞬时A相总无功功率
        dataIdent.add(15,new byte[]{(byte)0x00,(byte)0x02,(byte)0x04,(byte)0x02});//瞬时B相总无功功率
        dataIdent.add(16,new byte[]{(byte)0x00,(byte)0x03,(byte)0x04,(byte)0x02});//瞬时C相总无功功率

        dataIdent.add(17,new byte[]{(byte)0x00,(byte)0x00,(byte)0x05,(byte)0x02});//瞬时视在功率
        dataIdent.add(18,new byte[]{(byte)0x00,(byte)0x01,(byte)0x05,(byte)0x02});//A相视在功率
        dataIdent.add(19,new byte[]{(byte)0x00,(byte)0x02,(byte)0x05,(byte)0x02});//B相视在功率
        dataIdent.add(20,new byte[]{(byte)0x00,(byte)0x03,(byte)0x05,(byte)0x02});//C相视在功率

        dataIdent.add(21,new byte[]{(byte)0x00,(byte)0x00,(byte)0x06,(byte)0x02});//总功率因数
        dataIdent.add(22,new byte[]{(byte)0x00,(byte)0x01,(byte)0x06,(byte)0x02});//A相功率因数
        dataIdent.add(23,new byte[]{(byte)0x00,(byte)0x02,(byte)0x06,(byte)0x02});//B相功率因数
        dataIdent.add(24,new byte[]{(byte)0x00,(byte)0x03,(byte)0x06,(byte)0x02});//C相功率因数

        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00}),"total_power");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00}),"pos_positive_power");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x00}),"neg_positive_power");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x01,(byte)0x01,(byte)0x02}),"a_voltage");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x02,(byte)0x01,(byte)0x02}),"b_voltage");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x03,(byte)0x01,(byte)0x02}),"c_voltage");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x02}),"a_current");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x02,(byte)0x02,(byte)0x02}),"b_current");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x03,(byte)0x02,(byte)0x02}),"c_current");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x00,(byte)0x03,(byte)0x02}),"positive_power");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x01,(byte)0x03,(byte)0x02}),"a_positive_power");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x02,(byte)0x03,(byte)0x02}),"b_positive_power");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x03,(byte)0x03,(byte)0x02}),"c_positive_power");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x00,(byte)0x04,(byte)0x02}),"reactive_power");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x01,(byte)0x04,(byte)0x02}),"a_reactive_power");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x02,(byte)0x04,(byte)0x02}),"b_reactive_power");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x03,(byte)0x04,(byte)0x02}),"c_reactive_power");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x00,(byte)0x05,(byte)0x02}),"apparent_power");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x01,(byte)0x05,(byte)0x02}),"a_apparent_power");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x02,(byte)0x05,(byte)0x02}),"b_apparent_power");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x03,(byte)0x05,(byte)0x02}),"c_apparent_power");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x00,(byte)0x06,(byte)0x02}),"influence");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x01,(byte)0x06,(byte)0x02}),"a_influence");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x02,(byte)0x06,(byte)0x02}),"b_influence");
        identifyname.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x03,(byte)0x06,(byte)0x02}),"c_influence");
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00}),2);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00}),2);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x00}),2);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x01,(byte)0x01,(byte)0x02}),1);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x02,(byte)0x01,(byte)0x02}),1);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x03,(byte)0x01,(byte)0x02}),1);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x02}),3);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x02,(byte)0x02,(byte)0x02}),3);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x03,(byte)0x02,(byte)0x02}),3);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x00,(byte)0x03,(byte)0x02}),4);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x01,(byte)0x03,(byte)0x02}),4);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x02,(byte)0x03,(byte)0x02}),4);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x03,(byte)0x03,(byte)0x02}),4);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x00,(byte)0x04,(byte)0x02}),4);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x01,(byte)0x04,(byte)0x02}),4);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x02,(byte)0x04,(byte)0x02}),4);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x03,(byte)0x04,(byte)0x02}),4);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x00,(byte)0x05,(byte)0x02}),4);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x01,(byte)0x05,(byte)0x02}),4);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x02,(byte)0x05,(byte)0x02}),4);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x03,(byte)0x05,(byte)0x02}),4);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x00,(byte)0x06,(byte)0x02}),4);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x01,(byte)0x06,(byte)0x02}),4);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x02,(byte)0x06,(byte)0x02}),4);
        doc.put(Arrays.toString(new byte[]{(byte)0x00,(byte)0x03,(byte)0x06,(byte)0x02}),4);
    }
    public int  getLength()
    {
        return dataIdent.size();
    }
}
