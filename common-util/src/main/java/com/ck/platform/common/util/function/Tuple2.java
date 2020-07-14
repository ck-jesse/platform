package com.ck.platform.common.util.function;

/**
 * 二元组类型
 * 元组的使用可以读取 t1 和 t2 然后对它们执行任何操作，但无法对 t1 和 t2 重新赋值。final 可以实现同样的效果，并且更为简洁明了。
 *
 * @author chenck
 * @date 2020/4/22 11:35
 */
public class Tuple2<T1, T2> {

    public final T1 t1;
    public final T2 t2;

    public Tuple2(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public String rep() {
        return t1 + ", " + t2;
    }

    @Override
    public String toString() {
        return "(" + rep() + ')';
    }

    /**
     * 交换
     */
    public Tuple2<T2, T1> swap() {
        return new Tuple2(this.t2, this.t1);
    }

    /**
     * 快捷入口
     */
    public static <C1, C2> Tuple2<C1, C2> of(C1 c1, C2 c2) {
        return new Tuple2(c1, c2);
    }
}
