package ro.doruchidean.inputfields.validators;

import androidx.annotation.Nullable;

public abstract class InputValidator {

    public boolean isMandatory;

    int emptyResId;
    int invalidResId;

    public void setErrorMessages(int emptyResId, int invalidResId) {
        this.emptyResId = emptyResId;
        this.invalidResId = invalidResId;
    }

    public InputValidator(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public abstract @Nullable Integer getErrorMessageResId(@Nullable String input);

}
