package ro.doruchidean.inputfields.validators;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ro.doruchidean.inputfields.R;

public class CNPValidator extends InputValidator {

    private final int LENGTH = 13;
    private int[] CONTROL_VALUES = new int[] {
            2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9
    };
    private int[] YEAR_OFFSET = new int[] {
            0, 1900, 1900, 1800, 1800, 2000, 2000
    };

    private int emptyResId;
    private int invalidResId;

    public CNPValidator(boolean isMandatory) {
        super(isMandatory);
    }

    public void setErrorMessages(int emptyResId, int invalidResId) {
        this.emptyResId = emptyResId;
        this.invalidResId = invalidResId;
    }

    @Nullable
    @Override
    public Integer getErrorMessageResId(String cnp) {

        if (TextUtils.isEmpty(cnp)) {
            return R.string.validation_message_cnp_empty;
        }
        if (cnp.length() < 13) {
            return R.string.validation_message_cnp_invalid;
        }
        int[] cnpDigits = getDigits(cnp);
        if (cnpDigits == null) {
            return R.string.validation_message_cnp_invalid;
        }
        if (cnpDigits[0] > 8) {
            return R.string.validation_message_cnp_invalid;
        }
        if (cnpDigits[LENGTH - 1] != getControlSum(cnpDigits)) {
            return R.string.validation_message_cnp_invalid;
        }

        // validate birthdate
        int month = cnpDigits[3] * 10 + cnpDigits[4];
        if (month < 1 || month > 12) {
            return R.string.validation_message_cnp_invalid;
        }
        int dayOfMonth = cnpDigits[5] * 10 + cnpDigits[6];
        if (dayOfMonth < 1) {
            return R.string.validation_message_cnp_invalid;
        }
        int year = YEAR_OFFSET[cnpDigits[0]] + cnpDigits[1] * 10 + cnpDigits[2];
        int maxDayOfMonth = new GregorianCalendar(year, month, dayOfMonth).getActualMaximum(Calendar.DAY_OF_MONTH);
        if (dayOfMonth > maxDayOfMonth) {
            return R.string.validation_message_cnp_invalid;
        }

        return null;

    }

    private int[] getDigits(String cnp) {
        int[] digits = new int[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            char c = cnp.charAt(i);
            if (!Character.isDigit(c)) {
                return null;
            }
            digits[i] = (byte) Character.digit(c, 10);
        }
        return digits;
    }

    private int getControlSum(int[] twelveDigits) {
        int k = 0;
        for (int i = 0; i < 12; i++) {
            k += CONTROL_VALUES[i] * twelveDigits[i];
        }
        k %= 11;
        if (k == 10) {
            k = 1;
        }
        return k;
    }

}
