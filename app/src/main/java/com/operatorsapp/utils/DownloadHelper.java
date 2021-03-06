package com.operatorsapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

import static android.os.Environment.DIRECTORY_DCIM;

/**
 * David Vardi
 */
public class DownloadHelper {

    private static final String TAG = DownloadHelper.class.getSimpleName();
    private final WeakReference<Context> contextWeakReference;

    private File mFile;

    private DownloadFileListener mListener;

    private DownloadFileFromURL mDownloadFileFromURL;

    public DownloadHelper(Context context, DownloadFileListener listener) {

        this.contextWeakReference = new WeakReference<>(context);

        this.mListener = listener;

    }

    public void downloadFileFromUrl(String url) {

        mDownloadFileFromURL = new DownloadFileFromURL();
        mDownloadFileFromURL.execute(url.replace(" ", "%20"));

    }

    public void cancelDownloadFileFromUrl() {

        if (mDownloadFileFromURL != null) {
            mDownloadFileFromURL.cancel(true);
        }
    }

    @SuppressLint("StaticFieldLeak")
// work but can be problematic because there are non static fields in async class
    private class DownloadFileFromURL extends AsyncTask<String, String, String> {

        protected void onPreExecute() {

            super.onPreExecute();

//            showDialog();
        }

        @Override
        protected void onCancelled(String s) {

            mListener.onCancel();

            super.onCancelled();
        }

        @Override
        protected String doInBackground(String... params) {

            Context context = contextWeakReference.get();
            if (context == null){return null;}

            int count;

            File folder;

            try {

                URL url = new URL(params[0]);

//                if (url.toString() != null) {
//
//                    folder = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).toString(), String.valueOf(url));
//
//                    if (!folder.exists()) {
//
//                        //noinspection ResultOfMethodCallIgnored
//                        folder.mkdirs();
//                    }
//                } else {
//
//                    folder = context.getCacheDir();
//                }

                mFile = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM), url.getPath().substring(url.getPath().lastIndexOf("/")));

                URLConnection connection = url.openConnection();

                connection.setConnectTimeout(10000);

                connection.connect();

                int lenghtOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(), 1024);

                OutputStream output = new FileOutputStream(mFile);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {

                    total += count;

                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    output.write(data, 0, count);
                }

                output.flush();

                output.close();

                input.close();

            } catch (Exception e) {

                Log.e(TAG, "Error: " + e.getMessage());

                mListener.onLoadFileError();

            }

            return null;
        }


        protected void onProgressUpdate(String... progress) {

//            mDialog.setProgress(Integer.parseInt(progress[0]));
            mListener.onLoadFileProgress();
        }

        @Override
        protected void onPostExecute(String url) {

            dismissDialog();

            mListener.onPostExecute(mFile);

        }

    }

    private void dismissDialog() {

    }


    public interface DownloadFileListener {

        void onPostExecute(File file);

        void onLoadFileError();

        void onCancel();

        void onLoadFileProgress();
    }

}