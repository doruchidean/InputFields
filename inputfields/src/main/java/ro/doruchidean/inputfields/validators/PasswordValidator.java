package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;

import androidx.annotation.Nullable;

public class PasswordValidator extends InputValidator {

    private int passwordLength = 6;

    public PasswordValidator(boolean isMandatory) {
        super(isMandatory);
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
    }

    public void setErrorMessages(int emptyResId, int invalidResId, int tooShortResId) {
        this.invalidResId = invalidResId;
        this.emptyResId = emptyResId;
        this.tooShortResId = tooShortResId;
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(String input) {
        if (TextUtils.isEmpty(input)) {
            if (isMandatory) {
                return emptyResId;
            }
        } else if (input.length() < passwordLength) {
            return tooShortResId;
        }
        return null;
    }
}
