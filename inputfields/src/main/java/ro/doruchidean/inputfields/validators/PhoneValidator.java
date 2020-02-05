package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import ro.doruchidean.inputfields.R;

public class PhoneValidator extends InputValidator {

    private int emptyResId;
    private int tooShortResId;
    private int invalidResId;

    public PhoneValidator(boolean isMandatory) {
        super(isMandatory);
    }

    public void setErrorMessages(int emptyResId, int tooShortResId, int invalidResId) {
        this.emptyResId = emptyResId;
        this.tooShortResId = tooShortResId;
        this.invalidResId = invalidResId;
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(String input) {
        if (TextUtils.isEmpty(input)) {
            if (isMandatory) {
                return emptyResId > 0 ? emptyResId : R.string.validation_message_phone_empty;
            }
        } else {
            if (input.length() < 10) {
                return tooShortResId > 0 ? tooShortResId : R.string.validation_message_phone_too_short;
            }
            String firstChar = input.substring(0, 1);
            if (!firstChar.equals("0") && !firstChar.equals("+")) {
                return invalidResId > 0 ? invalidResId : R.string.validation_message_phone_invalid;
            }
        }
        return null;
    }
}
