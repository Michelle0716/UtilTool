package encryption;

import java.util.Arrays;

/**
 * Created by michelle on 2018/8/17.
 */

public class BCDCodeUtil {

    /***
     *
     * @method toString    解码，解码为16进制
     * @description BCD码（二进制数）转换成阿拉伯数字16进制
     * 				例如有数组{0x12,0x34,0x56}
     *              转换为阿拉伯数字字符串后为123456
     * @param bcd
     * @return
     */
    public static String toString(byte[] bcd) {

        // NULL处理
        if (null == bcd) {
            return null;
        }
        // 空处理
        if (bcd.length == 0) {
            return "";
        }

        StringBuffer buffer = new StringBuffer(bcd.length * 2);
        for (int i = 0; i < bcd.length; i++) {

            switch ((bcd[i] & 0xf0) >>> 4) {
                case 10:
                    buffer.append("A");
                    break;
                case 11:
                    buffer.append("B");
                    break;
                case 12:
                    buffer.append("C");
                    break;
                case 13:
                    buffer.append("D");
                    break;
                case 14:
                    buffer.append("E");
                    break;
                case 15:
                    buffer.append("F");
                    break;
                default:
                    buffer.append((byte) ((bcd[i] & 0xf0) >>> 4));
                    break;
            }
            switch (bcd[i] & 0x0f) {
                case 10:
                    buffer.append("A");
                    break;
                case 11:
                    buffer.append("B");
                    break;
                case 12:
                    buffer.append("C");
                    break;
                case 13:
                    buffer.append("D");
                    break;
                case 14:
                    buffer.append("E");
                    break;
                case 15:
                    buffer.append("F");
                    break;
                default:
                    buffer.append((byte) (bcd[i] & 0x0f));
                    break;
            }

        }
        return buffer.toString();
    }


    /**
     * @param bcd BCD字符串
     * @return
     * @method isBCD
     * @description 是否为BCD字符串     16进制
     */
    public static boolean isBCD(String bcd) {
        boolean ans = false;
        // 化为大写
        bcd = bcd.toUpperCase();
        for (int i = 0; i < bcd.length(); i++) {
            ans = false;
            char c = bcd.charAt(i);
            if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' ||
                    c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F') {
                ans = true;
            } else {
                break;
            }
        }
        return ans;
    }


    /**
     * @param asc ASCII字节
     * @return
     * @method toBCD
     * @description ASCII字节数组转BCD编码
     */
    public static byte[] toBCD(byte[] asc) throws IllegalArgumentException {
        // 空处理
        if (null == asc) {
            return null;
        }
        // 获得原始字节长度
        int len = asc.length;
        byte[] tmp = null;
        // 补位
        if (len % 2 != 0) {
            tmp = new byte[len + 1];
            //复制字节
            for (int i = 0; i < asc.length; i++) {
                tmp[i] = asc[i];
            }
            //补十六进制0
            tmp[len] = 0x0;

        } else {
            tmp = new byte[len];
            //复制字节
            for (int i = 0; i < asc.length; i++) {
                tmp[i] = asc[i];
            }
        }

        // 输出的bcd字节数组
        byte bcd[] = new byte[tmp.length / 2];


        int j, k;

        for (int p = 0; p < tmp.length / 2; p++) {
            // 双
            //处理0
            if (tmp[2 * p] == 0x0) {
                j = 0x0;
            } else if (tmp[2 * p] == 0xf) {
                j = 0xf;
            }
            //处理字符0-9
            else if ((tmp[2 * p] >= '0') && (tmp[2 * p] <= '9')) {
                j = tmp[2 * p] - '0';
            }
            //处理字符A
            else if (tmp[2 * p] == 'a' || tmp[2 * p] == 'A') {
                j = 0xa;
            }
            //处理字符B
            else if (tmp[2 * p] == 'b' || tmp[2 * p] == 'B') {
                j = 0xb;
            }
            //处理字符C
            else if (tmp[2 * p] == 'c' || tmp[2 * p] == 'C') {
                j = 0xc;
            }
            //处理字符D以及字符=
            else if (tmp[2 * p] == 'D' || tmp[2 * p] == 'd' || tmp[2 * p] == '=') {
                j = 0xd;
            }
            //处理字符E
            else if (tmp[2 * p] == 'e' || tmp[2 * p] == 'E') {
                j = 0xe;
            }
            //处理字符F
            else if (tmp[2 * p] == 'f' || tmp[2 * p] == 'F') {
                j = 0xf;
            } else {
                throw new IllegalArgumentException("非BCD字节");
            }

            // 单
            //处理0x0
            if (tmp[2 * p + 1] == 0x0) {
                k = 0x0;
            }
            //处理0xf
            else if (tmp[2 * p + 1] == 0xf) {
                k = 0xf;
            }
            //处理字符0-9
            else if ((tmp[2 * p + 1] >= '0') && (tmp[2 * p + 1] <= '9')) {
                k = tmp[2 * p + 1] - '0';
            }
            //处理字符A
            else if (tmp[2 * p + 1] == 'a' || tmp[2 * p + 1] == 'A') {
                k = 0xa;
            }
            //处理字符B
            else if (tmp[2 * p + 1] == 'b' || tmp[2 * p + 1] == 'B') {
                k = 0xb;
            }
            //处理字符C
            else if (tmp[2 * p + 1] == 'c' || tmp[2 * p + 1] == 'C') {
                k = 0xc;
            }
            //处理字符D以及字符=
            else if (tmp[2 * p + 1] == 'D' || tmp[2 * p + 1] == 'd' || tmp[2 * p + 1] == '=') {
                k = 0xd;
            }
            //处理字符E
            else if (tmp[2 * p + 1] == 'e' || tmp[2 * p + 1] == 'E') {
                k = 0xe;
            }
            //处理字符F
            else if (tmp[2 * p + 1] == 'f' || tmp[2 * p + 1] == 'F') {
                k = 0xf;
            } else {
                throw new IllegalArgumentException("非BCD字节");
            }

            int a = (j << 4) + k;
            byte b = (byte) a;

            bcd[p] = b;
        }

        return bcd;

    }


    /**
     * @param asc 需要进行压缩的字符串
     * @return
     * @method toBCD
     * @description 字符串转化为BCD压缩码
     * 例如字符串"12345678",
     * 压缩之后的字节数组内容为{0x12,0x34,0x56,0x78}
     */
    public static byte[] toBCD(String asc) throws IllegalArgumentException {
        // 空处理
        if (null == asc || asc.trim().equals("")) {
            return null;
        }
        //替换=号
        asc = asc.replace('=', 'D').toUpperCase();
        // 非BCD编码处理
        if (!isBCD(asc)) {
            return  null;
          //  throw new IllegalArgumentException("非BCD字符串");
        }
        return toBCD(asc.getBytes());
    }


    /**
     * :压缩8421 BCD码
     * 压缩BCD码与非压缩BCD码的区别—— 压缩BCD码的每一位用4位二进制表示,一个字节表示两位十进制数。
     * 字符串转bcd   byte
     *
     * @param input 如"1312371827381723"    输出：[19, 18, 55, 24, 39, 56, 23, 35]
     * @return
     */
    public static byte[] encode(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) < '0' ||
                    (input.charAt(i) > '9' && input.charAt(i) > 'A') ||
                    (input.charAt(i) > 'Z' && input.charAt(i) > 'a')) {
                throw new IllegalArgumentException("input should be sequence of decimal‎ character.");
            }
        }
        byte[] temp = (input.length() % 2 != 0 ? "0" + input : input).getBytes();
        byte[] output = new byte[temp.length / 2];
        for (int i = 0, j = 0, k = 0; i < output.length; i++) {
            if (temp[2 * i] >= '0' && temp[2 * i] <= '9') {
                j = temp[2 * i] - '0';
            } else if (temp[2 * i] >= 'a' && temp[2 * i] <= 'z') {
                j = temp[2 * i] - 'a' + 0x0a;
            } else {
                j = temp[2 * i] - 'A' + 0x0a;
            }
            if (temp[2 * i + 1] >= '0' && temp[2 * i + 1] <= '9') {
                k = temp[2 * i + 1] - '0';
            } else if (temp[2 * i + 1] >= 'a' && temp[2 * i + 1] <= 'z') {
                k = temp[2 * i + 1] - 'a' + 0x0a;
            } else {
                k = temp[2 * i + 1] - 'A' + 0x0a;
            }
            output[i] = (byte) ((j << 4) | k);
        }
        return output;
    }


    /**
     * :压缩8421 BCD码
     * 把bcd  byte解码
     *
     * @param input
     * @return
     */
    public static String decode(byte[] input) {
        StringBuilder temp = new StringBuilder(input.length * 2);
        for (int i = 0; i < input.length; i++) {
            temp.append((byte) ((input[i] & 0xf0) >>> 4));
            temp.append((byte) (input[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
                .toString().substring(1) : temp.toString();
    }


    /**
     * 把byte数组，以String形式输出
     *
     * @param b
     * @return
     */
    public static String byteToStr(byte[] b) {
        return Arrays.toString(b);

    }


    /**
     * bcd  byte转为int
     *
     * @param buffer
     * @param offset
     * @param len
     * @return
     */
    public static int BCDtoInteger(byte[] buffer, int offset, int len) {
        // TODO Auto-generated method stub
        if ((buffer == null) || (offset < 0) || (len <= 0) || (buffer.length < offset + len)) {
            return -2147483648;
        }
        int value = 0;
        for (int i = offset; i < len + offset; i++) {
            value = (value * 10) + ((buffer[i] >>> 4) & 0x0F);
            value = (value * 10) + (buffer[i] & 0x0F);
        }
        return value;
    }





    /**
     * @功能: BCD码转为10进制串(阿拉伯数据)
     * @参数: BCD码
     * @结果: 10进制串
     */
    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
                .toString().substring(1) : temp.toString();
    }

    /**
     * @功能: 10进制串转为BCD码
     * @参数: 10进制串
     * @结果: BCD码
     * byte[] b = str2Bcd("11");
    System.out.println(bcd2Str(b));
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }


}
