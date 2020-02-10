package ro.doruchidean.inputfieldsexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInputField();
        initAutocompleteInput();
        initSpinnerInput();
    }

    private void initInputField() {
        inputField = findViewById(R.id.input_field);
        inputField.setValidator(new CNPValidator(true), validationChangedListener);
    }

    private InputField.ValidationChangedListener validationChangedListener = new InputField.ValidationChangedListener() {
        @Override
        public void onInputValidationChanged() {
            if (inputField.isValid()
                    && autocompleteInput.isValid()
                    && spinnerInput.isValid()) {
                showToast("All input is valid.");
            } else {
                showToast("Some fields are not valid.");
            }
        }
    };

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void initAutocompleteInput() {
        autocompleteInput = findViewById(R.id.autocomplete_input);
        autocompleteInput.setValidator(new InputValidator(true) {
            @Nullable
            @Override
            public Integer getErrorMessageResId(String input) {
                if (TextUtils.isEmpty(input)) {
                    return R.string.message_empty_auto_complete;
                }
                if (input.length() < 5) {
                    return R.string.message_too_short_auto_complete;
                }
                if (!input.contains("9")) {
                    return R.string.message_invalid_auto_complete;
                }
                return null;
            }
        }, validationChangedListener);
        List<String> items = new ArrayList<>();
        for (int i=0; i<10; i++) {
            items.add("Autocomplete item " + i);
        }
        autocompleteInput.setItems(items);
        autocompleteInput.setSelectionListener(new AutocompleteInput.SelectionListener() {
            @Override
            public void onAutocompleteItemSelected(String item) {
                Log.e("tag", item + " was selected");
            }
        });
    }

    private void initSpinnerInput() {
        spinnerInput = findViewById(R.id.spinner_input);
        spinnerInput.setValidator(new NonEmptyValidator(true), validationChangedListener);
        List<String> items = new ArrayList<>();
        for (int i=0; i<10; i++) {
            items.add("Spinner item " + i);
        }
        spinnerInput.setItems(items);
    }
}
