package rudenia.fit.bstu.projectstpms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Rudenia.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table if not exists Category ( "
                + "NAME text primary key);");

        database.execSQL("create table if not exists Note ( "
                + "ID_NOTE integer primary key autoincrement, "
                + "Note text unique not null, "
                + "DATE text not null, "
                + "Time text not null, "
                + "CATEGORY text not null, "
                + "constraint FK_CATEGORY foreign key(CATEGORY) references Category(NAME) on delete cascade on update cascade);");

        database.execSQL("create index if not exists idx_сosts "
                + "on Note(CATEGORY);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("drop table Category;");
        database.execSQL("DROP TABLE Costs;");
        onCreate(database);
    }
}
