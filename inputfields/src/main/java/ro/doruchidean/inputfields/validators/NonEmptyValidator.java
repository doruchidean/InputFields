package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;

import androidx.annotation.Nullable;

public class NonEmptyValidator extends InputValidator {

    public NonEmptyValidator(boolean isMandatory) {
        super(isMandatory);
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(String input) {
        return isMandatory && TextUtils.isEmpty(input) ? emptyResId : null;
    }
}
