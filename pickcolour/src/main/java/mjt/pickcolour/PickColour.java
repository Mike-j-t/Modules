package mjt.pickcolour;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Mike on 22/09/2017.
 */

public class PickColour extends AppCompatActivity {

    public static final String INTENTKEY_STARTCOLOUR = "startcolour";
    public static final String INTENTKEY_RETURNCOLOUR = "returncolour";

    int defaultcolour = Color.argb(255,127,127,127);
    int mPassedColour = defaultcolour;

    LinearLayout mShowColour;
    TextView mHexColour;
    SeekBar mRedSeekBar;
    SeekBar mGreenSeekBar;
    SeekBar mBlueSeekBar;
    SeekBar mAlphaSeekBar;
    TextView mRedSeekbarValue;
    TextView mGreenSeekbarValue;
    TextView mBlueSeekBarValue;
    TextView mAlphaSeekBarValue;
    Button mApplyButton;
    Button mCancelButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickcolour);

        mShowColour = (LinearLayout) findViewById(R.id.showcolour);
        mHexColour = (TextView) findViewById(R.id.hexcolour);
        mRedSeekBar = (SeekBar) findViewById(R.id.seekbar_red);
        mGreenSeekBar = (SeekBar) findViewById(R.id.seekbar_green);
        mBlueSeekBar = (SeekBar) findViewById(R.id.seekbar_blue);
        mAlphaSeekBar = (SeekBar) findViewById(R.id.seekbar_alpha);
        mRedSeekbarValue = (TextView) findViewById(R.id.value_red);
        mGreenSeekbarValue = (TextView) findViewById(R.id.value_green);
        mBlueSeekBarValue = (TextView) findViewById(R.id.value_blue);
        mAlphaSeekBarValue = (TextView) findViewById(R.id.value_alpha);
        mApplyButton = (Button) findViewById(R.id.applybutton);
        mCancelButton = (Button) findViewById(R.id.cancelbutton);
        mPassedColour = this.getIntent().getIntExtra(INTENTKEY_STARTCOLOUR,defaultcolour);

        setSeekBarChangeListener(mRedSeekBar, mRedSeekbarValue);
        setSeekBarChangeListener(mGreenSeekBar, mGreenSeekbarValue);
        setSeekBarChangeListener(mBlueSeekBar,mBlueSeekBarValue);
        setSeekBarChangeListener(mAlphaSeekBar,mAlphaSeekBarValue);
        if (mPassedColour != defaultcolour) {
            int alpha = (mPassedColour >> 24) & 0xff;
            int red = (mPassedColour >> 16) & 0xff;
            int green = (mPassedColour >> 8) & 0xff;
            int blue = (mPassedColour >> 0) & 0xff;
            mAlphaSeekBar.setProgress(alpha);
            mRedSeekBar.setProgress(red);
            mGreenSeekBar.setProgress(green);
            mBlueSeekBar.setProgress(blue);
            mAlphaSeekBarValue.setText(Integer.toString(alpha));
            mRedSeekbarValue.setText(Integer.toString(red));
            mGreenSeekbarValue.setText(Integer.toString(green));
            mBlueSeekBarValue.setText(Integer.toString(blue));
        }
        setCancelButton(mCancelButton);
        setApplyButton(mApplyButton);
        setShowColour();
    }

    private void setSeekBarChangeListener(SeekBar sb, final TextView sbvalue) {
        if (sb == mAlphaSeekBar) {
            sb.setProgress(255);
        }
        sbvalue.setText(Integer.toString(sb.getProgress()));
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sbvalue.setText(Integer.toString(i));
                setShowColour();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setShowColour() {
        int argb = Color.argb(
                mAlphaSeekBar.getProgress(),
                mRedSeekBar.getProgress(),
                mGreenSeekBar.getProgress(),
                mBlueSeekBar.getProgress());
        mShowColour.setBackgroundColor(argb);
        mHexColour.setText("#"+(Integer.toHexString(argb)).toUpperCase());
    }

    private void setCancelButton(Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setApplyButton(Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rv = new Intent();
                long returncolour = ((long) Color.argb(
                        mAlphaSeekBar.getProgress(),
                        mRedSeekBar.getProgress(),
                        mGreenSeekBar.getProgress(),
                        mBlueSeekBar.getProgress()
                ) & 0x01_FF_FF_FF_FFL);
                rv.putExtra(INTENTKEY_RETURNCOLOUR,
                        returncolour
                );
                setResult(RESULT_OK,rv);
                finish();
            }
        });
    }
}
