package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import ro.doruchidean.inputfields.R;

public class PasswordValidator extends InputValidator {

    private int passwordLength = 6;
    private int emptyResId;
    private int tooShortResId;

    public PasswordValidator(boolean isMandatory) {
        super(isMandatory);
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
    }

    public void setErrorMessages(int emptyResId, int tooShortResId) {
        this.emptyResId = emptyResId;
        this.tooShortResId = tooShortResId;
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(String input) {
        if (TextUtils.isEmpty(input)) {
            if (isMandatory) {
                return emptyResId > 0 ? emptyResId : R.string.validation_message_empty_password;
            }
        } else if (input.length() < passwordLength) {
            return tooShortResId > 0 ? tooShortResId : R.string.validation_message_password_short;
        }
        return null;
    }
}
