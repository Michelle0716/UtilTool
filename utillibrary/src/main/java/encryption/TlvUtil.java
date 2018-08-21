package encryption;

/**
 * Created by centerm on 2018/8/21.
 */


import model.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cuixj tag标签的属性为bit，由16进制表示，占1～2个字节长度。例如，“9F33”为一个占用两个字节的tag标签。而“95”
 *         为一个占用一个字节的tag标签
 *         。若tag标签的第一个字节（注：字节排序方向为从左往右数，第一个字节即为最左边的字节。bit排序规则同理。）的后四个bit为
 *         “1111”，则说明该tag占两个字节，例如“9F33”；否则占一个字节，例如“95”。
 *         子域长度（即L本身）的属性也为bit，占1～3个字节长度。具体编码规则如下： a)
 *         当L字段最左边字节的最左bit位（即bit8）为0，表示该L字段占一个字节
 *         ，它的后续7个bit位（即bit7～bit1）表示子域取值的长度，
 *         采用二进制数表示子域取值长度的十进制数。例如，某个域取值占3个字节，那么其子域取值长度表示为
 *         “00000011”。所以，若子域取值的长度在1～127字节之间，那么该L字段本身仅占一个字节。 b)
 *         当L字段最左边字节的最左bit位（即bit8
 *         ）为1，表示该L字段不止占一个字节，那么它到底占几个字节由该最左字节的后续7个bit位（即bit7
 *         ～bit1）的十进制取值表示。例如，若最左字节为10000010
 *         ，表示L字段除该字节外，后面还有两个字节。其后续字节的十进制取值表示子域取值的长度。例如，若L字段为“1000 0001 1111
 *         1111”，表示该子域取值占255个字节。所以，若子域取值的长度在127～255字节之间，那么该L字段本身需占两个字节。
 *
 *         只要用于map对的编码和对卡片所传值tlv的解码,例如AID 、 rid
 */
public class TlvUtil {

    public static Map<String, String> tlvToMap(String tlv) {
        return tlvToMap(hexStringToByte(tlv));
    }

    public static String mapToTlvStr(Map<String, String> map) {
        return bcd2str(mapToTlv(map));
    }


    /**
     * map转tlv byte[]形式
     * @param map
     * @return
     */
    public static byte[] mapToTlv(Map<String, String> map) {
        if (map == null) {
            throw new RuntimeException("map数据不能为null");
        }
        int len = 0;
        // 获取长度
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                int lenght = entry.getValue().length() / 2;// 字节长度
                if (lenght > 0) {
                    if (lenght > 0xFFFF) {
                        throw new RuntimeException("value长度不能超过65535*2");
                    }
                    if (lenght <= 0x7F) { // 长度[1~127]
                        len += 2;
                    }
                    if (lenght > 0x7F && lenght <= 0xFF) {// 长度(127~255]
                        len += 4;
                    }
                    if (lenght > 0xFF && lenght <= 0xFFFF) {// 长度(255,65535]
                        len += 6;
                    }
                    len += entry.getValue().length();
                    len += entry.getKey().length();
                }
            }
        }
        byte[] tlvData = new byte[len / 2];
        int pos = 0;
        // 拷贝数据
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() != null) { // 拷贝Value
                byte[] value = hexStringToByte(entry.getValue());
                int lenght = value.length;
                if (lenght > 0) {
                    if (lenght > 0xFFFF) {
                        throw new RuntimeException("value长度不能超过65535*2");
                    }
                    byte[] key = hexStringToByte(entry.getKey());
                    System.arraycopy(key, 0, tlvData, pos, key.length);// 拷贝KEY
                    pos += key.length;
                    if (lenght <= 0x7F && lenght > 0) {// [1~127]
                        tlvData[pos] = (byte) lenght;
                        pos++;
                    }
                    if (lenght > 0x7F && lenght <= 0xFF) { // (127~255]
                        tlvData[pos] = (byte) (0x81);
                        pos++;
                        tlvData[pos] = (byte) lenght;
                        pos++;
                    }
                    if (lenght > 0xFF && lenght <= 0xFFFF) { // (255~65535]
                        tlvData[pos] = (byte) (0x82);
                        pos++;
                        tlvData[pos] = (byte) ((lenght >> 8) & 0xFF);
                        pos++;
                        tlvData[pos] = (byte) (lenght & 0xFF);
                        pos++;
                    }
                    System.arraycopy(value, 0, tlvData, pos, lenght);
                    pos += lenght;
                }
            }
        }
        return tlvData;
    }

    /**
     * 若tag标签的第一个字节后五个bit为“11111”,则说明该tag占两个字节 例如“9F33”;否则占一个字节，例如“95”
     *
     * @param tlv
     * @return    tlv解码转map
     * @throws
     */
    public static Map<String, String> tlvToMap(byte[] tlv) {
        if (tlv == null) {
            throw new RuntimeException("tlv数据不能为null");
        }
        Map<String, String> map = new HashMap<String, String>();
        int index = 0;
        // System.err.println(tlv.length);
        while (index < tlv.length) {
            if ((tlv[index] & 0x1F) == 0x1F) { // tag双字节
                byte[] tag = new byte[2];
                System.arraycopy(tlv, index, tag, 0, 2);
                index += 2;
                index = copyData(tlv, map, index, tag);
            } else {// tag单字节
                byte[] tag = new byte[1];
                System.arraycopy(tlv, index, tag, 0, 1);
                index++;
                index = copyData(tlv, map, index, tag);
            }
        }

        return map;
    }

    /**
     *
     * @param tlv 解码后，过滤，只取SpecialTag里面的key
     * @return
     */
    public static Map<String, String> SpecialtlvToMap(byte[] tlv){
        String[]  SpecialTag ={"8a","89","71","72","91"};
        Map<String, String> TmpMap = tlvToMap(tlv);//解码
        Map<String, String> SpecailTagMap = new HashMap<String, String>();

        for(Map.Entry<String, String> entry:TmpMap.entrySet()){
            for(int i = 0;i<SpecialTag.length;i++){
                if (entry.getKey().equalsIgnoreCase(SpecialTag[i])) {
                    SpecailTagMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return SpecailTagMap;
    }


    /**
     *
     * @param tlv
     * @param map  解码
     * @param index
     * @param tag
     * @return
     */
    private static int copyData(byte[] tlv, Map<String, String> map, int index,
                                byte[] tag) {
        int length = 0;
        if (tlv[index] >> 7 == 0) { // 表示该L字段占一个字节
            length = tlv[index]; // value字段长度
            index++;
        } else { // 表示该L字段不止占一个字节

            int lenlen = tlv[index] & 0x7F; // 获取该L字段占字节长度
            index++;
            if (lenlen > 2) {
                // throw new TlvExcetion(CODE_LENGTH_OVERLENGTH,
                // "tlvL字段字节长度不大于3");
                throw new RuntimeException("tlvL字段字节长度不大于3");
            }
            for (int i = 0; i < lenlen; i++) {
                length = length << 8;
                length += tlv[index] & 0xff; // value字段长度 &ff转为无符号整型
                index++;
            }
        }

        byte[] value = new byte[length];
        System.arraycopy(tlv, index, value, 0, length);
        index += length;
        map.put(bcd2str(tag), bcd2str(value));
        return index;
    }


    /**
     * BCD数组转string
     * @param bcds
     * @return
     */
    public static String bcd2str(byte[] bcds) {
        char[] ascii = "0123456789abcdef".toCharArray();
        byte[] temp = new byte[bcds.length * 2];

        for (int i = 0; i < bcds.length; i++) {
            temp[i * 2] = (byte) ((bcds[i] >> 4) & 0x0f);
            temp[i * 2 + 1] = (byte) (bcds[i] & 0x0f);
        }
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < temp.length; i++) {
            res.append(ascii[temp[i]]);
        }
        return res.toString().toUpperCase();
    }

    /**
     * 16进制转10进制
     * @param hex
     * @return
     */
    public static byte[] hexStringToByte(String hex) {
        hex = hex.toUpperCase();
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
     * char转byte
     * @param c
     * @return
     */
    private static byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static final int CODE_VALUE_OVERLENGTH = 51;
    public static final int CODE_LENGTH_OVERLENGTH = 52;
    public static final int CODE_PARAMS_INEXISTENCE = 53;


    /**
     * tlv异常
     */
    public static class TlvExcetion extends Exception {
        private static final long serialVersionUID = 5876132721837945560L;
        private int errCode;

        public TlvExcetion(String msg) {
            this(0, msg);
        }

        public TlvExcetion(int code, String msg) {
            super(msg);
            errCode = code;
        }

        public int getErrCode() {
            return errCode;
        }

    }


    /**
     *
     * @param tlv tlv8583解码为map
     * @return
     * @throws Exception
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> tlv8583ToMap(byte[] tlv) throws Exception, UnsupportedEncodingException {
        Map<String, String> map = new HashMap<String, String>();
        int pos = 0;
        int tlvLen = tlv.length;
        while (pos<tlvLen)
        {
            int tag = tlv[pos];
            pos++;
            byte [] lenByte = new byte[2];
            System.arraycopy(tlv, pos, lenByte, 0, lenByte.length);
            int tagLen = getInt(lenByte, false, lenByte.length);
            pos = pos+2;
            byte []valueByte= new byte[tagLen];
            System.arraycopy(tlv, pos, valueByte, 0, valueByte.length);
            String value = new String(valueByte,"GBK");
            map.put(Integer.toString(tag), value);
            pos = pos+valueByte.length;

            Logger.i(Integer.toString(tag) + " --- " + value);
        }

        return map;
    }

    /**
     * byte的tlv数据解码为列表map
     * @param tlv
     * @return
     * @throws Exception
     * @throws UnsupportedEncodingException
     */
    public static List<Map<String, String>> tlvToTradeList(byte[] tlv) throws Exception, UnsupportedEncodingException
    {
        Map<String, String> map = new HashMap<String, String>();
        List<Map<String, String>> list = new ArrayList<Map<String,String>>();
        int pos = 0;
        int tlvLen = tlv.length;

        int firstTag = 0;

        while (pos<tlvLen)
        {
            int tag = tlv[pos];

            if (pos == 0)
            {
                firstTag = tag;
            }
            else
            {
                if (tag == firstTag)
                {
                    // 另一笔交易
                    list.add(map);
                    map = new HashMap<String, String>();
                }
            }

            pos++;
            byte [] lenByte = new byte[2];
            System.arraycopy(tlv, pos, lenByte, 0, lenByte.length);
            int tagLen =getInt(lenByte, false, lenByte.length);
            pos = pos+2;
            byte []valueByte= new byte[tagLen];
            System.arraycopy(tlv, pos, valueByte, 0, valueByte.length);
            String value = new String(valueByte,"GBK");

            map.put(Integer.toString(tag), value);

            pos = pos+valueByte.length;
        }

        list.add(map);

        return list;
    }


    /**
     * 测试，进行tlv编码的一般都是map,编码后以string形式存在
     * @param args
     */
    public static void main(String[] args) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("5F11", null);
        map.put("5F12", "00120100");
        map.put("5F13", null);
        byte[] tlv = null;
        tlv = mapToTlv(map);
        System.out.println(bcd2str(tlv));
        Map<String, String> map1 = null;

        map1 = tlvToMap(tlv);
        for (String key : map1.keySet()) {
            System.out.print("key = " + key);
            System.out.println(" ||  value = " + map1.get(key));
        }

    }


    /* byte to  int */

    /**
     *
     * @param buf  byte[] 转int
     * @param asc  是不是ascill
     * @param len  byte长度
     * @return
     */
    public final static int getInt(byte[] buf, boolean asc, int len) {
        if (buf == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (len > 4) {
            throw new IllegalArgumentException("byte array size > 4 !");
        }
        int r = 0;
        if (asc){


            for (int i = len - 1; i >= 0; i--) {
                r <<= 8;
                r |= (buf[i] & 0x000000ff);
            }
        }
        else {
            for (int i = 0; i < len; i++) {
                r <<= 8;
                r |= (buf[i] & 0x000000ff);
            }
        }
        return r;
    }

}
