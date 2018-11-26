package com.aiprous.medicobox.prescription;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.BuildConfig;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrescriptionUploadActivity extends AppCompatActivity {

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
    ArrayList<ListModel> mlistModelsArray = new ArrayList<>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_prescription);

        ButterKnife.bind(this);
        init();
    }

    private void init() {

        searchview_medicine.setFocusable(false);
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

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
        setListAdapter();
    }
    private void setListAdapter() {
        layoutManager = new LinearLayoutManager(mContext);
        rc_medicine_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rc_medicine_list.setHasFixedSize(true);
        rc_medicine_list.setAdapter(new PrescriptionUploadAdapter(mContext, mlistModelsArray));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }
    }

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    @OnClick(R.id.rlayout_back_button)
    public void BackPressSDescription() {
        finish();
    }

    @OnClick(R.id.btnContinue)
    public void ButtonContinue() {
        startActivity(new Intent(this, PrescriptionUploadOptionActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick({R.id.cardview_take_photo, R.id.cardview_gallery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cardview_take_photo:
                selectImage("take_photo");
                break;
            case R.id.cardview_gallery:
                selectImage("open_gallery");
                break;
        }
    }

    public void selectImage(String type) {
        if (type.equalsIgnoreCase("take_photo")){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            strFileCamera = getImageName() + ".jpg";
            File filename = new File(Environment.getExternalStorageDirectory(), strFileCamera);
            mImageCaptureUri = Uri.fromFile(filename);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            startActivityForResult(intent, RESULT_TAKE_IMAGE);
        }else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RESULT_UPLOAD_IMAGE);
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

                    mImageCaptureUri = data.getData();
                    cropingIMG();
                    //mBitmap = getSelectedImage(mImageCaptureUri);
                } else if (requestCode == RESULT_CROPING_CODE) {
                    try {
                        if (outPutFile.exists()) {
                            mBitmap = decodeFile(outPutFile);
                            //ivProfile.setImageBitmap(mBitmap);
                            mlistModelsArray.add(new ListModel(mBitmap));
                            setListAdapter();
                            imageBinaryString = convertBitmapToString(mBitmap);
                        } else {

                            Toast.makeText(mContext, "error while save file", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                    }
                }
            }

        } catch (Exception e) {

        }
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

            //TODO: don't use return-data tag because it's not return large image data and crash not given any message
            //intent.putExtra("return-data", true);

            //Create output file here
            mProfilePicName = getImageName() + ".jpg";
            outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), mProfilePicName);
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
        if (mImageCaptureUri != null) {
            try {
                if (bitmapCamera != null)
                    mBitmap = bitmapCamera;
                else
                    mBitmap = getSelectedImage(mImageCaptureUri);
                //ivProfile.setImageBitmap(mBitmap);
                mlistModelsArray.add(new ListModel(mBitmap));
                setListAdapter();
                imageBinaryString = convertBitmapToString(mBitmap);
            } catch (Exception e) {
            }
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

    public class ListModel {
        // int image;
        //  String medicineName;
        // String value;
        // String mrp;
        // String discount;
        // String price;
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