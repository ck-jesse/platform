package com.ck.platform.common.util.function;

/**
 * 三元组类型
 *
 * @author chenck
 * @date 2020/7/14 15:49
 */
public class Tuple3<T1, T2, T3> extends Tuple2<T1, T2> {

    public final T3 t3;

    public Tuple3(T1 t1, T2 t2, T3 t3) {
        super(t1, t2);
        this.t3 = t3;
    }

    @Override
    public String rep() {
        return super.rep() + ", " + t3;
    }
}
