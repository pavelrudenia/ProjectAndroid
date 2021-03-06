package rudenia.fit.bstu.projectstpms.Activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import rudenia.fit.bstu.projectstpms.R;
import rudenia.fit.bstu.projectstpms.database.DbHelper;

public class AllNote extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private GridView mNoteList;
    public Integer counter = 0;
    private Spinner mNote;
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private EditText mDateWith, mDateOn;
    private String dateWith, dateOn;
    private NotificationManager notificationManager;
    String Author;
    // ?????????????????????????? ??????????????????????
    private static final int NOTIFY_ID = 1;
    // ?????????????????????????? ????????????
    private static final String CHANNEL_ID = "CHANNEL_ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_note);

        this.setTitle("??????????????");

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null) {
            Author = arguments.get("name").toString();
        }
        Toast.makeText(this, Author, Toast.LENGTH_SHORT).show();

        Toolbar toolbar = findViewById(R.id.toolbar_costs);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.costs_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_costs);
        navigationView.setNavigationItemSelectedListener(this);
        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();
        mDateWith = findViewById(R.id.date_with);
        mDateOn = findViewById(R.id.date_on);
        mNoteList = findViewById(R.id.notes_list);
        mNote = findViewById(R.id.spinnerNote);
        initSpiner();
        mNoteList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {

                int prevPosition = 0;
                if(counter==0) {
                    mNoteList.getChildAt(prevPosition).setBackgroundColor(Color.TRANSPARENT);
                    v.setBackgroundColor(Color.RED);
                    counter++;
                }

                else {
                    String getNote;
                    String getAllNote;
                    getAllNote=parent.getItemAtPosition(position).toString();
                    int p1 = getAllNote.indexOf("[") +1;
                    int p2 = getAllNote.indexOf("]",p1);
                    getNote = getAllNote.substring(p1,p2);
                    new AlertDialog.Builder(AllNote.this)
                            .setMessage("???? ?????????????????????????? ???????????? ?????????????? ??????????????")
                            .setNegativeButton("????????????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mNoteList.getChildAt(prevPosition).setBackgroundColor(Color.TRANSPARENT);
                                    v.setBackgroundColor(Color.TRANSPARENT);
                                    counter--;

                                }
                            })
                            .setPositiveButton("????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db = dbHelper.getWritableDatabase();
                                  db.delete("posts", "text = '"+"[" + getNote +"]"+ "'", null);
                                    String url = getNote.substring(0,5);

                                    Thread thread = new Thread(new Runnable() {

                                        @Override
                                        public void run() {
                                            try  {
                                                //"https://notes-webapps.herokuapp.com/post/"+url+"/delete"
                                                DefaultHttpClient hc = new DefaultHttpClient();
                                                ResponseHandler res = new BasicResponseHandler();
                                                String uri = url.replace(" ","%20");
                                                Log.d("aaa",uri);
                                                HttpGet http = new HttpGet("https://notes-webapps.herokuapp.com/post/"+uri+"/delete");

                                                //???????????????? ?????????? ???? ??????????????
                                                String response = (String) hc.execute(http, res);
                                            } catch (ClientProtocolException e) {
                                                e.printStackTrace();
                                            } catch (IOException exception) {
                                                exception.printStackTrace();
                                            }


                                        }
                                    });

                                    thread.start();


                                    Notification();
                                    onStart();
                                }
                            }).create().show();
                }
            }
        });
        notificationManager =(NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }


    @Override
    protected void onStart() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getCosts());
        mNoteList.setAdapter(adapter);
        super.onStart();
    }
    public void Sort(View view){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                selectSortNote());
        mNoteList.setAdapter(adapter);
        super.onStart();
    }
    public void Notification(){
        Intent intent = new Intent(getApplicationContext(),AllNote.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.note)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .setContentTitle("???? ?????????????? ??????????????!")
                        .setContentText("???? ???? ?? ?????????? ???????????? ???????????? ?????????????? ??????????");
        //.setPriority(PRIORITY_HIGH);
        createChannelIfNeeded(notificationManager);
        notificationManager.notify(NOTIFY_ID,notificationBuilder.build());

    }
    public static void createChannelIfNeeded(NotificationManager manager){
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,CHANNEL_ID,NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }
    protected void initSpiner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getFacultys());
        mNote.setAdapter(adapter);
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
    protected boolean formatDateIsCorrect(String date) {
        String DATE_FORMAT = "dd-MM-yyyy";
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setLenient(false);
        return df.parse(date, new ParsePosition(0)) != null;
    }

    protected boolean getPeriod(){
        if(formatDateIsCorrect(mDateWith.getText().toString())) {
            dateWith = mDateWith.getText().toString();
            if(formatDateIsCorrect(mDateOn.getText().toString())) {
                dateOn = mDateOn.getText().toString();
                return true;
            } else {
                mDateOn.setError("???????????????? ???????????? ????????");
                mDateOn.requestFocus();
                return false;
            }
        } else {
            mDateWith.setError("???????????????? ???????????? ????????");
            mDateWith.requestFocus();
            return false;
        }
    }

    public ArrayList<String> selectSortNote() {
        getPeriod();
        mNoteList.setAdapter(null);
            DbHelper dbHelper = new DbHelper(this);
             db = dbHelper.getWritableDatabase();
            ArrayList<String> data = new ArrayList<>();
//select * from Note where CATEGORY='test' and DATE BETWEEN '10.12.2021' and '12.12.2021'
            Cursor cursor = db.rawQuery("select text, date, time, category from posts where author='"+Author+"' "
                    + "and category = '" + mNote.getSelectedItem().toString() + "' "
                    + "and date BETWEEN '" + dateWith + "' and '" + dateOn + "' "
                    + "order by time desc", null);
            if (cursor.moveToFirst()) {
                int indexNote = cursor.getColumnIndex("text");
                int indexDate = cursor.getColumnIndex("date");
                int indexTime = cursor.getColumnIndex("time");
                int indexCategory = cursor.getColumnIndex("category");
                do {
                    data.add("---------------------------" + cursor.getString(indexDate) + " " +
                            cursor.getString(indexTime) + "--------------------------\n??????????????????:" +
                            cursor.getString(indexCategory) + "\n??????????????:" +
                            cursor.getString(indexNote) + "\n??????????????????????????????????????????????????????????????????");
                } while (cursor.moveToNext());
            }
            cursor.close();
            return data;
        }

    protected ArrayList<String> getCosts() {

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ArrayList<String> data = new ArrayList<>();


        Cursor cursor = database.rawQuery("select * from posts where author='"+Author+"'  ", null);
        if (cursor.moveToFirst()) {
            int indexNote = cursor.getColumnIndex("text");
            int indexDate = cursor.getColumnIndex("date");
            int indexTime = cursor.getColumnIndex("time");
            int indexCategory = cursor.getColumnIndex("category");
            do {
                data.add("---------------------------" + cursor.getString(indexDate) +" "+
                        cursor.getString(indexTime) + "--------------------------\n??????????????????:" +
                        cursor.getString(indexCategory) + "\n??????????????:" +
                        cursor.getString(indexNote) +"\n??????????????????????????????????????????????????????????????????" );
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_monthly_report_costs:
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra("name", Author);
                startActivity(intent);
                break;
            case R.id.nav_category_costs:
                Intent intent1 = new Intent(this,CategoryActivity.class);
                intent1.putExtra("name", Author);
                startActivity(intent1);
                break;
            case R.id.nav_costs_costs:
                break;
            case R.id.nav_diagram_main:
                Intent intent2 = new Intent(this,DiagramActivity.class);
                intent2.putExtra("name", Author);
                startActivity(intent2);
                break;
            case R.id.nav_about_costs:
                Intent intent3 = new Intent(this,AboutAppActivity.class);
                intent3.putExtra("name", Author);
                startActivity(intent3);
                break;
            case R.id.nav_logout:
                Intent intent4 = new Intent(this,StartActivity.class);
                startActivity(intent4);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.costs_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.costs_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



}