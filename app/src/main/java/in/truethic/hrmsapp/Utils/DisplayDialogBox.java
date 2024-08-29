package in.truethic.hrmsapp.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import in.truethic.hrmsapp.R;

public class DisplayDialogBox {

    public static Context mContext;
    public static String message;
    public static String title;
    public static int retValue = 0;

    public static void showDialogBox(Context ctx, String msg, String header) {
        message = msg;
        title = header;
        mContext = ctx;

        AlertDialog.Builder builder = new AlertDialog.Builder( mContext );
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View customLayout = layoutInflater.inflate( R.layout.general_alert_dialogbox, null );
        builder.setView( customLayout );

        AlertDialog dialog = builder.create();
        dialog.setCancelable( false );
        dialog.show();

        AppCompatButton btn_order_done;
        btn_order_done = customLayout.findViewById( R.id.btn_ok );
        TextView tv_message = customLayout.findViewById( R.id.tv_label );
        TextView tv_header = customLayout.findViewById( R.id.tv_header );
        TextView tv_close = customLayout.findViewById( R.id.tv_close );

        tv_message.setText( message );
        tv_header.setText( title );

        btn_order_done.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        } );

        tv_close.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        } );


    }

}
