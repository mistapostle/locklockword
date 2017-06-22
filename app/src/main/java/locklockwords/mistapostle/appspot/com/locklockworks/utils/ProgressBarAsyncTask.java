package locklockwords.mistapostle.appspot.com.locklockworks.utils;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by mistapostle on 17/6/22.
 */

public abstract class ProgressBarAsyncTask<Params, Progress, Result>
        extends AsyncTask<Params, Progress, Result> {

    private final ProgressBar pb;

    public ProgressBarAsyncTask(ProgressBar pb) {
        this.pb = pb;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pb.setVisibility(View.VISIBLE);
    }

    protected void onPostExecute() {
        super.onPreExecute();
        pb.setVisibility(View.INVISIBLE);
    }
}
