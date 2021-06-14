package com.operatorsapp.dialogs;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.StandardResponse;
import com.example.common.request.UpdateServiceCallDescriptionRequest;
import com.operators.reportrejectinfra.SimpleCallback;
import com.operatorsapp.R;
import com.operatorsapp.adapters.TaskPhotosAdapter;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ImageHelper;
import com.operatorsapp.utils.SimpleRequests;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;


public class ServiceReportDialog extends DialogFragment {
    private final static String KEY_NOTIFICATION_ID = "KEY_NOTIFICATION_ID";
    private static final int REQUEST_FOR_ACTIVITY_GALLERY = 1234;
    private static final int REQUEST_FOR_ACTIVITY_CAMERA = 2345;
    private static final int KEY_REQUEST_FOR_PERMISSIONS = 45678;

    private static final String[] PERMISSIONS = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static final String TAG = ServiceReportDialog.class.getSimpleName();
    private final ArrayList<File> mFilesForUpload = new ArrayList<>();
    private String mNotificationID;
    private File outputFile;
    private Uri photoURI;
    private int mApiCallsBeforeExitCounter = 0;
    private EditText reportEt;
    private RecyclerView photosRv;
    private ServiceReportDialogListener mListener;


    public static ServiceReportDialog newInstance(String notificationID) {
        ServiceReportDialog fragment = new ServiceReportDialog();
        Bundle args = new Bundle();
        args.putString(KEY_NOTIFICATION_ID, notificationID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (getParentFragment() instanceof ServiceReportDialogListener) {
            mListener = (ServiceReportDialogListener) getParentFragment();
        } else {
            Log.d(TAG, "onAttach: must override ServiceReportDialogListener");
        }
        super.onAttach(context);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        mListener.onComplete();
        super.onDismiss(dialog);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNotificationID = getArguments().getString(KEY_NOTIFICATION_ID);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.service_report_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reportEt = view.findViewById(R.id.FSRD_service_report_ET);
        photosRv = view.findViewById(R.id.FSRD_show_photos_rv);
        initListeners(view);
    }

    private void initListeners(View view) {
        view.findViewById(R.id.FSRD_attach_files_IV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    showImageDialog();
                } else {
                    if (getActivity() != null) {
                        requestPermissions(PERMISSIONS, KEY_REQUEST_FOR_PERMISSIONS);
                    }
                }
            }
        });

        view.findViewById(R.id.FSRD_done_BUT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportEt.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), getResources().getString(R.string.details_are_missing), Toast.LENGTH_SHORT).show();
                } else {
                    ProgressDialogManager.show(getActivity());
                    sendServiceReport(reportEt.getText().toString());
                    uploadFilesForServiceCall(Integer.parseInt(mNotificationID));
                }
            }
        });

        view.findViewById(R.id.FSRD_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private boolean checkPermission() {
        return getContext() != null
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {

        if (requestCode == KEY_REQUEST_FOR_PERMISSIONS) {
            if (grantResults.length > 0 && checkIfHaveAllPermissions(grantResults)) {
                showImageDialog();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.uploading_images_is_not_possible_without_these_permissions), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkIfHaveAllPermissions(int[] grantResults) {
        for (int requestPermission : grantResults) {
            if (requestPermission != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    private void showImageDialog() {

        if (getContext() == null) {
            return;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(R.string.show_camera_dialog_title);
        dialog.setPositiveButton(R.string.show_camera_dialog_gallery, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_FOR_ACTIVITY_GALLERY);
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton(R.string.show_camera_dialog_take_a_picture, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File root;
                if (VERSION.SDK_INT >= VERSION_CODES.Q) {
                    root = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                } else {
                    root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                }
                File dir = new File(root, "matics");
                outputFile = new File(dir, PersistenceManager.getInstance().getSiteName() + "_" + Calendar.getInstance().getTimeInMillis() + ".jpg");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoURI = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", outputFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_FOR_ACTIVITY_CAMERA);
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_FOR_ACTIVITY_GALLERY || requestCode == REQUEST_FOR_ACTIVITY_CAMERA) {
                if (requestCode == REQUEST_FOR_ACTIVITY_GALLERY) {
                    if (handleForRequestFromGallery(data)) return;
                }
                setImageForUpload(outputFile);
            }
        }
    }

    public void setImageForUpload(File file) {
        mFilesForUpload.add(new File(file, ""));
        setPhotoRecycler();
    }

    private void setPhotoRecycler() {
        photosRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        photosRv.setAdapter(new TaskPhotosAdapter(getChildFragmentManager(), mFilesForUpload, null));
    }


    private void uploadFilesForServiceCall(int notificationID) {

        PersistenceManager persistenceManager = PersistenceManager.getInstance();
        for (File file : mFilesForUpload) {
            mApiCallsBeforeExitCounter++;
            String fileExt = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            String imgBase64 = Base64.encodeToString(ImageHelper.getByteArrFromUri(Uri.fromFile(file), file.getAbsolutePath()), Base64.DEFAULT);

            SimpleRequests.postFilesForServiceCall(notificationID, file.getName(), fileExt, imgBase64, persistenceManager.getSiteUrl(), new SimpleCallback() {
                @Override
                public void onRequestSuccess(StandardResponse response) {
                    mApiCallsBeforeExitCounter--;

                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), getActivity().getString(R.string.upload_completed), Toast.LENGTH_SHORT).show();
                    }

                    completeDialog();
                }

                @Override
                public void onRequestFailed(StandardResponse reason) {
                    mApiCallsBeforeExitCounter--;
                    if (getActivity() != null) {
                        String msg = getActivity().getString(R.string.upload_failed);
                        if (reason != null && !reason.getError().getErrorDesc().isEmpty()) {
                            msg = reason.getError().getErrorDesc();
                        }
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                    completeDialog();
                }
            }, NetworkManager.getInstance(), persistenceManager.getTotalRetries(), persistenceManager.getRequestTimeout());
        }
    }

    private boolean handleForRequestFromGallery(Intent data) {

        if (getContext() == null) {
            return true;
        }

        if (VERSION.SDK_INT >= VERSION_CODES.Q) {
            photoURI = data.getData();
            android.database.Cursor cursor = getContext().getContentResolver().query(photoURI, null, null, null, null);
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            String name = (cursor.getString(nameIndex));

            outputFile = new File(getContext().getFilesDir(), name);
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(photoURI);
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                int read;
                int maxBufferSize = 1024 * 1024;
                int bytesAvailable = inputStream.available();
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);

                final byte[] buffers = new byte[bufferSize];
                while ((read = inputStream.read(buffers)) != -1) {
                    outputStream.write(buffers,
                            0,
                            read);
                }

                inputStream.close();
                outputStream.close();
                Log.e("File Path", "Path " + outputFile.getAbsolutePath());

            } catch (Exception e) {
                if (e.getMessage() != null) {
                    Log.e("Exception", e.getMessage());
                }
                return true;
            }

            cursor.close();
        } else {

            String filePath;
            photoURI = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContext().getContentResolver().query(photoURI, filePathColumn, null, null, null);
            if (cursor == null)
                return true;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
            outputFile = new File(filePath);
        }
        return false;
    }

    private void sendServiceReport(String textServiceReport) {
        PersistenceManager persistenceManager = PersistenceManager.getInstance();

        mApiCallsBeforeExitCounter++;

        SimpleRequests.updateServiceCallDescription(persistenceManager.getSiteUrl(),
                new UpdateServiceCallDescriptionRequest(persistenceManager.getSessionId(),
                        mNotificationID, textServiceReport), new SimpleCallback() {
                    @Override
                    public void onRequestSuccess(StandardResponse response) {
                        mApiCallsBeforeExitCounter--;
                        completeDialog();
                    }

                    @Override
                    public void onRequestFailed(StandardResponse reason) {
                        mApiCallsBeforeExitCounter--;
                        Toast.makeText(getContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                        completeDialog();
                    }
                }
                , NetworkManager.getInstance(), persistenceManager.getTotalRetries(), persistenceManager.getRequestTimeout());
    }

    private void completeDialog() {
        if (mApiCallsBeforeExitCounter == 0) {
            ProgressDialogManager.dismiss();
            dismiss();
        }
    }

    public interface ServiceReportDialogListener {
        void onComplete();
    }
}