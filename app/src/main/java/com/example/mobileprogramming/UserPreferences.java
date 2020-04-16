package com.example.mobileprogramming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UserPreferences extends AppCompatActivity {

    private static final String SP_PREFERENCES_NAME = "userInfo";
    private static final String SP_USER_NAME = "userName";
    private static final String SP_USER_AGE = "userAge";
    private static final String SP_USER_HEIGHT= "userHeight";
    private static final String SP_USER_WEIGHT= "userWeight";
    private static final String SP_LANGUAGE_ID = "languageId";
    private static final String SP_APP_THEME_ID= "appTheme";
    private static final String SP_USER_GENDER_ID = "userGenderId";
    private static final String SP_USER_GENDER = "userGender";
    private static final String SP_LANGUAGE = "language";


    private TextView tvUserName, tvBmi;
    private EditText etHeight, etWeight, etAge;
    private Spinner spnGender, spnLanguage;
    private RadioGroup rgAppTheme;
    private Button btnSave, btnBmi;
    private Toolbar toolbar;
    private String userAge, userWeight, userHeight, userGender, language;
    private int userGenderId, languageId, appThemeId;
    //SHARED PREFERENCES
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preferences);

        initView();

        final ArrayAdapter<String> dataAdapterForLanguage = new ArrayAdapter<String>(UserPreferences.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.language_array));
        final ArrayAdapter<String> dataAdapterForGender = new ArrayAdapter<String>(UserPreferences.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.gender_array));

        spnLanguage.setAdapter(dataAdapterForLanguage);
        spnGender.setAdapter(dataAdapterForGender);

        //SHARED PREFEFRENCES
        sp = getSharedPreferences(SP_PREFERENCES_NAME, MODE_PRIVATE);
        editor = sp.edit();
        tvUserName.setText(sp.getString(SP_USER_NAME, null));
        etAge.setText(sp.getString(SP_USER_AGE, null));
        etHeight.setText(sp.getString(SP_USER_HEIGHT, null));
        etWeight.setText(sp.getString(SP_USER_WEIGHT, null));
        spnGender.setSelection(sp.getInt(SP_USER_GENDER_ID,0));
        spnLanguage.setSelection(sp.getInt(SP_LANGUAGE_ID,0));
        rgAppTheme.check( sp.getInt(SP_APP_THEME_ID, -1));

        //BMI HESAPLAMA
        btnBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmptyEditText(etHeight) && !isEmptyEditText(etWeight)) {
                    int bmi = calculateBMI(Double.parseDouble(etHeight.getText().toString()), Double.parseDouble(etWeight.getText().toString()));
                    tvBmi.setText("Body Mass Index: " + String.valueOf(bmi));
                    tvBmi.setVisibility(View.VISIBLE);

                } else {
                    if (isEmptyEditText(etHeight) && isEmptyEditText(etWeight)) {
                        tvBmi.setText("Height and weight information are empty.");
                        tvBmi.setVisibility(View.VISIBLE);

                    } else {
                        if (isEmptyEditText(etHeight)) {
                            tvBmi.setText("Height information is empty.");
                            tvBmi.setVisibility(View.VISIBLE);
                        } else {
                            tvBmi.setText("Weight information is empty.");
                            tvBmi.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    userAge = etAge.getText().toString();
                    userHeight = etHeight.getText().toString();
                    userWeight = etWeight.getText().toString();
                    userGenderId = spnGender.getSelectedItemPosition();
                    languageId = spnLanguage.getSelectedItemPosition();
                    appThemeId = rgAppTheme.getCheckedRadioButtonId();
                    userGender = dataAdapterForGender.getItem(spnGender.getSelectedItemPosition());
                    language = dataAdapterForLanguage.getItem(spnLanguage.getSelectedItemPosition());
                    editor.putString(SP_USER_AGE, userAge);
                    editor.putString(SP_USER_HEIGHT, userHeight);
                    editor.putString(SP_USER_WEIGHT, userWeight);
                    editor.putInt(SP_USER_GENDER_ID, userGenderId); editor.putString(SP_USER_GENDER, userGender);
                    editor.putInt(SP_LANGUAGE_ID, languageId); editor.putString(SP_LANGUAGE, language);
                    editor.putInt(SP_APP_THEME_ID, appThemeId);
                    editor.apply();
                    Toast.makeText(UserPreferences.this, "Save successfully",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(UserPreferences.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initView(){
        tvUserName = findViewById(R.id.tvUserName);
        tvBmi = findViewById(R.id.tvBmi);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etAge = findViewById(R.id.etAge);
        spnGender = findViewById(R.id.spnGender);
        spnLanguage = findViewById(R.id.spnLanguage);
        rgAppTheme = findViewById(R.id.rgAppTheme);
        btnSave = findViewById(R.id.btnSave);
        btnBmi = findViewById(R.id.btnBmi);
        toolbar = findViewById(R.id.tbUserPreferences);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setTitle("User Preferences");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Edit Text boş mu
    boolean isEmptyEditText(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    //BMI HESAPLA
    public int calculateBMI(double height, double weight) {
        double BMI;
        height = height / 100;
        BMI = weight / (height * height);
        return (int) BMI;
    }
}
