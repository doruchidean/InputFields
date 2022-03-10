package ro.doruchidean.inputfields.validators;

import androidx.annotation.Nullable;

import ro.doruchidean.inputfields.R;

public abstract class InputValidator {

    public boolean isMandatory;

    public int emptyResId = R.string.validation_message_field_empty;
    public int invalidResId = R.string.validation_message_field_invalid;
    public int tooShortResId = R.string.validation_message_field_too_short;

    public InputValidator(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public abstract @Nullable Integer getErrorMessageResId(@Nullable String input);

}
