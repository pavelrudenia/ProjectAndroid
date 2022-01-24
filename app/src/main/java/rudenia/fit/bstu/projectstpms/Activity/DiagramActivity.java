package rudenia.fit.bstu.projectstpms.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;

import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import rudenia.fit.bstu.projectstpms.R;
import rudenia.fit.bstu.projectstpms.adapter.NotesDataAdapter;
import rudenia.fit.bstu.projectstpms.grafics.my_view.PieChartView;


import rudenia.fit.bstu.projectstpms.database.DbHelper;
import rudenia.fit.bstu.projectstpms.model.Notes;

public class DiagramActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PieChartView mPieChartView;
    private RecyclerView mMainList;
    private NotesDataAdapter mCostsDataAdapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagram);

        this.setTitle("Диаграмма");

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);



        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        //dbHelper.onUpgrade(database, 1, 1);
    }

    @Override
    protected void onStart() {
        mPieChartView = findViewById(R.id.pie_chart);
        float[] datapoints = getNote();
        drawRoundView(datapoints,80);

        mMainList = findViewById(R.id.main_list);
        List<Notes> costsList = getCategory();
        mCostsDataAdapter = new NotesDataAdapter(this, costsList);
        mMainList.setAdapter(mCostsDataAdapter);

        super.onStart();
    }


    protected float[] getNote() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<Float> listResult = new ArrayList<>();

        String query = "select CATEGORY, count(Note) count from Note group by CATEGORY order by CATEGORY asc;";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int sumIndex = cursor.getColumnIndex("count");
            do {
                listResult.add(Float.valueOf(cursor.getString(sumIndex)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        float[] result = new float[listResult.size()];
        for(int i = 0; i < listResult.size(); i++)
            result[i] = listResult.get(i);

        return result;
    }

    protected ArrayList<Notes> getCategory() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ArrayList<Notes> data = new ArrayList<>();

        String query = "select CATEGORY, count(co.Note) count from Note co, Category ca "
                + "where co.CATEGORY = ca.NAME group by CATEGORY order by count desc;";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int categoryIndex = cursor.getColumnIndex("CATEGORY");
            int sumIndex = cursor.getColumnIndex("count");
            do {
                data.add(new Notes(data.size(), cursor.getString(categoryIndex), cursor.getString(sumIndex)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_monthly_report_main:
                startActivity(new Intent(this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_category_main:
                startActivity(new Intent(this, CategoryActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_costs_main:
                startActivity(new Intent(this, AllNote.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_diagram_main:
                onBackPressed();
                break;
            case R.id.nav_about_main:
                startActivity(new Intent(this, AboutAppActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void drawRoundView(float[] datapoints, int wight) {
        mPieChartView.setWidth(wight);
        mPieChartView.setDataPoints(datapoints);
        mPieChartView.invalidate();
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
}