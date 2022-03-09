package ro.doruchidean.inputfields.validators;

import androidx.annotation.Nullable;

import ro.doruchidean.inputfields.R;

public abstract class InputValidator {

    public boolean isMandatory;

    int emptyResId = R.string.validation_message_field_empty;
    int invalidResId = R.string.validation_message_field_invalid;

    public void setErrorMessages(int emptyResId, int invalidResId) {
        this.emptyResId = emptyResId;
        this.invalidResId = invalidResId;
    }

    public InputValidator(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public abstract @Nullable Integer getErrorMessageResId(@Nullable String input);

}
