package com.bridge.soom.Helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.bridge.soom.Fragment.ProfessionalFragment;
import com.bridge.soom.Interface.GetCatDatas;
import com.bridge.soom.Model.Services;
import com.bridge.soom.R;

import java.util.List;

/**
 * Created by Thaher on 07-07-2017.
 */

public class MultiSpinner extends android.support.v7.widget.AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener,GetCatDatas {

    private List<String> items;
    private List<String> itemsid;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (isChecked)
            selected[which] = true;
        else
            selected[which] = false;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuffer spinnerBuffer = new StringBuffer();
        boolean allUnselected = true;
        for (int i = 0; i < items.size(); i++) {
            if (selected[i]) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
                allUnselected = false;
            }
        }
        String spinnerText;
        if (!allUnselected) {
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        } else {
            spinnerText = defaultText;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.simple_spinner_item,
                new String[] { spinnerText });
        setAdapter(adapter);
        listener.onItemsSelected(selected);
    }

    @Override
    public boolean performClick() {
        if(items.size()>0){ AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;}
        else return false;
    }

    public void setItems(List<String> items, String allText,
                         MultiSpinnerListener listener) {
        this.items = items;
        this.defaultText = allText;
        this.listener = listener;

        // all not selected by default
        selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++)
            selected[i] = false;

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.simple_spinner_item, new String[] { allText });
        setAdapter(adapter);
    }
    public void setItemsEdting(List<String> filters,List<String> filterid, String allText, MultiSpinnerListener listener, Services editingService) {

        Log.i("EDITINGSERVICE","Set item editing   :");

        this.items = filters;
        this.itemsid = filterid;
        this.defaultText = allText;
        this.listener = listener;

        // all not selected by default
        selected = new boolean[itemsid.size()];
//        for (int i = 0; i < selected.length; i++)
//            selected[i] = false;
        StringBuffer spinnerBuffer = new StringBuffer();
        boolean allUnselected = true;
        for(int j=0;j<itemsid.size();j++)
        {
            Log.i("EDITINGSERVICE"," for(int j   :"+j);
            Boolean ispresent = false;

            for(int k = 0;k<editingService.getSubServiceId().size();k++) {
                Log.i("EDITINGSERVICE", " for(int k   :" + k);

                Log.i("EDITINGSERVICE","Checking   :"+itemsid.get(j)+"   "+editingService.getSubServiceId().get(k));

                if (itemsid.get(j).trim().equals(editingService.getSubServiceId().get(k).trim())) {
                    ispresent = true;
                    Log.i("EDITINGSERVICE", "ispresent=true:");

                }
            }

                if(ispresent)
                { Log.i("EDITINGSERVICE", " FOUND 1");
                    selected[j] = true;
                    spinnerBuffer.append(items.get(j));
                    spinnerBuffer.append(", ");
                    allUnselected = false;
                    Log.i("EDITINGSERVICE", " allUnselected = false; "+allUnselected);

                }
                else {
               selected[j] = false;
                }



        }



        String spinnerText;
        Log.i("EDITINGSERVICE", " allUnselected = false; "+allUnselected);

        if (!allUnselected) {
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        } else {
            spinnerText = defaultText;
        }
        Log.i("EDITINGSERVICE", " spinnerText "+spinnerText);


        Log.i("EDITINGSERVICE", " Setting adapter");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.simple_spinner_item,
                new String[] { spinnerText });
        setAdapter(adapter);
        listener.onItemsSelected(selected);

    }

    @Override
    public void failedtoConnect() {

    }

    @Override
    public void GetCategoryListFailed(String msg) {

    }

    @Override
    public void GetCategoryList(List<String> catid, List<String> catname) {

    }

    @Override
    public void GetSubCategoryList(List<String> subcatid, List<String> subcatname, String highestWage) {
        items.clear();
        for(int i=0;i<subcatname.size();i++)
        {
            items.add(subcatname.get(i));
        }


       setItems(items,"Choosefilter",listener);
        Log.i("Reg2_submit","RetrieveGetCategoryListTask ---got2" );
    }

    @Override
    public void GetSubCategoryListFailed(String msg) {




    }

    @Override
    public void GetStateCategoryList(List<String> subcatid, List<String> subcatname) {

    }

    @Override
    public void GetStateListFailed(String msg) {

    }

    @Override
    public void GetCityCategoryList(List<String> subcatid, List<String> subcatname, List<String> lat, List<String> lng) {

    }

    @Override
    public void GetCityListFailed(String msg) {

    }

    @Override
    public void GetCountryCategoryList(List<String> subcatid, List<String> subcatname) {

    }

    @Override
    public void GetCountryListFailed(String msg) {

    }



    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }
}