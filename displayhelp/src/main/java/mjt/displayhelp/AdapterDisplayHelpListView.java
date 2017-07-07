package mjt.displayhelp;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mike on 13/06/2017.
 */

public class AdapterDisplayHelpListView extends BaseAdapter {

    private ArrayList<String> alist;
    private Context context;
    private String heading_indicator;
    private String tab1_indicator;
    private String tab2_indicator;
    private float headingTextSize;
    private float normalTextSize;
    int indentSize;
    private int colour;

    public AdapterDisplayHelpListView(Context context,
                                      ArrayList<String> list,
                                      int colour,
                                      float headingTextSize,
                                      float normalTextSize,
                                      int indentSize) {
        this.alist = list;
        this.context = context;
        this.colour = colour;
        heading_indicator = context.getResources().getString(R.string.heading_indicator);
        tab1_indicator = context.getResources().getString(R.string.tab1_indicator);
        tab2_indicator = context.getResources().getString(R.string.tab2_indicator);
        this.headingTextSize = headingTextSize;
        this.normalTextSize = normalTextSize;
        this.indentSize = indentSize;
    }

    public int getCount() {
        return alist.size();
    }
    public Object getItem(int position) {
        return alist.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertview, ViewGroup viewparent) {
        if (convertview == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(
                            Activity.LAYOUT_INFLATER_SERVICE
                    );
            convertview = inflater.inflate(R.layout.helplistviewitem,null);
        }

        TextView heading = (TextView) convertview.findViewById(
                R.id.helpviewlist_heading
        );

        TextView normal = (TextView) convertview.findViewById(
                R.id.helpviewlist_normal
        );
        TextView tab1 = (TextView) convertview.findViewById(
                R.id.helpviewlist_tab1
        );
        TextView tab2 = (TextView) convertview.findViewById(
                R.id.helpviewlist_tab2
        );

        heading.setTextColor(colour);
        heading.setTextSize(TypedValue.COMPLEX_UNIT_SP,headingTextSize);
        normal.setTextSize(TypedValue.COMPLEX_UNIT_SP,normalTextSize);
        tab1.setTextSize(TypedValue.COMPLEX_UNIT_SP,normalTextSize);
        tab2.setTextSize(TypedValue.COMPLEX_UNIT_SP,normalTextSize);

        heading.setPadding(indentSize,0,indentSize,0);
        normal.setPadding(indentSize * 2,0,indentSize,0);
        tab1.setPadding(indentSize * 3,0,indentSize,0);
        tab2.setPadding(indentSize * 4, 0,indentSize,0);
        String item = alist.get(position);
        heading.setVisibility(View.GONE);
        normal.setVisibility(View.GONE);
        tab1.setVisibility(View.GONE);
        tab2.setVisibility(View.GONE);

        if (item.contains(heading_indicator)) {
            convertItem(item,heading_indicator,heading);
            return convertview;
        }

        if (item.contains(tab1_indicator)) {
            convertItem(item,tab1_indicator,tab1);
            return convertview;
        }
        if (item.contains(tab2_indicator)) {
            convertItem(item,tab2_indicator,tab2);
            return convertview;
        }
        convertItem(item,"",normal);

        return convertview;
    }

    /**
     * If Item includes a type indicator, remove the indicator, set the
     * respective view to be visible and populate the view with the data
     * (allowing for HTML codes).
     * @param item      The String for the item
     * @param indicator The Indicator none results in normal
     * @param view      The view currently being processed
     */
    private void convertItem(String item, String indicator, TextView view) {
        // If an indicator exists then remove it
        if (indicator.length() > 0) {
            item = item.replace(indicator,"");
        }
        // set the view's text allowing HTML codes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.setText(Html.fromHtml(item,Html.FROM_HTML_MODE_COMPACT));
        } else {
            view.setText(Html.fromHtml(item));
        }
        view.setVisibility(View.VISIBLE);
    }
}
