package ro.doruchidean.inputfields.validators;

import androidx.annotation.Nullable;

import ro.doruchidean.inputfields.R;

public class MinCharsValidator extends InputValidator {

    private final int minChars;
    private int tooShortMessage = R.string.validation_message_field_too_short;
    private int emptyMessage = R.string.validation_message_empty_field;

    public MinCharsValidator(boolean isMandatory, int minChars) {
        super(isMandatory);
        this.minChars = minChars;
    }

    public void setErrorMessage(int tooShortMessage, int emptyMessage) {
        this.tooShortMessage = tooShortMessage;
        this.emptyMessage = emptyMessage;
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(@Nullable String input) {
        int length = input == null ? 0 : input.length();
        if (isMandatory) {
            if (length == 0) return emptyMessage;
            else if (length < minChars) return tooShortMessage;
            else return null;
        } else {
            return length == 0 ? null : length < minChars ? tooShortMessage : null;
        }
    }
}
