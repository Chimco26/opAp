package com.operatorsapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * David Vardi
 */
public class DownloadHelper {

    private static final String TAG = DownloadHelper.class.getSimpleName();

    private ProgressDialog mDialog;

    private Context mContext;

    private File mFile;

    private DownloadFileListener mListener;

    private DownloadFileFromURL mDownloadFileFromURL;

    public DownloadHelper(Context context, DownloadFileListener listener) {

        this.mContext = context;

        this.mListener = listener;

    }

    public void downloadFileFromUrl(String url) {

        mDownloadFileFromURL = new DownloadFileFromURL();
        mDownloadFileFromURL.execute(url);

    }

    public boolean cancelDownloadFileFromUrl() {

        if (mDownloadFileFromURL != null) {

            return mDownloadFileFromURL.cancel(true);

        }else {

            return false;
        }
    }

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

            int count;

            File folder;

            try {

                URL url = new URL(params[0]);

                if (url.toString() != null) {

                    folder = new File(Environment.getExternalStorageDirectory().toString(), String.valueOf(url));

                    if (!folder.exists()) {

                        folder.mkdirs();
                    }
                } else {

                    folder = mContext.getCacheDir();
                }

                mFile = new File(folder, url.getPath().substring(url.getPath().lastIndexOf("/")));

                URLConnection connection = url.openConnection();

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
        }

        @Override
        protected void onPostExecute(String url) {

            dismissDialog();

            mListener.onPostExecute(mFile);

        }

    }

    private void dismissDialog() {

//        mDialog.dismiss();

    }

    private void showDialog() {

//        mDialog = new ProgressDialog(mContext);

//        mDialog.setMessage(mContext.getString(R.string.download_file));
//
//        mDialog.setMax(100);
//
//        mDialog.setCancelable(true);
//
//        mDialog.show();

    }

    public interface DownloadFileListener {

        void onPostExecute(File file);

        void onLoadFileError();

        void onCancel();
    }

}