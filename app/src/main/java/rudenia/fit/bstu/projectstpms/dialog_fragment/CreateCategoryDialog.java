package rudenia.fit.bstu.projectstpms.dialog_fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import rudenia.fit.bstu.projectstpms.CategoryActivity;

import rudenia.fit.bstu.projectstpms.database.DbHelper;
import rudenia.fit.bstu.projectstpms.R;



public class CreateCategoryDialog extends DialogFragment {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.create_category, null));

        return builder
                .setTitle("Добавление новой категории")
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText mCategoryName = getDialog().findViewById(R.id.category_name);
                        String categoryName = mCategoryName.getText().toString();
                        if (!categoryName.isEmpty()) {
                            DbHelper dbHelper = new DbHelper(getContext());
                            SQLiteDatabase database = dbHelper.getWritableDatabase();

                            ContentValues cv = new ContentValues();
                            cv.put("NAME", categoryName);
                            database.insert("Category", null, cv);

                            dismiss();
                            getActivity().startActivity(new Intent(getContext(), CategoryActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            Toast.makeText(getActivity(), "Введите название категории", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Отмена", null)
                .create();
    }
}