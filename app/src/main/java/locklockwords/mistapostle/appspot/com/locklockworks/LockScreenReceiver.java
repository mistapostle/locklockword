package locklockwords.mistapostle.appspot.com.locklockworks;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.logging.Logger;

public class LockScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Logger.getLogger("LockLockWorks").info("onReceive   start ");
        String action = intent.getAction();

        //If the screen was just turned on or it just booted up, start your Lock Activity
        if (action.equals(Intent.ACTION_SCREEN_OFF) || action.equals(Intent.ACTION_BOOT_COMPLETED)) {

            Logger.getLogger("LockLockWorks").info("onReceive   expected action" + action);
            Intent i = new Intent(context, FullscreenActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if (action.equals(Intent.ACTION_SCREEN_ON)) {

//            KeyguardManager.KeyguardLock key;
//            KeyguardManager km = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
//
//            //This is deprecated, but it is a simple way to disable the lockscreen in code
////        key = km.newKeyguardLock("IN");
//            key = km.newKeyguardLock("");
//
//            key.disableKeyguard();

//            Logger.getLogger("LockLockWorks").info("onReceive   expected action" + action );
//            Intent i = new Intent(context, MainActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
        }

        Logger.getLogger("LockLockWorks").info("onReceive   done");
    }
}
