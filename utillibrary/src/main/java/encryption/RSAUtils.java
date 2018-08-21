package encryption;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;

/**
 * Created by michelle on 2018/8/20.
 * RSA加密和解密
 */

public class RSAUtils {

    /**
     * RSA算法
     */
    public static final String RSA = "RSA";
    /**加密方式，android的*/
// public static final String TRANSFORMATION = "RSA/None/NoPadding";
    /**
     * 加密方式，标准jdk的
     */
    public static final String TRANSFORMATION = "RSA/None/PKCS1Padding";

    /**
     * 使用公钥加密
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        // 加密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.ENCRYPT_MODE, pubKey);
        return cp.doFinal(data);
    }

    /**
     * 使用私钥解密
     */
    public static byte[] decryptByPrivateKey(byte[] encrypted, byte[] privateKey) throws Exception {
        // 得到私钥对象
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        // 解密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.DECRYPT_MODE, keyPrivate);
        byte[] arr = cp.doFinal(encrypted);
        return arr;
    }


    /**
     * 生成密钥对，即公钥和私钥。key长度是512-2048，一般为1024
     */
    public static KeyPair generateRSAKeyPair(int keyLength) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
        kpg.initialize(keyLength);
        return kpg.genKeyPair();
    }

    /**
     * 获取公钥，打印为48-12613448136942-12272-122-913111503-126115048-12...等等一长串用-拼接的数字
     */
    public static byte[] getPublicKey(KeyPair keyPair) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        return rsaPublicKey.getEncoded();
    }

    /**
     * 获取私钥，同上
     */
    public static byte[] getPrivateKey(KeyPair keyPair) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        return rsaPrivateKey.getEncoded();
    }


    public void testRSA() {
        String data = "hello world";
        try {
            int keyLength = 1024;
            //生成密钥对
            KeyPair keyPair = RSAUtils.generateRSAKeyPair(keyLength);
            //获取公钥
            byte[] publicKey = RSAUtils.getPublicKey(keyPair);
            //获取私钥
            byte[] privateKey = RSAUtils.getPrivateKey(keyPair);

            //用公钥加密
            byte[] encrypt = RSAUtils.encryptByPublicKey(data.getBytes(), publicKey);
            Log.d("TAG", "加密后的数据：" + Arrays.toString(encrypt));//输出109-1171951-2125-72-.......

            //用私钥解密
            byte[] decrypt = RSAUtils.decryptByPrivateKey(encrypt, privateKey);
            Log.d("TAG", "解密后的数据：" + new String(decrypt, "utf-8"));//"hello world"
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 从assets文件夹下读取rsa_public_key.pem文件的公钥
     */
    public void readKeyFromFile(Context context,String file) {
        String data = "hello world";
        file="rsa_public_key.pem";
        //读取公钥文件
        String publicKeyString = readAssetsFile(context, file);
        //针对读取的密钥加密类型，进行对应解码，如base64解码
        byte[] publicKey =decodeToBase(publicKeyString.getBytes()).getBytes();
        try {
            //加密
            byte[] encrypt = RSAUtils.encryptByPublicKey(data.getBytes(), publicKey);
            Log.d("TAG", "加密后的数据：" + Arrays.toString(encrypt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public  String readAssetsFile(Context context,String file){
         return "从assts里面读取该文件的数据";
    }


    /**
     * 秘钥默认长度
     */
    public static final int DEFAULT_KEY_SIZE = 1024;
    /**
     * 加密的数据最大的字节数，即117个字节
     */
    public static final int DEFAULT_BUFFERSIZE = (DEFAULT_KEY_SIZE / 8) - 11;
    /**
     * 当加密的数据超过DEFAULT_BUFFERSIZE，则使用分段加密
     */
    public static final byte[] DEFAULT_SPLIT = "#PART#".getBytes();

    /**
     * 使用公钥分段加密
     */
    public static byte[] encryptByPublicKeyForSpilt(byte[] data, byte[] publicKey) throws Exception {
        int dataLen = data.length;
        if (dataLen <= DEFAULT_BUFFERSIZE) {
            return encryptByPublicKey(data, publicKey);
        }
        List<Byte> allBytes = new ArrayList<Byte>(2048);
        int bufIndex = 0;
        int subDataLoop = 0;
        byte[] buf = new byte[DEFAULT_BUFFERSIZE];
        for (int i = 0; i < dataLen; i++) {
            buf[bufIndex] = data[i];
            if (++bufIndex == DEFAULT_BUFFERSIZE || i == dataLen - 1) {
                subDataLoop++;
                if (subDataLoop != 1) {
                    for (byte b : DEFAULT_SPLIT) {
                        allBytes.add(b);
                    }
                }
                byte[] encryptBytes = encryptByPublicKey(buf, publicKey);
                for (byte b : encryptBytes) {
                    allBytes.add(b);
                }
                bufIndex = 0;
                if (i == dataLen - 1) {
                    buf = null;
                } else {
                    buf = new byte[Math
                            .min(DEFAULT_BUFFERSIZE, dataLen - i - 1)];
                }
            }
        }
        byte[] bytes = new byte[allBytes.size()];
        int i = 0;
        for (Byte b : allBytes) {
            bytes[i++] = b.byteValue();
        }
        return bytes;
    }


    /**
     * 使用私钥分段解密
     */
    public static byte[] decryptByPrivateKeyForSpilt(byte[] encrypted, byte[] privateKey) throws Exception {
        int splitLen = DEFAULT_SPLIT.length;
        if (splitLen <= 0) {
            return decryptByPrivateKey(encrypted, privateKey);
        }
        int dataLen = encrypted.length;
        List<Byte> allBytes = new ArrayList<Byte>(1024);
        int latestStartIndex = 0;
        for (int i = 0; i < dataLen; i++) {
            byte bt = encrypted[i];
            boolean isMatchSplit = false;
            if (i == dataLen - 1) {
                // 到data的最后了
                byte[] part = new byte[dataLen - latestStartIndex];
                System.arraycopy(encrypted, latestStartIndex, part, 0, part.length);
                byte[] decryptPart = decryptByPrivateKey(part, privateKey);
                for (byte b : decryptPart) {
                    allBytes.add(b);
                }
                latestStartIndex = i + splitLen;
                i = latestStartIndex - 1;
            } else if (bt == DEFAULT_SPLIT[0]) {
                // 这个是以split[0]开头
                if (splitLen > 1) {
                    if (i + splitLen < dataLen) {
                        // 没有超出data的范围
                        for (int j = 1; j < splitLen; j++) {
                            if (DEFAULT_SPLIT[j] != encrypted[i + j]) {
                                break;
                            }
                            if (j == splitLen - 1) {
                                // 验证到split的最后一位，都没有break，则表明已经确认是split段
                                isMatchSplit = true;
                            }
                        }
                    }
                } else {
                    // split只有一位，则已经匹配了
                    isMatchSplit = true;
                }
            }
            if (isMatchSplit) {
                byte[] part = new byte[i - latestStartIndex];
                System.arraycopy(encrypted, latestStartIndex, part, 0, part.length);
                byte[] decryptPart = decryptByPrivateKey(part, privateKey);
                for (byte b : decryptPart) {
                    allBytes.add(b);
                }
                latestStartIndex = i + splitLen;
                i = latestStartIndex - 1;
            }
        }
        byte[] bytes = new byte[allBytes.size()];
        int i = 0;
        for (Byte b : allBytes) {
            bytes[i++] = b.byteValue();
        }
        return bytes;
    }


    /**
     * 数据多时
     * 测试分段加密和解密
     * @param publicKey
     * @param privateKey
     */
    public void testMoreRAS(byte[] publicKey,byte[] privateKey ){

        String data = "hello world hello world hello world hello world hello world hello world hello world hello world " +
                "hello world hello world hello world hello world hello world hello world hello world hello world hello world " +
                "hello world hello world hello world hello world hello world hello world hello world hello world hello world ";
        Log.e("TAG","要加密的数据："+data +", 要加密的数据长度："+data.length());
        try{
            //分段加密
            byte[] encrypt = RSAUtils.encryptByPublicKeyForSpilt(data.getBytes(), publicKey);
            Log.d("TAG", "加密后的数据：" + Arrays.toString(encrypt));

            //分段解密
            byte[] decrypt = RSAUtils.decryptByPrivateKeyForSpilt(encrypt, privateKey);
            Log.d("TAG", "解密后的数据：" + new String(decrypt, "utf-8"));
        } catch(Exception e)

        {
            e.printStackTrace();
        }

    }

    /**
     * 对生成后的公钥和私钥进行base64位加密
     */
   public void keyToBase64(){
       int keyLength = 1024;
       try {
           //生成密钥对
           KeyPair keyPair = RSAUtils.generateRSAKeyPair(keyLength);

           //获取公钥
           byte[] publicKey = RSAUtils.getPublicKey(keyPair);
           Log.d("TAG", "公钥：" + Arrays.toString(publicKey));
           //公钥用base64编码
           String encodePublic = encodeToString(publicKey);
           Log.d("TAG", "base64编码的公钥：" + encodePublic);

           //获取私钥
           byte[] privateKey = RSAUtils.getPrivateKey(keyPair);
           Log.d("TAG", "私钥：" + Arrays.toString(privateKey));
           //私钥用base64编码
           String encodePrivate = encodeToString(privateKey);
           Log.d("TAG", "base64编码的私钥：" + encodePrivate);
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       }
   }

    /**
     * base64 编码
     * @param key
     * @return
     */
 public String  encodeToString(byte[] key){
       return  Base64.encodeToString(key, Base64.DEFAULT);

 }

    /**
     * //base64解码
     * @param key
     * @return
     */
 public String decodeToBase(byte[] key){
     return  new String(Base64.decode(key, Base64.DEFAULT));

 }




}
