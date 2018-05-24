package pro200.smile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pro200.smile.service.LiveSmileService;

import static android.app.Activity.RESULT_OK;


public class SmileFragment extends Fragment {

    private View mContent;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private File recentImageFile;
    private ImageView mImageView;

    public File getRecentImageFile() {
        return recentImageFile;
    }

    public void setRecentImageFile(File recentImageFile) {
        this.recentImageFile = recentImageFile;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
        if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    private Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    private File getOutputMediaFile(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        if (! mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
        setRecentImageFile(mediaFile);


        return mediaFile;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Uri imageAsURI = android.net.Uri.parse(getRecentImageFile().toURI().toString());
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageAsURI);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("BITMAPS", "Could not retrieve most Recent Image");
            }

            mImageView.setImageBitmap(imageBitmap);
            //LiveSmileService ls =  new LiveSmileService(this.getContext());

        }
    }


    public static Fragment newInstance() {
        Fragment frag = new SmileFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_smile, container, false);
        dispatchTakePictureIntent();
        mImageView = (ImageView)v.findViewById(R.id.mImageView);
        // Inflate the layout for this fragment
        return v;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            Bundle args = getArguments();

            // initialize views
            mContent = view.findViewById(R.id.smile_content);
        }
    }
}
