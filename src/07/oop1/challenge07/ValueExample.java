package oop1.challenge07;

/**
 * Value<T>とValueUtilsの使用例を示すクラス。
 */
public class ValueExample {
    public static void main(String[] args) {
        // 例1: 文字列の値を保持するValueオブジェクト
        Value<String> stringValue = ValueUtils.of("Hello, World!");
        System.out.println("文字列の値: " + stringValue.get());
        System.out.println("nullチェック: " + stringValue.isNull());

        // 例2: 数値の変換
        Value<Integer> numberValue = ValueUtils.of(42);
        Value<String> stringNumber = ValueUtils.map(numberValue, Object::toString);
        System.out.println("数値から文字列への変換: " + stringNumber.get());

        // 例3: null値の処理
        Value<String> nullValue = ValueUtils.of(null);
        Value<Integer> lengthValue = ValueUtils.map(nullValue, String::length);
        System.out.println("null値の処理: " + lengthValue.isNull());

        // 例4: 複雑な変換
        Value<Double> priceValue = ValueUtils.of(100.0);
        Value<String> formattedPrice = ValueUtils.map(priceValue,
            price -> String.format("¥%.2f", price));
        System.out.println("価格のフォーマット: " + formattedPrice.get());
    }
} 