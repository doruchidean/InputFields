package ro.doruchidean.inputfields.model;

public class CustomSelectionItem {

    private final int layoutResId;
    private final int textViewResId;

    public CustomSelectionItem(int layoutResId, int textViewResId) {
        this.layoutResId = layoutResId;
        this.textViewResId = textViewResId;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    public int getTextViewResId() {
        return textViewResId;
    }
}
