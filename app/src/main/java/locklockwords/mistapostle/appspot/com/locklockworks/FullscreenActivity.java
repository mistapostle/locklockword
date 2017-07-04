package locklockwords.mistapostle.appspot.com.locklockworks;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import locklockwords.mistapostle.appspot.com.locklockworks.db.LockLockWorksContract;

import static locklockwords.mistapostle.appspot.com.locklockworks.utils.Constrant.LOCK_SCREEN_RANK_PREF;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    //private View mContentView;



    private boolean mVisible;
    private LockLockWorksContract.Word  questionWord;
    private TextView timeTv;

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        //mContentView = findViewById(R.id.fullscreen_content);


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

        questionWord = LockLockWorksContract.Word.findWordToReview(this);
        if(questionWord!=null){
            TextView questionTv = (TextView)findViewById(R.id.questionTv);
            questionTv.setText(questionWord.getDesc());

        }
        final ListView answerLv  =(ListView)findViewById(R.id.answerLv);
        CursorLoader cl = LockLockWorksContract.Word.getFuzzWordsLoader(this,questionWord.getRowId(),4);
        cl.registerListener(1, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
                Logger.getLogger("LockLockWorks").info("data = " + data);

                    CursorAdapter adapter = new CursorAdapter(FullscreenActivity.this, data, false) {
                        @Override
                        public View newView(Context context, Cursor cursor, ViewGroup parent) {
                            TextView tv = new TextView(context);
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,35);
                            tv.setPadding(0,20,0,0);
                            // tv.setText(cursor.getString(0) + cursor.getString(1));
                            Logger.getLogger("LockLockWorks").info("newView:" + cursor.getString(0) + cursor.getString(1));
                            return tv;
                        }

                        @Override
                        public void bindView(View view, Context context, Cursor cursor) {
                            TextView tv = (TextView) view;
                            tv.setText(cursor.getString(0) + cursor.getString(1));

                            Logger.getLogger("LockLockWorks").info("bindView:" + cursor.getString(0) + cursor.getString(1));

                        }
                    };

                answerLv.setAdapter(adapter);


            }
        });

        answerLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //showNewOrEditWordDialogFragment()
                Cursor cursor = (Cursor) answerLv.getAdapter().getItem(position);
                LockLockWorksContract.Word word = new LockLockWorksContract.Word(cursor);
                if(word.getRowId() == questionWord.getRowId()){
                    SharedPreferences sp =   PreferenceManager.getDefaultSharedPreferences (FullscreenActivity.this);
                    word.setRank(word.getRank() + Integer.parseInt( sp.getString(LOCK_SCREEN_RANK_PREF,"5") ) );
                    word.update(FullscreenActivity.this);
                    dismiss();
                }else{
                    Snackbar.make(view, "Wrong! Wrong! Wrong!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }

            }
        });

        cl.startLoading();



    }

    @Override
    protected void onResume() {
        super.onResume();
        initDateTv();
    }

    private void initDateTv() {
        timeTv = (TextView) findViewById(R.id.timeTv);
        putCurrentTime();
    }

    private void putCurrentTime() {
        SimpleDateFormat sf = new SimpleDateFormat("MM-dd EEE HH:mm");
        timeTv.setText(sf.format(new Date()));
    }


    public void onClickDummyBtn(View view) {
        dismiss();
    }

    public void dismiss() {
        //        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

//Unlock
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

//Lock device
        DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        android.os.Process.killProcess(android.os.Process.myPid());
    }


}
