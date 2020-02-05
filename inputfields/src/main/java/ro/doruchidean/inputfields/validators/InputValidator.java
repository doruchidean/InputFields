package ro.doruchidean.inputfields.validators;

import androidx.annotation.Nullable;

public abstract class InputValidator {

    boolean isMandatory;

    public InputValidator(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public abstract @Nullable Integer getErrorMessageResId(String input);

}
