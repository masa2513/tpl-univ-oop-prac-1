package oop1.section08.kadai1;

import java.util.List;

/**
 * SafeCollectionProcessorインターフェイスのデフォルト実装
 */
public class DefaultSafeCollectionProcessor implements SafeCollectionProcessor {
    @Override
    public int sumPositiveNumbers(List<Integer> numbers) {
        // 早期リターン: nullまたは空のリストの場合
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }

        // 正の数の合計を計算
        return numbers.stream()
                .filter(num -> num != null && num > 0)
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    public int countLongStrings(List<String> texts, int minLength) {
        // 早期リターン: nullまたは空のリストの場合
        if (texts == null || texts.isEmpty()) {
            return 0;
        }

        // 指定長以上の文字列をカウント
        return (int) texts.stream()
                .filter(text -> text != null && text.length() >= minLength)
                .count();
    }
} 