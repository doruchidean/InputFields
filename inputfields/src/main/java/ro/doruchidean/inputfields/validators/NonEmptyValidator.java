package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;

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
        if (TextUtils.isEmpty(input)) {
            return isMandatory() ? invalidMessageResId : null;
        } else {
            return null;
        }
    }
}
