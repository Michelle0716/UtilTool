package encryption;

import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by michelle on 2018/8/20.
 */

public class DESUtil {
    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

    /**
     * DES算法，加密
     *
     * @param data
     *            待加密字符串
     * @param key  需要加密的业务类型,如已经经过一层加密的登陆密码所形成的密钥，加密解密的key是一致的
     * @return 加密后的字节数组，一般结合Base64编码使用
     *
     * @throws Exception
     */
    public static String encode(byte[] key, String data) {
        if (data == null){
            return null;

        }
        try {
            byte[]value = key;//values是公钥，加密和解密需要一致的公钥
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(value);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

            // key的长度不能够小于8位字节
            //空处理
            if(null==data.getBytes()||data.getBytes().length==0||null==key||key.length==0)
            {
                return null;
            }
            if(data.getBytes().length%8!=0)
            {
                throw new IllegalArgumentException("加密数据非8字节的倍数");
            }

            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);//获取对象
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);//初始化
            byte[] bytes = cipher.doFinal(data.getBytes()); //执行加密


            //随机数加密
            // DES算法要求有一个可信任的随机数源
   //         Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
//            SecureRandom secureRandom = new SecureRandom();
//            cipher.init(Cipher.DECRYPT_MODE, secretKey, secureRandom);


            return byte2String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * DES算法，解密
     *
     * @param data
     *            待解密字符串
     * @param key
     *            需要解密的业务类型
     * @return 解密后的字节数组
     * @throws Exception
     *             异常
     */
    public static String decode(byte[] key, String data) {
        if (data == null)
            return null;
        try {
            byte[] value = key;//values是公钥，加密和解密需要一致的公钥
            DESKeySpec dks = new DESKeySpec(value);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return new String(cipher.doFinal(byte2hex(data.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * 二行制转字符串
     *
     * @param b
     * @return String
     */
    private static String byte2String(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase(Locale.CHINA);
    }

    /**
     * 二进制转化成16进制
     *
     * @param b
     * @return byte
     */
    private static byte[] byte2hex(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }



    /**
     *     3DES  加密  加-解-加
     * @param encrypted_data
     * @param key_left   左密钥
     * @param key_right  右密钥
     * @return          密钥要2个或3个，且不一样，如一样就变成des
     * 备注：如果解码是   解-加-解
     */
    public static byte[] TDES_DOUBLE(String encrypted_data, String  key_left,String key_right)
    {
        byte[] resault=null;
        //1.使用密钥的左半边对数据进行DES解密，得到temp1
        byte[] temp1= decode(key_left.getBytes(),encrypted_data).getBytes();
        //2.使用密钥的右半边对数据进行DES加密，得到temp2
        byte[] temp2= encode(temp1,key_right).getBytes();
        //3.使用密钥的左半边对数据进行DES解密，得到最终解密结果
        resault= decode(temp2,key_left).getBytes();

        return resault;
    }



}
