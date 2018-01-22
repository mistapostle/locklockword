package locklockwords.mistapostle.appspot.com.locklockworks.utils;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;

/**
 * Created by mistapostle on 18/1/22.
 */

public class WordsLoader extends CursorLoader {
    public String order;

    public WordsLoader(Context context) {
        super(context);
    }

    public WordsLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
