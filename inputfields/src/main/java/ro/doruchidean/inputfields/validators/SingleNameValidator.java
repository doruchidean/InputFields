package ro.doruchidean.inputfields.validators;

import androidx.annotation.Nullable;

public class SingleNameValidator extends InputValidator {

    private final MinCharsValidator minCharsValidator;

    public SingleNameValidator(
            boolean isMandatory,
            int invalidMessageResId
    ) {
        super(isMandatory, invalidMessageResId);
        minCharsValidator = new MinCharsValidator(isMandatory, 2, invalidMessageResId);
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(
            @Nullable String input
    ) {
        if (input != null) {
            if (input.contains(" ")) return invalidMessageResId;
            char[] chars = input.toCharArray();
            for (char c : chars) {
                if (!Character.isAlphabetic(c)) {
                    return invalidMessageResId;
                }
            }
            return minCharsValidator.getErrorMessageResId(input);
        }
        return invalidMessageResId;
    }
}
