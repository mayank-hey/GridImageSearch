package com.mchacko.gridimagesearch.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.mchacko.gridimagesearch.R;
import com.mchacko.gridimagesearch.models.ImageSearchSettings;

public class SettingsActivity extends ActionBarActivity {
    private ImageSearchSettings imageSearchSettings;
    private Spinner spSize;
    private Spinner spColor;
    private Spinner spType;
    private EditText etSite;
    private static final int SETTINGS_REQUEST_CODE = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spSize = (Spinner) findViewById(R.id.spSize);
        spColor = (Spinner) findViewById(R.id.spColor);
        spType = (Spinner) findViewById(R.id.spType);
        etSite = (EditText) findViewById(R.id.etSite);

        imageSearchSettings = (ImageSearchSettings) getIntent().getSerializableExtra("imageSearchSettings");

        spSize.setSelection(imageSearchSettings.size.value);
        spColor.setSelection(imageSearchSettings.color.value);
        spType.setSelection(imageSearchSettings.type.value);
        etSite.setText(imageSearchSettings.site);
        etSite.setSelection(etSite.getText().length());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCancel(View view) {
        this.finish();
    }

    public void onSave(View view) {
        imageSearchSettings.size = ImageSearchSettings.Size.fromInt(spSize.getSelectedItemPosition());
        imageSearchSettings.color = ImageSearchSettings.Color.fromInt(spColor.getSelectedItemPosition());
        imageSearchSettings.type = ImageSearchSettings.Type.fromInt(spType.getSelectedItemPosition());
        imageSearchSettings.site = etSite.getText().toString();
        setResult(RESULT_OK, getIntent().putExtra("imageSearchSettings", imageSearchSettings));
        this.finish();
    }

    public void onReset(View view) {
        spSize.setSelection(0);
        spColor.setSelection(0);
        spType.setSelection(0);
        etSite.setText("");
    }
}
