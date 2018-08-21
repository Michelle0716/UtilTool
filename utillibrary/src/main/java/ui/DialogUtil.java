package ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;


/**
 * 一个弹框工具类，以下是普通用法，
 * 还可以设置不同的布局，或者直接把布局文件传参进入
 */
public final class DialogUtil {
    private static final int textSize = 18;

    private DialogUtil() {
    }


    /**
     * 系统弹框-显示title和确认按键行为
     *
     * @param context
     * @param title
     * @param message
     * @param listener
     */
    public static void showMessage(Context context, String title, String message, final OnConfirmListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
//            .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确    认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onConfirm();
                        }
                    }
                })
                .create();
        dialog.show();
        setDialogAttribute(dialog, textSize);
        dialog.getWindow().setType(LayoutParams.TYPE_KEYGUARD_DIALOG);
    }


    /**
     * 确认取消回调
     *
     * @param context
     * @param title
     * @param message
     * @param listener
     */
    public static void showOption(Context context, String title, String message, final OnChooseListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
//            .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确    定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onConfirm();
                        }
                    }
                })
                .setNegativeButton("取    消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onCancel();
                        }
                    }
                })
                .create();
        dialog.show();
        setDialogAttribute(dialog, textSize);
        dialog.getWindow().setType(LayoutParams.TYPE_KEYGUARD_DIALOG);
    }


    /**
     * @param context
     * @param title
     * @param message
     * @param listener
     */
    public static void showOption(Context context, String title, String message, final OnConfirmListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
//            .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确    定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onConfirm();
                        }
                    }
                })
                .setNegativeButton("取    消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.show();
        setDialogAttribute(dialog, textSize);
        dialog.getWindow().setType(LayoutParams.TYPE_KEYGUARD_DIALOG);
    }

    public static void showOption(Context context, String message, final OnConfirmListener listener) {
        showOption(context, "系统提示", message, listener);
    }

    public static void showOption(Context context, String message, final OnChooseListener listener) {
        showOption(context, "系统提示", message, listener);
    }

    public static void showMessage(Context context, String message) {
        showMessage(context, "系统提示", message, null);
    }

    public static void showMessage(Context context, String message, OnConfirmListener listener) {
        showMessage(context, "系统提示", message, listener);
    }


    /**
     * 确认或取消监听回调
     */
    public interface OnChooseListener {
        void onConfirm();

        void onCancel();
    }

    /**
     * 确认按钮回调
     */
    public interface OnConfirmListener {
        void onConfirm();
    }

    /**
     * 设置对话框系统属性
     *
     * @param dialog
     */
    private static void setDialogAttribute(AlertDialog dialog, int fontSize) {
        LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
        setDialogFontSize(dialog, fontSize);
    }

    /**
     * 设置对话框字体属性
     *
     * @param dialog
     * @param size
     */
    private static void setDialogFontSize(Dialog dialog, int size) {
        Window window = dialog.getWindow();
        View view = window.getDecorView();
        setViewFontSize(view, size);
    }

    /**
     * 对传入控件判断是否是文本框子类，如果是可以进行属性设置，
     * 像smartlayout也是类似方法设置title属性
     *
     * @param view
     * @param size
     */
    private static void setViewFontSize(View view, int size) {
        if (view instanceof TextView) {
            TextView textview = (TextView) view;
            textview.setGravity(Gravity.CENTER);
            textview.setTextSize(size);
            textview.getPaint().setFakeBoldText(true);
            return;
        }

        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                setViewFontSize(parent.getChildAt(i), size);
            }
        }
    }
}