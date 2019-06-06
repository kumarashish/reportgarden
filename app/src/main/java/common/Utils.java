package common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Base64;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        boolean status= connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
        if (status == false) {
            Toast.makeText(context,"Internet Unavailable",Toast.LENGTH_SHORT).show();
        }
        return status;
    }

    public static String getDecodedBase64(String encodedValue)
    {
        byte[] decodeValue =  Base64.decode(encodedValue, Base64.DEFAULT);
        return new String(decodeValue);
    }

    public static String getCurrentDateTime()
    {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        String dateToStr = format.format(today);
        return dateToStr;
    }
}
