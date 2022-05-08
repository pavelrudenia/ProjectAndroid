package rudenia.fit.bstu.projectstpms.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.rengwuxian.materialedittext.MaterialEditText;

import rudenia.fit.bstu.projectstpms.R;
import rudenia.fit.bstu.projectstpms.database.DbHelper;

public class StartActivityNoEthernet extends AppCompatActivity {
    Button login;
    ConstraintLayout root;
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_no_ethernet);

        login = findViewById(R.id.login);

        root = findViewById(R.id.root_element);
        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

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
                        + "and psw = '" + Password + "'";
                Cursor userCursor = db.rawQuery(query, null);
                if(userCursor.moveToFirst()){
                    int facultyIndex = userCursor.getColumnIndex("A");
                    Integer test = Integer.valueOf(userCursor.getString(facultyIndex));
                    if(test == 1){
                        Snackbar.make(root,"Регистрация успешно пройдена,ожидайте пару секунд",Snackbar.LENGTH_SHORT).show();
                        Intent intent = new Intent(StartActivityNoEthernet.this,AllNoteNoEthernet.class);
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
}