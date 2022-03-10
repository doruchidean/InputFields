package ro.doruchidean.inputfields.validators;

import androidx.annotation.Nullable;

public class PhoneValidator extends InputValidator {

    public PhoneValidator(
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
        }
        int length = input.length();
        if (length == 0) {
            return isMandatory() ? invalidMessageResId : null;
        } else if (length < 10) {
            return invalidMessageResId;
        } else {
            String firstChar = input.substring(0, 1);
            if (!firstChar.equals("0") && !firstChar.equals("+")) {
                return invalidMessageResId;
            } else {
                return null;
            }
        }
    }
}
