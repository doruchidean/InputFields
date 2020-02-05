package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.Nullable;

import ro.doruchidean.inputfields.R;

public class EmailValidator extends InputValidator {

    private int emptyResId;
    private int invalidResId;

    public EmailValidator(boolean isMandatory) {
        super(isMandatory);
    }

    public void setErrorMessages(int emptyResId, int invalidResId) {
        this.emptyResId = emptyResId;
        this.invalidResId = invalidResId;
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(String input) {
        if (TextUtils.isEmpty(input)) {
            if (isMandatory) {
                return emptyResId > 0 ? emptyResId : R.string.validation_message_email_empty;
            }
        } else if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            return invalidResId > 0 ? invalidResId : R.string.validation_message_invalid_email;
        }
        return null;
    }
}
