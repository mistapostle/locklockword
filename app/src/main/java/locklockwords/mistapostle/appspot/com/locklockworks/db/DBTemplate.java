package locklockwords.mistapostle.appspot.com.locklockworks.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by mistapostle on 17/6/19.
 */

public abstract class DBTemplate {
    private final Context ctx;

    public DBTemplate(Context ctx) {
        this.ctx = ctx;
    }

    abstract public Object callback(SQLiteDatabase db);

    public Object exec() {
        try (LockLockWorksDbHelper dbHelper = new LockLockWorksDbHelper(ctx);
             SQLiteDatabase db = dbHelper.getWritableDatabase();
        ) {

            Object res = callback(db);
            return res;
        }
    }
}
