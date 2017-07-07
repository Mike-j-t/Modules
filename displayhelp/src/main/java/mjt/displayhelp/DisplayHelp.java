package mjt.displayhelp;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mike on 4/07/2017.
 */

public class DisplayHelp {

    public static final String INTENTKEY_TITLE = "ik_title";
    public static final String INTENTKEY_HELPTEXT = "ik_helptext";
    public static final String INTENTKEY_DSPSIZEPERCENTAGE = "ik_dspszpcnt";
    public static final String INTENTKEY_FULLDISPLAY = "ik_fulldisplay";
    public static final String INTENTKEY_COLOUR = "ik_colour";
    public static final String INTENTKEY_BACKGROUND = "ik_background";
    public static final String INTENTKEY_HEADINGTEXTSIZE = "ik_hdngtextsize";
    public static final String INTENTKEY_NORMALTEXTSIZE = "ik_nrmltxtsize";
    public static final String INTENTKEY_BASEINDENT = "ik_baseindent";

    private String helptext;

    public DisplayHelp(Context context,
                       String title,
                       ArrayList<String> helptext,
                       int displaysizepercentage,
                       boolean fulldisplay,
                       int basecolour,
                       int backgroundcolour,
                       float headingtextsize_sp,
                       float normaltextsize_sp,
                       int baseindent_dp) {
        initialiseDisplayHelp(context,
                title,
                displaysizepercentage,
                fulldisplay,
                basecolour,
                backgroundcolour,
                helptext,
                headingtextsize_sp,
                normaltextsize_sp,
                baseindent_dp
        );
    }

    public DisplayHelp(Context context,
                       String title,
                       int string_array_resource_id,
                       int displaysizepercentage,
                       boolean fulldisplay,
                       int basecolour,
                       int backgroundcolour,
                       float headingtextsize_sp,
                       float normaltextsize_sp,
                       int baseindent_dp) {

        initialiseDisplayHelp(context,
                title,
                displaysizepercentage,
                fulldisplay,
                basecolour,
                backgroundcolour,
                convertResourceToArrayList(context,
                        string_array_resource_id
                ),
                headingtextsize_sp,
                normaltextsize_sp,
                baseindent_dp
        );
    }

    private void initialiseDisplayHelp(Context context,
                                       String title,
                                       int dspszpcent,
                                       boolean fulldisplay,
                                       int baseColour,
                                       int backgroundColour,
                                       ArrayList<String> helptext,
                                       float headingtextsize_sp,
                                       float normaltextsize_sp,
                                       int baseindent_dp) {
        Intent intent = new Intent(context, DisplayHelpActivity.class);
        intent.putExtra(INTENTKEY_TITLE,title);
        intent.putExtra(INTENTKEY_DSPSIZEPERCENTAGE,dspszpcent);
        intent.putExtra(INTENTKEY_FULLDISPLAY,fulldisplay);
        intent.putExtra(INTENTKEY_COLOUR,baseColour);
        intent.putExtra(INTENTKEY_BACKGROUND,backgroundColour);
        intent.putExtra(INTENTKEY_HELPTEXT,helptext);
        intent.putExtra(INTENTKEY_HEADINGTEXTSIZE,headingtextsize_sp);
        intent.putExtra(INTENTKEY_NORMALTEXTSIZE,normaltextsize_sp);
        intent.putExtra(INTENTKEY_BASEINDENT,baseindent_dp);
        context.startActivity(intent);

    }

    public static ArrayList<String> convertResourceToArrayList(Context context, int id) {
        CharSequence[] csa = context.getResources().getTextArray(id);
        String[] sa = convertToStringArray(csa);
        return new ArrayList<>(Arrays.asList(sa));
    }

    private static String[] convertToStringArray(CharSequence[] charSequences) {
        if (charSequences instanceof String[]) {
            return (String[]) charSequences;
        }

        String[] strings = new String[charSequences.length];
        for (int index = 0; index < charSequences.length; index++) {
            strings[index] = charSequences[index].toString();
        }

        return strings;
    }
}
