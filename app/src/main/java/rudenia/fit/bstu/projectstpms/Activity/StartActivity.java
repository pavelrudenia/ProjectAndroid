package rudenia.fit.bstu.projectstpms.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.rengwuxian.materialedittext.MaterialEditText;


import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKeyFactory;

import rudenia.fit.bstu.projectstpms.R;
import rudenia.fit.bstu.projectstpms.database.DbHelper;

public class StartActivity extends AppCompatActivity {

    Button register,login;
    ConstraintLayout root;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

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
                        + "and password = '" + Password + "'";
                Cursor userCursor = db.rawQuery(query, null);
                if(userCursor.moveToFirst()){
                    int facultyIndex = userCursor.getColumnIndex("A");
                    Integer test = Integer.valueOf(userCursor.getString(facultyIndex));
                    if(test == 1){
                    Snackbar.make(root,"Регистрация успешно пройдена,ожидайте пару секунд",Snackbar.LENGTH_SHORT).show();
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
         MaterialEditText phone = register_window.findViewById(R.id.PhoneField);

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
                if(TextUtils.isEmpty(phone.getText().toString())){
                    Snackbar.make(root,"Введите вашу телефон",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(password.getText().toString().length() <= 4){
                    Snackbar.make(root,"Введите пароль более 4 символов",Snackbar.LENGTH_SHORT).show();
                    return;
                }

            //Регистрация пользователя
                String Email=email.getText().toString().trim();
                String Name=name.getText().toString().trim();
                String Phone=phone.getText().toString().trim();
                String Password=password.getText().toString().trim();
                Password = String.valueOf(Password.hashCode());
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                SecretKeyFactory keyFactory = null;
                try
                {
                    keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                }
                catch (NoSuchAlgorithmException e){
                    Toast.makeText(StartActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                }


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
                        cv.put("phone", String.valueOf(Phone));
                        cv.put("password", String.valueOf(Password));

                        database.insert("Users", null, cv);


                        Snackbar.make(root,"Пользователь добавлен",Snackbar.LENGTH_LONG).show();
                    }}


            }
        });
                dialog.show();
    }

}