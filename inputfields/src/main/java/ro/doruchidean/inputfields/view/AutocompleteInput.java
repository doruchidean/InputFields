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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ro.doruchidean.inputfields.R;
import ro.doruchidean.inputfields.validators.InputValidator;

public class AutocompleteInput extends LinearLayout {

    private final int
            INPUT_TYPE_TEXT = 0,
            INPUT_TYPE_PASSWORD = 1,
            INPUT_TYPE_EMAIL = 2,
            INPUT_TYPE_PHONE = 3,
            INPUT_TYPE_DIGITS = 4;

    private TextView tvLabel;
    private AutoCompleteTextView autoCompleteTextView;
    private TextView tvError;
    private ProgressBar progressBar;
    private View mainInputContainer;

    private InputValidator validator;

    private int normalBackground;
    private int errorBackground;
    private int correctBackground;

    /**
     * If set, it will invoke the callback method each time the validation is calculated
     */
    @Nullable
    private InputField.ValidationChangedListener validationListener;
    private ArrayAdapter<String> listAdapter;
    private SelectionListener selectionListener;

    public AutocompleteInput(@NonNull Context context,
                             @Nullable AttributeSet attrs) {
        super(context, attrs);
        initUI(context);
        setupAttrs(context, attrs);
    }

    private void initUI(Context context) {
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.view_autocomplete_input, this, true);
        mainInputContainer = findViewById(R.id.input_field_main_container);
        tvLabel = findViewById(R.id.tv_autocomplete_label);
        tvError = findViewById(R.id.tv_autocomplete_error);
        autoCompleteTextView = findViewById(R.id.autocomplete_tv);
        autoCompleteTextView.setOnItemClickListener(getOnSelectListener());
        autoCompleteTextView.addTextChangedListener(getOnInputChangedListener());
        findViewById(R.id.btn_open).setOnClickListener(onOpenListener());
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(INVISIBLE);
    }

    private OnClickListener onOpenListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.showDropDown();
            }
        };
    }

    private AdapterView.OnItemClickListener getOnSelectListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectionListener != null) {
                    selectionListener.onAutocompleteItemSelected(listAdapter.getItem(position));
                }
            }
        };
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
                Integer errorMessage = validator.getErrorMessageResId(s.toString());
                if (TextUtils.isEmpty(s)) {
                    setNormalBackground();
                } else if (errorMessage != null) {
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

    private void setupAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.AutocompleteInput, 0, 0);
        String label;
        String hint;
        int inputType;
        boolean textAllCaps;
        int threshHold;
        int maxChars;
        try {
            label = attrsArray.getString(R.styleable.AutocompleteInput_label);
            hint = attrsArray.getString(R.styleable.AutocompleteInput_hint);
            inputType = attrsArray.getInteger(R.styleable.AutocompleteInput_inputType, INPUT_TYPE_TEXT);
            textAllCaps = attrsArray.getBoolean(R.styleable.AutocompleteInput_textAllCaps, false);
            normalBackground = attrsArray.getResourceId(R.styleable.AutocompleteInput_normalBackground, -1);
            errorBackground = attrsArray.getResourceId(R.styleable.AutocompleteInput_errorBackground, -1);
            correctBackground = attrsArray.getResourceId(R.styleable.AutocompleteInput_correctBackground, -1);
            threshHold = attrsArray.getInt(R.styleable.AutocompleteInput_threshHold, 1);
            maxChars = attrsArray.getInteger(R.styleable.AutocompleteInput_maxChars, -1);
        } finally {
            attrsArray.recycle();
        }
        autoCompleteTextView.setHint(hint);
        autoCompleteTextView.setAllCaps(textAllCaps);
        autoCompleteTextView.setThreshold(threshHold);
        if (maxChars > 0) {
            autoCompleteTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxChars)});
        }
        tvLabel.setText(label);
        if (inputType == INPUT_TYPE_PASSWORD) {
            autoCompleteTextView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            autoCompleteTextView.setTransformationMethod(new PasswordTransformationMethod());
        }
        if (inputType == INPUT_TYPE_EMAIL) {
            autoCompleteTextView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }
        if (inputType == INPUT_TYPE_PHONE) {
            autoCompleteTextView.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        if (inputType == INPUT_TYPE_DIGITS) {
            autoCompleteTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    private void showError(int errorMessageReId) {
        tvError.setVisibility(VISIBLE);
        tvError.setText(errorMessageReId);
        mainInputContainer.setBackgroundResource(getErrorBackground());
    }

    private void hideError() {
        tvError.setVisibility(GONE);
        tvError.setText(null);
        mainInputContainer.setBackgroundResource(isValid() ? getCorrectBackground() : getNormalBackground());
    }

    private void setNormalBackground() {
        mainInputContainer.setBackgroundResource(getNormalBackground());
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

    public @Nullable String getInput() {
        return autoCompleteTextView.getText().toString();
    }

    public void setItems(List<String> items) {
        listAdapter = new ArrayAdapter<>(getContext(),
                R.layout.view_spinner_item, R.id.tv_spinner_text, items);
        autoCompleteTextView.setAdapter(listAdapter);
    }

    public void setSelectedItem(@Nullable String item) {
        autoCompleteTextView.setText(item);
    }

    public void setSelectionListener(SelectionListener listener) {
        this.selectionListener = listener;
    }

    public void setValidator(InputValidator validator,
                             @Nullable InputField.ValidationChangedListener withListener) {
        this.validator = validator;
        this.validationListener = withListener;
    }

    public boolean isValid() {
        if (validator == null) {
            return true;
        } else {
            return validator.getErrorMessageResId(getInput()) == null;
        }
    }

    public AutoCompleteTextView getInputView() {
        return autoCompleteTextView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setIsLoading(Boolean isLoading) {
        progressBar.setVisibility(isLoading ? VISIBLE : INVISIBLE);
    }

    public interface SelectionListener {
        void onAutocompleteItemSelected(String item);
    }
}
