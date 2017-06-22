package locklockwords.mistapostle.appspot.com.locklockworks.utils;

import android.app.Dialog;
import android.os.AsyncTask;

/**
 * Created by mistapostle on 17/6/21.
 */

public abstract class ShowProgressDialogAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    public Dialog onCreateLoadingDialog() {
        //TODO:
        return null;
    }
}
