package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;

import androidx.annotation.Nullable;

public class AddressValidator extends InputValidator {

    public AddressValidator(boolean isMandatory) {
        super(isMandatory);
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(String input) {
        if (TextUtils.isEmpty(input)) {
            if (isMandatory) {
                return emptyResId > 0 ? emptyResId : emptyResId;
            }
        } else {
            String lowered = input.toLowerCase();
            if (!lowered.contains("str")
                    && (!lowered.contains("nr") || !lowered.contains("num"))) {

                return invalidResId > 0 ? invalidResId : invalidResId;
            }
        }
        return null;
    }

}
