package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;

public class RecipeFragment extends Fragment implements View.OnClickListener, No0ChannelAdapter.Channel100AdapterListener {

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
    private ChannelItemsAdapters mChannelItemsAdapters;
    private SingleLineKeyboard mKeyBoard;
    private LinearLayout mKeyBoardLayout;
    private View mLayoutChannel0ItemSaveBtn;
    private ProgressBar mProgressBar;
    private boolean isUpdating;
    private LinearLayoutManager mChannel0ItemsAdaptersLyManager;
    private boolean mIsEditMode;

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

        View mLayoutChannel0Item = mLayoutChannel0.findViewById(R.id.C0L_item);

        mLayoutChannel0NoDataImage = mLayoutChannel0.findViewById(R.id.C0L_no_data_img);

        mLayoutChannel0NoDataTv = mLayoutChannel0.findViewById(R.id.C0L_no_data_tv);

        mLayoutChannel0ItemTitleTv = mLayoutChannel0Item.findViewById(R.id.IP_title);

        mLayoutChannel0ItemSaveBtn = mLayoutChannel0Item.findViewById(R.id.IP_save_btn);

        mLayoutChannel0ItemSubTitleTv = mLayoutChannel0Item.findViewById(R.id.IP_sub_title);

        mLayoutChannel0ItemSplitRV = mLayoutChannel0Item.findViewById(R.id.IP_split_rv);

    }

    public void closeKeyBoard() {
        if (mKeyBoardLayout != null) {
            mKeyBoardLayout.setVisibility(View.GONE);
        }
        if (mKeyBoard != null) {
            mKeyBoard.setListener(null);
            mKeyBoard.closeKeyBoard();
        }
    }

    public void openKeyboard(SingleLineKeyboard.OnKeyboardClickListener listener, String text, String[] complementChars) {
        if (mKeyBoardLayout != null) {
            mKeyBoardLayout.setVisibility(View.VISIBLE);
            if (mKeyBoard == null) {
                mKeyBoard = new SingleLineKeyboard(mKeyBoardLayout, getContext());
            }

            mKeyBoard.setChars(complementChars);
            mKeyBoard.openKeyBoard(text);
            mKeyBoard.setListener(listener);
        }
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
        }
    }

    private void initChannel100View() {

        if (mRecipeResponse != null && mRecipeResponse.getRecipe() != null
                && mRecipeResponse.getRecipe().getChannels() != null && mRecipeResponse.getRecipe().getChannels().size() > 0 && mRecipeResponse.getRecipe().getChannels().get(0) != null &&
                mRecipeResponse.getRecipe().getChannels().get(mRecipeResponse.getRecipe().getChannels().size() - 1).getChannelNumber() == 100) {

            mLayoutChannel100NoDataImage.setVisibility(View.GONE);
            mLayoutChannel100NoDataTv.setVisibility(View.GONE);
            mLayoutChannel100RvLy.setVisibility(View.VISIBLE);

            mLayoutChannel100Title.setText(mRecipeResponse.getRecipe().getChannels().get(mRecipeResponse.getRecipe().getChannels().size() - 1).getChannelEname());

            No0ChannelAdapter mNo0ChannelAdapter = new No0ChannelAdapter(getActivity(), this,
                    (ArrayList<ChannelSplits>) mRecipeResponse.getRecipe().getChannels().get(mRecipeResponse.getRecipe().getChannels().size() - 1).getChannelSplits(), No0ChannelAdapter.TYPE_CHANNEL_100);

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

        HashMap<Integer, String> editList = new HashMap<>();
        for (BaseSplits baseSplits : mChannel0BaseSplits) {
            if (baseSplits.isEditMode()) {
                editList.put(baseSplits.getProductRecipeID(), baseSplits.getEditValue());
            }
        }
        mChannel0BaseSplits.clear();
        if (mRecipeResponse != null && mRecipeResponse.getRecipe() != null && mRecipeResponse.getRecipe().getChannels() != null
                && mRecipeResponse.getRecipe().getChannels().size() > 0 && mRecipeResponse.getRecipe().getChannels().get(0) != null) {

            mLayoutChannel0MainLayout.setVisibility(View.VISIBLE);
            mLayoutChannel0NoDataImage.setVisibility(View.GONE);
            mLayoutChannel0NoDataTv.setVisibility(View.GONE);

            final Channel recipeChannel0 = mRecipeResponse.getRecipe().getChannels().get(0);

            if (mRecipeResponse.getProductData() != null && mRecipeResponse.getProductData().getFileUrl() != null &&
                    mRecipeResponse.getProductData().getFileUrl().size() > 0) {
                ImageLoader.getInstance().displayImage(mRecipeResponse.getProductData().getFileUrl().get(0), mLayoutChannel0Image);
            }

            if (getActivity().getResources() != null) {
                mLayoutChannel0ItemTitleTv.setText(getActivity().getResources().getString(R.string.production_parameters));
            }
            if (recipeChannel0.getChannelSplits().get(0).getBaseSplits() != null && recipeChannel0.getChannelSplits().get(0).getBaseSplits().size() > 0) {

                mLayoutChannel0ItemSubTitleTv.setText(recipeChannel0.getChannelSplits().get(0).getBaseSplits().get(0).getCatalogID());

            } else {

                mLayoutChannel0ItemSubTitleTv.setText("");

            }
            mLayoutChannel0ItemSaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    if (getActivity() != null && getActivity().getWindow() != null) {
                        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                    recipeChannel0.getChannelSplits().get(0).setBaseSplits(mChannel0BaseSplits);
                    RecipeUpdateRequest recipeUpdateRequest = createRecipeUpdateRequest();
                    SimpleRequests simpleRequests = new SimpleRequests();
                    PersistenceManager persistenceManager = PersistenceManager.getInstance();
                    isUpdating = true;
                    simpleRequests.updateRecipe(persistenceManager.getSiteUrl(), recipeUpdateRequest, new SimpleCallback() {
                        @Override
                        public void onRequestSuccess(StandardResponse response) {
                            if (response.getError().getErrorDesc() != null) {
                                onRequestFailed(response);
                            } else {
                                mListener.onRefreshRecipe();
                            }
                        }

                        @Override
                        public void onRequestFailed(StandardResponse reason) {
                            ShowCrouton.showSimpleCrouton((DashboardActivity) getActivity(), reason.getError().getErrorDesc(), CroutonCreator.CroutonType.CREDENTIALS_ERROR);
                            mProgressBar.setVisibility(View.GONE);
                            isUpdating = false;
                            if (getActivity() != null && getActivity().getWindow() != null) {
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        }
                    }, NetworkManager.getInstance(), persistenceManager.getTotalRetries(), persistenceManager.getRequestTimeout());
                }
            });

            if (recipeChannel0.getChannelSplits().get(0).getBaseSplits() != null) {

                mChannel0BaseSplits.addAll(recipeChannel0.getChannelSplits().get(0).getBaseSplits());

                for (BaseSplits baseSplits : mChannel0BaseSplits) {
                    if (editList.containsKey(baseSplits.getProductRecipeID())) {
                        baseSplits.setEditMode(true);
                        baseSplits.setEditValue(editList.get(baseSplits.getProductRecipeID()));
                        editList.remove(baseSplits.getProductRecipeID());
                    }
                }
            }

        } else {

            mLayoutChannel0MainLayout.setVisibility(View.GONE);
            mLayoutChannel0NoDataImage.setVisibility(View.VISIBLE);
            mLayoutChannel0NoDataTv.setVisibility(View.VISIBLE);

        }
        setChannel0Adapter();
    }

    private void setChannel0Adapter() {
        if (mChannelItemsAdapters == null) {
            mChannelItemsAdapters = new ChannelItemsAdapters(getActivity(),
                    mChannel0BaseSplits);

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

            mLayoutChannel0ItemSplitRV.setLayoutManager(layoutManager);

            if (!mChannelItemsAdapters.hasListener()){
                addChannelItemsAdapterListener();
            }

            mLayoutChannel0ItemSplitRV.setAdapter(mChannelItemsAdapters);

        } else {
            if (!mChannelItemsAdapters.hasListener()){
                addChannelItemsAdapterListener();
            }
            mChannelItemsAdapters.notifyDataSetChanged();
        }
    }

    private void addChannelItemsAdapterListener() {
        ChannelItemsAdapters.ChannelItemsAdaptersListener listener = null;
        if (mRecipeResponse != null && mRecipeResponse.getCanEditRecipe()) {
            listener = new ChannelItemsAdapters.ChannelItemsAdaptersListener() {
                @Override
                public void onOpenKeyboard(SingleLineKeyboard.OnKeyboardClickListener listener, String text, String[] complementChars) {
                    openKeyboard(listener, text, complementChars);
                }

                @Override
                public void onCloseKeyboard() {
                    closeKeyBoard();
                }

                @Override
                public void onEditMode(boolean isEditMode) {
                    mIsEditMode = isEditMode;
                    if (isEditMode) {
                        mLayoutChannel0ItemSaveBtn.setVisibility(View.VISIBLE);
                    } else {
                        mLayoutChannel0ItemSaveBtn.setVisibility(View.GONE);
                        closeKeyBoard();
                    }
                }

            };
        }
        mChannelItemsAdapters.addListener(listener);
    }

    private RecipeUpdateRequest createRecipeUpdateRequest() {
        PersistenceManager persistenceManager = PersistenceManager.getInstance();
        ArrayList<RecipeValue> recipeValues = new ArrayList<>();
        for (Channel channel : mRecipeResponse.getRecipe().getChannels()) {
            for (ChannelSplits channelSplits : channel.getChannelSplits()) {
                for (BaseSplits baseSplits : channelSplits.getBaseSplits()) {
                    if (baseSplits.getMandatoryField() || (baseSplits.getEditValue() != null
                            && !baseSplits.getEditValue().isEmpty() && !baseSplits.getFValue().equals(baseSplits.getEditValue()))) {
                        if ((baseSplits.getEditValue() != null
                                && !baseSplits.getEditValue().isEmpty() && !baseSplits.getFValue().equals(baseSplits.getEditValue()))) {
                            baseSplits.setFValue(baseSplits.getEditValue());
                        }
                        recipeValues.add(new RecipeValue(baseSplits.getProductRecipeID(), baseSplits.getFValue(), baseSplits.getLValue(), baseSplits.getHValue()));
                    }
                }
            }
        }
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

        mLayoutChannel0Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRecipeResponse.getProductData() != null && mRecipeResponse.getProductData().getFileUrl() != null &&
                        mRecipeResponse.getProductData().getFileUrl().size() > 0 &&
                        mRecipeResponse.getRecipe().getChannels() != null &&
                        mRecipeResponse.getRecipe().getChannels().size() > 0) {
                    mListener.onImageProductClick(mRecipeResponse.getProductData().getFileUrl(), mRecipeResponse.getRecipe().getChannels().get(0).getChannelEname());
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

        if (mRecipeResponse != null && mRecipeResponse.getRecipe() != null) {
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

                    ViewTagsHelper.addTitle(getActivity(), recipe.getChannelEname(), mlayoutChannel1_99);

                    ViewTagsHelper.addRv(getActivity(), recipe.getChannelSplits(), mlayoutChannel1_99, this);

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

    public void updateRecipeResponse(RecipeResponse recipeResponse) {

        if (!isUpdating && mIsEditMode) {
            return;
        }
        if (isUpdating) {
            ShowCrouton.showSimpleCrouton((DashboardActivity) getActivity(), getString(R.string.success), CroutonCreator.CroutonType.SUCCESS);
            mProgressBar.setVisibility(View.GONE);
            mChannel0BaseSplits.clear();
            mLayoutChannel0ItemSaveBtn.setVisibility(View.GONE);
            closeKeyBoard();
            if (getActivity() != null && getActivity().getWindow() != null) {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            isUpdating = false;
            mIsEditMode = false;
        }
        mRecipeResponse = recipeResponse;
        if (mLayoutChannel0MainLayout != null) {
            initView();
        }
    }

    @Override
    public void onImageProductClick(String fileUrls, String name) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(fileUrls);
        mListener.onImageProductClick(arrayList, name);
    }

    public interface OnRecipeFragmentListener {

        void onImageProductClick(List<String> fileUrl, String name);

        void onRefreshRecipe();
    }
}
