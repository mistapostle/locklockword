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
import locklockwords.mistapostle.appspot.com.locklockworks.utils.LoggerUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class WordsFragment extends Fragment {
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
    public void onResume() {
        super.onResume();
        LoggerUtils.getLogger().info("WordsFragment onResume");
        createWordsLoader();
    }

    public void createWordsLoader() {
        //TODO: why getView() will be null ??!?!?!? it happens when  after android.os.Process.killProcess(android.os.Process.myPid()) and the mainActivity resumed automatically  as it's the latest Activity at background
        final ListView wordLv = (ListView) getView().findViewById(R.id.word_lv);
        cl = LockLockWorksContract.Word.getAllWordsLoader(getContext());
        cl.registerListener(1, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
                Logger.getLogger("LockLockWorks").info("data = " + data);
                if (wordLv.getAdapter() == null) {
                    CursorAdapter adapter = new CursorAdapter(getContext(), data, true) {
                        @Override
                        public View newView(Context context, Cursor cursor, ViewGroup parent) {
                            LayoutInflater inflater = LayoutInflater.from(context);
                            View v = inflater.inflate(R.layout.item_wordlist, parent, false);
                            // tv.setText(cursor.getString(0) + cursor.getString(1));
                            Logger.getLogger("LockLockWorks").info("newView:" + cursor.getString(0) + cursor.getString(1));
                            return v;
                        }

                        @Override
                        public void bindView(View view, Context context, Cursor cursor) {
                            TextView wordTv = (TextView) view.findViewById(R.id.wordTv);
                            wordTv.setText(cursor.getString(1));


                            TextView rankTv = (TextView) view.findViewById(R.id.rankTv);
                            rankTv.setText(Integer.toString(cursor.getInt(4)));
                            Logger.getLogger("LockLockWorks").info("bindView:" + cursor.getString(0) + cursor.getString(1));

                        }
                    };

                    wordLv.setAdapter(adapter);
                } else {
                    CursorAdapter adapter = (CursorAdapter) wordLv.getAdapter();
                    // adapter.changeCursor(data);
                    adapter.swapCursor(data);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wordlist, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));


        ToggleButton lockScreenTb = (ToggleButton) rootView.findViewById(R.id.lockScreenTb);
        lockScreenTb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), FullscreenActivity.class);
                startActivity(i);
            }
        });
        initAddWordBtn(rootView);


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
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public void reloadWordLv() {
        if (cl == null) {
            // //TODO: note: cl would be null
            // as it seems when system resume a activity it may don't call fragment recall onCreateView/onResume ??? , but use the cached view directly

            createWordsLoader();
        } else {
            cl.reset();
            cl.startLoading();
        }
    }
}