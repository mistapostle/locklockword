package locklockwords.mistapostle.appspot.com.locklockworks;

/**
 * Created by mistapostle on 17/6/24.
 */

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.logging.Logger;

import locklockwords.mistapostle.appspot.com.locklockworks.db.LockLockWorksContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class WordsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private CursorLoader cl;

    public WordsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WordsFragment newInstance(int sectionNumber) {
        WordsFragment fragment = new WordsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wordlist, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        ToggleButton tb = (ToggleButton) rootView.findViewById(R.id.lockScreenTb);
        tb.setOnCheckedChangeListener(this);

        initAddWordBtn(rootView);

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
                    adapter.changeCursor(data);
//                        adapter.swapCursor(data);
                    // adapter.notifyDataSetChanged();
//                        wordLv.invalidate();
//                        wordLv.invalidateViews();
                }

            }
        });

        wordLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //showNewOrEditWordDialogFragment()
                Cursor cursor = (Cursor) wordLv.getAdapter().getItem(position);
                LockLockWorksContract.Word word = new LockLockWorksContract.Word(cursor);
                showNewOrEditWordDialogFragment(word);
            }
        });

        cl.startLoading();
        return rootView;
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    private void initAddWordBtn(View rootView) {
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewOrEditWordDialog();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    void showNewOrEditWordDialog() {
        showNewOrEditWordDialogFragment(null);
    }

    public void showNewOrEditWordDialogFragment(LockLockWorksContract.Word word) {
        NewOrEditWordDialogFragment dialog = new NewOrEditWordDialogFragment();
        dialog.existingWord = word;
        dialog.show(getActivity().getSupportFragmentManager(), "NewOrEditWordDialogFragment");
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
        // android.R.drawable.ic_input_add

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