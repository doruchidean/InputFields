package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import ro.doruchidean.inputfields.R;

public class NameValidator extends InputValidator {

    private int emptyResId;
    private int tooShortResId;

    public NameValidator(boolean isMandatory) {
        super(isMandatory);
    }

    public void setErrorMessages(int emptyResId, int tooShortResId) {
        this.emptyResId = emptyResId;
        this.tooShortResId= tooShortResId;
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(String input) {

        if (TextUtils.isEmpty(input)) {
            if (isMandatory) {
                return emptyResId > 0 ? emptyResId : R.string.validation_message_name_empty;
            }
        } else if (input.length() < 3) {
            return tooShortResId > 0 ? tooShortResId : R.string.validation_message_name_too_short;
        }
        return null;
    }
}
