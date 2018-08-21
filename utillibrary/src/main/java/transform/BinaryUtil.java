package transform;

import android.graphics.Color;

/**
 * Created by michelle on 2018/8/16.进制转换类
 */

public class BinaryUtil {

    /**
     * 10进制转化为16进制
     *
     * @param n
     * @return
     */
    public static String tenToHex(int n) {
        return Integer.toHexString(n);
    }

    /**
     * 16进制转为10进制
     *
     * @param hex
     * @return
     */
    public static int hexToTen(String hex) {
        return Integer.valueOf(hex, 16);
    }

    /**
     * 2进制转为10进制
     *
     * @param two
     * @return
     */
    public static int twoToTen(String two) {
        return Integer.valueOf(two, 2);
    }

    /**
     * @param colorHex 颜色的16进制转换为10进制，如"#ff123456"
     * @return 用于setBackgroundColor(Color.parseColor("#ff000000"));
     */
    public static int colorhexToTen(String colorHex) {
        return Color.parseColor(colorHex);
    }

}
