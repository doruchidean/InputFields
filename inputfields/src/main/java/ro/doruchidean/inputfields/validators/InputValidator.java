package ro.doruchidean.inputfields.validators;

import androidx.annotation.Nullable;

public abstract class InputValidator {

    private final boolean isMandatory;
    int invalidMessageResId;


    public InputValidator(
            boolean isMandatory,
            int invalidMessageResId
    ) {
        this.isMandatory = isMandatory;
        this.invalidMessageResId = invalidMessageResId;
    }

    @Nullable
    public abstract Integer getErrorMessageResId(@Nullable String input);

    public int getInvalidMessageResId() {
        return invalidMessageResId;
    }

    public boolean isMandatory() {
        return isMandatory;
    }
}
