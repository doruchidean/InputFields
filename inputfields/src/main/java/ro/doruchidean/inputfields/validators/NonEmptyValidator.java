package ro.doruchidean.inputfields.validators;

import androidx.annotation.Nullable;

public class NonEmptyValidator extends InputValidator {

    public NonEmptyValidator(
            boolean isMandatory,
            int invalidMessageResId
    ) {
        super(isMandatory, invalidMessageResId);
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(
            @Nullable String input
    ) {
        if (input == null) {
            return isMandatory() ? invalidMessageResId : null;
        } else {
            return input.trim().isEmpty() ? isMandatory() ? invalidMessageResId : null : null;
        }
    }
}
