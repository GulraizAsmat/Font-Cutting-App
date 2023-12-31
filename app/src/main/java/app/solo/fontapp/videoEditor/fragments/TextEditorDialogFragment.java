package app.solo.fontapp.videoEditor.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.employee.videoeditor.adapters.FontPickerAdapter;

import java.util.ArrayList;
import java.util.List;

import app.solo.fontapp.R;

/**
 * Created by Burhanuddin Rashid on 1/16/2018.
 */

public class TextEditorDialogFragment extends DialogFragment {

    public static final String TAG = TextEditorDialogFragment.class.getSimpleName();
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    public static final String SELECTED_POSITION = "extra_selected_position";
    private EditText mAddTextEditText;
    private ConstraintLayout mAddTextDoneTextView;
    private InputMethodManager mInputMethodManager;
    private static List<String> fontNames;
    private static List<Integer> fontIds;
    private int mColorCode;
    private static int position = 0;
    private TextEditor mTextEditor;

    public interface TextEditor {
        void onDone(String inputText, int colorCode, int position);
    }


    //Show dialog with provide text and text color
    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity,
                                                @NonNull String inputText,
                                                @ColorInt int colorCode, int position) {
        Bundle args = new Bundle();
        args.putString(EXTRA_INPUT_TEXT, inputText);
        args.putInt(EXTRA_COLOR_CODE, colorCode);
        args.putInt(SELECTED_POSITION, position);
        TextEditorDialogFragment fragment = new TextEditorDialogFragment();
        fragment.setArguments(args);
        fragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return fragment;
    }

    //Show dialog with default text input as empty and text color white
    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity, int position) {
        return show(appCompatActivity,
                "", ContextCompat.getColor(appCompatActivity, R.color.white), position);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //Make dialog full screen with transparent background
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_text_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddTextEditText = view.findViewById(R.id.add_text_edit_text);

        mAddTextEditText.requestFocus();
        mAddTextEditText.setCursorVisible(true);


        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mAddTextDoneTextView = view.findViewById(R.id.cl_done);

        //Setup the color picker for text color
//        RecyclerView addTextColorPickerRecyclerView = view.findViewById(R.id.add_text_color_picker_recycler_view);
//        RecyclerView reyFonts = view.findViewById(R.id.reyFonts);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layoutManagerFonts = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
//        reyFonts.setLayoutManager(layoutManagerFonts);
//        addTextColorPickerRecyclerView.setHasFixedSize(true);
//        reyFonts.setHasFixedSize(true);
//        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(getActivity());
//        //This listener will change the text color when clicked on any color from picker
//        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
//            @Override
//            public void onColorPickerClickListener(int colorCode) {
//                mColorCode = colorCode;
//                mAddTextEditText.setTextColor(colorCode);
//            }
//        });
//        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);
        position = getArguments().getInt(SELECTED_POSITION);
        final FontPickerAdapter fontPickerAdapter = new FontPickerAdapter(getActivity(), 0, getDefaultFonts(getActivity()), getDefaultFontIds(getActivity()));
        fontPickerAdapter.setOnFontSelectListener(new FontPickerAdapter.OnFontSelectListner() {
            @Override
            public void onFontSelcetion(int position) {
                TextEditorDialogFragment.position = position;
                Typeface typeface = ResourcesCompat.getFont(getActivity(), fontIds.get(4));
                mAddTextEditText.setTypeface(typeface);
            }
        });

//        reyFonts.setAdapter(fontPickerAdapter);

        mAddTextEditText.setText(getArguments().getString(EXTRA_INPUT_TEXT));

        mColorCode = getArguments().getInt(EXTRA_COLOR_CODE);

        mAddTextEditText.setTextColor(mColorCode);
        Typeface typeface = ResourcesCompat.getFont(getActivity(), fontIds.get(position));
        mAddTextEditText.setTypeface(typeface);
        mAddTextEditText.setSelection(mAddTextEditText.getText().length());
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        //Make a callback on activity when user is done with text editing
        mAddTextDoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dismiss();
                String inputText = mAddTextEditText.getText().toString();
                if (!TextUtils.isEmpty(inputText) && mTextEditor != null) {
                    Log.e("TAF123", "hllero with text");
                    mTextEditor.onDone(inputText, mColorCode, fontPickerAdapter.getSelecetedPosition());
                }
                else {
                    Log.e("TAF123", "hllero withoout text");
                    mTextEditor.onDone("onCancel--001", 0, 0);
                }
            }
        });

    }

    public static List<Integer> getDefaultFontIds(Context context) {
        fontIds = new ArrayList<>();
        fontIds.add(R.font.roboto_regular);
        fontIds.add(R.font.roboto_regular);
        fontIds.add(R.font.roboto_regular);
        fontIds.add(R.font.roboto_regular);
        fontIds.add(R.font.roboto_regular);
        fontIds.add(R.font.roboto_regular);
        fontIds.add(R.font.roboto_regular);
        return fontIds;
    }

    public static List<String> getDefaultFonts(Context context) {
        fontNames = new ArrayList<>();
        fontNames.add("Josefinsans");
        fontNames.add("Wonderland");
        fontNames.add("Cinzel");
        fontNames.add("Emojione");
        fontNames.add("Merriweather");
        fontNames.add("Raleway");
        fontNames.add("Roboto");
        return fontNames;
    }

    //Callback to listener if user is done with text editing
    public void setOnTextEditorListener(TextEditor textEditor) {
        mTextEditor = textEditor;
    }


    @Override
    public void dismissNow() {
        super.dismissNow();
        Log.e("TAG12125", "dismissNow ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG12125", "dismissNow ");

    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
//        SharedPref.setBoolean(requireContext(), "video_editor_cancel", true);
        dismiss();
        mTextEditor.onDone("onCancel--001", 0, 0);
        super.onCancel(dialog);

        Log.e("TAG12125", "onCancel ");


    }

    @Override
    public void onStop() {
        Log.e("TAG12125", "onStop ");
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TAG12125", "onPause ");
    }


}
