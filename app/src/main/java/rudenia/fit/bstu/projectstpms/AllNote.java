package rudenia.fit.bstu.projectstpms;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.GravityCompat;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.os.Bundle;
;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.google.android.material.navigation.NavigationView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_note);

        this.setTitle("Заметки");

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
                    mNoteList.getChildAt(prevPosition).setBackgroundColor(Color.TRANSPARENT);
                    v.setBackgroundColor(Color.WHITE);
                    counter--;
                }


            }
        });

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

    protected void initSpiner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getFacultys());
        mNote.setAdapter(adapter);
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
                mDateOn.setError("Неверный формат даты");
                mDateOn.requestFocus();
                return false;
            }
        } else {
            mDateWith.setError("Неверный формат даты");
            mDateWith.requestFocus();
            return false;
        }
    }

    public ArrayList<String> selectSortNote() {
        getPeriod();
        mNoteList.setAdapter(null);
            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ArrayList<String> data = new ArrayList<>();
//select * from Note where CATEGORY='test' and DATE BETWEEN '10.12.2021' and '12.12.2021'
            Cursor cursor = database.rawQuery("select Note, DATE, Time, CATEGORY from Note "
                    + "where CATEGORY = '" + mNote.getSelectedItem().toString() + "' "
                    + "and DATE BETWEEN '" + dateWith + "' and '" + dateOn + "' "
                    + "order by Time desc", null);
            if (cursor.moveToFirst()) {
                int indexNote = cursor.getColumnIndex("Note");
                int indexDate = cursor.getColumnIndex("DATE");
                int indexTime = cursor.getColumnIndex("Time");
                int indexCategory = cursor.getColumnIndex("CATEGORY");
                do {
                    data.add("---------------------------" + cursor.getString(indexDate) + " " +
                            cursor.getString(indexTime) + "--------------------------\nКатегория:" +
                            cursor.getString(indexCategory) + "\nЗаметка:" +
                            cursor.getString(indexNote) + "\n----------------------------------------------------------------------------------");
                } while (cursor.moveToNext());
            }
            cursor.close();

            return data;
        }

    protected ArrayList<String> getCosts() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ArrayList<String> data = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from Note", null);
        if (cursor.moveToFirst()) {
            int indexNote = cursor.getColumnIndex("Note");
            int indexDate = cursor.getColumnIndex("DATE");
            int indexTime = cursor.getColumnIndex("Time");
            int indexCategory = cursor.getColumnIndex("CATEGORY");
            do {
                data.add("---------------------------" + cursor.getString(indexDate) +" "+
                        cursor.getString(indexTime) + "--------------------------\nКатегория:" +
                        cursor.getString(indexCategory) + "\nЗаметка:" +
                        cursor.getString(indexNote) +"\n----------------------------------------------------------------------------------" );
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_monthly_report_costs:
                startActivity(new Intent(AllNote.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_category_costs:
                startActivity(new Intent(AllNote.this, CategoryActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_costs_costs:
                break;
            case R.id.nav_diagram_main:
                startActivity(new Intent(AllNote.this, DiagramActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_about_costs:
                startActivity(new Intent(AllNote.this, AboutAppActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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