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
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import rudenia.fit.bstu.projectstpms.Activity.CategoryActivity;

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
                        categoryName = categoryName.trim();
                        if (!categoryName.isEmpty()) {
                            DbHelper dbHelper = new DbHelper(getContext());
                            SQLiteDatabase database = dbHelper.getWritableDatabase();

                            ContentValues cv = new ContentValues();
                            cv.put("category", categoryName);
                            database.insert("Category", null, cv);

                            String finalCategoryName = categoryName;
                            Thread thread = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    try  {
                                        HttpClient httpclient = new DefaultHttpClient();
                                        HttpPost http = new HttpPost("https://notes-webapps.herokuapp.com/add_category?");
                                        List nameValuePairs = new ArrayList(1);
                                        nameValuePairs.add(new BasicNameValuePair("name", finalCategoryName));
                                            try
                                            {
                                                http.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                            }
                                            catch (UnsupportedEncodingException e)
                                            {
                                                e.printStackTrace();
                                           }

                                        try {
                                            String response = (String) httpclient.execute(http, new BasicResponseHandler());
                                        } catch (IOException exception) {
                                            exception.printStackTrace();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            thread.start();



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