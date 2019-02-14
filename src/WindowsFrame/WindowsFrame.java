package WindowsFrame;

import Sendrequest.SendRequest;
import Server.Server;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.border.SimplisticSoftBorderPainter;
import org.jvnet.substance.button.StandardButtonShaper;
import org.jvnet.substance.painter.SimplisticGradientPainter;
import org.jvnet.substance.skin.*;
import org.jvnet.substance.theme.SubstanceSteelBlueTheme;;
import org.jvnet.substance.watermark.SubstanceBubblesWatermark;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by Administrator on 2017/5/25 0025.
 *
 * 几乎不改变之前Socket的源码
 * 主线程用于图形界面的更新
 *      by zxy on 2018.10.2
 */
public class WindowsFrame {
    public static String message="Number of Current Client ：    ";
    private  static JFrame frame=new JFrame("Collecting Data For Smart Ammeter");
    public static JLabel label97=new JLabel("1997 Ammeter Checking Windows");
    public static JLabel label07=new JLabel("2007 Ammeter Checking Windows");
    public static JTextArea textArea1997=new JTextArea();
    public static JTextArea textArea2007=new JTextArea();
    private static JButton button_start=new JButton("Start");
    private  static JTextField tip=new JTextField("  Please Input the Checking Port：");
    public   static JTextField port=new JTextField();
    public static JLabel count=new JLabel();
    public  static JLabel Message=new JLabel();

    public WindowsFrame()
    {
        frame.setLayout(null);
        label97.setBounds(30,20,860,25);
        label97.setHorizontalAlignment(JLabel.CENTER);
        frame.add(label97);
        label07.setBounds(910,20,860,24);
        label07.setHorizontalAlignment(JLabel.CENTER);
        frame.add(label07);
        textArea1997.setEditable(false);
        textArea1997.setRows(1000);
        JScrollPane jScrollPane1997=new JScrollPane(textArea1997);
//        jScrollPane1997.setBounds(30,50,860,730);
        jScrollPane1997.setBounds(30,50,600,400);
        frame.add(jScrollPane1997);
        textArea2007.setEditable(false);
        textArea2007.setRows(1000);
        JScrollPane jScrollPane2007=new JScrollPane(textArea2007);
//        jScrollPane2007.setBounds(910,50,860,730);
        jScrollPane2007.setBounds(810,50,600,400);
        frame.add(jScrollPane2007);
        count.setHorizontalAlignment(JLabel.CENTER);
//        count.setBounds(30,800,1740,25);
        count.setBounds(30,445,1740,25);
        count.setText(message+'0');
        frame.add(count);
        Message.setHorizontalAlignment(JLabel.CENTER);
//        Message.setBounds(30,830,1740,25);
        Message.setBounds(80,475,1740,20);
//        tip.setBounds(400,885,350,25);
        tip.setBounds(400,500,350,25);
        Message.setText("Click start button after you set checking port !");
        tip.setEditable(false);
//        port.setBounds(800,885,200,25);
        port.setBounds(800,600,200,25);
        port.setEditable(true);
//        button_start.setBounds(1050,885,350,25);
        button_start.setBounds(1050,600,350,25);
        frame.add(Message);
        frame.add(tip);
        frame.add(port);
        frame.add(button_start);
//        frame.setBounds(30,30,1815,1000);
        frame.setBounds(30,30,1500,700);
        frame.setVisible(true);
        frame.setResizable(true);
    }
    public static void main(String[] args)
    {
        try {
            UIManager.setLookAndFeel(new SubstanceBusinessBlueSteelLookAndFeel());
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            SubstanceLookAndFeel.setCurrentTheme(new SubstanceSteelBlueTheme());
            SubstanceLookAndFeel.setSkin(new BusinessBlueSteelSkin());
            SubstanceLookAndFeel.setCurrentButtonShaper(new StandardButtonShaper());
            SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());
            SubstanceLookAndFeel.setCurrentBorderPainter(new SimplisticSoftBorderPainter());
            SubstanceLookAndFeel.setCurrentGradientPainter(new SimplisticGradientPainter());
        } catch (Exception e) {
            System.err.println("Something went wrong!");
        }
        new WindowsFrame();
        button_start.addActionListener(new StartListener());
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });


    }
    private static class StartListener implements ActionListener  {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
               // System.out.println(Thread.currentThread().getName());
                WindowsFrame.Message.setText("Server is Going to Start>>>>>>");
               new Server().start();    // 生成一个线程专门用于管理netty的线程池
               new SendRequest().start();
            }
            catch (Exception ex0)
            {
               System.out.println("netty初始化失败！");
            }
        }


    }
}
