package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;

import androidx.annotation.Nullable;

public class CIFValidator extends InputValidator {

    public CIFValidator(
            boolean isMandatory,
            int invalidMessageResId
    ) {
        super(isMandatory, invalidMessageResId);
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(
            @Nullable String input
    ) {
        if (TextUtils.isEmpty(input)) {
            return isMandatory ? invalidMessageResId : null;
        } else {
            assert input != null;
            if (isCIFValid(input)) {
                return null;
            } else {
                return invalidMessageResId;
            }
        }
    }

    private boolean isCIFValid(
            String cif
    ) {
        if (!TextUtils.isDigitsOnly(cif.replace(" ", ""))) {
            cif = cif.replace("RO", "").trim();
        }
        if (!TextUtils.isDigitsOnly(cif)) {
            return false;
        }
        int length = cif.length();
        if (length > 10 || length < 2) {
            return false;
        }
        int cui;
        try {
            cui = Integer.parseInt(cif);
        } catch (NumberFormatException e) {
            return false;
        }
        int controlDigit = cui % 10;

        int controlNr = 753217532;
        cui = cui / 10;

        int sum = 0;
        while (cui > 0) {
            sum += (cui % 10) * (controlNr % 10);
            cui = cui / 10;
            controlNr = controlNr / 10;
        }

        int result = sum * 10 % 11;
        if (result == 10) { result = 0; }

        return result == controlDigit;
    }

}
