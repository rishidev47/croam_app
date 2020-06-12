package com.example.croam;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Objects;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportFragment extends Fragment {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;
    EditText description;
    Button video;

//    VideoView videoView;
    Uri videoUri;
    ImageView reportImage;
    private String currentPhotoPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_report, container, false);

        video = view.findViewById(R.id.video);
        reportImage = view.findViewById(R.id.reportImage);

        final Button captureButton = view.findViewById(R.id.capture);
        description = view.findViewById(R.id.description);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakeVideoIntent();
            }
        });

        Button report = view.findViewById(R.id.report);

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(description.getText().toString().equals("")){
                    description.setError("Please enter description");
                }
                if (currentPhotoPath != null) {
                    if(!description.getText().toString().equals(""))
                    uploadFile(Uri.parse(currentPhotoPath), REQUEST_TAKE_PHOTO);

                } else if (videoUri != null) {
                    if(!description.getText().toString().equals(""))
                    uploadFile(videoUri, REQUEST_VIDEO_CAPTURE);

                } else {

                    captureButton.setError("Please upload an image or video!");
                    video.setError("Please upload an image or video!");
                }
            }
        });


        return view;
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(android.provider.MediaStore.EXTRA_VIDEO_QUALITY, 0);
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20);
//        takeVideoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,20);
        if (takeVideoIntent.resolveActivity(
                Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Objects.requireNonNull(getActivity()).getExternalFilesDir(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(
                Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getContext()),
                        "com.example.croam.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = intent.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(videoUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(picturePath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            File file=persistImage(bitmap,"a");
            Picasso.get().load(file).resize(0,1024).centerCrop().placeholder(R.drawable.report).into(reportImage);
//            reportImage.setImageBitmap(bitmap);

        }
         else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            if (currentPhotoPath != null) {
                File file = new File(Uri.parse(currentPhotoPath).getPath());
//                uploadFile(Uri.parse(currentPhotoPath), REQUEST_TAKE_PHOTO);
                Picasso.get().load(file).resize(0,1024).centerCrop().placeholder(R.drawable.report).into(reportImage);
//                reportImage.setImageURI(Uri.parse(currentPhotoPath));

            }

        }
    }

    private void uploadFile(Uri fileUri, int code) {

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro
        // /afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new File(fileUri.getPath());

        Log.e("File", file.toString());

        RequestBody requestFile = null;

        // create RequestBody instance from file
        if (code == REQUEST_VIDEO_CAPTURE) {
            requestFile =
                    RequestBody.create(
                            MediaType.parse(getActivity().getContentResolver().getType(fileUri)),
                            file
                    );
        }
        if (code == REQUEST_TAKE_PHOTO) {
            requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        }

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                null;
        if (requestFile != null) {
            body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
        }

        // add another part within the multipart request
        String descriptionString = description.getText().toString();
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);


        // finally, execute the request
        Call<ResponseBody> call = MyApi.Companion.invoke().upload(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                    Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    private  File persistImage(Bitmap bitmap, String name) {
        File filesDir = getContext().getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            return imageFile;
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            return null;
        }
    }
}
