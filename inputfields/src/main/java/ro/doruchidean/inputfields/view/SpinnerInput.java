package ro.doruchidean.inputfields.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

import ro.doruchidean.inputfields.R;
import ro.doruchidean.inputfields.validators.InputValidator;

public class SpinnerInput extends FrameLayout {

    private TextView tvLabel;
    private TextView tvError;
    private Spinner spinner;
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
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_spinner_input, this, true);
        tvLabel = findViewById(R.id.tv_spinner_label);
        tvError = findViewById(R.id.tv_spinner_error);
        spinner = findViewById(R.id.sp_spinner);
        spinner.setOnItemSelectedListener(getOnPickListener());
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
                if (validationListener != null) {
                    validationListener.onInputValidationChanged();
                }
                if (selectionListener != null) {
                    selectionListener.onSpinnerItemSelected(getInput());
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
        spinner.setBackgroundResource(errorBackground > 0 ?
                errorBackground : R.drawable.bg_input_state_error);
    }

    public void hideError() {
        tvError.setVisibility(GONE);
        tvError.setText(null);
        spinner.setBackgroundResource(isValid() ?
                correctBackground > 0 ? correctBackground : R.drawable.bg_input_state_normal :
                normalBackground > 0 ? normalBackground : R.drawable.bg_input_state_normal);
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
        try {
            label = attrsArray.getString(R.styleable.SpinnerInput_label);
            normalBackground = attrsArray.getResourceId(R.styleable.SpinnerInput_normalBackground, -1);
            errorBackground = attrsArray.getResourceId(R.styleable.SpinnerInput_errorBackground, -1);
            correctBackground = attrsArray.getResourceId(R.styleable.SpinnerInput_correctBackground, -1);
        } finally {
            attrsArray.recycle();
        }
        tvLabel.setText(label);
    }

    public @Nullable String getInput() {
        return listAdapter.getItem(spinner.getSelectedItemPosition());
    }

    public void setValidator(@Nullable InputValidator validator,
                             @Nullable InputField.ValidationChangedListener withListener) {
        this.validator = validator;
        this.validationListener = withListener;
    }

    public void setItems(List<String> items) {
        listAdapter = new ArrayAdapter<>(getContext(),
                R.layout.view_spinner_item, R.id.tv_spinner_text, items);
        spinner.setAdapter(listAdapter);
    }

    public void setSelectedItem(@Nullable String item) {
        if (item != null) {
            spinner.setSelection(listAdapter.getPosition(item));
        }
    }

    public interface SelectionListener {
        void onSpinnerItemSelected(String item);
    }
}
