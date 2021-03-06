package cn.xyz.test.function;

public class Num<T> {
    public T merge(T key, T value, Cal<T> cal) {
        return cal.apply(key, value);
    }
}
