package org.coderead.mybatis;

/**
 * @author HanBin_Yang
 * @since 2023/7/14 14:44
 */
public class VectorUtil {

    /**
     * vector日志采集函数seahash, 对于的java实现
     * @param str str
     * @return 64位hashCode
     */
    public static long seaHash(String str) {
        State state = new State();
        return state.seaHash(str);
    }
}
