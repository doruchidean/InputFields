package ro.doruchidean.inputfields.validators;

import androidx.annotation.Nullable;

public abstract class InputValidator {

    boolean isMandatory;
    int invalidMessageResId;


    public InputValidator(boolean isMandatory, int invalidMessageResId) {
        this.isMandatory = isMandatory;
        this.invalidMessageResId = invalidMessageResId;
    }

    public abstract @Nullable Integer getErrorMessageResId(@Nullable String input);

    public int getInvalidMessageResId() {
        return invalidMessageResId;
    }

}
