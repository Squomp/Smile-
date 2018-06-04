package pro200.smile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.facebook.Profile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pro200.smile.service.LiveSmileService;

import static android.app.Activity.RESULT_OK;


public class SmileFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private View mContent;
    private Button takeSmileButton;
    private File recentImageFile;
    private ImageView mImageView;
    private VideoView mVideoView;
    private Profile profile = Profile.getCurrentProfile();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_smile, container, false);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            dispatchTakePictureIntent();
//            dispatchTakeVideoIntent();
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }


        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            Bundle args = getArguments();

            // initialize views
            profile = Profile.getCurrentProfile();
            mContent = view.findViewById(R.id.smile_content);
            takeSmileButton = view.findViewById(R.id.takeSmileButton);
            mVideoView = view.findViewById(R.id.mVideoView);
            mImageView = view.findViewById(R.id.mImageView);

            takeSmileButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dispatchTakePictureIntent();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    Log.d("Tag?", "Permission granted");
                } else {
                    // Permission denied
                    Log.d("Tag?", "Permission denied");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(this.getContext().getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
        if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private File getOutputMediaFile() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        setRecentImageFile(mediaFile);


        return mediaFile;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            //IMAGE WAS TAKEN

            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

                Uri imageAsURI = android.net.Uri.parse(getRecentImageFile().toURI().toString());
                Bitmap imageBitmap = null;
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageAsURI);
//                    imageBitmap = changeImageOrientation(imageBitmap, 90);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("BITMAPS", "Could not retrieve most Recent Image");
                }
                Log.e("STATE", getRecentImageFile().toString());
                mVideoView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
//                ExifInterface exif = new ExifInterface(filename);
                try {
                    ExifInterface exif = new ExifInterface(getRecentImageFile().getPath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    switch (orientation) {
                        case 1:
                            break;
                        case 3:
                        case 4:
                            imageBitmap = changeImageOrientation(imageBitmap,180);
                            break;
                        case 5:
                        case 6:
                            imageBitmap = changeImageOrientation(imageBitmap,90);
                            break;
                        case 7:
                        case 8:
                            imageBitmap = changeImageOrientation(imageBitmap,270);
                            break;
                        default:
                            break;
                    }
//
                } catch (IOException e) {
                    Log.d("STATE", "EXIF");
                    e.printStackTrace();
                }

                mImageView.setImageBitmap(imageBitmap);
                LiveSmileService ls = new LiveSmileService(this.getContext());
                ls.AddSmile(profile.getId(), imageBitmap);
//                SmileList retrievedList = ls.GetUserSmiles(profile.getId());
//                if (retrievedList.getSmiles().size() > 0) {
//                    Smile newSmile = retrievedList.getSmiles().get(retrievedList.getSmiles().size() - 1);
//                    if (newSmile instanceof PhotoSmile) {
//                        mImageView.setImageBitmap(((PhotoSmile) newSmile).getImage());
//                    }
//                }


            }

        }
    }

    private Bitmap changeImageOrientation(Bitmap imageBitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(),
                matrix, true);
    }

    public static Fragment newInstance() {
        Fragment frag = new SmileFragment();
        return frag;
    }

    public File getRecentImageFile() {
        return recentImageFile;
    }

    public void setRecentImageFile(File recentImageFile) {
        this.recentImageFile = recentImageFile;
    }
}