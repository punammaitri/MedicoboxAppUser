package com.aiprous.medicobox.prescription;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.BuildConfig;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.DELETE_IMAGE_PRESCRIPTION;
import static com.aiprous.medicobox.utils.APIConstant.GET_UPLOADED_PRESCRIPTION;
import static com.aiprous.medicobox.utils.APIConstant.UPLOAD_PRESCRIPTION_IMAGE;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;

public class PrescriptionUploadActivity extends AppCompatActivity implements PrescriptionUploadAdapter.DeleteImageInterface {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    @BindView(R.id.rc_medicine_list)
    RecyclerView rc_medicine_list;
    @BindView(R.id.img_valid_prescription)
    ImageView img_valid_prescription;
    ArrayList<ListModel> mBitmapModelsArray = new ArrayList<>();
    ArrayList<ImageUrlModel> mlistModelsArray = new ArrayList<>();
    @BindView(R.id.radioButtonYes)
    RadioButton radioButtonYes;
    @BindView(R.id.radioButtonNo)
    RadioButton radioButtonNo;
    @BindView(R.id.edt_patient_name)
    EditText edtPatientName;
    @BindView(R.id.edt_additional_comment)
    EditText edtAdditionalComment;

    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;

    //*************************************for browse Images************************
    private static final int REQUEST_PERMISSIONS = 20;
    int TAKE_IMAGE = 1;
    Bitmap bitmapCamera = null;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    String flagVariableImageEmpty = "";
    Uri mImageCaptureUri;
    final String TAG = getClass().getSimpleName();
    public Bitmap mBitmap;
    int UPLOAD_IMAGE = 2;
    private static File outPutFile = null;
    int CROPING_CODE = 3;
    private String picturePath = "";
    public String mProfilePicName = "";
    static String strFileCamera = "";
    public int RESULT_TAKE_IMAGE = 1;
    public int RESULT_UPLOAD_IMAGE = 2;
    public int RESULT_CROPING_CODE = 3;
    public int RESULT_ORGANIZTION = 4;
    public String imageBinaryString = "";
    public static int activityCalled = 0;
    public static final int RESULT_OK = -1;
    private Uri multipleImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_prescription);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mBitmapModelsArray.clear();
        mlistModelsArray.clear();

        searchview_medicine.setFocusable(false);
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        edtPatientName.setText(MedicoboxApp.onGetFirstName() + " " + MedicoboxApp.onGetLastName());
        //rc_medicine_list = findViewById(R.id.rc_medicine_list);

        //check is device above lollipop
        if (isAboveLollipop()) {
            if (!checkAppPermission()) requestPermission();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //set list adapter
        // setListAdapter();
    }

    private void setListAdapter(ArrayList<ImageUrlModel> mlistModelsArray) {
        layoutManager = new LinearLayoutManager(mContext);
        rc_medicine_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rc_medicine_list.setHasFixedSize(true);
        rc_medicine_list.setAdapter(new PrescriptionUploadAdapter(PrescriptionUploadActivity.this, mlistModelsArray));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }

        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            GetAllPrescriptionAPI();
        }
    }

    private void GetAllPrescriptionAPI() {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.post(GET_UPLOADED_PRESCRIPTION)
                .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            if (response.getString("status").equals("success")) {
                                mlistModelsArray.clear();
                                JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                                JsonObject responseObject = getAllResponse.get("data").getAsJsonObject();
                                JsonArray getImageUrl = responseObject.get("images").getAsJsonArray();

                                for (int i = 0; i < getImageUrl.size(); i++) {
                                    String getUrl = getImageUrl.get(i).getAsString();
                                    ImageUrlModel imageUrlModel = new ImageUrlModel(getUrl);
                                    imageUrlModel.setImageUrl(getUrl);
                                    mlistModelsArray.add(imageUrlModel);
                                }
                                setListAdapter(mlistModelsArray);
                                CustomProgressDialog.getInstance().dismissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(PrescriptionUploadActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

    @OnClick({R.id.cardview_take_photo, R.id.cardview_gallery, R.id.radioButtonYes, R.id.radioButtonNo,
            R.id.rlayout_back_button, R.id.btnContinue, R.id.rlayout_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cardview_take_photo:
                selectImage("take_photo");
                break;
            case R.id.cardview_gallery:
                selectImage("open_gallery");
                break;
            case R.id.radioButtonYes:
                edtPatientName.setText(MedicoboxApp.onGetFirstName() + " " + MedicoboxApp.onGetLastName());
                break;
            case R.id.radioButtonNo:
                edtPatientName.setText("");
                break;
            case R.id.rlayout_back_button:
                finish();
                break;
            case R.id.btnContinue:
                if (edtPatientName.getText().length() == 0) {
                    edtPatientName.setError("add patient name");
                } else if (edtAdditionalComment.getText().length() == 0) {
                    Toast.makeText(mContext, "Add comment", Toast.LENGTH_SHORT).show();
                } else {
                    String getPatientName = edtPatientName.getText().toString().trim();
                    String getAdditionalComment = edtAdditionalComment.getText().toString().trim();
                    //send all data to prescription activity
                    try {
                        startActivity(new Intent(mContext, PrescriptionUploadOptionActivity.class)
                                .putExtra("getPatientName", getPatientName)
                                .putExtra("getAdditionalComment", getAdditionalComment));
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.rlayout_cart:
                startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
        }
    }

    public void selectImage(String type) {
        if (type.equalsIgnoreCase("take_photo")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            strFileCamera = getImageName() + ".jpg";
            File filename = new File(Environment.getExternalStorageDirectory(), strFileCamera);
            mImageCaptureUri = Uri.fromFile(filename);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            startActivityForResult(intent, RESULT_TAKE_IMAGE);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_UPLOAD_IMAGE);
        }
    }

    private String getImageName() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date now = new Date();
            String fileName = "IMG_" + formatter.format(now);
            return fileName;
        } catch (Exception e) {
            return "IMG_temp";
        }
    }

    private boolean isAboveLollipop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.INTERNET}, REQUEST_PERMISSIONS);
    }

    private boolean checkAppPermission() {
        int WRITE_EXTERNAL_STORAGE_PERMISSION = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int READ_EXTERNAL_STORAGE_PERMISSION = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int CAMERA_PERMISSION = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int INTERNET_PERMISSION = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
        return WRITE_EXTERNAL_STORAGE_PERMISSION == PackageManager.PERMISSION_GRANTED && READ_EXTERNAL_STORAGE_PERMISSION == PackageManager.PERMISSION_GRANTED && CAMERA_PERMISSION == PackageManager.PERMISSION_GRANTED && INTERNET_PERMISSION == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmapCamera = null;
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == RESULT_TAKE_IMAGE) {
                    bitmapCamera = getCapturedImage(strFileCamera);
                    cropingIMG();
                        /*mBitmap = (Bitmap) data.getExtras().get("data");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);*/
                } else if (requestCode == RESULT_UPLOAD_IMAGE) {

                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        for (int i = 0; i < count; i++) {
                            multipleImageUrl = data.getClipData().getItemAt(i).getUri();
                            // Create content resolver.
                            ContentResolver contentResolver = getContentResolver();
                            InputStream inputStream = contentResolver.openInputStream(multipleImageUrl);
                            Bitmap imgBitmap = BitmapFactory.decodeStream(inputStream);
                            imageBinaryString = convertBitmapToString(imgBitmap);
                            //add to bitmap array
                            mBitmapModelsArray.add(new ListModel(imgBitmap));
                           /* //Add image to image Array
                            ImageUrlModel imageUrlModel = new ImageUrlModel(imageBinaryString);
                            imageUrlModel.setImageUrl(imageBinaryString);
                            mlistModelsArray.add(imageUrlModel);*/
                            callUploadPrescriptionImageAPI(imageBinaryString);
                            inputStream.close();
                        }
                    } else if (data.getData() != null) {
                        multipleImageUrl = data.getData();
                        // Create content resolver.
                        ContentResolver contentResolver = getContentResolver();
                        InputStream inputStream = contentResolver.openInputStream(multipleImageUrl);
                        Bitmap imgBitmap = BitmapFactory.decodeStream(inputStream);
                        imageBinaryString = convertBitmapToString(imgBitmap);
                        //add to bitmap array
                        mBitmapModelsArray.add(new ListModel(imgBitmap));
                        //setListAdapter();
                        /*//Add image to image Array
                        ImageUrlModel imageUrlModel = new ImageUrlModel(imageBinaryString);
                        imageUrlModel.setImageUrl(imageBinaryString);
                        mlistModelsArray.add(imageUrlModel);*/
                        callUploadPrescriptionImageAPI(imageBinaryString);
                        inputStream.close();
                    }
                } else if (requestCode == RESULT_CROPING_CODE) {

                    try {
                        if (outPutFile.exists()) {
                            mBitmap = decodeFile(outPutFile);
                            //ivProfile.setImageBitmap(mBitmap);
                            imageBinaryString = convertBitmapToString(mBitmap);
                            //add to bitmap array
                            mBitmapModelsArray.add(new ListModel(mBitmap));
                            //setListAdapter(mlistModelsArray);
                            //Add image to image Array
                            ImageUrlModel imageUrlModel = new ImageUrlModel(imageBinaryString);
                            imageUrlModel.setImageUrl(imageBinaryString);
                            mlistModelsArray.add(imageUrlModel);
                            //Call  upload prescription API
                            callUploadPrescriptionImageAPI(imageBinaryString);
                        } else {
                            Toast.makeText(mContext, "error while save file", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callUploadPrescriptionImageAPI(String imageBinaryString) {

        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("image", "" + imageBinaryString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(UPLOAD_PRESCRIPTION_IMAGE)
                .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            if (response.has("data")) {
                                mlistModelsArray.clear();

                                JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                                JsonObject responseObject = getAllResponse.get("data").getAsJsonObject();
                                JsonArray getImageUrl = responseObject.get("images").getAsJsonArray();

                                for (int i = 0; i < getImageUrl.size(); i++) {
                                    String getUrl = getImageUrl.get(i).getAsString();
                                    ImageUrlModel imageUrlModel = new ImageUrlModel(getUrl);
                                    imageUrlModel.setImageUrl(getUrl);
                                    mlistModelsArray.add(imageUrlModel);
                                }
                                setListAdapter(mlistModelsArray);
                                CustomProgressDialog.getInstance().dismissDialog();
                                GetAllPrescriptionAPI();
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(PrescriptionUploadActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 512;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap getCapturedImage(String imageName) {
        Bitmap bitmap = null;
        try {
            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals(imageName)) {
                    f = temp;
                    mImageCaptureUri = FileProvider.getUriForFile(mContext,
                            BuildConfig.APPLICATION_ID + ".provider",
                            temp);
                    break;
                }
            }

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
        } catch (Exception e) {
        }
        return bitmap;
    }

    private void cropingIMG() {
        final ArrayList<CropingOption> cropOptions = new ArrayList<CropingOption>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        int size = list.size();
        if (size == 0) {
            setInitialImage();
            return;
        } else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);

            //intent.putExtra("return-data", true);
            //Create output file here
            mProfilePicName = getImageName() + ".jpg";
            outPutFile = new File(Environment.getExternalStorageDirectory(), mProfilePicName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));
            Intent i = new Intent(intent);
            boolean isAppFound = false;
            for (ResolveInfo res : list) {
                String strAppTitle = mContext.getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo).toString();
                if (strAppTitle.equalsIgnoreCase("Gallery") || strAppTitle.equalsIgnoreCase("Photos")) {
                    i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    isAppFound = true;
                    break;
                }
            }
            if (isAppFound) {
                activityCalled = RESULT_CROPING_CODE;
                startActivityForResult(i, RESULT_CROPING_CODE);
            } else {
                setInitialImage();
            }
        }
    }

    private void setInitialImage() {
        try {
            if (mImageCaptureUri != null){
                if (bitmapCamera != null)
                    mBitmap = bitmapCamera;
                else
                    mBitmap = getSelectedImage(mImageCaptureUri);
                //ivProfile.setImageBitmap(mBitmap);
                mBitmapModelsArray.add(new ListModel(mBitmap));
                // setListAdapter();
                imageBinaryString = convertBitmapToString(mBitmap);
                //Call  upload prescription API
                //callUploadPrescriptionImageAPI(imageBinaryString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getSelectedImage(Uri selectedImage) throws Exception {
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = mContext.getContentResolver().query(selectedImage, filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        String picturePath = c.getString(columnIndex);
        mProfilePicName = picturePath.substring(picturePath.lastIndexOf("/") + 1);
        c.close();
        Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
        return thumbnail;
    }

    @Override
    public void DeleteImage(String delete, String url) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", delete);
            jsonObject.put("image", url);
            Log.e("url", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(DELETE_IMAGE_PRESCRIPTION)
                .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            if (response.has("data")) {
                                mlistModelsArray.clear();
                                JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                                String status = getAllResponse.get("status").getAsString();
                                if (status.equals("success")) {
                                    Toast.makeText(mContext, "Image deleted successfully", Toast.LENGTH_SHORT).show();
                                }
                                CustomProgressDialog.getInstance().dismissDialog();
                                GetAllPrescriptionAPI();
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(PrescriptionUploadActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }


    public class ListModel {

        Bitmap Imagebitmap;

        public ListModel(Bitmap imagebitmap) {
            Imagebitmap = imagebitmap;
        }

        public Bitmap getImagebitmap() {
            return Imagebitmap;
        }

        public void setImagebitmap(Bitmap imagebitmap) {
            Imagebitmap = imagebitmap;
        }
    }

}