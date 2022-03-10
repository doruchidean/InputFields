package ro.doruchidean.inputfields.validators;

import androidx.annotation.Nullable;

public class PasswordValidator extends InputValidator {

    private final int passwordLength;
    private final int emptyResId;

    public PasswordValidator(
            int emptyMessageResId,
            int invalidMessageResId,
            int passwordLength
    ) {
        super(true, invalidMessageResId);
        this.passwordLength = passwordLength;
        this.emptyResId = emptyMessageResId;
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(
            @Nullable String input
    ) {
        if (input == null) {
            return emptyResId;
        } else {
            int length = input.length();
            if (length == 0) {
                return emptyResId;
            }
            if (length < passwordLength) {
                return invalidMessageResId;
            }
        }
        return null;
    }
}
