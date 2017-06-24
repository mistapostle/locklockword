package locklockwords.mistapostle.appspot.com.locklockworks.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

/**
 * Created by mistapostle on 17/6/24.
 */

public abstract class BackendDBTemplate extends  DBTemplate {
    public BackendDBTemplate(Context ctx) {
        super(ctx);
    }

    @Override
    public Object exec() {
       return  new AsyncTask(){

            @Override
            protected Object doInBackground(Object[] params) {
                return BackendDBTemplate.super.exec();
            }
        };
    }
}
