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
        if (input == null) {
            return isMandatory() ? invalidMessageResId : null;
        } else {
            if (input.contains(" ")) return invalidMessageResId;
            return input.isEmpty() ? isMandatory() ? invalidMessageResId : null : null;
        }
    }
}
