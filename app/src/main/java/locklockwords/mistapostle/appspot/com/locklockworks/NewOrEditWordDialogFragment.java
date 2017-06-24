package locklockwords.mistapostle.appspot.com.locklockworks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.logging.Level;

import locklockwords.mistapostle.appspot.com.locklockworks.db.LockLockWorksContract;
import locklockwords.mistapostle.appspot.com.locklockworks.html.YoudaoHelper;
import locklockwords.mistapostle.appspot.com.locklockworks.utils.LoggerUtils;
import locklockwords.mistapostle.appspot.com.locklockworks.utils.ProgressBarAsyncTask;

/**
 * Created by mistapostle on 17/6/24.
 */

public class NewOrEditWordDialogFragment extends DialogFragment {


    public static final String EXISTING_WORD_KEY = "ExistingWord";
    public LockLockWorksContract.Word existingWord;

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
                            private LockLockWorksContract.Word word;

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                final EditText nameEt = (EditText) getDialog().findViewById(R.id.name);
                                final EditText descEt = (EditText) getDialog().findViewById(R.id.desc);
                                final EditText produnceEt = (EditText) getDialog().findViewById(R.id.produnce);

                                word = new LockLockWorksContract.Word(-1, nameEt.getText().toString(),
                                        produnceEt.getText().toString(), descEt.getText().toString() , 0);
                            }

                            @Override
                            protected MainActivity doInBackground(MainActivity... params) {
                                if (existingWord != null) {
                                    word.setRowId(existingWord.getRowId());
                                    word.setRank(existingWord.getRank());
                                    word.update(params[0]);
                                } else {
                                    //LockLockWorksContract.Word word = new LockLockWorksContract.Word("test","data");
                                    word.insert(params[0]);
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
                .setNeutralButton("LOAD", null)
                .setCancelable(false);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        //Note: doing below 3 lines got a requestFeature() must be called before adding content
//            this.nameEt = (EditText) dialog.findViewById(R.id.name);
//            this.descEt = (EditText)dialog.findViewById(R.id.desc);
//            this.produnceEt = (EditText)dialog.findViewById(R.id.produnce);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {
                final AlertDialog alertDialog = (AlertDialog) dialog;
                final EditText nameEt = (EditText) alertDialog.findViewById(R.id.name);
                final EditText descEt = (EditText) alertDialog.findViewById(R.id.desc);
                final EditText produnceEt = (EditText) alertDialog.findViewById(R.id.produnce);
                final ProgressBar pb = (ProgressBar) alertDialog.findViewById(R.id.loadingPb);
                if (existingWord != null) {
                    nameEt.setText(existingWord.getWord());
                    descEt.setText(existingWord.getDesc());
                    produnceEt.setText(existingWord.getPronounce());
                }

                Button button = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // fab button
//                            final FabButton fb = (FabButton) alertDialog.findViewById(R.id.loadBtn);
//                            fb.showProgress(true);


                        new ProgressBarAsyncTask<Object, Object, Object[]>(pb) {

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
                                super.onPostExecute();
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
