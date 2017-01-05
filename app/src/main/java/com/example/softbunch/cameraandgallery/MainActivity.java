package com.example.softbunch.cameraandgallery;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.softbunch.cameraandgallery.contributer.upLoadImageFile;
import com.example.softbunch.cameraandgallery.docsfile.DocumentFolder;
import com.example.softbunch.cameraandgallery.image.ImageFolder;
import com.example.softbunch.cameraandgallery.utils.RestClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect, compress, btnSelectfiles, upload;
    private ImageView ivImage, image_compress;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 123;
    TextView txtSize, txtSize_compress;
    File actualImage, compressedImage;
    RestClient restClient;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restClient = new RestClient();

        btnSelect = (Button) findViewById(R.id.btnSelectPhoto);
        compress = (Button) findViewById(R.id.compress);
        btnSelectfiles = (Button) findViewById(R.id.btnSelectfiles);
        upload = (Button) findViewById(R.id.upload);

        ivImage = (ImageView) findViewById(R.id.ivImage);
        image_compress = (ImageView) findViewById(R.id.image_compress);

        txtSize = (TextView) findViewById(R.id.txtSize);
        txtSize_compress = (TextView) findViewById(R.id.txtSize_compress);

        btnSelectfiles.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DocumentFolder.class);
                startActivity(intent);

            }
        });

        upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                MultipartBody.Part body = null;
                RequestBody number = null;
                RequestBody ext = null;

                try {
                    File file = new File(actualImage.getPath());
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                    body = MultipartBody.Part.createFormData("audio_file", file.getName(), reqFile);
                    number = RequestBody.create(MediaType.parse("text/plain"), "7039490130");
                    ext = RequestBody.create(MediaType.parse("text/plain"), "png");

                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Loading....");
                    progressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }



                restClient.getService().getUploadFile(
                        body,
                        number,
                        ext)
                        .enqueue(new Callback<upLoadImageFile>() {
                            @Override
                            public void onResponse(Call<upLoadImageFile> call, Response<upLoadImageFile> response) {
                                try {
                                    progressDialog.dismiss();
                                    String status = response.body().getListObject().getStatus();
                                    String message = response.body().getListObject().getMessage();
                                    String url = response.body().getListObject().getUrl();

                                    Log.e("status", status);
                                    Log.e("message", message);
                                    Log.e("url", url);

                                    Toast.makeText(MainActivity.this, message + " \n" + url, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<upLoadImageFile> call, Throwable t) {

                            }
                        });
            }
        });


        btnSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    /** Select  image from Gallery and Camera **/
                    selectImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        compress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actualImage != null) {
                    Compressor.getDefault(MainActivity.this)
                            .compressToFileAsObservable(actualImage)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<File>() {
                                @Override
                                public void call(File file) {
                                    compressedImage = file;
                                    setCompressedImage();
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    //     showError(throwable.getMessage());
                                    Toast.makeText(MainActivity.this, "Erroe", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });


        try {
            /** Check Mobile Version **/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    /** Check Permission **/
                    getPermission();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setCompressedImage() {
        try {
            /** Image Load Using Glide **/
            Glide.with(MainActivity.this)
                    .load(compressedImage.getAbsolutePath())
                    .into(image_compress);

            txtSize_compress.setText(String.format("Size : %s", getReadableFileSize(compressedImage.length())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Check Permission
     **/
    public void getPermission() {
        try {
            int readDataFlag = 0;
            int writeDataFlag = 0;

            int permissionCount = 0;
            int permissionCountNew = 0;
            int flag = 0;

            /** check permission is GRANTED or Not in Marshmallow */
            int readData = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int writeData = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (readData != PackageManager.PERMISSION_GRANTED) {
                readDataFlag = 1;
                permissionCount += 1;
                flag = 1;
            }

            if (writeData != PackageManager.PERMISSION_GRANTED) {
                writeDataFlag = 1;
                permissionCount += 1;
                flag = 1;
            }
            String[] permissionCArr = new String[permissionCount];

            if (readDataFlag == 1) {
                permissionCArr[permissionCountNew] = Manifest.permission.READ_EXTERNAL_STORAGE;
                permissionCountNew += 1;
            }

            if (writeDataFlag == 1) {
                permissionCArr[permissionCountNew] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                permissionCountNew += 1;
            }

            if (flag == 1) {
                ActivityCompat.requestPermissions(
                        this,
                        permissionCArr,
                        PERMISSIONS_REQUEST_READ_PHONE_STATE
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
            }
        }
    }


    /**
     * Select Image From Camera and Gellary
     **/
    private void selectImage() {
        try {
            /** Display Alert Dialog **/
            LayoutInflater layoutInflater = LayoutInflater
                    .from(MainActivity.this);
            View promptView = layoutInflater.inflate(R.layout.dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(promptView);
            builder.setCancelable(true);

            TextView txtTitle = (TextView) promptView.findViewById(R.id.title);
            TextView camera = (TextView) promptView.findViewById(R.id.camera);
            TextView gallery = (TextView) promptView.findViewById(R.id.gallery);
            TextView cancel = (TextView) promptView.findViewById(R.id.cancel);

            final AlertDialog alert = builder.create();
            alert.show();

            camera.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        cameraIntent();
                        alert.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            gallery.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        galleryIntent();
                        alert.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Open Gallery
     **/
    private void galleryIntent() {
        try {
            Intent intent = new Intent(MainActivity.this, ImageFolder.class);
            startActivityForResult(intent, SELECT_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open Camera
     **/
    private void cameraIntent() {
        try {
            /** open System Camera **/
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            /** Check requestCode of Camera and Gallery **/
            if (requestCode == SELECT_FILE) {
                /** Gallery  select Image path**/
                String path = null;
                try {
                    path = data.getExtras().getString("image_path");
                    actualImage = new File(path);
                    txtSize.setText(String.format("Size : %s", getReadableFileSize(actualImage.length())));

                    /** Image Load Using Glide **/
                    Glide.with(MainActivity.this)
                            .load(path)
                            .into(ivImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_CAMERA) {
                /** Open camera **/
                onCaptureImageResult(data);
            }

        }
    }

    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * Open camera
     **/
    private void onCaptureImageResult(Intent data) {
        ByteArrayOutputStream bytes = null;
        String path = null;
        try {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);


        } catch (Exception e) {
            e.printStackTrace();
        }
        File imagesFolder = null;
        File destination = null;

        try {
            /** Create Folder to Save Image **/
            imagesFolder = new File(Environment.getExternalStorageDirectory() + "/MiiImages/");
            destination = new File(imagesFolder.getPath() + File.separator + "img_" + System.currentTimeMillis() + ".jpg");

            /** Folder Show in immediate Mobile Gallery **/
            MainActivity.this.sendBroadcast(
                    new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(destination))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!imagesFolder.exists()) {
            if (!imagesFolder.mkdir()) {
                Log.d("CameraSample", "failed to create directory");
            }
        }


        if (!destination.exists()) {
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.flush();
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**  Capture Image Path **/
        try {
            path = destination.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            actualImage = new File(path);
            /** Display Image using Glide **/
            Glide.with(MainActivity.this)
                    .load(path)
                    .into(ivImage);
            // ivImage.setImageBitmap(thumbnail);

            txtSize.setText(String.format("Size : %s", getReadableFileSize(actualImage.length())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
