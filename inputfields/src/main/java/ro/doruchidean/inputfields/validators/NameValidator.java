package ro.doruchidean.inputfields.validators;

import androidx.annotation.Nullable;

public class NameValidator extends InputValidator {

    private final MinCharsValidator minCharsValidator;

    public NameValidator(
            boolean isMandatory,
            int invalidMessageResId
    ) {
        super(isMandatory, invalidMessageResId);
        minCharsValidator = new MinCharsValidator(isMandatory, 3, invalidMessageResId);
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(
            @Nullable String input
    ) {
        return minCharsValidator.getErrorMessageResId(input);
    }
}
