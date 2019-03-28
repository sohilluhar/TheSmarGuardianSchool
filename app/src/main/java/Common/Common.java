package Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import Model.Admin;

public class Common {
    public static boolean haveInternet(Context ctx) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        return true;
    }

    public static Admin currentAdmin;
}
