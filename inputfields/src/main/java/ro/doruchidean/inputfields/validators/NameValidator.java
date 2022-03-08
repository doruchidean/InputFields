package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;

import androidx.annotation.Nullable;

public class NameValidator extends InputValidator {

    public NameValidator(boolean isMandatory) {
        super(isMandatory);
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(String input) {
        if (TextUtils.isEmpty(input)) {
            if (isMandatory) {
                return emptyResId;
            }
        } else if (input.length() < 3) {
            return invalidResId;
        }
        return null;
    }
}
