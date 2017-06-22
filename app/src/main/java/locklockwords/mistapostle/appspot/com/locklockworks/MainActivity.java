package locklockwords.mistapostle.appspot.com.locklockworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import locklockwords.mistapostle.appspot.com.locklockworks.db.LockLockWorksContract;
import locklockwords.mistapostle.appspot.com.locklockworks.db.LockLockWorksDbHelper;
import locklockwords.mistapostle.appspot.com.locklockworks.html.YoudaoHelper;
import locklockwords.mistapostle.appspot.com.locklockworks.utils.LoggerUtils;
import mbanje.kurt.fabbutton.FabButton;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewOrEditWordDialog();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        Logger.getLogger("LockLockWorks").info("MainActivity created");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private CursorLoader cl;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            ToggleButton tb = (ToggleButton) rootView.findViewById(R.id.lockScreenTb);
            tb.setOnCheckedChangeListener(this);

            final ListView wordLv = (ListView) rootView.findViewById(R.id.word_lv);
            cl = LockLockWorksContract.Word.getAllWordsLoader(getContext());
            cl.registerListener(1, new Loader.OnLoadCompleteListener<Cursor>() {
                @Override
                public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
                    Logger.getLogger("LockLockWorks").info("data = " + data);
                    if (wordLv != null) {
                        CursorAdapter adapter = new CursorAdapter(getContext(), data, false) {
                            @Override
                            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                                TextView tv = new TextView(context);
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
                        wordLv.setAdapter(adapter);
                    } else {
                        CursorAdapter adapter = (CursorAdapter) wordLv.getAdapter();
//                        adapter.changeCursor(data);
                        adapter.swapCursor(data);
                        // adapter.notifyDataSetChanged();
//                        wordLv.invalidate();
//                        wordLv.invalidateViews();
                    }

                }
            });

            cl.startLoading();
            return rootView;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            switch (compoundButton.getId()) {
                case R.id.lockScreenTb: {
                    if (checked) {
                        enableLockScreen();
                    } else {
                        disableLookScreen();
                    }
                    break;
                }
                default: {

                }
            }
        }

        private void disableLookScreen() {
            Activity activity = this.getActivity();
            activity.stopService(new Intent(activity, LockScreenService.class));
            Snackbar.make(this.getView(), "Disabled Lock scrren", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

        private void enableLockScreen() {
            Activity activity = this.getActivity();
            activity.startService(new Intent(activity, LockScreenService.class));
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            Snackbar.make(this.getView(), "Enabled Lock scrren", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();


        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

        }

        public void reloadWordLv() {
            cl.reset();
            cl.startLoading();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private Fragment[] cachedFragments = new Fragment[3];

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (cachedFragments[position] == null) {
                cachedFragments[position] = PlaceholderFragment.newInstance(position + 1);
            }
            return cachedFragments[position];
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    public static class NewOrEditWordDialogFragment extends DialogFragment {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.new_edit_word_dialog, null))
                    // Add action buttons
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO: make it run in background


                            new AsyncTask<MainActivity, Object, MainActivity>() {

                                @Override
                                protected MainActivity doInBackground(MainActivity... params) {
                                    try {
                                        LockLockWorksContract.Word word = null;
                                        word = YoudaoHelper.getInstance().findWord("test");

                                        //LockLockWorksContract.Word word = new LockLockWorksContract.Word("test","data");
                                        word.insert(params[0]);
                                    } catch (IOException e) {
                                        LoggerUtils.getLogger().log(Level.WARNING, "failed to findWord", e);

                                    }
                                    return params[0];
                                }

                                @Override
                                protected void onPostExecute(MainActivity o) {

                                    o.getCurrentFrame().reloadWordLv();
                                }
                            }.execute((MainActivity) getActivity());

                            NewOrEditWordDialogFragment.this.getDialog().dismiss();

                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            NewOrEditWordDialogFragment.this.getDialog().cancel();
                        }
                    })
                    .setNeutralButton("LOAD", null);
            Dialog dialog = builder.create();
            //Note: doing below 3 lines got a requestFeature() must be called before adding content
//            this.nameEt = (EditText) dialog.findViewById(R.id.name);
//            this.descEt = (EditText)dialog.findViewById(R.id.desc);
//            this.produnceEt = (EditText)dialog.findViewById(R.id.produnce);
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(final DialogInterface dialog) {

                    final AlertDialog alertDialog = (AlertDialog) dialog;
                    Button button = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                    button.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            // fab button
                            final FabButton fb = (FabButton) alertDialog.findViewById(R.id.loadBtn);
                            fb.showProgress(true);
                            // TODO Do something
                            final EditText nameEt = (EditText) alertDialog.findViewById(R.id.name);
                            final EditText descEt = (EditText) alertDialog.findViewById(R.id.desc);
                            final EditText produnceEt = (EditText) alertDialog.findViewById(R.id.produnce);
                            new AsyncTask<Object, Object, Object[]>() {

                                @Override
                                protected Object[] doInBackground(Object... params) {
                                    try {
                                        MainActivity activity = (MainActivity) params[0];
                                        String name = (String) params[1];
                                        LockLockWorksContract.Word word = YoudaoHelper.getInstance().findWord(name);
                                        return new Object[]{activity, word};
                                    } catch (IOException e) {
                                        LoggerUtils.getLogger().log(Level.WARNING, "failed to findWord", e);

                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Object[] os) {
                                    if (os != null) {
                                        MainActivity activity = (MainActivity) os[0];
                                        LockLockWorksContract.Word word = (LockLockWorksContract.Word) os[1];

                                        descEt.setText(word.getDesc());
                                        produnceEt.setText(word.getPronounce());
                                        //activity.getCurrentFrame().reloadWordLv();
                                    }
                                }
                            }.execute((MainActivity) getActivity(), nameEt.getText().toString());
                            //Dismiss once everything is OK.
                            // dialog.dismiss();
                        }
                    });
                }
            });
            return dialog;
        }


    }

    private PlaceholderFragment getCurrentFrame() {

        return (PlaceholderFragment) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());
    }

    void showNewOrEditWordDialog() {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.

        DialogFragment dialog = new NewOrEditWordDialogFragment();

        dialog.show(getSupportFragmentManager(), "NewOrEditWordDialogFragment");
    }
}
