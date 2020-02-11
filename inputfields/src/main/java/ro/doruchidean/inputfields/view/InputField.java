package ro.doruchidean.inputfields.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import ro.doruchidean.inputfields.R;
import ro.doruchidean.inputfields.validators.InputValidator;

public class InputField extends LinearLayout {

    private final int
            INPUT_TYPE_TEXT = 0,
            INPUT_TYPE_PASSWORD = 1,
            INPUT_TYPE_EMAIL = 2,
            INPUT_TYPE_PHONE = 3,
            INPUT_TYPE_DIGITS = 4;

    private TextView tvLabel;
    private AppCompatEditText etInput;
    private TextView tvError;

    @Nullable
    private InputValidator validator;
    /**
     * If set, it will invoke the callback method each time the validation is calculated
     */
    @Nullable
    private ValidationChangedListener validationListener;
    private int normalBackground;
    private int errorBackground;
    private int correctBackground;

    public InputField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        init(context);
        setupAttrs(context, attrs);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_input_field, this, true);
        setFocusable(false);
        setFocusableInTouchMode(false);
        tvLabel = findViewById(R.id.tv_autocomplete_label);
        etInput = findViewById(R.id.et_input_field_input);
        tvError = findViewById(R.id.tv_input_field_error);
        etInput.addTextChangedListener(getOnInputChangedListener());
    }

    private void setupAttrs(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.InputField, 0, 0);
        String label;
        int inputType;
        String hint;
        boolean textAllCaps;

        try {
            label = attrsArray.getString(R.styleable.InputField_label);
            inputType = attrsArray.getInteger(R.styleable.InputField_inputType, INPUT_TYPE_TEXT);
            hint = attrsArray.getString(R.styleable.InputField_hint);
            textAllCaps = attrsArray.getBoolean(R.styleable.InputField_textAllCaps, false);
            normalBackground = attrsArray.getResourceId(R.styleable.InputField_normalBackground, -1);
            errorBackground = attrsArray.getResourceId(R.styleable.InputField_errorBackground, -1);
            correctBackground = attrsArray.getResourceId(R.styleable.InputField_correctBackground, -1);
        } finally {
            attrsArray.recycle();
        }
        tvLabel.setText(label);
        etInput.setHint(hint);
        etInput.setAllCaps(textAllCaps);
        if (inputType == INPUT_TYPE_PASSWORD) {
            etInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etInput.setTransformationMethod(new PasswordTransformationMethod());
        }
        if (inputType == INPUT_TYPE_EMAIL) {
            etInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }
        if (inputType == INPUT_TYPE_PHONE) {
            etInput.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        if (inputType == INPUT_TYPE_DIGITS) {
            etInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        etInput.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
    }

    private TextWatcher getOnInputChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    setNormalBackground();
                    return;
                }
                if (validator == null) {
                    return;
                }
                Integer errorMessage = validator.getErrorMessageResId(s.toString());
                if (errorMessage != null) {
                    showError(errorMessage);
                } else {
                    hideError();
                }
                if (validationListener != null) {
                    validationListener.onInputValidationChanged();
                }
            }
        };
    }

    private void setNormalBackground() {
        etInput.setBackgroundResource(getNormalBackground());
    }

    private int getNormalBackground() {
        return normalBackground > 0 ? normalBackground : R.drawable.bg_input_state_normal;
    }

    private int getErrorBackground() {
        return errorBackground > 0 ? errorBackground : R.drawable.bg_input_state_error;
    }

    private int getCorrectBackground() {
        return correctBackground > 0 ? correctBackground : R.drawable.bg_input_state_normal;
    }

    private void hideError() {
        tvError.setVisibility(GONE);
        tvError.setText(null);
        etInput.setBackgroundResource(isValid() ? getCorrectBackground() : getNormalBackground());
    }

    private void showError(int errorMessage) {
        tvError.setVisibility(VISIBLE);
        tvError.setText(errorMessage);
        etInput.setBackgroundResource(getErrorBackground());
    }

    public String getInput() {
        Editable input = etInput.getText();
        if (input != null) {
            return input.toString();
        } else {
            return null;
        }
    }

    public AppCompatEditText getInputView() {
        return etInput;
    }

    public void setValidator(InputValidator validator, @Nullable ValidationChangedListener withListener) {
        this.validator = validator;
        this.validationListener = withListener;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(getInput());
    }

    public boolean isValid() {
        if (validator == null) {
            return true;
        } else {
            return validator.getErrorMessageResId(getInput()) == null;
        }
    }

    public void setText(String input) {
        etInput.setText(input);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        etInput.setClickable(true);
        etInput.setOnClickListener(l);
    }

    public void setLabel(int stringResId) {
        tvLabel.setText(stringResId);
    }

    public interface ValidationChangedListener {
        void onInputValidationChanged();
    }
}
