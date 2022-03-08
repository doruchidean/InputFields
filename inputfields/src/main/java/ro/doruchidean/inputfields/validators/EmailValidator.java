package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.Nullable;

public class EmailValidator extends InputValidator {

    public EmailValidator(boolean isMandatory) {
        super(isMandatory);
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(@Nullable String input) {
        if (TextUtils.isEmpty(input)) {
            if (isMandatory) {
                return emptyResId;
            }
        } else if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            return invalidResId;
        }
        return null;
    }
}
