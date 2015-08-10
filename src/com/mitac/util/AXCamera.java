/**
 * @Title AXCamera.java
 * @Package com.feong.util.android
 * @author Alex feong@live.com
 * @date 2013-11-20 12:42:28pm
 * @version V1.4
 */
package com.mitac.util;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.SurfaceHolder;
//import bom.mitac.bist.burnin.module.TestClass;

import java.io.File;


/**
 * @author Alex feong@live.com
 * @version V1.6
 *          2014-1-27 13:22:30
 *          Add "setErrorCallback" in surfaceCreated
 *          V1.7
 *          2014-2-11 9:33:38
 *          Add "isError" to prevent the camera do anything when error happens.
 * @ClassName AXCamera
 * @date 2013-11-20 12:42:28pm
 */
public class AXCamera {
    private static AXCamera instance;

    private Camera camera;
    private Parameters mParameters;

    private boolean isError; // Added on 2014-2-11 9:31:25

    private boolean isOpening;
    private boolean isClosing;
    private boolean isShotting;

    private boolean isPreviewing;
    private boolean isOpened;
    private File path;
    private File file;


    private AXCamera(File path) {
        this.path = path;
    }

    public static AXCamera getInstance(File path) {
        if (instance == null)
            instance = new AXCamera(path);
        return instance;
    }

    private boolean isActing() {
        return (isOpening || isShotting || isClosing);
    }

    private boolean setPreviewDisplay(SurfaceHolder surfaceHolder) {
        if (camera == null || surfaceHolder == null)
            return false;

        try {
            camera.setPreviewDisplay(surfaceHolder);
            return true;
        } catch (Exception e) {
            Log.d("feong", "setPreviewDisplay err");
            e.printStackTrace();
        }
        return false;
    }

    private boolean openCamera(int facing) {
        if (camera == null) {
            try {
                camera = Camera.open(facing);
                camera.setErrorCallback(new Camera.ErrorCallback() {
                    @Override
                    public void onError(int i, Camera camera) {
                        Log.d("feong", "camera error");
                        //TestClass.saveLog("Camera error: code="+i+" camera"+camera);
                        isError = true;
                        if (camera != null)
                            camera.release();
                        camera = null;
                    }
                });
                Log.d("feong", "open camera finish");
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("feong", "open camera error");
                if (camera != null)
                    camera.release();
                camera = null;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean open(int facing) {
        if (isActing() || isOpened)
            return false;

        if (facing != 0 && facing != 1)
            return false;

        isOpening = true;
        Log.d("feong", "open camera");
        if (openCamera(facing)) {
            isOpening = false;
            isOpened = true;
            isError = false;
            return true;
        } else {
            isOpening = false;
            isOpened = false;
            return false;
        }
    }

    public boolean preview(SurfaceHolder surfaceHolder, int orientation) {
        if (isActing() || camera == null)
            return false;

        if (isPreviewing)
            camera.stopPreview();

        isPreviewing = false;
        if (setPreviewDisplay(surfaceHolder)) {
            try {
                camera.startPreview();
                camera.setDisplayOrientation(orientation);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            isPreviewing = true;
            return true;
        } else {
            return false;
        }
    }

    public void close() {
        if (isActing() || camera == null)
            return;

        isClosing = true;

        try {
//            camera.setPreviewCallback(null);
            Log.d("feong", "stopPreview");
            camera.stopPreview();
            camera.setPreviewDisplay(null);
        } catch (Exception e) {
            Log.d("feong", "stopPreview err");
            isError = true;
        }
        Log.d("feong", "release b");
        if (camera != null)
            camera.release();
        Log.d("feong", "release f");
        camera = null;
        isOpened = false;
        isClosing = false;
    }

    public void shot() {
        if (isActing() || isError || camera == null || path == null)
            return;
        isShotting = true;
        file = null;
        try {
            mParameters = camera.getParameters();
            mParameters.setWhiteBalance(Parameters.WHITE_BALANCE_AUTO);
            camera.setParameters(mParameters);
            
            camera.autoFocus(null);
            
//            camera.takePicture(null, null, new Camera.PictureCallback() {
//                @Override
//                public void onPictureTaken(byte[] bytes, Camera camera) {
//                    file = new AXMediaFile().genMedia(path, AXMediaFile.MediaType.IMAGE, bytes);
//                    camera.stopPreview();
//                    camera.startPreview();
//                }
//            });
            
       } catch (Exception e) {
            e.printStackTrace();
            isError = true;
            file = null;
        }
        isShotting = false;
    }

    public boolean isOpened() {
        return isOpened;
    }

    /**
     * Return the saved picture
     *
     * @return Return null if there is error during taking a shot
     */
    public File getFile() {
        return file;
    }

}
