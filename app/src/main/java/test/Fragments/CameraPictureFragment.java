package test.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
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
        Log.i("Camera Button", "start");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            String pictureId = UUID.randomUUID().toString();
            Storage.storePicture(getActivity(), pictureId, bytes);
            parentListener.returnImageId(pictureId);
        }
    }

    public interface CameraPictureFragmentCallback {
        public void returnImageId(String imageId);
    }
}
