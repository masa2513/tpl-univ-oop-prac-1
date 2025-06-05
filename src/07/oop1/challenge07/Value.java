package oop1.challenge07;

import java.util.Objects;

/**
 * 任意の型の単一の値を保持する、イミュータブルなジェネリック値オブジェクト。
 * @param <T> 保持する値の型
 */
public final class Value<T> {
    private final T value;

    /**
     * 指定された値を保持するValueオブジェクトを生成します。
     * @param value 保持する値
     */
    public Value(T value) {
        this.value = value;
    }

    /**
     * 保持している値を取得します。
     * @return 保持している値
     */
    public T get() {
        return value;
    }

    /**
     * 保持している値がnullかどうかを判定します。
     * @return 値がnullの場合はtrue、そうでない場合はfalse
     */
    public boolean isNull() {
        return value == null;
    }

    @Override
    public String toString() {
        return "Value[value: " + value + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value<?> other = (Value<?>) o;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
} 