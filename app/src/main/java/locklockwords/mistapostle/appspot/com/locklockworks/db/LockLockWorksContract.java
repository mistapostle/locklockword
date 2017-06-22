package locklockwords.mistapostle.appspot.com.locklockworks.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.io.Serializable;

import locklockwords.mistapostle.appspot.com.locklockworks.MainActivity;

/**
 * Created by mistapostle on 17/6/12.
 */

public final class LockLockWorksContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private LockLockWorksContract() {
    }

    /* inner class that defines the table contents */
    public static class Word implements BaseColumns, Serializable {
        public static final String TABLE_NAME = "TBL_WORD";
        public static final String SQL_DELETE_ENTRIES = "drop table " + TABLE_NAME;
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_PRONOUNCE = "PRONOUNCE";
        public static final String COLUMN_NAME_WORD = "WORD";
        public static final String COLUMN_NAME_DESC = "DESC";
        public static final String SQL_CREATE_ENTRIES = "create table " + TABLE_NAME
                + "( _id INTEGER PRIMARY KEY , "
                + COLUMN_NAME_WORD + " text,"
                + COLUMN_NAME_PRONOUNCE + " text,"
                + COLUMN_NAME_DESC + " text"
                + ")";
        private String word;
        private String pronounce;
        private String desc;
        private int rowId;

        public Word(String word, String pronounce, String desc) {
            this.word = word;
            this.desc = desc;
            this.pronounce = pronounce;
        }

        public Word(int rowId, String word, String pronounce, String desc) {
            this(word, pronounce, desc);
            this.rowId = rowId;
        }

        public Word() {
        }

        public Word(Cursor cursor) {
            this(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        }

        public void insert(Context ctx) {
            new DBTemplate(ctx) {
                @Override
                public Object callback(SQLiteDatabase db) {
                    ContentValues cv = new ContentValues(2);
                    cv.put(COLUMN_NAME_WORD, word);
                    cv.put(COLUMN_NAME_DESC, desc);
                    cv.put(COLUMN_NAME_PRONOUNCE, pronounce);
                    db.insertOrThrow(TABLE_NAME, null, cv);
                    return null;
                }
            }.exec();

        }

        public void update(MainActivity ctx) {
            new DBTemplate(ctx) {
                @Override
                public Object callback(SQLiteDatabase db) {
                    ContentValues cv = new ContentValues(2);
                    cv.put(COLUMN_NAME_WORD, word);
                    cv.put(COLUMN_NAME_DESC, desc);
                    cv.put(COLUMN_NAME_PRONOUNCE, pronounce);
                    db.update(TABLE_NAME, cv, COLUMN_NAME_ID + "= ? ", new String[]{Integer.toString(rowId)});
                    return null;
                }
            }.exec();
        }

        public static CursorLoader getAllWordsLoader(final Context ctx) {
            // new CursorLoader(ctx,null,new String[]{"proj"}, "sel" ,new String[]{"selArg"},"sortOrder");
            CursorLoader cl = new CursorLoader(ctx) {
                @Override
                public Cursor loadInBackground() {
                    LockLockWorksDbHelper dbHelper = new LockLockWorksDbHelper(ctx);
                    // You better know how to get your database.
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    // You can use any query that returns a cursor.
                    return db.query(TABLE_NAME, new String[]{
                                    COLUMN_NAME_ID, COLUMN_NAME_WORD, COLUMN_NAME_PRONOUNCE, COLUMN_NAME_DESC},
                            null, null, null, null, COLUMN_NAME_WORD + " asc", null);
                }

                @Override
                protected void onReset() {
                    super.onReset();
                }
            };
            return cl;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getPronounce() {
            return pronounce;
        }

        public void setPronounce(String pronounce) {
            this.pronounce = pronounce;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getRowId() {
            return rowId;
        }

        public void setRowId(int rowId) {
            this.rowId = rowId;
        }

    }
}
