package ro.doruchidean.inputfields.validators;

import androidx.annotation.Nullable;

import ro.doruchidean.inputfields.R;

public class IdDocumentSeriesValidator extends InputValidator {

    private int invalidResId;

    public IdDocumentSeriesValidator(boolean isMandatory) {
        super(isMandatory);
    }

    public void setErrorMessage(int invalidResId) {
        this.invalidResId = invalidResId;
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(String input) {
        if (input.length() == 2) {
            return null;
        }
        if (!isMandatory && input.length() == 0) {
            return null;
        }
        return invalidResId > 0 ? invalidResId : R.string.validation_message_id_document_series_invalid;
    }

}
