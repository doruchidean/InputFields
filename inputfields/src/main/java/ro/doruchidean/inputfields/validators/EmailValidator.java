package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.Nullable;

public class EmailValidator extends InputValidator {

    public EmailValidator(
            boolean isMandatory,
            int invalidMessageResId
    ) {
        super(isMandatory, invalidMessageResId);
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(@Nullable String input) {
        if (TextUtils.isEmpty(input)) {
            if (isMandatory) {
                return invalidMessageResId;
            }
        } else if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            return invalidMessageResId;
        }
        return null;
    }
}
