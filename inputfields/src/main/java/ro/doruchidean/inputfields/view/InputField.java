package ro.doruchidean.inputfields.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.ArrayList;

import ro.doruchidean.inputfields.R;
import ro.doruchidean.inputfields.validators.InputValidator;

public class InputField extends LinearLayout {

    private final int
            INPUT_TYPE_TEXT = 0,
            INPUT_TYPE_PASSWORD = 1,
            INPUT_TYPE_EMAIL = 2,
            INPUT_TYPE_PHONE = 3,
            INPUT_TYPE_DIGITS = 4,
            CAP_WORDS_STRATEGY = 0,
            CAP_SENTENCES_STRATEGY = 1,
            CAP_ALL_STRATEGY = 2;

    public TextView tvLabel;
    private View mainContainer;
    public View progressBar;
    public AppCompatEditText etInput;
    public TextView tvError;
    public TextView tvPersistentHint;
    public ImageView ivRhsIcon;

    @Nullable
    public InputValidator validator;
    /**
     * If set, it will invoke the callback method each time the validation is performed
     */
    @Nullable
    public ValidationChangedListener validationListener;
    private int normalBackground;
    private int errorBackground;
    private int correctBackground;
    private View nextFocusView;
    @Nullable
    private TextView.OnEditorActionListener editorActionListener;

    public InputField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        init(context);
        setupAttrs(context, attrs);
        showNormalBackground();
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_input_field, this, true);

        mainContainer = findViewById(R.id.input_field_main_container);
        tvLabel = findViewById(R.id.tv_input_field_label);
        tvPersistentHint = findViewById(R.id.tv_persistent_hint);
        etInput = findViewById(R.id.et_input_field_input);
        etInput.addTextChangedListener(getOnInputChangedListener());
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(GONE);
        tvError = findViewById(R.id.tv_input_field_error);
        ivRhsIcon = findViewById(R.id.iv_input_field);
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
        int maxChars;
        int rhsIcon;
        int capitalizeStrategy;
        String persistentHint;
        try {
            label = attrsArray.getString(R.styleable.InputField_label);
            inputType = attrsArray.getInteger(R.styleable.InputField_inputType, INPUT_TYPE_TEXT);
            hint = attrsArray.getString(R.styleable.InputField_hint);
            normalBackground = attrsArray.getResourceId(R.styleable.InputField_normalBackground, -1);
            errorBackground = attrsArray.getResourceId(R.styleable.InputField_errorBackground, -1);
            correctBackground = attrsArray.getResourceId(R.styleable.InputField_correctBackground, -1);
            maxChars = attrsArray.getInteger(R.styleable.InputField_maxChars, -1);
            rhsIcon = attrsArray.getResourceId(R.styleable.InputField_rhsIcon, -1);
            persistentHint = attrsArray.getString(R.styleable.InputField_persistentHint);
            capitalizeStrategy = attrsArray.getInteger(R.styleable.InputField_capitalizeStrategy, -1);
        } finally {
            attrsArray.recycle();
        }
        tvLabel.setText(label);
        if (TextUtils.isEmpty(label)) {
            tvLabel.setVisibility(GONE);
        }
        if (TextUtils.isEmpty(persistentHint)) {
            tvPersistentHint.setVisibility(GONE);
        } else {
            tvPersistentHint.setVisibility(View.VISIBLE);
            tvPersistentHint.setText(persistentHint);
        }
        etInput.setHint(hint);
        if (inputType == INPUT_TYPE_TEXT) {
            inputType = InputType.TYPE_CLASS_TEXT;
        } else if (inputType == INPUT_TYPE_PASSWORD) {
            inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD;
            etInput.setTransformationMethod(new PasswordTransformationMethod());
        } else if (inputType == INPUT_TYPE_EMAIL) {
            inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
        } else if (inputType == INPUT_TYPE_PHONE) {
            inputType = InputType.TYPE_CLASS_PHONE;
        } else if (inputType == INPUT_TYPE_DIGITS) {
            inputType = InputType.TYPE_CLASS_NUMBER;
        }
        if (capitalizeStrategy == CAP_WORDS_STRATEGY) {
            inputType |= InputType.TYPE_TEXT_FLAG_CAP_WORDS;
        } else if (capitalizeStrategy == CAP_SENTENCES_STRATEGY) {
            inputType |= InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;
        } else if (capitalizeStrategy == CAP_ALL_STRATEGY) {
            inputType |= InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
        }
        etInput.setInputType(inputType);
        ArrayList<InputFilter> filters = new ArrayList<>();
        if (maxChars > 0) {
            filters.add(new InputFilter.LengthFilter(maxChars));
        }
        etInput.setFilters(filters.toArray(new InputFilter[]{}));
        etInput.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        if (rhsIcon > 0) {
            ivRhsIcon.setVisibility(VISIBLE);
            ivRhsIcon.setImageResource(rhsIcon);
        } else {
            ivRhsIcon.setVisibility(GONE);
        }
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
                if (validator == null) {
                    return;
                }
                refreshErrorState();
                if (validationListener != null) {
                    validationListener.onInputValidationChanged(InputField.this);
                }
            }
        };
    }

    public void refreshErrorState() {
        if (validator == null) return;
        Editable s = etInput.getText();
        if (TextUtils.isEmpty(s)) {
            hideError();
            showNormalBackground();
        } else {
            assert s != null;
            Integer errorMessage = validator.getErrorMessageResId(s.toString());
            if (errorMessage != null) {
                showError(errorMessage);
            } else {
                hideError();
                showCorrectBackground();
            }
        }
    }

    private void showNormalBackground() {
        mainContainer.setBackgroundResource(getNormalBackground());
    }

    private int getNormalBackground() {
        return normalBackground > 0 ? normalBackground : R.drawable.bg_input_state_normal;
    }

    private void showErrorBackground() {
        mainContainer.setBackgroundResource(getErrorBackground());
    }

    private int getErrorBackground() {
        return errorBackground > 0 ? errorBackground : R.drawable.bg_input_state_error;
    }

    private void showCorrectBackground() {
        mainContainer.setBackgroundResource(getCorrectBackground());
    }

    private int getCorrectBackground() {
        return correctBackground > 0 ? correctBackground : getNormalBackground();
    }

    private void hideError() {
        tvError.setVisibility(GONE);
        tvError.setText(null);
    }

    private void showError(int errorMessage) {
        tvError.setVisibility(VISIBLE);
        tvError.setText(errorMessage);
        showErrorBackground();
    }

    public String getInput() {
        Editable input = etInput.getText();
        if (input != null) {
            return input.toString();
        } else {
            return null;
        }
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

    public void setText(@Nullable String input) {
        etInput.setText(input);
    }

    public void setOnClickListener(@Nullable OnClickListener l, Boolean blockInput) {
        etInput.setClickable(true);
        etInput.setFocusable(!blockInput);
        etInput.setFocusableInTouchMode(!blockInput);
        etInput.setOnClickListener(l);
    }

    public void setLabel(int stringResId) {
        tvLabel.setText(stringResId);
        tvLabel.setVisibility(VISIBLE);
    }

    public interface ValidationChangedListener {
        void onInputValidationChanged(@NonNull Object caller);
    }

    public void setIsLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? VISIBLE : GONE);
    }

    public void setNextFocusView(View view) {
        if (editorActionListener == null) {
            initEditorActionListener();
        }
        nextFocusView = view;
    }

    private void initEditorActionListener() {
        editorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT
                        && nextFocusView != null) {
                    nextFocusView.requestFocus();
                }
                return true;
            }
        };
        etInput.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        etInput.setOnEditorActionListener(editorActionListener);
    }

    public void setIconClickListener(View.OnClickListener listener) {
        ivRhsIcon.setOnClickListener(listener);
    }

}
