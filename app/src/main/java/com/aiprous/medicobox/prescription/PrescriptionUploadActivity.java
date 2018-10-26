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
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;

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
    }


    @OnClick(R.id.rlayout_back_button)
    public void BackPressSDescription() {
        finish();
    }

    @OnClick(R.id.btnContinue)
    public void ButtonContinue() {
        startActivity(new Intent(this, PrescriptionUploadOptionActivity.class));
    }

    @OnClick({R.id.cardview_take_photo, R.id.cardview_gallery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cardview_take_photo:
                // startCamera();
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, TAKE_IMAGE);
                }
                break;
            case R.id.cardview_gallery:
                Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent2, UPLOAD_IMAGE);
                break;
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
                if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {


                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");

                    mlistModelsArray.add(new ListModel(imageBitmap));
                    setListAdapter();
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    mImageCaptureUri = getImageUri(getApplicationContext(), imageBitmap);
                    flagVariableImageEmpty = mImageCaptureUri.toString();

                }
                else if (requestCode == UPLOAD_IMAGE) {
                    mImageCaptureUri = data.getData();
                    cropingIMG();
                    flagVariableImageEmpty = mImageCaptureUri.toString();
                    mBitmap = getSelectedImage(mImageCaptureUri);

                    mlistModelsArray.add(new ListModel(mBitmap));
                    setListAdapter();

                }

            }else if (requestCode == CROPING_CODE) {
                try {
                    if (outPutFile.exists()) {
                        mBitmap = decodeFile(outPutFile);

                        mlistModelsArray.add(new ListModel(mBitmap));
                        setListAdapter();

                        //imageBinaryString = convertBitmapToString(mBitmap);
                    } else {
                        Toast.makeText(mContext, "Error while save image", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Crop image", e.toString());
                }
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void cropingIMG() {
        final ArrayList<CropingOption> cropOptions = new ArrayList<CropingOption>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = mContext.getPackageManager().queryIntentActivities(intent, 0);
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
            outPutFile = new File(Environment.getExternalStorageDirectory(), getImageName() + ".jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));

            if (size >= 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = (ResolveInfo) list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                startActivityForResult(i, CROPING_CODE);
            } else {
                for (ResolveInfo res : list) {
                    final CropingOption co = new CropingOption();
                    co.title = mContext.getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = mContext.getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    cropOptions.add(co);
                }
                CropingOption co = new CropingOption();
                co.title = "Cancel";
                co.icon = getResources().getDrawable(R.drawable.cross_black);
                cropOptions.add(co);
                final CropingOptionAdapter adapter = new CropingOptionAdapter(getApplicationContext(), cropOptions);

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Croping App");
                builder.setCancelable(false);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item != adapter.getLastIndex()) {
                            startActivityForResult(cropOptions.get(item).appIntent, CROPING_CODE);
                        } else {
                            setInitialImage();
                        }
                    }
                });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        setInitialImage();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
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
                //imageViewSelectedImage.setImageBitmap(mBitmap);
                mlistModelsArray.add(new ListModel(mBitmap));
                setListAdapter();
                /////////////////////////circularProfileInfo.setImageBitmap(mBitmap);
                //imageBinaryString = convertBitmapToString(mBitmap);
            } catch (Exception e) {
                Log.e("Crop Image", e.toString());
            }
        }
    }

    private Bitmap getSelectedImage(Uri selectedImage) throws Exception {
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c =mContext.getContentResolver().query(selectedImage, filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        picturePath = "";
        picturePath = c.getString(columnIndex);
        mProfilePicName = picturePath.substring(picturePath.lastIndexOf("/") + 1);
        c.close();
        Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
        return thumbnail;
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

    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            try {
                BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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