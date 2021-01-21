package logo.philist.csd_blindwalls_location_aware.Views.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import logo.philist.csd_blindwalls_location_aware.R;

public class MessageDialog extends AppCompatDialogFragment {

    private String title;
    private String message;

    public MessageDialog(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_view, null);
        TextView titleTV = view.findViewById(R.id.dialog_title);
        TextView textTV = view.findViewById(R.id.dialog_text);

        titleTV.setText(title);
        textTV.setText(message);

        builder.setView(view);
        return builder.create();
    }
}
