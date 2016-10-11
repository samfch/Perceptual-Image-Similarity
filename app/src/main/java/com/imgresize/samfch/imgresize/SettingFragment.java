package com.imgresize.samfch.imgresize;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;
import java.util.Arrays;

/**
 * Created by samfch on 8/6/16.
 */
public class SettingFragment extends PreferenceFragment {

    private DialogProperties properties;
    private FilePickerDialog fileDialog;
    private SharedPreferences sp;
    private Preference filePicker;
    private Preference thresholdPref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.setting_fragment);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        properties=new DialogProperties();
        properties.selection_mode= DialogConfigs.SINGLE_MODE;
        properties.selection_type= DialogConfigs.DIR_SELECT;
        properties.root=new File("/");
        properties.extensions=null;

        fileDialog = new FilePickerDialog(getActivity(),properties);
        filePicker = findPreference("folderPicker");
        thresholdPref = findPreference("threshold");
        //init summary
//        thresholdPref.setSummary(sp.getString("threshold","90"));
//        filePicker.setSummary(sp.getString("folderPicker","/sdcard/Download"));


        fileDialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                Log.e("PATH", Arrays.toString(files));
//                fileDialog.dismiss();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("folderPicker", files[0]);
                editor.commit();

            }
        });


        filePicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                fileDialog.show();

                return true;
            }
        });

//
//        filePicker.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object o) {
//                preference.setSummary(sp.getString("folderPicker","/sdcard/Download"));
//                return true;
//            }
//        });
//
//        thresholdPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object o) {
//                preference.setSummary(sp.getString("threshold","90"));
//                return true;
//            }
//        });

    }
}