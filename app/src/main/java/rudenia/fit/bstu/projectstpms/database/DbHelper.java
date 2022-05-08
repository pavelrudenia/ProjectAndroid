package rudenia.fit.bstu.projectstpms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Rudenia1.db";
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
                + "category text unique primary key);");

        database.execSQL("create table if not exists posts ( "
                + "id integer primary key autoincrement, "
                + "title text DEFAULT Null, "
                + "text text unique not null, "
                + "url text unique not null, "
                + "date text not null, "
                + "time text not null, "
                + "category text not null, "
                + "author text  not null, "
                + "constraint FK_Author foreign key(author) references Users(name) on delete cascade on update cascade,"
                + "constraint FK_CATEGORY foreign key(category) references Category(category) on delete cascade on update cascade);");

        database.execSQL("create index if not exists idx_сosts "
                + "on posts(category);");

        database.execSQL("create table if not exists users ( "
                + "id integer primary key autoincrement, "
                + "name text  unique not null, "
                + "email text not null, "
                + "psw text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("drop table Category;");
        database.execSQL("DROP TABLE Costs;");
        onCreate(database);
    }
}
