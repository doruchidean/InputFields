package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import ro.doruchidean.inputfields.R;

public class NonEmptyValidator extends InputValidator {

    private int emptyResId;

    public NonEmptyValidator(boolean isMandatory) {
        super(isMandatory);
    }

    public void setErrorMessage(int emptyResId) {
        this.emptyResId = emptyResId;
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(String input) {
        if (isMandatory && TextUtils.isEmpty(input)) {
            return emptyResId > 0 ? emptyResId : R.string.validation_message_empty_field;
        }
        return null;
    }
}
