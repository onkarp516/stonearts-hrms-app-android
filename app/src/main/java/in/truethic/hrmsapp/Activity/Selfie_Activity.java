package in.truethic.hrmsapp.Activity;

import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.DisplayDialogBox;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;
import in.truethic.hrmsapp.Utils.ShowToast;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Selfie_Activity extends AppCompatActivity {

    AppCompatButton btn_reset_selfie,btn_take_selfie;
    ImageView iv_selfie,iv_back;
    ProgressDialog dialog;
    SessionManager sessionManager;
    int isImageSelected=0;
    int CAPTURE_IMAGE = 1234;
    File selectedfile;
    boolean isCameraAndMemoryAccess = false;
    String currentPhotoPath;
    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie);
        sessionManager = new SessionManager(Selfie_Activity.this);

        iv_selfie = findViewById(R.id.iv_selfie);
        iv_back = findViewById(R.id.iv_back);
        btn_take_selfie = findViewById(R.id.btn_take_selfie);
        btn_reset_selfie = findViewById(R.id.btn_reset_selfie);

        dialog = new ProgressDialog(Selfie_Activity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");

        // takeSelfie();

        btn_take_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageSelected == 0) {
                    ShowToast("Please Take Selfie");
                    return;
                }
                gotoSubmit();
            }
        });

        btn_reset_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeSelfie();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                takeSelfie();
            }
        }, 1000);
    }

    private void gotoSubmit() {

        RequestBody status = RequestBody.create("" + Globals.canHePunchIn, MediaType.parse("multipart/form-data"));
        RequestBody requestFile=null;
        MultipartBody.Part multipartBody=null;
        Call<JsonObject> responseBodyCall=null;
        dialog.show();
        AppService appService = RetrofitInstance.getService();
        if(selectedfile!=null){
            requestFile =
                    RequestBody.create(
                            selectedfile, MediaType.parse("multipart/form-data")
                    );
        }
        if(Globals.canHePunchIn){
            multipartBody = MultipartBody.Part.createFormData("punch_in_image", selectedfile.getName(), requestFile);
        }
        else{
            multipartBody = MultipartBody.Part.createFormData("punch_out_image", selectedfile.getName(), requestFile);
        }

        responseBodyCall = appService.saveAttendance("Bearer " + sessionManager.getStringData( Globals.tokent ),status,multipartBody);
        responseBodyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();

                if (response.body() != null && response.isSuccessful()) {
                    //Log.e("Success1", "success " + response.body());
                    if(response.body().get("responseStatus").getAsInt()==200) {
                        ShowToast(response.body().get("message").getAsString());
                        finish();
                    }
                    else
                        ShowToast("Attendance not Successful");
                }
                else {
                    ShowToast("Please Try Later");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("failure", "message = " + t.getMessage());
                Log.d("failure", "cause = " + t.getCause());
                dialog.dismiss();
            }
        });
    }

    private void takeSelfie() {
        if (checkPermissionGranted()) {
            takeSelfieNow();
        }
        else
        {
            if(isCameraAndMemoryAccess)
                takeSelfieNow();
        }
    }

    private void takeSelfieNow() {
        try {
           /* if (SDK_INT >= Build.VERSION_CODES.R) {
                if (checkMediaPermission()) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(intent, CAPTURE_IMAGE);
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION,Manifest.permission.CAMERA}, 1234);
                }

            }
            else
            {
                File imagePath = new File(Environment.getExternalStorageDirectory(), "att");
                if (!imagePath.exists()) {
                    imagePath.mkdir();
                }
                else {
                    Log.e("File exist: ","File Exits already");
                }
                selectedfile = new File(imagePath.getPath(), "att.png");
                imageUri = FileProvider.getUriForFile(selfiActivity.this, getPackageName() + ".provider", selectedfile);

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CAPTURE_IMAGE);
            }*/

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            if (true) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (Exception ex) {
                    Log.e("CFL",ex.toString());
                }

                if (photoFile != null) {
                    Log.e("PhotoFile is Null","No");
                    Uri photoURI = FileProvider.getUriForFile(Selfie_Activity.this,
                            "com.example.android.hrms.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE);
                }else{
                    Log.e("PhotoFile is Null","Yes");
                }
            }
        } catch (Exception x) {
            Log.e("ERS", x.toString());;
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean checkPermissionGranted() {

        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (checkMediaPermission()) {
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1234);
                isCameraAndMemoryAccess = true;
                return false;
            }
        } else if (SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1234);
                isCameraAndMemoryAccess = true;
                return false;
            }
        } else {
            return true;
        }
    }

    private boolean checkMediaPermission() {
        if (ContextCompat.checkSelfPermission(Selfie_Activity.this, Manifest.permission.ACCESS_MEDIA_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(Selfie_Activity.this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            //iv_selfie.setImageURI(data.getData());

            if (requestCode == 1234) {
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        Toast.makeText(this, "You can continue now", Toast.LENGTH_SHORT).show();

                    } else {
                        // Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            /*if (requestCode == CAPTURE_IMAGE) {
                isImageSelected=1;
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    try {
                        File deleteFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + File.separator + "att.jpg");
                        if (deleteFile.exists())
                        {
                            deleteFile.delete();
                        }
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        iv_selfie.setImageBitmap(bitmap);
                        FileOutputStream fos;
                        ContentResolver contentResolver = getContentResolver();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "att.jpg");
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                        Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        Log.e("FLOC",getRealPathFromURI(uri));

                        fos = (FileOutputStream) contentResolver.openOutputStream(Objects.requireNonNull(uri));

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                        selectedfile = new File(getRealPathFromURI(uri));

                    } catch (Exception e) {
                        Log.e(TAG, "122" + e);
                    }
                } else {
                    try {
                        File imagePath = new File(Environment.getExternalStorageDirectory(), "att");
                        if (imagePath.exists()) {

                            imageUri = FileProvider.getUriForFile(selfiActivity.this, getPackageName() + ".provider", selectedfile);
                            thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            iv_selfie.setImageBitmap(thumbnail);

                            File file = new File(imagePath.getPath(), "att.jpg");
                            FileOutputStream out = new FileOutputStream(file);
                            thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, out);
                            out.flush();
                            out.close();

                            double fileSizeInBytes = Double.parseDouble(file.length() + "");
                            double fileSizeInKB = fileSizeInBytes / 1024;
                            fileSizeInMB = fileSizeInKB / 1024;

                            if (fileSizeInMB <= 1) {

                                selectedfile = file;
                            } else {
                                FileOutputStream outs = new FileOutputStream(file);
                                thumbnail.compress(Bitmap.CompressFormat.JPEG, 40, outs);
                                outs.flush();
                                outs.close();
                                Log.e("FSIZE2",file.length()+"");

                                selectedfile = file;
                            }
                        }

                    } catch (Exception e) {
                        Log.e("ERS", e.toString());
                        Toast.makeText(selfiActivity.this, "Internal Error, Try Later", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }*/


            if (requestCode == CAPTURE_IMAGE) {

                isImageSelected=1;

                selectedfile=new File(currentPhotoPath);
                Log.e("ImageUrl",currentPhotoPath);
                iv_selfie.setImageURI(Uri.parse(currentPhotoPath));
            }
            else
            {
                Log.e("RequestCode==>>","Image Capture Code is not same with request Code");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1234)
        {
            if(grantResults.length>0)
            {
                int media_access = grantResults[0];
                int camera = grantResults[1];

                boolean m_access = media_access == PackageManager.PERMISSION_GRANTED;
                boolean cam = camera == PackageManager.PERMISSION_GRANTED;

                if(m_access && cam)
                {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(intent, CAPTURE_IMAGE);
                }
                else
                {
                    //ShowToast.show(selfiActivity.this,"Camera and Media Access permission must required to take selfi!");
                    DisplayDialogBox.showDialogBox(Selfie_Activity.this,"Camera and Media Access required to take selfie","Selfie Alert");
                    //finish();
                }
            }
        }
    }

    private void ShowToast(String message) {
        Toast.makeText(Selfie_Activity.this, message, Toast.LENGTH_LONG).show();
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


}