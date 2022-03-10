package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;

import androidx.annotation.Nullable;

public class AddressValidator extends InputValidator {

    public AddressValidator(
            boolean isMandatory,
            int invalidMessageResId
    ) {
        super(isMandatory, invalidMessageResId);
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(String input) {
        if (TextUtils.isEmpty(input)) {
            if (isMandatory) {
                return invalidMessageResId;
            }
        } else {
            String lowered = input.toLowerCase();
            if (!lowered.contains("str")
                    && (!lowered.contains("nr") || !lowered.contains("num"))) {

                return invalidMessageResId;
            }
        }
        return null;
    }

}
