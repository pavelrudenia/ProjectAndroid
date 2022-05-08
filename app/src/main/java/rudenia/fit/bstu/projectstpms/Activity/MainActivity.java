package rudenia.fit.bstu.projectstpms.Activity;

import java.io.IOException;
import java.net.URLEncoder;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import rudenia.fit.bstu.projectstpms.R;
import rudenia.fit.bstu.projectstpms.database.DbHelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private Spinner mCategory;
    private EditText EnteredText;
    private String selectedCategory;
    private ImageView ConvertAnim;
    String Author;

    private static final int Print_Words = 100;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("Главная");

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null) {
            Author = arguments.get("name").toString();
        }



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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                // do something
                break;
        }

        return super.onOptionsItemSelected(item);
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
                Intent intent = new Intent(this,CategoryActivity.class);
                intent.putExtra("name", Author);
                startActivity(intent);
                break;
            case R.id.nav_costs_main:
//                startActivity(new Intent(MainActivity.this, AllNote.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                Intent intent1 = new Intent(this,AllNote.class);
                intent1.putExtra("name", Author);
                startActivity(intent1);
                break;
            case R.id.nav_diagram_main:
                Intent intent2 = new Intent(this,DiagramActivity.class);
                intent2.putExtra("name", Author);
                startActivity(intent2);
                break;
            case R.id.nav_about_main:
                Intent intent3 = new Intent(this,AboutAppActivity.class);
                intent3.putExtra("name", Author);
                startActivity(intent3);
                break;
            case R.id.nav_logout:
                Intent intent4 = new Intent(this,StartActivity.class);
                startActivity(intent4);
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
        String query = "select Category.category as category from Category";
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
        String url = note.substring(1,6);
        @SuppressLint("SimpleDateFormat")
        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        @SuppressLint("SimpleDateFormat")
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        try {
            selectedCategory = mCategory.getSelectedItem().toString();



            if (!note.isEmpty()) {
                DbHelper dbHelper = new DbHelper(this);
                SQLiteDatabase database = dbHelper.getWritableDatabase();


               ;

                ContentValues cv = new ContentValues();
                cv.put("text", note);
                cv.put("url", url);
                cv.put("date", date);
                cv.put("time", time);
                cv.put("category", selectedCategory);
                cv.put("author",Author);

                database.insert("posts", null, cv);
                startAnimation();

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try  {

                            String text = note.replace("[", "");
                            text = text.replace("]", "");
                            text = URLEncoder.encode(text, "UTF-8");
                            String uri = URLEncoder.encode(url, "UTF-8");
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost http = new HttpPost("https://notes-webapps.herokuapp.com/add_post?name="+text+"&url="+uri+"&language="+selectedCategory+"&post=%D0%B7%D0%B0%D0%BC%D0%B5%D1%82%D0%BA%D0%B0+%D1%87%D0%B5%D1%80%D0%B5%D0%B7+%D1%81%D1%81%D1%8B%D0%BB%D0%BA%D1%83&author="+Author+"");

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



                Toast t = Toast.makeText(getApplicationContext(),"Заметка добавлена успешно!",Toast.LENGTH_LONG);
                t.setGravity(Gravity.BOTTOM,0,0);
                t.show();

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
    public void Test(View view){
        Intent intent = new Intent(this,StartActivity.class);
        startActivity(intent);
    }
}