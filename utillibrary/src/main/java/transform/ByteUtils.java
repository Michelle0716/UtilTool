package transform;

import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by michelle on 2018/8/16.
 */

public class ByteUtils {
    private static String hexString = "0123456789ABCDEF";
    static String hexStringLow = "0123456789abcdef";//小写的形式


    /*将字符串转化为16进制，存入字符串数组中*/
//    将字节数组中每个字节拆解成2位16进制整数
    public static String string2Hex(String content) {
        byte[] bytes = content.getBytes();//10进制
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            Log.e("move", "====" + (bytes[i] & 0xf0) + "====" + ((bytes[i] & 0xf0) >> 4));
            sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
            sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
        }
        return sb.toString();
    }


    /*将bytes数组转化为16进制字符串*/
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    /*将十六进制字符串解码成字符串===解码*/
    public static String decode(String bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        //将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
        return new String(baos.toByteArray());
    }


    /*将16进制字符串转化为bytes数组*/
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * 将16进制字符串转化为字符串==解码
     **/
    //将每2位16进制整数组装成一个字节
    public static String hexStr2Str(String hexStr) {
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = hexString.indexOf(hexs[2 * i]) * 16;
            n += hexString.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /*将16进制字符串转为10进制byte后，解码出原来的字符串===解码*/
    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];//10进制
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");//UTF-16 le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }


    /**
     * bytes字符串转换为Byte值
     *
     * @param src 16进制字符串   解码
     *            16 src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src) throws NumberFormatException {
        Log.e("str", src);
        int m = 0, n = 0;
        if ((src.length() % 2) != 0)
            src = "0" + src;
        int l = src.length() / 2;
        // System.out.println(l);
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            Integer num = Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
            Log.e("num", num + "");
            ret[i] = num.byteValue();
        }
        return ret;
    }


    /*将Short Int类型转化为16进制再转化为byte数组*/
    private static byte toByte(char c) {
        byte b = (byte) hexString.indexOf(c);
        return b;
    }


    /*将INT类型转化为10进制byte数组（占4字节）*/
    public static byte[] int2Bytes(int num) {
        byte[] byteNum = new byte[4];
        for (int ix = 0; ix < 4; ++ix) {
            int offset = 32 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }


    /*将int类型转化为16进制数，转化为byte数组类型*/
    public static byte[] intToHexToBytes(short num) {
        byte[] byteNum = new byte[]{(byte) ((num >> 8) & 0xFF), (byte) (num & 0xFF)};
        return byteNum;
    }

    /*将int类型转化为16进制数，转化为byte类型*/
    public static byte intToHexToByte(int integer) {
        String hexStr = Integer.toHexString(integer);
        Log.i("data", hexStr);
        return Byte.valueOf(hexStr, 16);
    }


    /*将int类型转化为byte类型*/
    public static byte int2OneByte(int num) {
        return (byte) (num & 0x000000ff);
    }


    /*将2个字节的byte数组转化为int类型*/
    public static int twoBytes2Int(byte[] buffer) {
        return buffer[0] | buffer[1] << 8;
    }

    /*将byte类型数转化为int类型*/
    public static int oneByte2Int(byte byteNum) {
        return byteNum & 0xFF;
    }

    /*将16进制的byte类型转化为10进制的int类型*/
    public static int byteToInt16(byte b) {
        String result = Integer.toHexString(b & 0xFF);
        return Integer.valueOf(result, 16);
    }

    /*将byte类型数组（4字节）转化为int类型*/
    public static int bytes2Int(byte[] byteNum) {
        int num = 0;
        for (int ix = 0; ix < 4; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }


    /*将长整形转化为byte数组*/
    public static byte[] long2Bytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    /*将byte数组（长度为8）转化为长整形*/
    public static long bytes2Long(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }


    /**将16进制的byte数组转化为float类型**/
    /**
     * Float.intBitsToFloat int转float
     * Integer temp=Integer.valueOf(hexString.trim(), 16);进制转换、
     * Integer.valueOf(hexString.trim())，字符串数字转换，如"111"
     *
     * @param num
     * @return
     */
    public static float byte162float(byte[] num) {
        String hexString = bytesToHexString(num);
        Integer temp = Integer.valueOf(hexString.trim(), 16);
        float value = Float.intBitsToFloat(temp.intValue());
        System.out.println(value);
        return value;
    }


    /**
     * 将float转化为byte数组，占用4个字节
     **/
    public static byte[] float2ByteArray(float value) {
        return ByteBuffer.allocate(4).putFloat(value).array();
    }

    /**
     * 将10进制byte数组转化为Float
     *
     * @param b     字节（至少4个字节）
     * @param index 开始位置
     * @return
     */
    public static float bytes2float(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }


    /*两个byte数组是否值相等的比较*/
    public static boolean byteCompare(byte[] data1, byte[] data2, int len) {
        if (data1 == null && data2 == null) {
            return true;
        }
        if (data1 == null || data2 == null) {
            return false;
        }
        if (data1 == data2) {
            return true;
        }
        boolean bEquals = true;
        int i;
        for (i = 0; i < data1.length && i < data2.length && i < len; i++) {
            if (data1[i] != data2[i]) {
                bEquals = false;
                break;
            }
        }
        return bEquals;
    }

    /*将byte（字节）类型转化为位*/
    public static String byteToBit(byte b) {
        return "" + (byte) ((b >> 7) & 0x1) +
                (byte) ((b >> 6) & 0x1) +
                (byte) ((b >> 5) & 0x1) +
                (byte) ((b >> 4) & 0x1) +
                (byte) ((b >> 3) & 0x1) +
                (byte) ((b >> 2) & 0x1) +
                (byte) ((b >> 1) & 0x1) +
                (byte) ((b >> 0) & 0x1);
    }


    /*将指定byte数组以16进制的形式打印到控制台 */
    public static void printHexString(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase());
        }
    }

    /*判断字符串中是否出现非数字字符，如果出现非数字字符，返回true，否则返回false*/
    public static boolean ExistOtherChar(String str) {
        String numstr = "0123456789";
        int i = 0;
        for (i = 0; i < str.length(); i++) {
            if (numstr.indexOf(str.charAt(i)) == -1) {
                return true;
            }
        }
        return false;
    }


    /*判断字符串中是否只包含字母，只包含字母，返回true，否则返回false*/
    public static boolean ExistChar(String str) {
        String regex = "[a-zA-Z]+$";
        return str.matches(regex);
    }

    /*判断版本号是否符合格式*/
    public static boolean LegalVersion(String str) {
        String regex = "[0-9]+[.][0-9]+[.][0-9]+";
        return str.matches(regex);
    }


    /**
     * @param length 长度
     * @param value  默认值
     * @return
     * @method create
     * @description 创建数组
     */
    public static byte[] create(int length, byte value) {
        // 空处理
        if (length <= 0) {
            return null;
        }
        // 构造数组
        byte[] create = new byte[length];
        for (int i = 0; i < create.length; i++) {
            create[i] = value;
        }
        return create;
    }

    /**
     * @param data
     * @param length
     * @return
     * @method split
     * @description 分割字节数组
     */
    public static byte[][] split(byte[] data, int length) {
        // 空处理
        if (null == data || data.length == 0) {
            return null;
        }

        // 输出结果
        byte[][] split = null;
        // 行
        int row;
        // 列
        int col = length;

        // 构造数组
        if (data.length % length == 0) {
            row = data.length / length;
        } else {
            row = data.length / length + 1;
        }
        split = new byte[row][col];

        // 分割
        int index = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (index < data.length) {
                    split[i][j] = data[index];
                } else {
                    split[i][j] = (byte) 0x00;
                    break;
                }
                index++;
            }
        }

        return split;
    }

    /**
     * @param x 数据X
     * @param y 数据Y
     * @return
     * @method xor
     * @description 异或运算
     */
    public static byte[] xor(byte[] x, byte[] y)
            throws IllegalArgumentException {
        // 空处理
        if (null == x || x.length == 0 || null == y || y.length == 0) {
            return null;
        }
        // 长度不同，不进行异或运算
        if (x.length != y.length) {
            throw new IllegalArgumentException("传入的两个字节数组长度不同，不进行异或运算");
        }

        byte[] xor = new byte[x.length];

        for (int i = 0; i < xor.length; i++) {
            xor[i] = (byte) (x[i] ^ y[i]);
        }
        return xor;
    }

    /***
     *
     * @method format
     * @description 格式化字节
     * @param data
     *            数据
     * @param gourp
     *            按几位分组
     * @param
     * @param value1
     *            补位数值2
     * @param append
     *            是否强制追补字节
     * @return
     */
    public static byte[] format(byte[] data, int gourp, int value1, int value2,
                                boolean append) {
        // 空处理
        if (null == data || data.length == 0) {
            return null;
        }
        // 不追补直接返回
        if (!append && data.length % gourp == 0) {
            return data;
        }
        // 构建字节数组
        byte[] format = new byte[data.length + (gourp - data.length % gourp)];
        // 复制字节
        for (int i = 0; i < data.length; i++) {
            format[i] = data[i];
        }

        // 组装字节
        for (int i = 0; i < gourp - data.length % gourp; i++) {
            if (i > 0) {
                format[data.length + i] = (byte) value2;
            } else {
                format[data.length] = (byte) value1;
            }
        }
        return format;
    }

    /***
     *
     * @method format
     * @description 格式化字节数组
     * @param data
     *            数据
     * @param gourp
     *            按几位分组
     * @param value
     *            补位数值
     * @param append
     *            是否强制追补字节
     * @return
     */
    public static byte[] format(byte[] data, int gourp, int value,
                                boolean append) {
        // 空处理
        if (null == data || data.length == 0) {
            return null;
        }
        // 不追补直接返回
        if (!append && data.length % gourp == 0) {
            return data;
        }
        // 构建字节数组
        byte[] format = new byte[data.length + (gourp - data.length % gourp)];
        // 复制字节
        for (int i = 0; i < data.length; i++) {
            format[i] = data[i];
        }

        // 组装字节
        for (int i = 0; i < gourp - data.length % gourp; i++) {
            format[data.length + i] = (byte) value;
        }
        return format;
    }

    /**
     * @param
     * @param start  正数从左边开始截，负数从右边开始截
     * @param length 长度
     * @return
     * @method cut
     * @description 截取字节数组
     */
    public static byte[] cut(byte[] data, int start, int length) {
        // 空处理
        if (null == data || data.length == 0) {
            return null;
        }
        // 边界判断
        if ((start >= 0 && (start + length) > data.length)
                || (start < 0 && (length - start - 1) > data.length)) {
            throw new ArrayIndexOutOfBoundsException("超出原数组边界");
        }

        // 返回的数组
        byte[] sub_byte = new byte[length];

        for (int i = 0; i < sub_byte.length; i++) {
            // 正数从左边开始截取
            if (start >= 0) {
                sub_byte[i] = data[start + i];
            }
            // 负数从右边开始截取
            else {
                sub_byte[sub_byte.length - 1 - i] = data[data.length + start
                        - i];
            }

        }
        return sub_byte;
    }

    /**
     * @param src_bytes 原始字节数组
     * @param new_bytes 新字节
     * @param start     正数从左边开始替换，负数从右边开始替换
     * @return
     * @method replace
     * @description 替换字节数组中的字节
     */
    public static byte[] replace(byte[] src_bytes, byte[] new_bytes, int start) {
        // 空处理
        if (null == src_bytes || src_bytes.length == 0) {
            return null;
        }
        if (new_bytes == null || new_bytes.length == 0) {
            return src_bytes;
        }
        if (new_bytes.length >= src_bytes.length) {
            return new_bytes;
        }

        // 开始替换
        for (int i = 0; i < new_bytes.length; i++) {
            // 正数从左边开始替换
            if (start >= 0) {
                src_bytes[start + i] = new_bytes[i];
            }
            // 负数从右边开始替换
            else {
                src_bytes[src_bytes.length + start - i] = new_bytes[new_bytes.length
                        - 1 - i];
            }

        }
        return src_bytes;
    }

    /**
     * @param data1 字节数组1
     * @param data2 字节数组2
     * @return
     * @method append
     * @description 追加字节数组
     */
    public static byte[] append(byte[] data1, byte[] data2) {
        byte[] append = null;
        // 空处理
        if (null == data1 || data1.length == 0) {
            return data2;
        }
        if (null == data2 || data2.length == 0) {
            return data1;
        }
        // 构造数组
        append = new byte[data1.length + data2.length];
        // 复制data1
        for (int i = 0; i < data1.length; i++) {
            append[i] = data1[i];
        }
        // 追加data2
        for (int i = 0; i < data2.length; i++) {
            append[data1.length + i] = data2[i];
        }
        return append;
    }

    /**
     * @param data1 字节
     * @param data2 字节数组
     * @return
     * @method append
     * @description 追加字节数组
     */
    public static byte[] append(byte data1, byte[] data2) {
        byte[] append = null;
        // 空处理
        if (null == data2 || data2.length == 0) {
            append = new byte[1];
            append[0] = data1;
            return append;
        }
        // 构造数组
        append = new byte[data2.length + 1];
        // 复制data1
        append[0] = data1;
        // 追加data2
        for (int i = 0; i < data2.length; i++) {
            append[i + 1] = data2[i];
        }
        return append;
    }

    /**
     * @param data1
     * @param data2
     * @return
     * @method append
     * @description 追加字节
     */
    public static byte[] append(byte[] data1, byte data2) {
        byte[] append = null;
        // 空处理
        if (null == data1 || data1.length == 0) {
            append = new byte[1];
            append[0] = data2;
            return append;
        }
        // 构造数组
        append = new byte[data1.length + 1];
        // 复制data1
        for (int i = 0; i < data1.length; i++) {
            append[i] = data1[i];
        }
        // 追加data2
        append[data1.length] = data2;
        return append;
    }

    /**
     * 功能描述：把两个字节的字节数组转化为整型数据，高位补零，例如：<br/>
     * 有字节数组byte[] data = new byte[]{1,2};转换后int数据的字节分布如下：<br/>
     * 00000000 00000000 00000001 00000010,函数返回258
     *
     * @param lenData 需要进行转换的字节数组
     * @return 字节数组所表示整型值的大小
     */
    public static int bytesToIntWhereByteLengthEquals2(byte lenData[]) {
        if (lenData.length != 2) {
            return -1;
        }
        byte fill[] = new byte[]{0, 0};
        byte real[] = new byte[4];
        System.arraycopy(fill, 0, real, 0, 2);
        System.arraycopy(lenData, 0, real, 2, 2);
        int len = byteToInt(real);
        return len;

    }

    /**
     * 功能描述：将byte数组转化为int类型的数据
     *
     * @param byteVal 需要转化的字节数组
     * @return 字节数组所表示的整型数据
     */
    public static int byteToInt(byte[] byteVal) {
        int result = 0;
        for (int i = 0; i < byteVal.length; i++) {
            int tmpVal = (byteVal[i] << (8 * (3 - i)));
            switch (i) {
                case 0:
                    tmpVal = tmpVal & 0xFF000000;
                    break;
                case 1:
                    tmpVal = tmpVal & 0x00FF0000;
                    break;
                case 2:
                    tmpVal = tmpVal & 0x0000FF00;
                    break;
                case 3:
                    tmpVal = tmpVal & 0x000000FF;
                    break;
            }

            result = result | tmpVal;
        }
        return result;
    }


    public static int[] getBit(byte src, int bitLen) {
        int[] result = new int[bitLen];
        for (int i = 0; i < bitLen; i++) {
            result[i] = src >> i & 0x01;
        }

        return result;
    }

    /**
     * 对byte[]进行SHA256摘要运算
     */
    public static byte[] encodeBySHA256(byte[] one_code) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(one_code);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return md.digest();
    }

    /**
     * 判断byte数组是否是随机数(每一位都非00)
     */
    public static boolean isRandom(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xFF) == 0x00)
                return false;
        }
        return true;
    }

    /**
     * 比较两个byte[]时候相同
     */
    public static boolean isTheSame(byte[] a, byte[] b) {
        if (a.length != b.length)
            return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i])
                return false;
        }
        return true;
    }

    /**
     * 将一个整数转换位字节数组(2个字节)，b[0]存储高位字符，大端
     *
     * @param i 整数
     * @return 代表整数的字节数组
     */
    public static byte[] intTo2Bytes(int i) {
        byte[] b = new byte[2];
        b[0] = (byte) (i >>> 8);
        b[1] = (byte) i;
        return b;
    }

    /**
     * int转byte[]数组
     */
    public static byte[] intToBytes(int a) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (0xFF & a);
        bytes[1] = (byte) (0xFF00 & a >> 8);
        bytes[2] = (byte) (0xFF0000 & a >> 16);
        bytes[3] = (byte) (0xFF000000 & a >> 24);
        return bytes;
    }

    /**
     * byte转int[]数组
     */
    public static int[] byteToIntBit(byte b) {
        int array[] = new int[8];
        array[0] = (byte) ((b >> 7) & 0x1);
        array[1] = (byte) ((b >> 6) & 0x1);
        array[2] = (byte) ((b >> 5) & 0x1);
        array[3] = (byte) ((b >> 4) & 0x1);
        array[4] = (byte) ((b >> 3) & 0x1);
        array[5] = (byte) ((b >> 2) & 0x1);
        array[6] = (byte) ((b >> 1) & 0x1);
        array[7] = (byte) ((b >> 0) & 0x1);
        return array;
    }


    /**
     * byte转16进制
     *
     * @param data
     * @return
     */
    public static String bytes2HexString(byte data[]) {
        StringBuilder buffer = new StringBuilder();
        byte abyte0[];
        int j = (abyte0 = data).length;
        for (int i = 0; i < j; i++) {
            byte b = abyte0[i];
            String hex = Integer.toHexString(b & 255);
            if (hex.length() == 1)
                buffer.append('0');
            buffer.append(hex);
        }

        return buffer.toString().toUpperCase();
    }


    /**
     * 16进制转byte
     *
     * @param data
     * @return
     */
    public static byte[] hexString2Bytes(String data) {
        byte result[] = new byte[(data.length() + 1) / 2];
        if ((data.length() & 1) == 1)
            data = (new StringBuilder(String.valueOf(data))).append("0").toString();
        for (int i = 0; i < result.length; i++)
            result[i] = (byte) (hex2byte(data.charAt(i * 2 + 1)) | hex2byte(data.charAt(i * 2)) << 4);

        return result;
    }


    /**
     * char转byte
     *
     * @param hex
     * @return
     */
    public static byte hex2byte(char hex) {
        if (hex <= 'f' && hex >= 'a')
            return (byte) ((hex - 97) + 10);
        if (hex <= 'F' && hex >= 'A')
            return (byte) ((hex - 65) + 10);
        if (hex <= '9' && hex >= '0')
            return (byte) (hex - 48);
        else
            return 0;
    }

    /**
     * @param data
     * @param offset
     * @param len
     * @return
     */
    public static byte[] subBytes(byte data[], int offset, int len) {
        if (offset < 0 || data.length <= offset)
            return null;
        if (len < 0 || data.length < offset + len)
            len = data.length - offset;
        byte ret[] = new byte[len];
        System.arraycopy(data, offset, ret, 0, len);
        return ret;
    }


    /**
     * byte 转 int
     *
     * @param bytes
     * @return
     */
    public static int bytesToInt(byte bytes[]) {
        if (bytes.length > 4)
            return -1;
        int lastIndex = bytes.length - 1;
        int result = 0;
        for (int i = 0; i < bytes.length; i++)
            result |= (bytes[i] & 255) << (lastIndex - i << 3);

        return result;
    }

    /**
     * @param bytes
     * @return
     */
    public static int littleEndianBytesToInt(byte bytes[]) {
        if (bytes.length > 4)
            return -1;
        int result = 0;
        for (int i = 0; i < bytes.length; i++)
            result |= (bytes[i] & 255) << (i << 3);

        return result;
    }


    public static byte[] intToLittleEndianBytes(int intValue) {
        byte bytes[] = new byte[4];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte) (intValue >> (i << 3) & 255);

        return bytes;
    }


    /**
     * bcd  转ascii
     *
     * @param bcd
     * @return
     */
    public static String bcd2Ascii(byte bcd[]) {
        if (bcd == null)
            return "";
        StringBuilder sb = new StringBuilder(bcd.length << 1);
        byte abyte0[];
        int j = (abyte0 = bcd).length;
        for (int i = 0; i < j; i++) {
            byte ch = abyte0[i];
            byte half = (byte) (ch >> 4);
            sb.append((char) (half + (half <= 9 ? 48 : 55)));
            half = (byte) (ch & 15);
            sb.append((char) (half + (half <= 9 ? 48 : 55)));
        }

        return sb.toString();
    }


    /**
     * @param ascii 转  bcd转为密，bcd是
     * @return
     */
    public static byte[] ascii2Bcd(String ascii) {
        if (ascii == null)
            return null;
        if ((ascii.length() & 1) == 1)
            ascii = (new StringBuilder("0")).append(ascii).toString();
        byte asc[] = ascii.getBytes();
        byte bcd[] = new byte[ascii.length() >> 1];
        for (int i = 0; i < bcd.length; i++)
            bcd[i] = (byte) (hex2byte((char) asc[2 * i]) << 4 | hex2byte((char) asc[2 * i + 1]));

        return bcd;
    }


    /**
     * @param data        字符串按照某字符集的规则转换为byte
     * @param charsetName
     * @return
     */
    public static byte[] toBytes(String data, String charsetName) {
        try {
            return data.getBytes(charsetName);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 编码
     *
     * @param data
     * @return
     */
    public static byte[] toBytes(String data) {
        return toBytes(data, "ISO-8859-1");
    }

    public static byte[] toGBK(String data) {
        return toBytes(data, "GBK");
    }

    public static byte[] toGB2312(String data) {
        return toBytes(data, "GB2312");
    }

    public static byte[] toUtf8(String data) {
        return toBytes(data, "UTF-8");
    }

    /**
     * 解码
     *
     * @param data
     * @param charsetName
     * @return
     */
    public static String fromBytes(byte data[], String charsetName) {
        try {
            return new String(data, charsetName);
        } catch (Exception e) {
            return null;
        }
    }

    public static String fromBytes(byte data[]) {
        return fromBytes(data, "ISO-8859-1");
    }

    public static String fromGBK(byte data[]) {
        return fromBytes(data, "GBK");
    }

    public static String fromGB2312(byte data[]) {
        return fromBytes(data, "GB2312");
    }

    public static String fromUtf8(byte data[]) {
        return fromBytes(data, "UTF-8");
    }

    private static final String CHARSET_ISO8859_1 = "ISO-8859-1";
    private static final String CHARSET_GBK = "GBK";
    private static final String CHARSET_GB2312 = "GB2312";
    private static final String CHARSET_UTF8 = "UTF-8";


}
