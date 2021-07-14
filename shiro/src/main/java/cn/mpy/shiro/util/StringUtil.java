package cn.mpy.shiro.util;

import java.util.List;

/**
 * 字符串工具类
 * @author mpy
 */
public class StringUtil {
    public static String Addall(List<String> list) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                str += list.get(i);
            } else {
                str += list.get(i) + ",";
            }
        }
        return str;
    }
}
