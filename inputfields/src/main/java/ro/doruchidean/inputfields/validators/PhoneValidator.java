package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;

import androidx.annotation.Nullable;

public class PhoneValidator extends InputValidator {

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
                return emptyResId;
            }
        } else {
            if (input.length() < 10) {
                return tooShortResId;
            }
            String firstChar = input.substring(0, 1);
            if (!firstChar.equals("0") && !firstChar.equals("+")) {
                return invalidResId;
            }
        }
        return null;
    }
}
