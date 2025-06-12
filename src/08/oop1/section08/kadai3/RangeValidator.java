package oop1.section08.kadai3;

public class RangeValidator implements InputValidator {
    private final int min;
    private final int max;

    public RangeValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void validate(String input) throws ValidationException {
        try {
            int value = Integer.parseInt(input);
            if (value < min || value > max) {
                throw new ValidationException(
                    String.format("数値は%dから%dの間である必要があります。", min, max)
                );
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("数値として解釈できません。");
        }
    }
} 