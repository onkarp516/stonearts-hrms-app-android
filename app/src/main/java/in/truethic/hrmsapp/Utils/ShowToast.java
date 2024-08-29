package in.truethic.hrmsapp.Utils;

import android.content.Context;
import android.widget.Toast;

public class ShowToast {
    public static void show(Context mContext, String message)
    {
        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }

}
