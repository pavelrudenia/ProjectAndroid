package rudenia.fit.bstu.projectstpms.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.rengwuxian.materialedittext.MaterialEditText;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import rudenia.fit.bstu.projectstpms.R;
import rudenia.fit.bstu.projectstpms.database.DbHelper;

public class StartActivity extends AppCompatActivity {

    Button register,login;
    ConstraintLayout root;
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    TextView Title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        Title=findViewById(R.id.title2);

        root = findViewById(R.id.root_element);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterWindow();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInWindow();
            }
        });
    }



    @Override
    public void onBackPressed() {
        // чтобы нельзя было назад вернуться
    }

    public void AreYouOnline(){
        if ( !isOnline() ){
            Intent intent = new Intent(this,AllNote.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Cоединение с интернетом установлено!",Toast.LENGTH_SHORT).show();
            register.setClickable(true);
            register.setEnabled(true);

        }
    }
    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }

    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Войти");
        dialog.setMessage("Введите  данные для входа");

        LayoutInflater inflater = LayoutInflater.from(this);
        View signin_window=inflater.inflate(R.layout.signin_window,null);
        dialog.setView(signin_window);

        MaterialEditText password = signin_window.findViewById(R.id.passField);
        MaterialEditText name = signin_window.findViewById(R.id.NameField);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                if(TextUtils.isEmpty(name.getText().toString())){
                    Snackbar.make(root,"Введите ваше имя",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(password.getText().toString().length() <= 4){
                    Snackbar.make(root,"Введите пароль более 4 символов",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //Авторизация
                String Name=name.getText().toString().trim();
                String Password=password.getText().toString().trim();
                Password = String.valueOf(Password.hashCode());
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                String query = "SELECT COUNT(*) as A FROM Users "
                        + "where name = '" + Name + "' "
                        + "and psw = '" + Password + "'";
                Cursor userCursor = db.rawQuery(query, null);
                if(userCursor.moveToFirst()){
                    int facultyIndex = userCursor.getColumnIndex("A");
                    Integer test = Integer.valueOf(userCursor.getString(facultyIndex));
                    if(test == 1){
                    Snackbar.make(root,"Регистрация успешно пройдена,ожидайте пару секунд",Snackbar.LENGTH_SHORT).show();
                        Intent intent = new Intent(StartActivity.this,MainActivity.class);
                        intent.putExtra("name", Name);
                        startActivity(intent);
                }
                    else{
                        Snackbar.make(root,"Ошибка,пользователь не существует",Snackbar.LENGTH_SHORT).show();
                    }}


            }
        });
        dialog.show();

    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Зарегистрироваться");
        dialog.setMessage("Введите все данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_window=inflater.inflate(R.layout.register_window,null);
        dialog.setView(register_window);

         MaterialEditText email = register_window.findViewById(R.id.emailField);
         MaterialEditText password = register_window.findViewById(R.id.passField);
         MaterialEditText name = register_window.findViewById(R.id.NameField);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Зарегистрироваться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    Snackbar.make(root,"Введите вашу почту",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    Snackbar.make(root,"Почта не существует",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(name.getText().toString())){
                    Snackbar.make(root,"Введите ваше имя",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(password.getText().toString().length() <= 4){
                    Snackbar.make(root,"Введите пароль более 4 символов",Snackbar.LENGTH_SHORT).show();
                    return;
                }

            //Регистрация пользователя
                String Email=email.getText().toString().trim();
                String Name=name.getText().toString().trim();
                String Password=password.getText().toString().trim();
//                Password = String.valueOf(Password.hashCode());
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                String passwordToHash = password.getText().toString().trim();
                String generatedPassword = null;
                try
                {
                    // Create MessageDigest instance for MD5
                    MessageDigest md = MessageDigest.getInstance("MD5");

                    // Add password bytes to digest
                    md.update(passwordToHash.getBytes());

                    // Get the hash's bytes
                    byte[] bytes = md.digest();

                    // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < bytes.length; i++) {
                        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                    }

                    // Get complete hashed password in hex format
                    generatedPassword = sb.toString();
                }
                catch (NoSuchAlgorithmException e)
                {
                    e.printStackTrace();
                }
                System.out.println(generatedPassword);

                //TODO ---------------------------
                String query = "SELECT COUNT(*) as A FROM Users "
                        + "where name = '" + Name + "' ";
                Cursor userCursor = db.rawQuery(query, null);
                if(userCursor.moveToFirst()){
                    int facultyIndex = userCursor.getColumnIndex("A");
                    Integer test = Integer.valueOf(userCursor.getString(facultyIndex));
                    if(test == 1){
                        Snackbar.make(root,"Пользователь с таким именем уже существует",Snackbar.LENGTH_SHORT).show();
                    }
                    else{
                        ContentValues cv = new ContentValues();
                        cv.put("email", String.valueOf(Email));
                        cv.put("name", String.valueOf(Name));
                        cv.put("psw", String.valueOf(generatedPassword));

                        database.insert("Users", null, cv);


                        Snackbar.make(root,"Пользователь добавлен",Snackbar.LENGTH_LONG).show();
                    }}


                String finalPassword = Password;
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try  {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost http = new HttpPost("https://notes-webapps.herokuapp.com/register?");

                List nameValuePairs = new ArrayList(6);
                nameValuePairs.add(new BasicNameValuePair("csrf_token", "ImVjZDExOTgzOTE5NzQ5M2YzOWNlZjI4Mjg3MGVmY2NiNzA0MTZhNzIi.YnDP1Q.2zuvpB4hCJYiHd3jCocbxOa-vKk"));
                nameValuePairs.add(new BasicNameValuePair("name", Email));
                nameValuePairs.add(new BasicNameValuePair("email", Name));
                nameValuePairs.add(new BasicNameValuePair("psw", finalPassword));
                nameValuePairs.add(new BasicNameValuePair("psw2", finalPassword));
                nameValuePairs.add(new BasicNameValuePair("submit", "%D0%A0%D0%B5%D0%B3%D0%B8%D1%81%D1%82%D1%80%D0%B0%D1%86%D0%B8%D1%8F"));
                try {
                    http.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                } catch (UnsupportedEncodingException e) {
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

            }
        });
                dialog.show();
    }


}