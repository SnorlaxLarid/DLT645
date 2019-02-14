package DataIdentify;

import java.util.*;


public class DataIdentify1997 {
    private List<byte[]> dataIdent = new ArrayList<>();
    private Map<String ,String> identifyname=new HashMap<String ,String>();
    private Map<String ,Integer> doc=new HashMap<String ,Integer>();
    /**为了保证各个通道的协调，需要增加几条重复的命令帧，选择瞬时项**/
    public DataIdentify1997() {
        dataIdent.add(new byte[]{(byte)0x10,(byte)0x90});//正向有功总电能
        dataIdent.add(new byte[]{(byte)0x20,(byte)0x90});//反向有功总电能
        dataIdent.add(new byte[]{(byte)0x11,(byte)0xB6});//A相电压
        dataIdent.add(new byte[]{(byte)0x12,(byte)0xB6});//B相电压
        dataIdent.add(new byte[]{(byte)0x13,(byte)0xB6});//C相电压
        dataIdent.add(new byte[]{(byte)0x21,(byte)0xB6});//A相电流
        dataIdent.add(new byte[]{(byte)0x22,(byte)0xB6});//B相电流
        dataIdent.add(new byte[]{(byte)0x23,(byte)0xB6});//C相电流
        dataIdent.add(new byte[]{(byte)0x30,(byte)0xB6});//瞬时总功率
        dataIdent.add(new byte[]{(byte)0x31,(byte)0xB6});//瞬时A相有功功率
        dataIdent.add(new byte[]{(byte)0x32,(byte)0xB6});//瞬时B相有功功率
        dataIdent.add(new byte[]{(byte)0x33,(byte)0xB6});//瞬时C相有功功率
        dataIdent.add(new byte[]{(byte)0x40,(byte)0xB6});//瞬时总无功功率
        dataIdent.add(new byte[]{(byte)0x41,(byte)0xB6});//瞬时A相总无功功率
        dataIdent.add(new byte[]{(byte)0x42,(byte)0xB6});//瞬时B相总无功功率
        dataIdent.add(new byte[]{(byte)0x43,(byte)0xB6});//瞬时C相总无功功率
        dataIdent.add(new byte[]{(byte)0x50,(byte)0xB6});//总功率因数
        dataIdent.add(new byte[]{(byte)0x51,(byte)0xB6});//A相功率因数
        dataIdent.add(new byte[]{(byte)0x52,(byte)0xB6});//B相功率因数
        dataIdent.add(new byte[]{(byte)0x53,(byte)0xB6});//C相功率因数
        //增加重复项
        dataIdent.add(new byte[]{(byte)0x30,(byte)0xB6});//瞬时总功率
        dataIdent.add(new byte[]{(byte)0x31,(byte)0xB6});//瞬时A相有功功率
        dataIdent.add(new byte[]{(byte)0x32,(byte)0xB6});//瞬时B相有功功率
        dataIdent.add(new byte[]{(byte)0x33,(byte)0xB6});//瞬时C相有功功率
        dataIdent.add(new byte[]{(byte)0x40,(byte)0xB6});//瞬时总无功功率

        identifyname.put(Arrays.toString(new byte[]{(byte)0x10,(byte)0x90}),"total_power");//正向有功总电能
        identifyname.put(Arrays.toString(new byte[]{(byte)0x20,(byte)0x90}),"neg_positive_power");//反向有功总电能
        identifyname.put(Arrays.toString(new byte[]{(byte)0x11,(byte)0xB6}),"a_voltage");//A相电压
        identifyname.put(Arrays.toString(new byte[]{(byte)0x12,(byte)0xB6}),"b_voltage");//B相电压
        identifyname.put(Arrays.toString(new byte[]{(byte)0x13,(byte)0xB6}),"c_voltage");//C相电压
        identifyname.put(Arrays.toString(new byte[]{(byte)0x21,(byte)0xB6}),"a_current");//A相电流
        identifyname.put(Arrays.toString(new byte[]{(byte)0x22,(byte)0xB6}),"b_current");//B相电流
        identifyname.put(Arrays.toString(new byte[]{(byte)0x23,(byte)0xB6}),"c_current");//C相电流
        identifyname.put(Arrays.toString(new byte[]{(byte)0x30,(byte)0xB6}),"positive_power");//瞬时有功总功率
        identifyname.put(Arrays.toString(new byte[]{(byte)0x31,(byte)0xB6}),"a_positive_power");//瞬时A相有功功率
        identifyname.put(Arrays.toString(new byte[]{(byte)0x32,(byte)0xB6}),"b_positive_power");//瞬时B相有功功率
        identifyname.put(Arrays.toString(new byte[]{(byte)0x33,(byte)0xB6}),"c_positive_power");//瞬时C相有功功率
        identifyname.put(Arrays.toString(new byte[]{(byte)0x40,(byte)0xB6}),"reactive_power");//瞬时总无功功率
        identifyname.put(Arrays.toString(new byte[]{(byte)0x41,(byte)0xB6}),"a_reactive_power");//瞬时A相总无功功率
        identifyname.put(Arrays.toString(new byte[]{(byte)0x42,(byte)0xB6}),"b_reactive_power");//瞬时B相总无功功率
        identifyname.put(Arrays.toString(new byte[]{(byte)0x43,(byte)0xB6}),"c_reactive_power");//瞬时C相总无功功率
        identifyname.put(Arrays.toString(new byte[]{(byte)0x50,(byte)0xB6}),"influence");//总功率因数
        identifyname.put(Arrays.toString(new byte[]{(byte)0x51,(byte)0xB6}),"a_influence");//A相功率因数
        identifyname.put(Arrays.toString(new byte[]{(byte)0x52,(byte)0xB6}),"b_influence");//B相功率因数
        identifyname.put(Arrays.toString(new byte[]{(byte)0x53,(byte)0xB6}),"c_influence");//C相功率因数
        //增加重复项
        identifyname.put(Arrays.toString(new byte[]{(byte)0x30,(byte)0xB6}),"positive_power");//瞬时有功总功率
        identifyname.put(Arrays.toString(new byte[]{(byte)0x31,(byte)0xB6}),"a_positive_power");//瞬时A相有功功率
        identifyname.put(Arrays.toString(new byte[]{(byte)0x32,(byte)0xB6}),"b_positive_power");//瞬时B相有功功率
        identifyname.put(Arrays.toString(new byte[]{(byte)0x33,(byte)0xB6}),"c_positive_power");//瞬时C相有功功率
        identifyname.put(Arrays.toString(new byte[]{(byte)0x40,(byte)0xB6}),"reactive_power");//瞬时总无功功率

        doc.put(Arrays.toString(new byte[]{(byte)0x10,(byte)0x90}),2);//正向有功总电能
        doc.put(Arrays.toString(new byte[]{(byte)0x20,(byte)0x90}),2);//反向有功总电能
        doc.put(Arrays.toString(new byte[]{(byte)0x11,(byte)0xB6}),0);//A相电压
        doc.put(Arrays.toString(new byte[]{(byte)0x12,(byte)0xB6}),0);//B相电压
        doc.put(Arrays.toString(new byte[]{(byte)0x13,(byte)0xB6}),0);//C相电压
        doc.put(Arrays.toString(new byte[]{(byte)0x21,(byte)0xB6}),2);//A相电流
        doc.put(Arrays.toString(new byte[]{(byte)0x22,(byte)0xB6}),2);//B相电流
        doc.put(Arrays.toString(new byte[]{(byte)0x23,(byte)0xB6}),2);//C相电流
        doc.put(Arrays.toString(new byte[]{(byte)0x30,(byte)0xB6}),4);//瞬时有功总功率
        doc.put(Arrays.toString(new byte[]{(byte)0x31,(byte)0xB6}),4);//瞬时A相有功功率
        doc.put(Arrays.toString(new byte[]{(byte)0x32,(byte)0xB6}),4);//瞬时B相有功功率
        doc.put(Arrays.toString(new byte[]{(byte)0x33,(byte)0xB6}),4);//瞬时C相有功功率
        doc.put(Arrays.toString(new byte[]{(byte)0x40,(byte)0xB6}),2);//瞬时总无功功率
        doc.put(Arrays.toString(new byte[]{(byte)0x41,(byte)0xB6}),2);//瞬时A相总无功功率
        doc.put(Arrays.toString(new byte[]{(byte)0x42,(byte)0xB6}),2);//瞬时B相总无功功率
        doc.put(Arrays.toString(new byte[]{(byte)0x43,(byte)0xB6}),2);//瞬时C相总无功功率
        doc.put(Arrays.toString(new byte[]{(byte)0x50,(byte)0xB6}),3);//总功率因数
        doc.put(Arrays.toString(new byte[]{(byte)0x51,(byte)0xB6}),3);//A相功率因数
        doc.put(Arrays.toString(new byte[]{(byte)0x52,(byte)0xB6}),3);//B相功率因数
        doc.put(Arrays.toString(new byte[]{(byte)0x53,(byte)0xB6}),3);//C相功率因数
        //增加重复项
        doc.put(Arrays.toString(new byte[]{(byte)0x30,(byte)0xB6}),4);//瞬时有功总功率
        doc.put(Arrays.toString(new byte[]{(byte)0x31,(byte)0xB6}),4);//瞬时A相有功功率
        doc.put(Arrays.toString(new byte[]{(byte)0x32,(byte)0xB6}),4);//瞬时B相有功功率
        doc.put(Arrays.toString(new byte[]{(byte)0x33,(byte)0xB6}),4);//瞬时C相有功功率
        doc.put(Arrays.toString(new byte[]{(byte)0x40,(byte)0xB6}),2);//瞬时总无功功率
    }
    public int getLength()
    {
        return identifyname.size();
    }

    public List<byte[]> getDataIdent() {
        return dataIdent;
    }

    public Map<String, Integer> getDoc() {
        return doc;
    }

    public Map<String, String> getIdentifyname() {
        return identifyname;
    }
}

