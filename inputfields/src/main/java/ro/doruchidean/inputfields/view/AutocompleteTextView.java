package ro.doruchidean.inputfields.view;

import android.content.Context;
import android.util.AttributeSet;

public class AutocompleteTextView extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    private boolean preventFiltering = false;

    public AutocompleteTextView(Context context) {
        super(context);
    }

    public AutocompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        super.performFiltering(preventFiltering ? "" : text, keyCode);
    }

    public void setPreventFiltering(boolean preventFiltering) {
        this.preventFiltering = preventFiltering;
    }
}
