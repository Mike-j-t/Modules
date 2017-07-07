package mjt.displayhelp;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import static mjt.displayhelp.DisplayHelp.*;


/**
 * Created by Mike on 4/07/2017.
 */

public class DisplayHelpActivity extends Activity {

    int basecolour;
    int backcolour;
    boolean fulldisplay;
    int fulldisplaydivisor = 1;
    LinearLayout outermost;
    ListView hlv;
    ArrayList<String> helptext;
    AdapterDisplayHelpListView adhlv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayhelp);
        outermost = (LinearLayout) this.findViewById(R.id.outermost);
        hlv = (ListView) this.findViewById(R.id.helptextlist);

        basecolour = this.getIntent().getIntExtra(INTENTKEY_COLOUR,0xffffffff);
        backcolour = this.getIntent().getIntExtra(INTENTKEY_BACKGROUND,0xccffffff);
        helptext = this.getIntent().getStringArrayListExtra(INTENTKEY_HELPTEXT);
        /* If not to use full display area then set the divsor from 1 to 2 so
        that the height of the display is halved.
         */
        fulldisplay = this.getIntent().getBooleanExtra(INTENTKEY_FULLDISPLAY,true);
        if (!fulldisplay) {
            fulldisplaydivisor = 2;
        }

        // Set the title of the activities display
        this.setTitle(
            this.getIntent().getStringExtra(INTENTKEY_TITLE)
        );
        // Set the background colour
        this.getWindow().setBackgroundDrawable(
                argbToColor(basecolour)
        );
        // Set the background colours
        outermost.setBackgroundColor( backcolour);
        hlv.setBackgroundColor(backcolour);
        // Set the Display size based upon the actual device's display size
        setDisplaySize();

        // Create the ListView Adapter
        adhlv = new AdapterDisplayHelpListView(this,helptext,basecolour,
                this.getIntent().getFloatExtra(INTENTKEY_HEADINGTEXTSIZE,20f),
                this.getIntent().getFloatExtra(INTENTKEY_NORMALTEXTSIZE,16f),
                this.getIntent().getIntExtra(INTENTKEY_BASEINDENT,12)
        );
        // Tie the adpater to the ListView
        hlv.setAdapter(adhlv);
    }

    /**
     * Finish the Activity if the DONE button is clicked
     * @param v The View (Button) that was clicked
     */
    public void doneButton(View v) {
        this.finish();
    }
    /**
     * Set the display size as a percentage, as passed to activity as an
     * intentExtra, of the actual DisplaySize. If fulldisplay is false
     * then the height is halved.
     *
     */
    private void setDisplaySize() {

        // Get the device's display information
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int dspszpcnt =
                this.getIntent().getIntExtra(
                        INTENTKEY_DSPSIZEPERCENTAGE,
                        90
                );

        // Determine the height to use after applying the percentage
        int dspheight =
                Math.round(
                        ((float) dm.heightPixels * ((float) dspszpcnt / 100f ))
                                / (float) fulldisplaydivisor
                );

        // Determine the width to use by applying the percentage
        int dspwidth =
                Math.round(
                        ((float)dm.widthPixels * ((float) dspszpcnt / 100f))
                );

        // Get the encompassing layout's parameters
        FrameLayout.LayoutParams flp =
                (FrameLayout.LayoutParams) outermost.getLayoutParams();

        // Apply the new values
        flp.height = dspheight;
        flp.width = dspwidth;
        flp.gravity = Gravity.CENTER;
    }

    /**************************************************************************
     * Convert an argb integer into a ColorDrawable i.e.
     * exract the 4 componants Alpha, Red, Green and Blue and use them to
     * build the ColorDrawable
     * i.e.
     * @param argb  The colour to ve converted
     * @return      The resultant ColorDrawable
     */
    private ColorDrawable argbToColor(int argb) {
        int alphapart = (argb >> 24) & 0xff;
        int redpart = (argb >> 16) & 0xff;
        int greenpart = (argb >> 8) & 0xff;
        int bluepart = argb & 0xff;
        return  new ColorDrawable(Color.argb(
                alphapart,
                redpart,
                greenpart,
                bluepart
        ));
    }
}
