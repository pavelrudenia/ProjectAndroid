package rudenia.fit.bstu.projectstpms.Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import rudenia.fit.bstu.projectstpms.R;
import rudenia.fit.bstu.projectstpms.database.DbHelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private Spinner mCategory;
    private TextView EnteredText;
    private String selectedCategory;
    private ImageView ConvertAnim;
    //Это значение мы используем для проверки успеха
    //получения обратной информации в onActivityResult ():
    private static final int Print_Words = 100;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("Главная");

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);
        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();
        EnteredText = findViewById(R.id.tv);
        mCategory = findViewById(R.id.spinner);
        ConvertAnim = findViewById(R.id.ConvertAnim);


        ConvertAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation2();
            }
        });
        Button mButton =  findViewById(R.id.btnVoice);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //Вызываем RecognizerIntent для голосового ввода и преобразования голоса в текст:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Скажите слово для распознавания");
                startActivityForResult(intent, Print_Words);
            }
        });
        initSpiner();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Проверяем успешность получения обратного ответа:
        if (requestCode==Print_Words && resultCode==RESULT_OK) {
            //Как результат получаем строковый массив слов, похожих на произнесенное:
            ArrayList<String>result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //и отображаем их в элементе TextView:
//            String VoiceTXT1 =result.toString();
//            String VoiceTXT2 =VoiceTXT1.substring(1, VoiceTXT1.length() - 1);
            EnteredText.setText(result.toString());

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_monthly_report_main:
                onBackPressed();
                break;
            case R.id.nav_category_main:
                startActivity(new Intent(MainActivity.this, CategoryActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_costs_main:
                startActivity(new Intent(MainActivity.this, AllNote.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_diagram_main:
                startActivity(new Intent(MainActivity.this, DiagramActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_about_main:
                startActivity(new Intent(MainActivity.this, AboutAppActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void initSpiner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getFacultys());
        mCategory.setAdapter(adapter);
    }

    protected ArrayList<String> getFacultys() {
        ArrayList<String> data = new ArrayList<>();
        String query = "select Category.NAME as category from Category";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int facultyIndex = cursor.getColumnIndex("category");
            do {
                data.add(cursor.getString(facultyIndex));
            } while (cursor.moveToNext());
        }
        return data;

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void addNote(View view) {
        String note = EnteredText.getText().toString();
        @SuppressLint("SimpleDateFormat")
        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        @SuppressLint("SimpleDateFormat")
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        try {
            selectedCategory = mCategory.getSelectedItem().toString();



            if (!note.isEmpty()) {
                DbHelper dbHelper = new DbHelper(this);
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put("Note", note);
                cv.put("DATE", date);
                cv.put("Time", time);
                cv.put("CATEGORY", selectedCategory);

                database.insert("Note", null, cv);
                // super.onBackPressed();
                startAnimation();
                //Toast.makeText(MainActivity.this,"Заметка добавлена успешно!", Toast.LENGTH_SHORT).show();
                Toast t = Toast.makeText(getApplicationContext(),"Заметка добавлена успешно!",Toast.LENGTH_LONG);
                t.setGravity(Gravity.BOTTOM,0,0);
                t.show();
//                toast.setGravity(Gravity.TOP, 0,0);
//                toast.show();
            } else {
                Toast.makeText(MainActivity.this, "ошибочка", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception exception){
            Toast.makeText(this, "Вы не создали категорию", Toast.LENGTH_SHORT).show();
            EnteredText.setText("Здесь появится текст после записи");
        }
    }
    private void startAnimation(){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim);
        ConvertAnim.startAnimation(animation);
    }
    private void startAnimation2(){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim2);
        ConvertAnim.startAnimation(animation);
    }
}