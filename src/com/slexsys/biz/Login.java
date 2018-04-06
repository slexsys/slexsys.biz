package com.slexsys.biz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.slexsys.biz.comp.LoginInfo;
import com.slexsys.biz.database.items.iUser;
import com.slexsys.biz.database.sqls.MySQL.MySQLInfo;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.json.JSON_STD;
import com.slexsys.biz.main.menu.MainMenu;
import com.slexsys.biz.main.operations.Operation;
import com.slexsys.biz.main.operations.json.OperationGenerator;
import com.slexsys.biz.main.operations.json.OperationJSON;
import com.slexsys.biz.main.operations.json.OperationsJSON;
import com.slexsys.biz.main.report.Report;
import com.slexsys.biz.main.report.json.ReportGenerator;
import com.slexsys.biz.main.report.json.ReportJSON;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Login extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Init();
        Test();
    }

    private void TestOperation() {
        LoginInfo.user = GO.users.items.get(0);
        LoginInfo.object = GO.objects.items.get(0);
        OperationsJSON osjson = OperationGenerator.generateOperations();
        for (OperationJSON ojson : osjson) {
            String json = ojson.toJSON().toString();
            Log.d("O JSON > ", json);
            OperationJSON ojson1 = OperationJSON.fromJSON(JSON_STD.fromString(json));
            Log.d("O JSON name> ", ojson1.getName());
        }
        Intent intent = new Intent(getApplicationContext(), Operation.class);
        intent.putExtra(Operation.PUTTER_POS, 0);
        startActivity(intent);
    }

    private void TestReport() {
        //ReportJSON report = ReportGenerator.GenerateReportOperation();
        ReportJSON report = ReportGenerator.GenerateReportItemInStockMySQL();
        Intent intent = new Intent(getApplicationContext(), Report.class);
        intent.putExtra("JSON", report.toJSON().toString());
        startActivity(intent);
    }

    private void Test() {
        StartMainMenu(GO.users.items.get(0));
        //startActivity(new Intent(getApplicationContext(), Report.class));
        //Edit.type = Menus.Edit.Item;
        //startActivity(new Intent(getApplicationContext(), Edit.class));
        //TextView tv = (TextView) findViewById(R.id.textViewPing);
        //tv.setText(executeCmd("ping -c 1 -w 1 192.168.1.53", false));
        //mysql();
    }

    //region views
    private Spinner spinnerLogin;
    private EditText editTextPassword;
    private Button btnLogin;
    //endregion

    //region methods
    private void Init() {
        //InitSQLiteDatabase();
        InitMySQLDatabase();
        InitControls();
    }

    private void InitSQLiteDatabase() {
        //String file = Login.this.getFilesDir().getAbsolutePath() + "/database.sqlite";
        String file = "/storage/sdcard0/Database/database.sqlite";
        File f = new File(file);
        if(f.exists()) {
            GO.Fill(file);
        } else {
            Toast.makeText(this, "database not exist", Toast.LENGTH_SHORT).show();
        }
    }

    private void InitMySQLDatabase() {
        iSQL.setActivity(this);
        GO.Fill(new MySQLInfo());
    }

    private void InitControls() {
        spinnerLogin = (Spinner) findViewById(R.id.spinnerLogin);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, GetUserNames());
        spinnerLogin.setAdapter(adapter);

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        btnLogin = (Button) findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ClickButtonLogin();
            }
        });
    }

    private List<String> GetUserNames() {
        List<String> list = new LinkedList<String>();
        for (iUser u : GO.users.items){
            list.add(u.getName() + "(" + u.getCard() + ")");
        }
        return list;
    }

    private void ClickButtonLogin() {
        iUser user = GO.users.items.get(spinnerLogin.getSelectedItemPosition());
        if (editTextPassword.getText().toString().equals(user.getCard())){
            editTextPassword.setText("");
            StartMainMenu(user);
        } else {
            Toast.makeText(this, R.string.wrong_password, Toast.LENGTH_SHORT).show();
        }
    }

    private void StartMainMenu(iUser user) {
        LoginInfo.user = user;
        LoginInfo.object = GO.objects.items.get(0);
        GO.setPig(0);
        GO.setPog(2);
        GO.setCurrencyId(1);
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
    }
    //endregion
}
