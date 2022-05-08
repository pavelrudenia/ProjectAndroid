package rudenia.fit.bstu.projectstpms.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import rudenia.fit.bstu.projectstpms.R;
import rudenia.fit.bstu.projectstpms.database.DbHelper;

public class AllNoteNoEthernet extends AppCompatActivity {


    private GridView mNoteList;
    public Integer counter = 0;
    private Spinner mNote;
    private EditText mDateWith, mDateOn;
    private String dateWith, dateOn;
    String Author;
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_note_no_ethernet);

        this.setTitle("Заметки");

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null) {
            Author = arguments.get("name").toString();
        }
        Toast.makeText(this, Author, Toast.LENGTH_SHORT).show();
        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();
        mDateWith = findViewById(R.id.date_with);
        mDateOn = findViewById(R.id.date_on);
        mNoteList = findViewById(R.id.notes_list);
        mNote = findViewById(R.id.spinnerNote);
        initSpiner();
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
                        cursor.getString(indexTime) + "--------------------------\nКатегория:" +
                        cursor.getString(indexCategory) + "\nЗаметка:" +
                        cursor.getString(indexNote) + "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
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
                        cursor.getString(indexTime) + "--------------------------\nКатегория:" +
                        cursor.getString(indexCategory) + "\nЗаметка:" +
                        cursor.getString(indexNote) +"\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" );
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }

}