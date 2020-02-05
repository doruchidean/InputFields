# InputFields

![Alt Text](https://drive.google.com/uc?export=view&id=10NpY7mbgCDcCACS9M_SoX22FbeQOlpdf)

### Gradle Setup

```gradle
// root/build.gradle
repositories {
    maven { url 'https://jitpack.io' }
}

// app/build.gradle
dependencies {
    implementation 'com.github.doruchidean:InputFields:v1.0'
}
```

### Example
```xml
<LinearLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <ro.doruchidean.inputfields.view.InputField
        android:id="@+id/input_field"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:label="CNP"
        app:hint="Type here"
        app:textAllCaps="true"
        app:inputType="digits"
        app:normalBackground="@color/gray"
        app:errorBackground="@color/red"
        app:correctBackground="@color/green"
        />

    <ro.doruchidean.inputfields.view.AutocompleteInput
        android:id="@+id/autocomplete_input"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:label="Select an item"
        app:textAllCaps="true"
        app:inputType="text"
        app:hint="Must contain character `9`, 5 chars long"
        app:threshHold="1"
        />

    <ro.doruchidean.inputfields.view.SpinnerInput
        android:id="@+id/spinner_input"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:label="Select an item"
        />

</LinearLayout>
        
```

```java
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

    private InputField.ValidationChangedListener validationChangedListener = 
        new InputField.ValidationChangedListener() {
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
```

**Available xml attributes**
```xml
<resources>
    <declare-styleable name="SpinnerInput">
        <attr name="label" format="string"/>
        <attr name="normalBackground" format="reference"/>
        <attr name="errorBackground" format="reference"/>
        <attr name="correctBackground" format="reference"/>
    </declare-styleable>

    <declare-styleable name="InputField">
        <attr name="label"/>
        <attr name="hint" format="string"/>
        <attr name="inputType" format="enum">
            <enum name="text"/>
            <enum name="password"/>
            <enum name="email"/>
            <enum name="phone"/>
            <enum name="digits"/>
        </attr>
        <attr name="textAllCaps"/>
        <attr name="normalBackground"/>
        <attr name="errorBackground"/>
        <attr name="correctBackground"/>
    </declare-styleable>

    <declare-styleable name="AutocompleteInput">
        <attr name="label"/>
        <attr name="hint"/>
        <attr name="inputType"/>
        <attr name="textAllCaps"/>
        <attr name="normalBackground"/>
        <attr name="errorBackground"/>
        <attr name="correctBackground"/>
        <attr name="threshHold" format="integer"/>
    </declare-styleable>
</resources>
```