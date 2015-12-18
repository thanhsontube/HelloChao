package son.nt.hellochao.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import son.nt.hellochao.R;


public class DialogSetting extends DialogFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";

    // TODO: Rename and change types of parameters
    private String title;
    private String message;
    private int icon = -1;
    private String positive = "";
    private String negative = "";

    private IDialogListener mListener;


    public static DialogSetting newInstance(String param1, String param2) {
        DialogSetting fragment = new DialogSetting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogSetting newInstance(String title, String message, int iconId, String positive, String negative) {
        DialogSetting fragment = new DialogSetting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, message);
        args.putInt(ARG_PARAM3, iconId);
        args.putString(ARG_PARAM4, positive);
        args.putString(ARG_PARAM5, negative);
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogSetting newInstanceSetting(String positive, String negative) {
        DialogSetting fragment = new DialogSetting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM4, positive);
        args.putString(ARG_PARAM5, negative);
        fragment.setArguments(args);
        return fragment;
    }

    public DialogSetting() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            message = getArguments().getString(ARG_PARAM2);
            icon = getArguments().getInt(ARG_PARAM3);
            positive = getArguments().getString(ARG_PARAM4);
            negative = getArguments().getString(ARG_PARAM5);
        }
    }

    SeekBar volumeBar;
    TextView txtVolume;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_settings, null);
        txtVolume = (TextView) view.findViewById(R.id.setting_txt_volume);
        volumeBar = (SeekBar) view.findViewById(R.id.setting_volume);
        view.findViewById(R.id.setting_btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        final AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        volumeBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
        volumeBar.setProgress(currentVolume);
        txtVolume.setText(String.valueOf(currentVolume));
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    txtVolume.setText(String.valueOf(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        builder.setView(view);
        return builder.create();
    }


    @Override
    public void onClick(View v) {
    }


    public interface IDialogListener {
        void onIDialogCancel(int currentVolume);

    }

    public void setOnIDialogListener(IDialogListener callback) {
        this.mListener = callback;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mListener != null) {
            mListener.onIDialogCancel(volumeBar.getProgress());
        }
    }


}
