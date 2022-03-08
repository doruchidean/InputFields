package ro.doruchidean.inputfields.validators;

import androidx.annotation.Nullable;

public class MinCharsValidator extends InputValidator {

    private final int minChars;

    public MinCharsValidator(boolean isMandatory, int minChars) {
        super(isMandatory);
        this.minChars = minChars;
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(@Nullable String input) {
        int length = input == null ? 0 : input.length();
        if (isMandatory) {
            if (length == 0) return emptyResId;
            else if (length < minChars) return invalidResId;
            else return null;
        } else {
            return length == 0 ? null : length < minChars ? invalidResId : null;
        }
    }
}
