package ro.doruchidean.inputfieldsexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ro.doruchidean.inputfields.validators.CNPValidator;
import ro.doruchidean.inputfields.validators.InputValidator;
import ro.doruchidean.inputfields.validators.NonEmptyValidator;
import ro.doruchidean.inputfields.view.AutocompleteInput;
import ro.doruchidean.inputfields.view.InputField;
import ro.doruchidean.inputfields.view.SpinnerInput;

public class MainActivity extends AppCompatActivity {

    private InputField inputField;
    private AutocompleteInput autocompleteInput;
    private SpinnerInput spinnerInput;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tv_validation_status);

        initInputField();
        initAutocompleteInput();
        initSpinnerInput();

    }

    private void initInputField() {
        inputField = findViewById(R.id.input_field);
        inputField.setValidator(new CNPValidator(true, R.string.message_invalid_field), validationChangedListener);
        InputField inputField1 = findViewById(R.id.input_field1);
        inputField.setNextFocusView(inputField1);
    }

    private final InputField.ValidationChangedListener validationChangedListener = new InputField.ValidationChangedListener() {
        @Override
        public void onInputValidationChanged() {
            if (inputField.isValid()
                    && autocompleteInput.isValid()
                    && spinnerInput.isValid()) {
                tvStatus.setText("All input is valid.");
                tvStatus.setBackgroundColor(ContextCompat.getColor(tvStatus.getContext(), R.color.green));
            } else {
                tvStatus.setText("Some fields are not valid.");
                tvStatus.setBackgroundColor(ContextCompat.getColor(tvStatus.getContext(), R.color.light_red));
            }
        }
    };

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void initAutocompleteInput() {
        autocompleteInput = findViewById(R.id.autocomplete_input);
        autocompleteInput.setValidator(new InputValidator(true, R.string.message_invalid_field) {
            @Nullable
            @Override
            public Integer getErrorMessageResId(String input) {
                if (TextUtils.isEmpty(input)) {
                    return R.string.message_empty_field;
                }
                if (input.length() < 5) {
                    return R.string.message_too_short_field;
                }
                if (!input.contains("9")) {
                    return getInvalidMessageResId();
                }
                return null;
            }
        }, validationChangedListener);
        autocompleteInput.setIsLoading(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> items = new ArrayList<>();
                for (int i=0; i<10; i++) {
                    items.add("Autocomplete item " + i);
                }
                autocompleteInput.setIsLoading(false);
                autocompleteInput.setItems(items, null, false);
                autocompleteInput.setSelectionListener(new AutocompleteInput.SelectionListener() {
                    @Override
                    public void onAutocompleteItemSelected(String item) {
                        showToast("Autocomplete item selected: " + item);
                    }
                });
            }
        }, 1000);
    }

    private void initSpinnerInput() {
        spinnerInput = findViewById(R.id.spinner_input);
        spinnerInput.setValidator(new NonEmptyValidator(true, R.string.message_empty_field), validationChangedListener);
        spinnerInput.setIsLoading(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spinnerInput.setIsLoading(false);
                List<String> items = new ArrayList<>();
                for (int i=0; i<10; i++) {
                    items.add("Spinner item " + i);
                }
                spinnerInput.setItems(items, null);
            }
        }, 1500);
    }
}
