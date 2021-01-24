package cn.xyz.common.pojo;

import cn.xyz.common.exception.CustomException;

public class NotNull<T> {
    public T value;
    public NotNull(T value) throws CustomException {
        this.value = value;
    }

    /*public static NotNull<T>(T value)
    {
        if (value == null)
            throw new CustomException("");
        return new NotNull<>(value);
    }*/

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
