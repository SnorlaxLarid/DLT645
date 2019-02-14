package ReadData;

import DataIdentify.DataIdentify1997;
import OtherFuction.GetTime;
import OtherFuction.ToHex;
import OtherFuction.check;
import WindowsFrame.WindowsFrame;
import WriteDataToDatabase.WriteDataToDatabase;

import java.util.Arrays;
import java.util.Random;


public class ReadData1997{
    private  int  lengthOfData;
    private  int doc;//确定小数点
    private  String Ammeter_Address;
    WriteDataToDatabase writeToDataBase = new WriteDataToDatabase();
    public  String Read_data(byte[] bytes) {
        Ammeter_Address = check.GetBCDAddress(bytes);
        if (bytes[0] == 0x51) {
            WindowsFrame.textArea1997.append("This Is Heart bit" + "\n");
            return "bit";
        } else {
           // System.out.println(Arrays.toString(bytes));
            boolean check = OtherFuction.check.checkData(bytes);
            if (!check) {
                return "checking_error";
            } else {
                int[] read_ints = new int[bytes.length];
                for (int i = 0; i < bytes.length; i++) {
                    if ((int) bytes[i] > 0) {
                        read_ints[i] = (int) bytes[i];
                    } else {
                        read_ints[i] = (int) bytes[i] + 256;
                    }
                }
                switch (read_ints[8])//read_int[8]为控制码
                {
                    case 0x81:
                        //System.out.print("normally " + "\n");
                        lengthOfData = read_ints[9];
                        byte[] type = new byte[2];
                        int[] data = new int[lengthOfData - 2];
                        for (int t = 0; t < 2; t++) {
                            type[t] = (byte) (read_ints[10 + t] - 0x33);
                        }
                        for (int d = 0; d < lengthOfData - 2; d++) {
                            data[d] = read_ints[12 + d] - 0x33;
                        }
                        DataIdentify1997 dataIdentify = new DataIdentify1997();
                        String datatpye = dataIdentify.getIdentifyname().get(Arrays.toString(type));
                        doc = dataIdentify.getDoc().get(Arrays.toString(type));
                        int value = 0;
                        for (int v = 0; v < data.length; v++) {
                            value = value + (data[v] / 16 * 10 + (data[v]) % 16) * (int) Math.pow(100, v);
                        }
                        //System.out.println(value + " " + doc);
                        double dataresult = ((double) value) / (Math.pow(10, doc)) * 30.0;
                        String str_addr = ToHex.ToHex(bytes);
                        WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + datatpye + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                        WindowsFrame.textArea1997.append("*******************" + datatpye + " : " + dataresult + "***********************\n");
                        //WriteDataToDatabase writeToDataBase = new WriteDataToDatabase();
                        writeToDataBase.write(datatpye, dataresult, Ammeter_Address);
                        Random random = new Random();
                        double voltage = 210 + 20.0 * random.nextFloat();
                        double inf=0.8+0.1*random.nextFloat();

                        switch (datatpye) {
                            case "a_positive_power":
                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "a_influence" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "a_influence" + " : " + inf + "***********************\n");
                                writeToDataBase.write("a_influence", inf, Ammeter_Address);

                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "a_apparent_power" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "a_apparent_power" + " : " + dataresult/inf + "***********************\n");
                                writeToDataBase.write("a_apparent_power", dataresult/inf, Ammeter_Address);

                                double gou=Math.sqrt(1.0-inf*inf);
                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "a_reactive_power" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "a_reactive_power" + " : " + dataresult*gou + "***********************\n");
                                writeToDataBase.write("a_reactive_power", dataresult*gou, Ammeter_Address);

                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "a_voltage" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "a_voltage" + " : " + voltage + "***********************\n");
                                writeToDataBase.write("a_voltage", voltage, Ammeter_Address);

                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "a_current" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "a_current" + " : " + (1000 * dataresult / voltage) + "***********************\n");
                                writeToDataBase.write("a_current", 1000 * dataresult / voltage, Ammeter_Address);

                                break;
                            case "b_positive_power":
                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "b_influence" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "b_influence" + " : " + inf + "***********************\n");
                                writeToDataBase.write("b_influence", inf, Ammeter_Address);

                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "b_apparent_power" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "b_apparent_power" + " : " + dataresult/inf + "***********************\n");
                                writeToDataBase.write("b_apparent_power", dataresult/inf, Ammeter_Address);

                                gou=Math.sqrt(1.0-inf*inf)/inf;
                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "b_reactive_power" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "b_reactive_power" + " : " + dataresult/gou + "***********************\n");
                                writeToDataBase.write("b_reactive_power", dataresult*gou, Ammeter_Address);

                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "b_voltage" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "b_voltage" + " : " + voltage + "***********************\n");
                                WriteDataToDatabase writeToDataBase_bv = new WriteDataToDatabase();
                                writeToDataBase.write("b_voltage", voltage, Ammeter_Address);

                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "b_current" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "b_current" + " : " + (1000 * dataresult / voltage) + "***********************\n");
                                writeToDataBase.write("b_current", 1000 * dataresult / voltage, Ammeter_Address);
                                break;
                            case "c_positive_power":
                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "c_influence" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "c_influence" + " : " + inf + "***********************\n");
                                writeToDataBase.write("c_influence", inf, Ammeter_Address);

                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "c_apparent_power" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "c_apparent_power" + " : " + dataresult/inf + "***********************\n");
                                writeToDataBase.write("c_apparent_power", dataresult/inf, Ammeter_Address);

                                gou=Math.sqrt(1.0-inf*inf)/inf;
                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "c_reactive_power" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "c_reactive_power" + " : " + dataresult*gou + "***********************\n");
                                writeToDataBase.write("c_reactive_power", dataresult*gou, Ammeter_Address);

                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "c_voltage" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "c_voltage" + " : " + voltage + "***********************\n");
                                writeToDataBase.write("c_voltage", voltage, Ammeter_Address);

                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "c_current" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "c_current" + " : " + (1000 * dataresult / voltage) + "***********************\n");
                                writeToDataBase.write("c_current", 1000 * dataresult / voltage, Ammeter_Address);
                                break;
                            case "positive_power":
                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "influence" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "influence" + " : " + inf + "***********************\n");
                                writeToDataBase.write("influence", inf, Ammeter_Address);

                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "apparent_power" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "apparent_power" + " : " + dataresult/inf + "***********************\n");
                                writeToDataBase.write("apparent_power", dataresult/inf, Ammeter_Address);

                                gou=Math.sqrt(1.0-inf*inf)/inf;
                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "reactive_power" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "reactive_power" + " : " + dataresult*gou + "***********************\n");
                                writeToDataBase.write("reactive_power", dataresult*gou, Ammeter_Address);

                                WindowsFrame.textArea1997.append(GetTime.NowTime() + " Received  " + "wave" + " from " + Ammeter_Address + "  Client : " + str_addr + "\n");
                                WindowsFrame.textArea1997.append("*******************" + "wave" + " : " + dataresult/inf + "***********************\n");
                                writeToDataBase.write("wave", inf*5000, Ammeter_Address);
                            default:
                            writeToDataBase = null;
                            writeToDataBase_bv = null;
                        }
                        return "complete";
                    default:
                        return "error";
                }
            }

        }
    }
}

