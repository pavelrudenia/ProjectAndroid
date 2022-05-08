package rudenia.fit.bstu.projectstpms.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

import rudenia.fit.bstu.projectstpms.R;
import rudenia.fit.bstu.projectstpms.adapter.CategoryDataAdapter;
import rudenia.fit.bstu.projectstpms.database.DbHelper;
import rudenia.fit.bstu.projectstpms.dialog_fragment.CreateCategoryDialog;
import rudenia.fit.bstu.projectstpms.model.Category;


public class CategoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mCategoryList;
    private CategoryDataAdapter mCategoryDataAdapter;
    protected List<Category> categoryList;
    String Author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        this.setTitle("Категории");

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null) {
            Author = arguments.get("name").toString();
        }

        Toolbar toolbar = findViewById(R.id.toolbar_category);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.category_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_category);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCategory();
            }
        });
    }


    @Override
    protected void onStart() {
        mCategoryList = findViewById(R.id.category_list);
        categoryList = getCategory();
        mCategoryDataAdapter = new CategoryDataAdapter(this, categoryList);
        mCategoryList.setAdapter(mCategoryDataAdapter);
        super.onStart();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = CategoryDataAdapter.getPosition();
        Category category = categoryList.get(position);
        showDialogMessage("Удалить категорию " +
                category.getName() + "?", category);
        return super.onContextItemSelected(item);
    }

    private void showDialogMessage(String msg, final Category category) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCategory(category);
                        onStart();
                    }
                }).create().show();
    }

    protected void deleteCategory(Category category) {
        DbHelper dbHelper = new DbHelper(CategoryActivity.this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete("Category", "category = '" + category.getName() + "'", null);
    }

    protected ArrayList<Category> getCategory() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ArrayList<Category> data = new ArrayList<>();

        Cursor cursor = database.rawQuery("select category from Category", null);
        if (cursor.moveToFirst()) {
            int indexName = cursor.getColumnIndex("category");
            do {
                data.add(new Category(cursor.getString(indexName)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_monthly_report_category:
                Intent intent1 = new Intent(this,MainActivity.class);
                intent1.putExtra("name", Author);
                startActivity(intent1);
                break;
            case R.id.nav_category_category:
                break;
            case R.id.nav_costs_category:
                Intent intent2 = new Intent(this,AllNote.class);
                intent2.putExtra("name", Author);
                startActivity(intent2);
                break;
            case R.id.nav_diagram_main:
                Intent intent3 = new Intent(this,DiagramActivity.class);
                intent3.putExtra("name", Author);
                startActivity(intent3);
                break;
            case R.id.nav_about_category:
                Intent intent4 = new Intent(this,AboutAppActivity.class);
                intent4.putExtra("name", Author);
                startActivity(intent4);
                break;
            case R.id.nav_logout:
                Intent intent5 = new Intent(this,StartActivity.class);
                startActivity(intent5);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.category_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createCategory() {
        CreateCategoryDialog dialog = new CreateCategoryDialog();
        dialog.show(getSupportFragmentManager(), null);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.category_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}