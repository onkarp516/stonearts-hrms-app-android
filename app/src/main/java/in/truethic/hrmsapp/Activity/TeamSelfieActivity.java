package in.truethic.hrmsapp.Activity;

import static android.os.Build.VERSION.SDK_INT;

import static in.truethic.hrmsapp.Utils.Globals.finalList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import in.truethic.hrmsapp.Model.FinalTeamAttendaceModel;
import in.truethic.hrmsapp.Model.TeamMembersModel;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamSelfieActivity extends AppCompatActivity {



    ImageView iv_selfie;

    ImageView iv_back;

    Button btn_take_selfie;

    Button btn_reset_selfie;

    int CAPTURE_IMAGE = 102;
    SessionManager sessionManager;
    ProgressDialog dialog;
    File selectedfile;
    Uri imageUri;
    Bitmap thumbnail;
    String realImageUri;
    public double fileSizeInMB;
    int isImageSelected=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_selfie);

        sessionManager = new SessionManager(TeamSelfieActivity.this);

        dialog = new ProgressDialog(TeamSelfieActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        iv_selfie = findViewById(R.id.iv_selfie);
        iv_back = findViewById(R.id.iv_back);
        btn_take_selfie = findViewById(R.id.btn_take_selfie);
        btn_reset_selfie = findViewById(R.id.btn_reset_selfie);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_take_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageSelected == 0) {
                    ShowToast("Please Take Selfie");

                }

                gotoSubmit();
            }
        });

        Log.e("TeamID==>",Globals.teamId);
        Log.e("SiteID==>",Globals.branchId);


        btn_reset_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeSelfie();
            }
        });


        takeSelfie();
    }

    public void takeSelfie(){
        if (checkPermissionGranted()) {
            takeSelfieNow();

        }
    }
    private void takeSelfieNow() {
        try {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (checkMediaPermission()) {

                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(intent, CAPTURE_IMAGE);


                    /*File file = new File(Environment.DIRECTORY_PICTURES+File.separator+"att1");
                    if (file.exists()) {
                        ShowToast("Folder Exist");
                    } else {
                        if(file.mkdir()) {
                            ShowToast("FolderCreated");
                        }else{
                            ShowToast("Failed Create Folder");
                        }
                    }*/
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION}, 1234);
                }

            } else {
                File imagePath = new File(Environment.getExternalStorageDirectory(), "att");
                if (!imagePath.exists()) {
                    imagePath.mkdir();
                }
                selectedfile = new File(imagePath.getPath(), "att.png");
                imageUri = FileProvider.getUriForFile(TeamSelfieActivity.this, getPackageName() + ".provider", selectedfile);

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CAPTURE_IMAGE);
            }
        } catch (Exception x) {
            Log.e("ERS", x.toString());
        }
    }
    private boolean checkPermissionGranted() {

        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (checkMediaPermission()) {
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.CAMERA}, 1234);
                return false;
            }
        } else if (SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1234);
                return false;
            }
        } else {
            return true;
        }

    }

    private boolean checkMediaPermission() {
        if (ContextCompat.checkSelfPermission(TeamSelfieActivity.this, Manifest.permission.ACCESS_MEDIA_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public void gotoSubmit() {



        Set<TeamMembersModel> dFinalList=new LinkedHashSet<>(Globals.finalList);

        List<TeamMembersModel> ofinalList = new ArrayList<>(dFinalList);
        List<FinalTeamAttendaceModel> finalList = new ArrayList<>();

        for (int i = 0; i < ofinalList.size(); i++) {
            finalList.add(new FinalTeamAttendaceModel(ofinalList.get(i).getEmpployeeId(), ofinalList.get(i).getTeamId()));
        }



        Log.e("OFINALLIST", "" + ofinalList.size());
        Log.e("FINALLIST", "" + finalList.size());
        Gson gson = new Gson();
        Log.e("FINALLIST", gson.toJson(finalList));


        RequestBody attType = RequestBody.create(String.valueOf(Globals.att_type), MediaType.parse("multipart/form-data"));

        RequestBody latitude = RequestBody.create(String.valueOf(Globals.teamlat), MediaType.parse("multipart/form-data"));
        RequestBody longitude = RequestBody.create(String.valueOf(Globals.teamlong), MediaType.parse("multipart/form-data"));
        RequestBody siteid = RequestBody.create(String.valueOf(Globals.branchId), MediaType.parse("multipart/form-data"));
        RequestBody teamid = RequestBody.create(String.valueOf(Globals.teamId), MediaType.parse("multipart/form-data"));

        RequestBody requestFile=null;
        MultipartBody.Part multipartBody=null;
        Call<JsonObject> responseBodyCall=null;

        JsonArray jsonArray = new JsonArray();

        RequestBody memberList = RequestBody.create(gson.toJson(finalList), MediaType.parse("multipart/form-data"));


        dialog.show();

        AppService appService = RetrofitInstance.getService();
        if(selectedfile!=null){
            requestFile =
                    RequestBody.create(
                            selectedfile, MediaType.parse("multipart/form-data")
                    );


           // multipartBody = MultipartBody.Part.createFormData("punch_in_image", selectedfile.getName(), requestFile);

            if(Globals.att_type.equals(true)){
                multipartBody = MultipartBody.Part.createFormData("punch_in_image", selectedfile.getName(), requestFile);
            }
            else{
                multipartBody = MultipartBody.Part.createFormData("punch_out_image", selectedfile.getName(), requestFile);
            }

            responseBodyCall = appService.saveTeamAttendance("Bearer " + sessionManager.getStringData( Globals.tokent ), attType, multipartBody, memberList, latitude, longitude, siteid, teamid);
        }else{
         //   responseBodyCall = appService.saveTeamAttendanceNI("token " + sessionManager.getUserDetails("user").getToken(), attType, memberList, latitude, longitude, siteid, teamid);
        }

        responseBodyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();

                if (response.body() != null) {
                    Log.e("Success1", "success " + response.body());
                    //Log.e("Success2", "success " + response.code());
                    //Log.e("Success3", "success " + response.message());
                    if (!response.body().get("responseStatus").isJsonNull()) {
                        if (response.body().get("responseStatus").getAsInt() == 200) {
                            ShowToast(response.body().get("message").getAsString());

                            Intent i = new Intent(TeamSelfieActivity.this, HomeActivity.class);
                            startActivity(i);
                          //  TeamSelfieActivity.this.finish();
                        } else {
                            ShowToast(response.body().get("message").getAsString());
                        }
                    } else {
                        ShowToast("Attendance Not Submitted!");
                    }

                } else {
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


    private void ShowToast(String message) {
        Toast.makeText(TeamSelfieActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode==1234){
            takeSelfie();
        }else{
            //takeSelfie();
            ShowToast("Try again or goto settings and allow permission manually");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            //iv_selfie.setImageURI(data.getData());

            if (requestCode == 2296) {
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        Toast.makeText(this, "You can continue now", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if (requestCode == CAPTURE_IMAGE) {

                isImageSelected=1;

                if (SDK_INT >= Build.VERSION_CODES.R) {

                    try {

                        //Log.e("FLOC1",Environment.DIRECTORY_PICTURES+File.separator+"att.jpg");
                        File deleteFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + File.separator + "att.jpg");
                        if (deleteFile.exists()) {
                            deleteFile.delete();
                            /*if()
                            ShowToast("File Deleted");
                            else
                                ShowToast(("Exist but not deleted"));*/
                        }/* else {
                            ShowToast("No File is available");
                        }*/


                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        iv_selfie.setImageBitmap(bitmap);
                        FileOutputStream fos;
                        ContentResolver contentResolver = getContentResolver();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "att.jpg");
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                        Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        //Log.e("FLOC",getRealPathFromURI(uri));


                        fos = (FileOutputStream) contentResolver.openOutputStream(Objects.requireNonNull(uri));

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                        selectedfile = new File(getRealPathFromURI(uri));

                    } catch (Exception e) {
                        //ShowToast("Failed File Create");
                        //Log.e(TAG, "122" + e);
                    }
                } else {
                    try {


                        File imagePath = new File(Environment.getExternalStorageDirectory(), "att");
                        if (imagePath.exists()) {

                            imageUri = FileProvider.getUriForFile(TeamSelfieActivity.this, getPackageName() + ".provider", selectedfile);
                            thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            iv_selfie.setImageBitmap(thumbnail);

                            File file = new File(imagePath.getPath(), "att.jpg");
                            //File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/attp.jpg");
                            FileOutputStream out = new FileOutputStream(file);
                            thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, out);
                            out.flush();
                            out.close();

                            double fileSizeInBytes = Double.parseDouble(file.length() + "");
                            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                            double fileSizeInKB = fileSizeInBytes / 1024;
                            //  Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                            fileSizeInMB = fileSizeInKB / 1024;
                            //Log.e("FSIZE1",fileSizeInBytes+","+fileSizeInKB+","+fileSizeInMB);

                            if (fileSizeInMB <= 1) {

                                selectedfile = file;
                            } else {
                                FileOutputStream outs = new FileOutputStream(file);
                                thumbnail.compress(Bitmap.CompressFormat.JPEG, 40, outs);
                                outs.flush();
                                outs.close();

                                //Log.e("FSIZE2",file.length()+"");

                                selectedfile = file;
                            }


                        }




                    } catch (Exception e) {
                        Log.e("ERS", e.toString());
                        Toast.makeText(TeamSelfieActivity.this, "Internal Error, Try Later", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                /*
                Working code 1
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                iv_selfie.setImageBitmap(thumbnail);

                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                File myDir = new File(root);
                //myDir.mkdirs();

                String fname = "attimg.jpg";
                File file = new File(myDir, fname);
                *//*if (file.exists())
                    file.delete();*//*
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    selectedfile = file;

                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public void gotoReset(View view) {
        takeSelfie();
    }
}