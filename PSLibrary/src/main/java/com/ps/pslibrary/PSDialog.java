package com.ps.pslibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.android.material.snackbar.Snackbar;

import es.dmoral.toasty.Toasty;

/**
 * Created by Sanam Mijar on 11, Aug, 2021.
 */

public class PSDialog {
    private final Context context;
    public static final int DIALOG_PSD = 0;
    public static final int DIALOG_TOAST = 1;
    public static final int DIALOG_TOASTY = 2;
    public static final int DIALOG_SNACK_BAR = 3;
    public static final int WARNING = 0;
    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final int INFO = 3;
    public static final int NO_INTERNET = 404;

    // Loading dialog
    public static final int LD_ANDROID_CUSTOM = 0; //Android studio custom loading dialog
    public static final int LD_PS = 1; //PS Loading Dialog
//    public static final int LD_MATERIAL_LINEAR = 2; //Material LinearProgressIndicator Loading Dialog
//    public static final int LD_MATERIAL_CIRCULAR = 3; //Material CircularProgressIndicator Loading Dialog
    public static final String MSG_LOADING = "Loading please wait...";

    private ProgressDialog progressDialog;
    private Dialog dialog;
    private View materialView;

    private OnActionButtonPress onActionButtonPress;

    public PSDialog(Context context){ this.context = context; }

    public interface OnActionButtonPress { void onOkPressed(); }


    public void showLoadingDialog(int dialogType, String loadingMessage){
        if (dialogType == LD_ANDROID_CUSTOM){
            showAndroidCustomLD(loadingMessage);
        }else{
            showPSLoadingDialog(loadingMessage);
        }
    }

    private void showAndroidCustomLD(String msg){
        progressDialog = ProgressDialog.show(context.getApplicationContext(), "", msg);
    }

    private void showPSLoadingDialog(String msg){
        dialog = new Dialog(context, R.style.ActionDialog);
        dialog.setContentView(R.layout.ps_loading_dialogue);
        TextView dialogMsg = dialog.findViewById(R.id.title);
        dialogMsg.setText(msg);
        dialog.setCanceledOnTouchOutside(false);

        if (dialog.getWindow() != null) {
            dialog.getWindow().getAttributes().windowAnimations = R.style.ActionDialogAnimation;
        }
        dialog.show();
    }

    public void dismissDialog(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    public void showMaterialProgress(View materialView){
        this.materialView = materialView;
        if (materialView != null){
            materialView.setVisibility(View.VISIBLE);
        }
    }
    public void dismissMaterialProgress(){
        if (materialView.getVisibility() == View.VISIBLE){
            materialView.setVisibility(View.GONE);
        }
    }



    public void showPSDialog(int dialogType, int alertType, String dialogMessage, OnActionButtonPress onActionButtonPress){
        this.onActionButtonPress = onActionButtonPress;
        if (dialogType == DIALOG_PSD){
            PSAlertDialog(alertType, dialogMessage);
        } else if (dialogType == DIALOG_TOAST){
            PSToastDialog(alertType, dialogMessage);
        } else if (dialogType == DIALOG_TOASTY){
            PSToastyDialog(alertType, dialogMessage);
        } else if (dialogType == DIALOG_SNACK_BAR){
            PSSnackBarDialog(alertType, dialogMessage);
        }
    }

    private void PSAlertDialog(int alertType, String dialogMessage){
        final Dialog dialog = new Dialog(context, R.style.ActionDialog);
        dialog.setContentView(R.layout.action_dialog_manager);
        dialog.setCanceledOnTouchOutside(false);
        LottieAnimationView statusIcon = dialog.findViewById(R.id.dialogStatusIcon);
        TextView title = dialog.findViewById(R.id.title);
        TextView message = dialog.findViewById(R.id.message);
        Button closeBtn = dialog.findViewById(R.id.okBtn);
        if (alertType == WARNING){
            statusIcon.setRepeatCount(LottieDrawable.INFINITE);
            statusIcon.setAnimation(R.raw.warning);
            title.setText(R.string.warning);
        }else if (alertType == SUCCESS){
            statusIcon.setAnimation(R.raw.success);
            title.setText(R.string.success);
        }else if (alertType == ERROR){
            statusIcon.setRepeatCount(LottieDrawable.INFINITE);
            statusIcon.setAnimation(R.raw.error);
            title.setText(R.string.error);
        }else if (alertType == NO_INTERNET){
            statusIcon.setRepeatCount(LottieDrawable.INFINITE);
            statusIcon.setAnimation(R.raw.no_internet);
            title.setText(R.string.network_error);
        }
        message.setText(dialogMessage);

        closeBtn.setOnClickListener(v -> {
            onActionButtonPress.onOkPressed();
            dialog.dismiss();
        });
        if (dialog.getWindow()!=null){ dialog.getWindow().getAttributes().windowAnimations = R.style.ActionDialogAnimation; }
        if(!((Activity) context ).isFinishing()){
            dialog.show();
        }
    }

    @SuppressLint("WrongConstant")
    private void PSToastDialog(int alertType, String dialogMessage) {
        final Dialog dialog = new Dialog(context, R.style.ToastDialog);
        dialog.setContentView(R.layout.toast_dialog_manager);
        dialog.setCanceledOnTouchOutside(true);
        LinearLayout llToastBody = dialog.findViewById(R.id.toast_body);
        LottieAnimationView statusIcon = dialog.findViewById(R.id.dialogIcon);
        TextView message = dialog.findViewById(R.id.message);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = 53;
//        wlp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = () -> { onActionButtonPress.onOkPressed(); dialog.dismiss(); };
        handler.postDelayed(runnable, 4000);

        int bgColor = context.getResources().getColor(R.color.info);
        if (alertType == WARNING){
            statusIcon.setRepeatCount(LottieDrawable.INFINITE);
            statusIcon.setAnimation(R.raw.warning);
            bgColor = context.getResources().getColor(R.color.warning);
        }else if (alertType == SUCCESS){
            statusIcon.setAnimation(R.raw.success);
            bgColor = context.getResources().getColor(R.color.success);
        }else if (alertType == ERROR){
            statusIcon.setRepeatCount(LottieDrawable.INFINITE);
            statusIcon.setAnimation(R.raw.error);
            bgColor = context.getResources().getColor(R.color.danger);
        }else if (alertType == NO_INTERNET){
            statusIcon.setRepeatCount(LottieDrawable.INFINITE);
            statusIcon.setAnimation(R.raw.no_internet);
            bgColor = context.getResources().getColor(R.color.info);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            llToastBody.getBackground().setColorFilter(new BlendModeColorFilter(bgColor, BlendMode.SRC_ATOP));
        } else {
            llToastBody.getBackground().setColorFilter(bgColor, PorterDuff.Mode.SRC_ATOP);
        }

        message.setText(dialogMessage);
        if (dialog.getWindow()!=null){ dialog.getWindow().getAttributes().windowAnimations = R.style.ActionDialogAnimation;}
        dialog.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void PSToastyDialog(int alertType, String dialogMessage) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_success);
        if (alertType == NO_INTERNET){
            Toasty.error(context, dialogMessage, Toasty.LENGTH_LONG, true).show();
        } else if (alertType == ERROR){
            Toasty.error(context, dialogMessage, Toasty.LENGTH_LONG, true).show();
        } else if (alertType == SUCCESS){
            Toasty.success(context, dialogMessage, Toasty.LENGTH_LONG, true).show();
        } else if (alertType == WARNING){
            Toasty.warning(context, dialogMessage, Toasty.LENGTH_LONG, true).show();
        } else if (alertType == INFO){
            Toasty.info(context, dialogMessage, Toasty.LENGTH_LONG, true).show();
        } else {
            Toasty.normal(context, dialogMessage, drawable).show();
        }
        onActionButtonPress.onOkPressed();
    }

    private void PSSnackBarDialog(int alertType, String dialogMessage) {
        int bgColor = 0, textColor = 0;
        if (alertType == WARNING){
            bgColor = context.getResources().getColor(R.color.warning);
            textColor = context.getResources().getColor(R.color.text_color);
        }else if (alertType == SUCCESS){
            bgColor = context.getResources().getColor(R.color.success);
            textColor = context.getResources().getColor(R.color.white);
        }else if (alertType == ERROR){
            bgColor = context.getResources().getColor(R.color.danger);
            textColor = context.getResources().getColor(R.color.white);
        }else if (alertType == NO_INTERNET){
            bgColor = context.getResources().getColor(R.color.info);
            textColor = context.getResources().getColor(R.color.text_color);
        }

        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = () -> onActionButtonPress.onOkPressed();
        handler.postDelayed(runnable, 4000);

        Snackbar.make(((Activity)context).findViewById(android.R.id.content), dialogMessage, Snackbar.LENGTH_LONG)
                .setBackgroundTint(bgColor)
                .setTextColor(textColor)
                .show();
    }
}
