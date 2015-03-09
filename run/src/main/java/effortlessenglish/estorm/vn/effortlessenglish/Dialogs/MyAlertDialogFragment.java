package effortlessenglish.estorm.vn.effortlessenglish.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import effortlessenglish.estorm.vn.effortlessenglish.R;

public class MyAlertDialogFragment extends DialogFragment {

    public interface MyAlertListener {

        public void onLeftClick(Dialog dialog);

        public void onRightClick(Dialog dialog);
    }

	public Context mContext;

	public TextView mLeftButton;
	public TextView mRightButton;

	public TextView mTitleText;
	public TextView mMessageText;

	String mTitle = "";
	String mMessage = "";

	String mLableLeftButton = "";
	String mLableRightButton = "";

	boolean oneButton = false;

	private static final String EXTRA_TITLE = "title";
	private static final String EXTRA_MESSAGE = "message";
	private static final String EXTRA_LABEL_LEFT = "label_left";
	private static final String EXTRA_LABEL_RIGHT = "label_right";

	MyAlertListener mAlertListener;

	// public MyAlertDialogFragment(Context context, String title, String
	// message,
	// String lableLeftButton, String lableRightButton,
	// MyAlertListener listener) {
	// this.mContext = context;
	// this.mTitle = title;
	// this.mLableLeftButton = lableLeftButton;
	// this.mLableRightButton = lableRightButton;
	// this.mMessage = message;
	// this.mAlertListener = listener;
	// this.mInflater = LayoutInflater.from(context);
	// }

	public static MyAlertDialogFragment newInstance(Context context,
			String title, String message, String labelLeftButton,
			String labelRightButton, MyAlertListener listener) {

		Bundle bundle = new Bundle();
		bundle.putString(EXTRA_TITLE, title);
		bundle.putString(EXTRA_MESSAGE, message);
		bundle.putString(EXTRA_LABEL_LEFT, labelLeftButton);
		bundle.putString(EXTRA_LABEL_RIGHT, labelRightButton);
		MyAlertDialogFragment alertDialog = new MyAlertDialogFragment();
		alertDialog.setArguments(bundle);
		alertDialog.setContext(context);
		alertDialog.setAlertListener(listener);
		return alertDialog;
	}

	private void setContext(Context context) {
		mContext = context;
	}

	private void setAlertListener(MyAlertListener listener) {
		mAlertListener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getArguments();
		if (bundle != null) {
			mMessage = bundle.getString("message");
			mTitle = bundle.getString("title");
			mLableLeftButton = bundle.getString("label_left");
			mLableRightButton = bundle.getString("label_right");
		}

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(mContext, R.style.MyDialogTheme);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);

		View view = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_fragment, null);

		mTitleText = (TextView) view.findViewById(R.id.title_text);
		mTitleText.setText(mTitle);

		mMessageText = (TextView) view.findViewById(R.id.message_text);
		mMessageText.setText(mMessage);

		mLeftButton = (TextView) view.findViewById(R.id.left_button);
		mLeftButton.setText(mLableLeftButton);
		mLeftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAlertListener.onLeftClick(dialog);
			}
		});

		mRightButton = (TextView) view.findViewById(R.id.right_button);
		mRightButton.setText(mLableRightButton);
		mRightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAlertListener.onRightClick(dialog);
			}
		});

		/*
		 * set dialog with one button default is hide right button
		 */
		if (oneButton) {
			mRightButton.setVisibility(View.GONE);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			mLeftButton.setGravity(Gravity.CENTER);
			mLeftButton.setLayoutParams(params);
			view.findViewById(R.id.split_view).setVisibility(View.GONE);
		}

		dialog.setContentView(view);

		return dialog;
	}

	public void setOneButton(boolean oneButton) {
		this.oneButton = oneButton;
	}

}
