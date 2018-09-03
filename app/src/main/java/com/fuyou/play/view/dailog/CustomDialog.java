package com.fuyou.play.view.dailog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuyou.play.R;
import com.fuyou.play.util.ExceptionUtils;


/**
 * Created by Administrator on 2018/5/2 0002.
 * 项目常用dialog
 * @author wwei
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context){
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private boolean cancelAble = true;
        private boolean miss = false;
        private View contentView;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder disMissLine(boolean miss){
            this.miss = miss;
            return this;
        }

        public Builder setCancelAble(boolean cancelAble){
            this.cancelAble = cancelAble;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CustomDialog create() {
            try {
                LayoutInflater inflater = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final CustomDialog dialog = new CustomDialog(context, R.style.AlertActivity_AlertStyle);
                View layout = inflater.inflate(R.layout.dialog_normal_layout, null);
                dialog.addContentView(layout, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ((TextView) layout.findViewById(R.id.dialog_title)).setText(title);
                if (positiveButtonText != null) {
                    ((TextView) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
                    ((TextView) layout.findViewById(R.id.negativeButton)).setBackgroundResource(R.drawable.dialog_button_middle);
                    if (positiveButtonClickListener != null) {
                        ((TextView) layout.findViewById(R.id.positiveButton))
                                .setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        positiveButtonClickListener.onClick(dialog,
                                                DialogInterface.BUTTON_POSITIVE);
                                    }
                                });
                    }
                } else {
                    layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
                }
                if (negativeButtonText != null) {
                    ((TextView) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
                    if (negativeButtonClickListener != null) {
                        ((TextView) layout.findViewById(R.id.negativeButton)).setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                            }
                        });
                    }
                } else {
                    layout.findViewById(R.id.negativeButton).setVisibility( View.GONE);
                    layout.findViewById(R.id.line_zy_dialog).setVisibility(View.GONE);
                }
                if (message != null) {
                    ((TextView) layout.findViewById(R.id.message)).setText(message);
                } else {
                    ((TextView) layout.findViewById(R.id.message)).setVisibility(View.GONE);
                }
                //如果不设置否定按钮，自动隐藏分割线
                if(miss){
                    layout.findViewById(R.id.line_zy_dialog).setVisibility(View.GONE);
                }else {
                    layout.findViewById(R.id.line_zy_dialog).setVisibility(View.VISIBLE);
                }
                if(cancelAble){
                    dialog.setCancelable(true);
                }else{
                    dialog.setCancelable(false);
                }
                dialog.setContentView(layout);
                return dialog;
            } catch (Exception e){
                ExceptionUtils.ExceptionSend(e);
            }
            return null;
        }
    }
}
