package OtherFuction;

import java.text.SimpleDateFormat;
import java.util.Date;


public class GetTime {

    public static String NowTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());// new Date()为获取当前系统时间
        return  time;
    }
}
