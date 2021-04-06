package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.StandardResponse;
import com.example.common.request.RecipeUpdateRequest;
import com.example.common.request.RecipeValue;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.operators.reportrejectinfra.SimpleCallback;
import com.operators.reportrejectnetworkbridge.server.request.PostUpdateNotesForJobRequest;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.BaseSplits;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.Channel;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.ChannelSplits;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeResponse;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.adapters.ChannelItemsAdapters;
import com.operatorsapp.adapters.No0ChannelAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.callback.PostUpdateNotesForJobCallback;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.SimpleRequests;
import com.operatorsapp.utils.ViewTagsHelper;
import com.operatorsapp.view.SingleLineKeyboard;

import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = RecipeFragment.class.getSimpleName();
    private static final String RECIPE_RESPONS_KEY = "RECIPE_RESPONS_KEY";
    private static final String LOG_TAG = RecipeFragment.class.getSimpleName();

    private OnRecipeFragmentListener mListener;
    private RecipeResponse mRecipeResponse;
    private View mchannel0BotomView;
    private View mchannel1_99BotomView;
    private View mchannel100BotomView;
    private View mLayoutChannel0;
    private ImageView mLayoutChannel0Image;
    private TextView mLayoutChannel0ItemTitleTv;
    private TextView mLayoutChannel0ItemSubTitleTv;
    private RecyclerView mLayoutChannel0ItemSplitRV;
    private View mMainView;
    private View mLayoutChannel100;
    private TextView mLayoutChannel100Title;
    private RecyclerView mLayoutChannel100Rv;
    private LinearLayout mlayoutChannel1_99;
    private View mLayoutChannel0NoDataImage;
    private View mLayoutChannel100NoDataImage;
    private View mChannel1_99NoDataImage;
    private View mLayoutChannel0MainLayout;
    private View mChannel1_99NoDataTv;
    private View mLayoutChannel100NoDataTv;
    private View mLayoutChannel0NoDataTv;
    private View mlayoutChannel1_99NoDataLayout;
    private View mlayoutChannel1_99MainLy;
    private View mLayoutChannel100RvLy;
    private ImageView mNoteIv;
    private TextView mNoteTv;
    private LinearLayout mNoteLy;
    private List<BaseSplits> mChannel0BaseSplits = new ArrayList<>();
    private List<ChannelSplits> mChannels100BaseChannelSplits = new ArrayList<>();
    private ChannelItemsAdapters mChannelItemsAdapters;
    private SingleLineKeyboard mKeyBoard;
    private LinearLayout mKeyBoardLayout;
    private View mSaveBtn;
    private ProgressBar mProgressBar;
    private boolean isUpdating;
    private LinearLayoutManager mChannel0ItemsAdaptersLyManager;
    private boolean mIsEditMode;
    private List<Channel> mChannels1_99BaseChannelSplits = new ArrayList<>();
    private Button mChan0GalleryBtn;

    public static RecipeFragment newInstance(RecipeResponse recipeResponse) {
        RecipeFragment recipeFragment = new RecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_RESPONS_KEY, recipeResponse);
        recipeFragment.setArguments(bundle);
        return recipeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getParcelable(RECIPE_RESPONS_KEY) != null) {
                mRecipeResponse = getArguments().getParcelable(RECIPE_RESPONS_KEY);
            }
        }

        // Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), "Recipe screen");

    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof RecipeFragment.OnRecipeFragmentListener) {
            mListener = (RecipeFragment.OnRecipeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeFragmentListener");
        }
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.frament_recipe, container, false);

        return mMainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVars(view);
        initView();
        initListener(view);
    }

    private void initVars(View view) {

        mKeyBoardLayout = view.findViewById(R.id.FR_keyboard);
        mchannel0BotomView = view.findViewById(R.id.FR_channel_0_btn_bottom);

        mchannel1_99BotomView = view.findViewById(R.id.FR_channel_1_99_btn_bottom);

        mchannel100BotomView = view.findViewById(R.id.FR_channel_100_btn_bottom);

        mNoteIv = view.findViewById(R.id.FR_open_edit_text_btn);
        mNoteTv = view.findViewById(R.id.FR_notes_tv);
        mNoteLy = view.findViewById(R.id.FR_note_ly);

        mProgressBar = view.findViewById(R.id.FR_progress_dialog);

        initChannel0Vars(view);

        initChannel1_99Vars(view);

        initChannel100Vars(view);

    }

    private void initChannel1_99Vars(View view) {

        mlayoutChannel1_99 = view.findViewById(R.id.FR_channel_1_99_ly);

        mlayoutChannel1_99MainLy = view.findViewById(R.id.FR_channel_1_99_main_ly);

        mlayoutChannel1_99NoDataLayout = view.findViewById(R.id.FR_channel_1_99_no_data_ly);

        mChannel1_99NoDataImage = view.findViewById(R.id.FR_C1_99_no_data_img);

        mChannel1_99NoDataTv = view.findViewById(R.id.FR_C1_99_no_data_tv);
    }

    private void initChannel100Vars(View view) {

        mLayoutChannel100 = view.findViewById(R.id.FR_channel_100_ly);

        mLayoutChannel100RvLy = view.findViewById(R.id.channel_100_rv_ly);

        mLayoutChannel100Title = mLayoutChannel100.findViewById(R.id.C100_tv);

        mLayoutChannel100Rv = mLayoutChannel100.findViewById(R.id.channel_100_rv);

        mLayoutChannel100NoDataImage = mLayoutChannel100.findViewById(R.id.C100_no_data_img);

        mLayoutChannel100NoDataTv = mLayoutChannel100.findViewById(R.id.C100_no_data_tv);
    }

    private void initChannel0Vars(View view) {

        mLayoutChannel0 = view.findViewById(R.id.FR_channel_0_ly);

        mLayoutChannel0MainLayout = view.findViewById(R.id.C0L_layout);

        mLayoutChannel0Image = mLayoutChannel0.findViewById(R.id.C0L_img);

        mChan0GalleryBtn = mLayoutChannel0.findViewById(R.id.C0L_gallery_btn);

        View mLayoutChannel0Item = mLayoutChannel0.findViewById(R.id.C0L_item);

        mLayoutChannel0NoDataImage = mLayoutChannel0.findViewById(R.id.C0L_no_data_img);

        mLayoutChannel0NoDataTv = mLayoutChannel0.findViewById(R.id.C0L_no_data_tv);

        mLayoutChannel0ItemTitleTv = mLayoutChannel0Item.findViewById(R.id.IP_title);

        mLayoutChannel0ItemSubTitleTv = mLayoutChannel0Item.findViewById(R.id.IP_sub_title);

        mLayoutChannel0ItemSplitRV = mLayoutChannel0Item.findViewById(R.id.IP_split_rv);

    }

    private void initView() {

        if (getActivity() != null) {
            initChannel0View();

            initChannel100View();

            initChanne1_1_99_View();

            if (mRecipeResponse != null && mRecipeResponse.getNote() != null && mRecipeResponse.getNote().length() > 0) {
                mNoteTv.setText(mRecipeResponse.getNote());
            } else {
                mNoteTv.setText("");
            }

//                    mProgressBar.setVisibility(View.VISIBLE);
//                    if (getActivity() != null && getActivity().getWindow() != null) {
//                        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                    }
//                    recipeChannel0.getChannelSplits().get(0).setBaseSplits(mChannel0BaseSplits);
//                    postUpdateValues();

        }
    }

    private void initChannel100View() {

        if (mRecipeResponse != null && mRecipeResponse.getRecipe() != null
                && mRecipeResponse.getRecipe().getChannels() != null && mRecipeResponse.getRecipe().getChannels().size() > 0 && mRecipeResponse.getRecipe().getChannels().get(0) != null &&
                mRecipeResponse.getRecipe().getChannels().get(mRecipeResponse.getRecipe().getChannels().size() - 1).getChannelNumber() == 100) {

            mLayoutChannel100NoDataImage.setVisibility(View.GONE);
            mLayoutChannel100NoDataTv.setVisibility(View.GONE);
            mLayoutChannel100RvLy.setVisibility(View.VISIBLE);

            mLayoutChannel100Title.setText(
                    OperatorApplication.isEnglishLang() ?
                            mRecipeResponse.getRecipe().getChannels().get(mRecipeResponse.getRecipe().getChannels().size() - 1).getChannelEname()
                            : mRecipeResponse.getRecipe().getChannels().get(mRecipeResponse.getRecipe().getChannels().size() - 1).getChannelLname());

            No0ChannelAdapter mNo0ChannelAdapter = new No0ChannelAdapter(getActivity(), new No0ChannelAdapter.Channel100AdapterListener() {
                @Override
                public void onImageProductClick(String fileUrl, String s) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(fileUrl);
                    mListener.onImageProductClick(arrayList, s);
                }

                @Override
                public void onEditMode(BaseSplits item) {

                    openEditValue(item);
                }
            },
                    (ArrayList<ChannelSplits>) mRecipeResponse.getRecipe().getChannels()
                            .get(mRecipeResponse.getRecipe().getChannels().size() - 1).getChannelSplits()
                    , No0ChannelAdapter.TYPE_CHANNEL_100);

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

            mLayoutChannel100Rv.setLayoutManager(layoutManager);

            mLayoutChannel100Rv.setAdapter(mNo0ChannelAdapter);

        } else {

            mLayoutChannel100NoDataImage.setVisibility(View.VISIBLE);
            mLayoutChannel100NoDataTv.setVisibility(View.VISIBLE);
            mLayoutChannel100RvLy.setVisibility(View.GONE);
        }
    }

    private void initChannel0View() {


        if (mRecipeResponse != null && mRecipeResponse.getRecipe() != null && mRecipeResponse.getRecipe().getChannels() != null
                && mRecipeResponse.getRecipe().getChannels().size() > 0 && mRecipeResponse.getRecipe().getChannels().get(0) != null) {

            mChannel0BaseSplits.clear();
            mChannel0BaseSplits.addAll(mRecipeResponse.getRecipe().getChannels().get(0).getChannelSplits().get(0).getBaseSplits());

            mLayoutChannel0MainLayout.setVisibility(View.VISIBLE);
            mLayoutChannel0NoDataImage.setVisibility(View.GONE);
            mLayoutChannel0NoDataTv.setVisibility(View.GONE);

            final Channel recipeChannel0 = mRecipeResponse.getRecipe().getChannels().get(0);

            if (mRecipeResponse.getProductData() != null && mRecipeResponse.getProductData().getFileUrl() != null &&
                    mRecipeResponse.getProductData().getFileUrl().size() > 0) {
                ImageLoader.getInstance().displayImage(mRecipeResponse.getProductData().getFileUrl().get(0), mLayoutChannel0Image);
            } else if (mRecipeResponse.getProductFiles() != null && mRecipeResponse.getProductFiles().size() > 0) {
                ImageLoader.getInstance().displayImage(mRecipeResponse.getProductFiles().get(0), mLayoutChannel0Image);
            }

            if (getActivity() != null && getActivity().getResources() != null) {
                mLayoutChannel0ItemTitleTv.setText(getActivity().getResources().getString(R.string.production_parameters));
            }
            if (recipeChannel0.getChannelSplits().get(0).getBaseSplits() != null && recipeChannel0.getChannelSplits().get(0).getBaseSplits().size() > 0) {

                mLayoutChannel0ItemSubTitleTv.setText(recipeChannel0.getChannelSplits().get(0).getBaseSplits().get(0).getCatalogID());

            } else {

                mLayoutChannel0ItemSubTitleTv.setText("");

            }

        } else {

            mLayoutChannel0MainLayout.setVisibility(View.GONE);
            mLayoutChannel0NoDataImage.setVisibility(View.VISIBLE);
            mLayoutChannel0NoDataTv.setVisibility(View.VISIBLE);

        }
        setChannel0Adapter();
    }

    public void postUpdateValues(RecipeUpdateRequest recipeUpdateRequest) {
//        RecipeUpdateRequest recipeUpdateRequest = createRecipeUpdateRequest();
        SimpleRequests simpleRequests = new SimpleRequests();
        PersistenceManager persistenceManager = PersistenceManager.getInstance();
        isUpdating = true;
        showProgress(true);
        simpleRequests.updateRecipe(persistenceManager.getSiteUrl(), recipeUpdateRequest, new SimpleCallback() {
            @Override
            public void onRequestSuccess(StandardResponse response) {
                if (response.getError().getErrorDesc() != null) {
                    onRequestFailed(response);
                } else {
                    mListener.onRefreshRecipe();
                    new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.RECIPE_EDIT, true, "Recipe edited successfully");
                }
            }

            @Override
            public void onRequestFailed(StandardResponse reason) {
                new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.RECIPE_EDIT, false, "Error: " + reason.getError().getErrorDesc());
                ShowCrouton.showSimpleCrouton((DashboardActivity) getActivity(), reason.getError().getErrorDesc(), CroutonCreator.CroutonType.CREDENTIALS_ERROR);
                showProgress(false);
                isUpdating = false;
                if (getActivity() != null && getActivity().getWindow() != null) {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        }, NetworkManager.getInstance(), persistenceManager.getTotalRetries(), persistenceManager.getRequestTimeout());
    }

    private void setChannel0Adapter() {
        if (mChannelItemsAdapters == null) {
            mChannelItemsAdapters = new ChannelItemsAdapters(mChannel0BaseSplits, new ChannelItemsAdapters.ChannelItemsAdaptersListener() {
                @Override
                public void onEditMode(BaseSplits item) {
                    openEditValue(item);
                }
            });

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

            mLayoutChannel0ItemSplitRV.setLayoutManager(layoutManager);

            mLayoutChannel0ItemSplitRV.setAdapter(mChannelItemsAdapters);

        } else {

            mChannelItemsAdapters.notifyDataSetChanged();
        }
    }

    private RecipeUpdateRequest createRecipeUpdateRequest() {
        ArrayList<RecipeValue> recipeValues = new ArrayList<>();
        for (Channel channel : mRecipeResponse.getRecipe().getChannels()) {
            for (ChannelSplits channelSplits : channel.getChannelSplits()) {
                for (BaseSplits baseSplits : channelSplits.getBaseSplits()) {
                    if (baseSplits.getMandatoryField() || (baseSplits.getEditValue() != null
                            && !baseSplits.getEditValue().isEmpty() && !baseSplits.getFValue().equals(baseSplits.getEditValue()))) {
//                        if ((baseSplits.getEditValue() != null
//                                && !baseSplits.getEditValue().isEmpty() && !baseSplits.getFValue().equals(baseSplits.getEditValue()))) {
//                            baseSplits.setFValue(baseSplits.getEditValue());
//                        }
                        recipeValues.add(new RecipeValue(baseSplits.getProductRecipeID(), baseSplits.getFValue(), baseSplits.getLValue(), baseSplits.getHValue()));
                    }
                }
            }
        }
        PersistenceManager persistenceManager = PersistenceManager.getInstance();

        return new RecipeUpdateRequest(persistenceManager.getSessionId(),
                persistenceManager.getJobId(), recipeValues, mRecipeResponse.getRecipeRefStandardID(), mRecipeResponse.getRecipeRefType());
    }

    private void openNotesDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_notes, null);
        builder.setView(dialogView);

        TextView noteTitleTv = dialogView.findViewById(R.id.DN_note_title);
        final EditText noteEt = dialogView.findViewById(R.id.DN_note);
        Button submitBtn = dialogView.findViewById(R.id.DN_btn);
        ImageButton closeButton = dialogView.findViewById(R.id.DN_close_btn);

        if (mNoteTv.getText().length() > 0) {

            noteTitleTv.setText(getActivity().getString(R.string.edit_note));
            noteEt.setText(mNoteTv.getText());
        } else {
            noteTitleTv.setText(getActivity().getString(R.string.add_note));
            noteEt.setHint(getActivity().getString(R.string.enter_note_here));
        }

        final AlertDialog alert = builder.create();
        alert.show();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateNotes(alert, noteEt.getText().toString());

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();
            }
        });

    }

    private void openEditValue(final BaseSplits splits) {

        if (mIsEditMode) {
            return;
        }
        mIsEditMode = true;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_edit_recipe, null);
        builder.setView(dialogView);

        TextView titleTv = dialogView.findViewById(R.id.DER_main_title);
        TextView titleValue = dialogView.findViewById(R.id.DER_title_value);
        TextView rangeTv = dialogView.findViewById(R.id.DER_range_tv);
        final EditText editEtText = dialogView.findViewById(R.id.DER_et_text);
        final EditText editEtNum = dialogView.findViewById(R.id.DER_et_num);
        Button submitBtn = dialogView.findViewById(R.id.DER_btn);
        RadioGroup radioGroup = dialogView.findViewById(R.id.DER_boolean_radio);
        RadioButton passedBtn = dialogView.findViewById(R.id.DER_parameter_radio_passed);
        RadioButton failedBtn = dialogView.findViewById(R.id.DER_parameter_radio_failed);
        ImageButton closeButton = dialogView.findViewById(R.id.DER_close_btn);

        editEtNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        final String nameByLang = OperatorApplication.isEnglishLang() ? splits.getPropertyEName() : splits.getPropertyHName();
        titleTv.setText(nameByLang);
        editEtText.setHint(splits.getFValue());
        editEtNum.setHint(splits.getFValue());

        switch (splits.getDisplayType()) {
            case "text":
                editEtText.setVisibility(View.VISIBLE);
                editEtNum.setVisibility(View.GONE);
                radioGroup.setVisibility(View.GONE);
                titleValue.setText(String.format("%s: %s", getString(R.string.current_value_is), splits.getFValue()));
                break;
            case "Boolean":
                editEtText.setVisibility(View.GONE);
                editEtNum.setVisibility(View.GONE);
                radioGroup.setVisibility(View.VISIBLE);
                if (splits.getFValue().toLowerCase().equals(Boolean.toString(true))) {
                    passedBtn.setChecked(true);
                    failedBtn.setChecked(false);
                } else {
                    passedBtn.setChecked(false);
                    failedBtn.setChecked(true);
                }
                passedBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        splits.setEditValue(Boolean.toString(b));
                    }
                });
                break;
//            case "num":
            default:
                editEtText.setVisibility(View.GONE);
                editEtNum.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.GONE);
                titleValue.setText(String.format("%s: %s", getString(R.string.current_value_is), splits.getFValue()));
                rangeTv.setText(String.format("%s-%s", splits.getLValue(), splits.getHValue()));
                break;

        }

        final AlertDialog alert = builder.create();
        alert.show();


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (splits.getDisplayType()) {
                    case "text":
                        splits.setEditValue(editEtText.getText().toString());
                        break;
                    case "Boolean":
                        break;
//                    case "num":
                    default:
                        if (nameByLang != null && !nameByLang.isEmpty() && nameByLang.contains("%")) {
                            String s = editEtNum.getText().toString();
                            try {
                                if (Integer.parseInt(s) > 100) {
                                    editEtNum.setText(null);
                                    editEtNum.setHint(getString(R.string.invalid_value));
                                    return;
                                } else {
                                    splits.setEditValue(s);
                                }
                            } catch (Exception e) {
                                editEtNum.setText(null);
                                editEtNum.setHint(getString(R.string.invalid_value));
                                return;
                            }
                        } else {
                            splits.setEditValue(editEtNum.getText().toString());
                        }
                        break;

                }

                ArrayList<RecipeValue> recipeValues = new ArrayList<>();
                recipeValues.add(new RecipeValue(splits.getProductRecipeID(), splits.getEditValue(), splits.getLValue(), splits.getHValue()));

                PersistenceManager persistenceManager = PersistenceManager.getInstance();

                postUpdateValues(new RecipeUpdateRequest(persistenceManager.getSessionId(),
                        persistenceManager.getJobId(), recipeValues,
                        mRecipeResponse.getRecipeRefStandardID(), mRecipeResponse.getRecipeRefType()));
                mIsEditMode = false;
                alert.dismiss();

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIsEditMode = false;
                alert.dismiss();
            }
        });
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mIsEditMode = false;
            }
        });

    }

    private void updateNotes(final AlertDialog alert, final String note) {

        ProgressDialogManager.show(getActivity());

        SimpleRequests simpleRequests = new SimpleRequests();
        PersistenceManager pm = PersistenceManager.getInstance();
        PostUpdateNotesForJobRequest updateNotesRequest = new PostUpdateNotesForJobRequest(pm.getSessionId(), pm.getJobId(), note);
        simpleRequests.postUpdateNotesForJob(pm.getSiteUrl(), new PostUpdateNotesForJobCallback() {
            @Override
            public void onUpdateNotesSuccess(StandardResponse responseNewVersion) {
                mNoteTv.setText(note);
                ProgressDialogManager.dismiss();
                alert.dismiss();
            }

            @Override
            public void onUpdateNotesFailed(StandardResponse reason) {
                ProgressDialogManager.dismiss();
            }
        }, NetworkManager.getInstance(), updateNotesRequest, pm.getTotalRetries(), pm.getRequestTimeout());


    }


    private void initListener(View view) {

        view.findViewById(R.id.FR_channel_0_btn).setOnClickListener(this);

        view.findViewById(R.id.FR_channel_1_99_btn).setOnClickListener(this);

        view.findViewById(R.id.FR_channel_100_btn).setOnClickListener(this);

        mChan0GalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRecipeResponse.getProductFiles() != null &&
                        mRecipeResponse.getProductFiles().size() > 0) {
                    String title = "";
                    if (mRecipeResponse.getRecipe().getChannels() != null &&
                            mRecipeResponse.getRecipe().getChannels().size() > 0) {
                        title = mRecipeResponse.getRecipe().getChannels().get(0).getChannelEname();
                    }
                    mListener.onImageProductClick(mRecipeResponse.getProductFiles(), title);
                }
            }
        });

        mNoteLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotesDialog();
            }
        });
    }

    private void initChanne1_1_99_View() {

        if (mRecipeResponse != null && mRecipeResponse.getRecipe() != null && mRecipeResponse.getRecipe().getChannels() != null) {
            List<Channel> recipeResponse_1_99 = new ArrayList<>(mRecipeResponse.getRecipe().getChannels());

            if (mRecipeResponse.getRecipe().getChannels() != null && mRecipeResponse.getRecipe().getChannels().size() > 0 &&
                    mRecipeResponse.getRecipe().getChannels().get(mRecipeResponse.getRecipe().getChannels().size() - 1).getChannelNumber() == 100) {

                recipeResponse_1_99.remove(mRecipeResponse.getRecipe().getChannels().get(mRecipeResponse.getRecipe().getChannels().size() - 1));
            }

            if (mRecipeResponse.getRecipe().getChannels().size() > 0) {
                recipeResponse_1_99.remove(0);
            }

            if (recipeResponse_1_99.size() > 0 && getActivity() != null) {

                mChannel1_99NoDataImage.setVisibility(View.GONE);
                mChannel1_99NoDataTv.setVisibility(View.GONE);
                mlayoutChannel1_99NoDataLayout.setVisibility(View.GONE);

                if (mlayoutChannel1_99.getChildAt(0) != null) {

                    mlayoutChannel1_99.removeAllViews();
                }

                for (Channel recipe : recipeResponse_1_99) {

                    ViewTagsHelper.addTitle(getActivity(), OperatorApplication.isEnglishLang() ? recipe.getChannelEname() : recipe.getChannelLname(), mlayoutChannel1_99);

                    ViewTagsHelper.addRv(getActivity(), recipe.getChannelSplits(), mlayoutChannel1_99, new No0ChannelAdapter.Channel100AdapterListener() {
                        @Override
                        public void onImageProductClick(String fileUrl, String s) {

                        }

                        @Override
                        public void onEditMode(BaseSplits item) {
                            openEditValue(item);
                        }
                    });

                    ViewTagsHelper.addSeparator(getActivity(), mlayoutChannel1_99);
                }

            } else {

                mlayoutChannel1_99.removeAllViews();

                mChannel1_99NoDataImage.setVisibility(View.VISIBLE);

                mChannel1_99NoDataTv.setVisibility(View.VISIBLE);

                mlayoutChannel1_99NoDataLayout.setVisibility(View.VISIBLE);
            }
        } else {

            mlayoutChannel1_99.removeAllViews();

            mChannel1_99NoDataImage.setVisibility(View.VISIBLE);

            mChannel1_99NoDataTv.setVisibility(View.VISIBLE);

            mlayoutChannel1_99NoDataLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.FR_channel_0_btn:

                updateBar(view.getId());

                break;

            case R.id.FR_channel_1_99_btn:

                updateBar(view.getId());

                break;

            case R.id.FR_channel_100_btn:

                updateBar(view.getId());

                break;

        }
    }

    private void updateBar(int id) {

        mchannel0BotomView.setVisibility(View.INVISIBLE);
        mchannel1_99BotomView.setVisibility(View.INVISIBLE);
        mchannel100BotomView.setVisibility(View.INVISIBLE);

        switch (id) {

            case R.id.FR_channel_0_btn:

                mchannel1_99BotomView.setVisibility(View.INVISIBLE);
                mchannel100BotomView.setVisibility(View.INVISIBLE);
                mchannel0BotomView.setVisibility(View.VISIBLE);

                mlayoutChannel1_99MainLy.setVisibility(View.GONE);
                mLayoutChannel100.setVisibility(View.GONE);
                mLayoutChannel0.setVisibility(View.VISIBLE);
                break;

            case R.id.FR_channel_1_99_btn:

                mchannel0BotomView.setVisibility(View.INVISIBLE);
                mchannel100BotomView.setVisibility(View.INVISIBLE);
                mchannel1_99BotomView.setVisibility(View.VISIBLE);

                mLayoutChannel0.setVisibility(View.GONE);
                mLayoutChannel100.setVisibility(View.GONE);
                mlayoutChannel1_99MainLy.setVisibility(View.VISIBLE);

                break;

            case R.id.FR_channel_100_btn:

                mchannel0BotomView.setVisibility(View.INVISIBLE);
                mchannel1_99BotomView.setVisibility(View.INVISIBLE);
                mchannel100BotomView.setVisibility(View.VISIBLE);

                mLayoutChannel0.setVisibility(View.GONE);
                mlayoutChannel1_99MainLy.setVisibility(View.GONE);
                mLayoutChannel100.setVisibility(View.VISIBLE);

                break;

        }

    }

    public void updateRecipeResponse(RecipeResponse recipeResponse, StandardResponse reason) {

        if (mIsEditMode || mProgressBar == null) {
            return;
        }
        mProgressBar.setVisibility(View.GONE);
        if (isUpdating) {
            isUpdating = false;
            if (recipeResponse != null) {
                mListener.onShowCrouton(getString(R.string.success), CroutonCreator.CroutonType.SUCCESS);
            } else {
                mListener.onShowCrouton(reason.getError().getErrorDesc(), CroutonCreator.CroutonType.NETWORK_ERROR);
            }
        }
        if (recipeResponse != null) {
            mRecipeResponse = recipeResponse;
        }
        if (mLayoutChannel0MainLayout != null) {
            initView();
        }
    }

    public void showProgress(boolean showProgress) {
        if (mProgressBar == null) {
            return;
        }
        if (showProgress) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public interface OnRecipeFragmentListener {

        void onImageProductClick(List<String> fileUrl, String name);

        void onRefreshRecipe();

        void onShowCrouton(String string, CroutonCreator.CroutonType type);
    }
}
