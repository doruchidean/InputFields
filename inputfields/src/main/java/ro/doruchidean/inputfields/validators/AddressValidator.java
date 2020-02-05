package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import ro.doruchidean.inputfields.R;

public class AddressValidator extends InputValidator {

    private int emptyResId;
    private int invalidResId;

    public AddressValidator(boolean isMandatory) {
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
                return emptyResId > 0 ? emptyResId : R.string.validation_message_empty_address;
            }
        } else {
            String lowered = input.toLowerCase();
            if (!lowered.contains("str")
                    && (!lowered.contains("nr") || !lowered.contains("num"))) {

                return invalidResId > 0 ? invalidResId : R.string.validation_message_address_incorrect;
            }
        }
        return null;
    }

}
