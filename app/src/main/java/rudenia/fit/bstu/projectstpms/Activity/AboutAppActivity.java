package rudenia.fit.bstu.projectstpms.Activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import rudenia.fit.bstu.projectstpms.R;


public class AboutAppActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    String Author;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        this.setTitle("О приложении");

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


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_monthly_report_main:
                Intent intent2 = new Intent(this,MainActivity.class);
                intent2.putExtra("name", Author);
                startActivity(intent2);
                break;
            case R.id.nav_category_main:
                Intent intent = new Intent(this,CategoryActivity.class);
                intent.putExtra("name", Author);
                startActivity(intent);
                break;
            case R.id.nav_costs_main:
                Intent intent1 = new Intent(this,AllNote.class);
                intent1.putExtra("name", Author);
                startActivity(intent1);
                break;
            case R.id.nav_diagram_main:
                Intent intent3= new Intent(this,DiagramActivity.class);
                intent3.putExtra("name", Author);
                startActivity(intent3);
                break;
            case R.id.nav_about_main:
                onBackPressed();
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