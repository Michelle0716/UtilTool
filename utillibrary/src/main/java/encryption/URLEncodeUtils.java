package encryption;


import android.util.Log;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *发送给服务端的请求中的参数值，如果含有特殊符号，需要是做URLEncode，服务端才可以正常解析，否则可能会出错。

 URLEncode主要是把一些特殊字符转换成转移字符，比如：&要转换成&amp;这样的。

 如果不转换，可能会在运行时直接报错。

 如果全部转换，也会报错，因为会把其中非参数的部分也给转换了。

 所以要确保只有参数部分被转换。
 */
public class URLEncodeUtils {

    /**
     * 编码
     * @param paramString
     * @return
     */
    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            Log.d("toURLEncoded error:", paramString);
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            Log.d("toURLEncoded error:", paramString, localException);
        }

        return "";
    }






    /**
     * 解码
     * @param paramString
     * @return
     */
    public static String toURLDecoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            Log.d("toURLDecoded error:", paramString);
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLDecoder.decode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            Log.e("toURLDecoded error:", paramString, localException);
        }

        return "";
    }



}