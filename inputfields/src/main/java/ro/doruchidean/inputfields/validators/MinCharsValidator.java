package ro.doruchidean.inputfields.validators;

import androidx.annotation.Nullable;

public class MinCharsValidator extends InputValidator {

    private final int minChars;

    public MinCharsValidator(
            boolean isMandatory,
            int minChars,
            int invalidMessageResId
    ) {
        super(isMandatory, invalidMessageResId);
        this.minChars = minChars;
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(
            @Nullable String input
    ) {
        int length = input == null ? 0 : input.length();
        if ((!isMandatory() && length == 0) || length >= minChars) {
            return null;
        } else {
            return invalidMessageResId;
        }
    }
}
