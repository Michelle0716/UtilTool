package model;


/**
 * Created by michelle on 2018/8/17.
 * 可以在android studio 里面安装GsonFormat，快速生成json的实体类bean
 * 安装后重启as,在code-Generate-GsonFormat，复制进去json的一串数据，确定即可
 *
 * String Data = "{students:[{name:'魏祝林',age:25},{name:'阿魏',age:26}],class:'三年二班'}  ";
   Gson gson = new Gson();
    bean b = gson.fromJson(Data,bean.class);
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;


/**
 * 此类是以泛型的方式整合的一个gson工具类
    gson:把数据转为json,或解析json
 */
public class GsonTools {

    public GsonTools() {
    }


    /**
     * 把一个对象转为gson
     * @param object
     * @return
     */
    public static String createGsonString(Object object) {
        Gson gson = new Gson();
        String gsonString = gson.toJson(object);
        return gsonString;
    }

    /**
     * 把gson与我们创建的实体类绑定，赋值给实体类，
     * 如用@sys请参数命名与服务端应该一致
     * @param gsonString
     * @param cls 实体类bean
     * @param <T>   泛型，不限制实体的类型
     * @return
     */
    public static <T> T changeGsonToBean(String gsonString, Class<T> cls) {
        Gson gson = new Gson();
        T t = gson.fromJson(gsonString, cls);
        return t;
    }

    /**
     * 解析出gson里面的数组
     * @param gsonString
     * @param cls   实体类，实体类列表List<T>
     * @param <T>
     * @return
     */
    public static <T> List<T> changeGsonToList(String gsonString, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
        }.getType());
        return list;
    }

    /**
     *解析gson返回是key-values的健值对数组
     * @param gsonString
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, T>> changeGsonToListMaps(
            String gsonString) {
        List<Map<String, T>> list = null;
        Gson gson = new Gson();
        list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
        }.getType());
        return list;
    }

    /**
     * 解析gson，返回一个key-values对象
     * @param gsonString
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> changeGsonToMaps(String gsonString) {
        Map<String, T> map = null;
        Gson gson = new Gson();
        map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
        }.getType());

        return map;
    }
}