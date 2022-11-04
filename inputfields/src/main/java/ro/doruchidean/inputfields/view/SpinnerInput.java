package ro.doruchidean.inputfields.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

import ro.doruchidean.inputfields.R;
import ro.doruchidean.inputfields.model.CustomSelectionItem;
import ro.doruchidean.inputfields.validators.InputValidator;

public class SpinnerInput extends FrameLayout {

    public TextView tvLabel;
    public TextView tvError;
    public TextView tvPersistentHint;
    public View inputContainer;
    public Spinner spinner;
    public ProgressBar progressBar;

    private ArrayAdapter<String> listAdapter;
    @Nullable
    private InputValidator validator;
    @Nullable
    private InputField.ValidationChangedListener validationListener;
    private int normalBackground;
    private int errorBackground;
    private int correctBackground;
    private SelectionListener selectionListener;

    public SpinnerInput(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        setupAttrs(context, attrs);
        inputContainer.setBackgroundResource(getNormalBackground());
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_spinner_input, this, true);
        tvLabel = findViewById(R.id.tv_spinner_label);
        tvPersistentHint = findViewById(R.id.tv_persistent_hint);
        tvError = findViewById(R.id.tv_spinner_error);
        inputContainer = findViewById(R.id.input_main_container);
        spinner = findViewById(R.id.sp_spinner);
        spinner.setOnItemSelectedListener(getOnPickListener());
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(GONE);
    }

    private AdapterView.OnItemSelectedListener getOnPickListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (validator != null) {
                    Integer error = validator.getErrorMessageResId(getInput());
                    if (error != null) {
                        showError(error);
                    } else {
                        hideError();
                    }
                }
                if (selectionListener != null) {
                    selectionListener.onSpinnerItemSelected(getInput());
                }
                if (validationListener != null) {
                    validationListener.onInputValidationChanged(SpinnerInput.this);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //stub
            }
        };
    }

    public void setSelectionListener(SelectionListener listener) {
        selectionListener = listener;
    }

    public void showError(@Nullable Integer errorResId) {
        if (errorResId != null) {
            tvError.setVisibility(VISIBLE);
            tvError.setText(errorResId);
        } else {
            tvError.setVisibility(GONE);
            tvError.setText("");
        }
        inputContainer.setBackgroundResource(getErrorBackground());
    }

    public void hideError() {
        tvError.setVisibility(GONE);
        tvError.setText(null);
        inputContainer.setBackgroundResource(isValid() ? getCorrectBackground() : getNormalBackground());
    }

    public boolean isValid() {
        if (validator == null) {
            return true;
        } else {
            return validator.getErrorMessageResId(getInput()) == null;
        }
    }

    private void setupAttrs(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SpinnerInput, 0, 0);
        String label;
        String persistentHint;
        try {
            label = attrsArray.getString(R.styleable.SpinnerInput_label);
            normalBackground = attrsArray.getResourceId(R.styleable.SpinnerInput_normalBackground, -1);
            errorBackground = attrsArray.getResourceId(R.styleable.SpinnerInput_errorBackground, -1);
            correctBackground = attrsArray.getResourceId(R.styleable.SpinnerInput_correctBackground, -1);
            persistentHint = attrsArray.getString(R.styleable.SpinnerInput_persistentHint);
        } finally {
            attrsArray.recycle();
        }
        tvLabel.setText(label);
        if (TextUtils.isEmpty(persistentHint)) {
            tvPersistentHint.setVisibility(GONE);
        } else {
            tvPersistentHint.setVisibility(VISIBLE);
            tvPersistentHint.setText(persistentHint);
        }
        if (TextUtils.isEmpty(label)) {
            tvLabel.setVisibility(GONE);
        }
    }

    public @Nullable String getInput() {
        if (listAdapter == null || listAdapter.isEmpty()) {
            return null;
        }
        return listAdapter.getItem(spinner.getSelectedItemPosition());
    }

    private int getNormalBackground() {
        return normalBackground > 0 ? normalBackground : R.drawable.bg_input_state_normal;
    }

    private int getErrorBackground() {
        return errorBackground > 0 ? errorBackground : R.drawable.bg_input_state_error;
    }

    private int getCorrectBackground() {
        return correctBackground > 0 ? correctBackground : getNormalBackground();
    }

    public void setValidator(@Nullable InputValidator validator,
                             @Nullable InputField.ValidationChangedListener withListener) {
        this.validator = validator;
        this.validationListener = withListener;
    }

    public void setItems(List<String> items, @Nullable CustomSelectionItem customSpinnerItem) {
        if (customSpinnerItem == null) {
            customSpinnerItem = new CustomSelectionItem(R.layout.view_spinner_item, R.id.tv_spinner_text);
        }
        listAdapter = new ArrayAdapter<>(getContext(),
                customSpinnerItem.getLayoutResId(),
                customSpinnerItem.getTextViewResId(),
                items);
        spinner.setAdapter(listAdapter);
    }

    public void setLabel(int stringResId) {
        tvLabel.setText(stringResId);
        tvLabel.setVisibility(VISIBLE);
    }

    public void setSelectedItem(@Nullable String item) {
        if (item != null) {
            spinner.setSelection(listAdapter.getPosition(item));
        }
    }

    public void setIsLoading(Boolean isLoading) {
        progressBar.setVisibility(isLoading ? VISIBLE : GONE);
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        spinner.setEnabled(isEnabled);
    }

    public interface SelectionListener {
        void onSpinnerItemSelected(String item);
    }

}
