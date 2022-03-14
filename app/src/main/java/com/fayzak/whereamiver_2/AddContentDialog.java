package com.fayzak.whereamiver_2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

// this dialog will be used for both of the activities - websites and keywords
public class AddContentDialog extends DialogFragment {
    private TextView title; // of the dialog
    private String titleText;
    private EditText editText; // will contain the url/keywords
    private DialogListener listener;

    public AddContentDialog(String title_text){
        this.titleText = title_text;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // layout inflater will allow me to attach the layout i created to this activity view
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_custom_layout, null);

        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // might need to add some checks for correctness
                        listener.createNewContent(editText.getText().toString());
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        title = view.findViewById(R.id.dcl_title);
        editText = view.findViewById(R.id.dcl_text);
        title.setText(titleText);
        return builder.create();
    }

    // in it i will put the listener (which is the calling activity)
    // that will help me get the data.
    // because the main activity implements the listener interface i created here
    // i can be sure that the create function in  the interface is implemented.
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (DialogListener) context;
    }

    public interface DialogListener{
        // each of the activities that makes use of this dialog will implement this
        // interface and this method on its wen way.
        // in my case - one will create a new website object and the other a new keyword
        void createNewContent(String content);
    }
}
