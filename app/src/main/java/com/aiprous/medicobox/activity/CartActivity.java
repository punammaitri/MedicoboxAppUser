package com.aiprous.medicobox.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
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
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.BuildConfig;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.CartAdapter;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.CartModel;
import com.aiprous.medicobox.prescription.CropingOption;
import com.aiprous.medicobox.prescription.ImageUrlModel;
import com.aiprous.medicobox.prescription.PrescriptionUploadActivity;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
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
import static com.aiprous.medicobox.utils.APIConstant.GETCARTITEMS;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;

public class CartActivity extends AppCompatActivity implements CartAdapter.ShowPrescriptionInterface {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rc_cart)
    RecyclerView rc_cart;
    @BindView(R.id.tv_shipping_note)
    TextView tv_shipping_note;
    @BindView(R.id.btn_upload_prescription)
    Button btnUploadPrescription;
    @BindView(R.id.txtUploadPrescription)
    TextView txtUploadPrescription;

    public static TextView tv_mrp_total, tv_price_discount, tv_to_be_paid, tv_total_saving;
    public static TextView tv_cart_size, tv_cart_empty;
    public static RelativeLayout rlayout_cart;
    public static NestedScrollView nestedscroll_cart;
    ArrayList<CartModel.Response> cartModelArrayList = new ArrayList<>();

    CustomProgressDialog mAlert;
    private RecyclerView.LayoutManager layoutManager;
    private Context mContext = this;

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
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mAlert = CustomProgressDialog.getInstance();

        tv_mrp_total = (TextView) findViewById(R.id.tv_mrp_total);
        tv_price_discount = (TextView) findViewById(R.id.tv_price_discount);
        tv_to_be_paid = (TextView) findViewById(R.id.tv_to_be_paid);
        tv_total_saving = (TextView) findViewById(R.id.tv_total_saving);
        tv_cart_empty = (TextView) findViewById(R.id.tv_cart_empty);
        nestedscroll_cart = (NestedScrollView) findViewById(R.id.nestedscroll_cart);
        rlayout_cart = (RelativeLayout) findViewById(R.id.rlayout_cart);
        tv_cart_size = (TextView) findViewById(R.id.tv_cart_size);

        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        tv_shipping_note.setText("Free shipping for orders above " + mContext.getResources().getString(R.string.Rs) + "500");

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //check is device above lollipop
        if (isAboveLollipop()) {
            if (!checkAppPermission()) requestPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            //get cart items through api
            getCartItems(MedicoboxApp.onGetAuthToken());
        }

        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            tv_cart_empty.setVisibility(View.VISIBLE);
            nestedscroll_cart.setVisibility(View.GONE);
            rlayout_cart.setVisibility(View.GONE);
        }

        rlayout_cart.setClickable(false);
    }

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.btn_continue_cart)
    public void onClickContinue() {
        if (txtUploadPrescription.getVisibility() == View.VISIBLE) {
            startActivity(new Intent(this, OrderSummaryActivity.class)
                    .putExtra("upload_presc_url", "" + mBitmap));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            Toast.makeText(mContext, "Please upload prescription", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail() {
        finish();
    }

    private void getCartItems(final String bearerToken) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.post(GETCARTITEMS)
                .addHeaders(Authorization, BEARER + bearerToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            JSONObject getAllObject = new JSONObject(getAllResponse.toString()); //first, get the jsonObject
                            JSONArray getAllProductList = getAllObject.getJSONArray("res");//get the array with the key "response"

                            if (getAllProductList != null) {
                                cartModelArrayList.clear();
                                for (int i = 0; i < getAllProductList.length(); i++) {
                                    int item_id = Integer.parseInt(getAllProductList.getJSONObject(i).get("item_id").toString());
                                    String sku = getAllProductList.getJSONObject(i).get("sku").toString();
                                    int qty = Integer.parseInt(getAllProductList.getJSONObject(i).get("qty").toString());
                                    String id = getAllProductList.getJSONObject(i).get("id").toString();
                                    String name = getAllProductList.getJSONObject(i).get("name").toString();
                                    String price = getAllProductList.getJSONObject(i).get("price").toString();
                                    String product_type = getAllProductList.getJSONObject(i).get("product_type").toString();
                                    String lquoteId = getAllProductList.getJSONObject(i).get("quote_id").toString();
                                    String sale_price = getAllProductList.getJSONObject(i).get("sale_price").toString();
                                    String short_description = getAllProductList.getJSONObject(i).get("short_description").toString();
                                    String image = getAllProductList.getJSONObject(i).get("image").toString();
                                    String prescription = getAllProductList.getJSONObject(i).get("prescription").toString();
                                    int discount = Integer.parseInt(getAllProductList.getJSONObject(i).get("discount").toString());
                                    int prescription_req = Integer.parseInt(getAllProductList.getJSONObject(i).get("prescription").toString());

                                    CartModel.Response listModel = new CartModel.Response(discount, prescription, image,
                                            short_description, sale_price, lquoteId, product_type, price, name, id, qty, sku, item_id, prescription_req);
                                    listModel.setItem_id(item_id);
                                    listModel.setSku(sku);
                                    listModel.setQty(qty);
                                    listModel.setId(id);
                                    listModel.setName(name);
                                    listModel.setPrice(price);
                                    listModel.setProduct_type(product_type);
                                    listModel.getQuote_id();
                                    listModel.setSale_price(sale_price);
                                    listModel.setShort_description(short_description);
                                    listModel.setImage(image);
                                    listModel.setPrescription(prescription);
                                    listModel.setDiscount(discount);
                                    listModel.setPrescription_req(prescription_req);
                                    cartModelArrayList.add(listModel);
                                }
                            }

                            //visible cart layout
                            if (!cartModelArrayList.isEmpty()) {
                                tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
                                tv_cart_empty.setVisibility(View.GONE);
                                nestedscroll_cart.setVisibility(View.VISIBLE);
                                rlayout_cart.setVisibility(View.VISIBLE);
                            }

                            //set Adapter
                            layoutManager = new LinearLayoutManager(mContext);
                            rc_cart.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
                            rc_cart.setHasFixedSize(true);
                            rc_cart.setAdapter(new CartAdapter(CartActivity.this, cartModelArrayList));

                            CustomProgressDialog.getInstance().dismissDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(CartActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

    @OnClick(R.id.searchview_medicine)
    public void onClicksearch() {
        startActivity(new Intent(this, SearchViewActivity.class));
    }

    @Override
    public void showPrescriptionUpload(int value) {
        if (value == 1) {
            txtUploadPrescription.setVisibility(View.VISIBLE);
            btnUploadPrescription.setVisibility(View.VISIBLE);
        }
    }

    private boolean isAboveLollipop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(CartActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.INTERNET}, REQUEST_PERMISSIONS);
    }

    private boolean checkAppPermission() {
        int WRITE_EXTERNAL_STORAGE_PERMISSION = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int READ_EXTERNAL_STORAGE_PERMISSION = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
        int CAMERA_PERMISSION = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
        int INTERNET_PERMISSION = ContextCompat.checkSelfPermission(mContext, Manifest.permission.INTERNET);
        return WRITE_EXTERNAL_STORAGE_PERMISSION == PackageManager.PERMISSION_GRANTED && READ_EXTERNAL_STORAGE_PERMISSION == PackageManager.PERMISSION_GRANTED && CAMERA_PERMISSION == PackageManager.PERMISSION_GRANTED && INTERNET_PERMISSION == PackageManager.PERMISSION_GRANTED;
    }


    @OnClick(R.id.btn_upload_prescription)
    public void onViewClicked() {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.from_gallary), getString(R.string.cancel)};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        builder.setTitle(getString(R.string.add_photo));
        builder.setCancelable(false);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.take_photo))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    strFileCamera = getImageName() + ".jpg";
                    File filename = new File(Environment.getExternalStorageDirectory(), strFileCamera);
                    mImageCaptureUri = Uri.fromFile(filename);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, RESULT_TAKE_IMAGE);
                } else if (options[item].equals(getString(R.string.from_gallary))) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_UPLOAD_IMAGE);
                } else if (options[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                    finish();
                }
            }
        });
        builder.show();
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
                            mBitmap = BitmapFactory.decodeStream(inputStream);
                            imageBinaryString = convertBitmapToString(mBitmap);
                            //add to bitmap array
                            inputStream.close();
                        }
                    } else if (data.getData() != null) {
                        multipleImageUrl = data.getData();
                        // Create content resolver.
                        txtUploadPrescription.setVisibility(View.VISIBLE);
                        txtUploadPrescription.setText(multipleImageUrl.toString());
                        ContentResolver contentResolver = getContentResolver();
                        InputStream inputStream = contentResolver.openInputStream(multipleImageUrl);
                        mBitmap = BitmapFactory.decodeStream(inputStream);
                        imageBinaryString = convertBitmapToString(mBitmap);
                        //add to bitmap array
                        inputStream.close();
                    }
                } else if (requestCode == RESULT_CROPING_CODE) {

                    try {
                        if (outPutFile.exists()) {
                            txtUploadPrescription.setVisibility(View.VISIBLE);
                            txtUploadPrescription.setText(outPutFile.getAbsolutePath());

                            mBitmap = decodeFile(outPutFile);
                            //ivProfile.setImageBitmap(mBitmap);
                            //imageBinaryString = convertBitmapToString(mBitmap);
                            //add to bitmap array
                        } else {
                            txtUploadPrescription.setVisibility(View.GONE);
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
            if (mImageCaptureUri != null) {
                if (bitmapCamera != null)
                    mBitmap = bitmapCamera;
                else
                    mBitmap = getSelectedImage(mImageCaptureUri);
                //ivProfile.setImageBitmap(mBitmap);
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
}