package test.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import test.persistence.Storage;

/**
 * Created by otf on 7/27/15.
 */
public class CameraPictureFragment extends Fragment {
    private static final int CAMERA_REQUEST = 101;
    CameraPictureFragmentCallback parentListener;

    public CameraPictureFragment() {}

    public static CameraPictureFragment newInstance(CameraPictureFragmentCallback parentListener) {
        CameraPictureFragment cameraPictureFragment = new CameraPictureFragment();
        cameraPictureFragment.setParentListener(parentListener);
        return cameraPictureFragment;
    }

    private void setParentListener(CameraPictureFragmentCallback parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setRetainInstance(true);
      //  Log.i("Camera Button", "start");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                AsyncTask<Bitmap, Void, String> storePictureAndSendId = new AsyncTask<Bitmap, Void, String>() {
                    @Override
                    protected String doInBackground(Bitmap... bitmaps) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmaps[0].compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bytes = stream.toByteArray();
                        String pictureId = UUID.randomUUID().toString();
                        Storage.storePicture(getActivity(), pictureId, bytes);
                        return pictureId;
                    }

                    @Override
                    public void onPostExecute(String picId) {
                        parentListener.returnImageId(picId);
                    }

                };
                storePictureAndSendId.execute((Bitmap) data.getExtras().get("data"));
            }
        }
    }

    public interface CameraPictureFragmentCallback {
        public void returnImageId(String imageId);
    }
}
