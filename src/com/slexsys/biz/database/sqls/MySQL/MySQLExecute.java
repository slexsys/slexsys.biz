package com.slexsys.biz.database.sqls.MySQL;

import android.app.Activity;
import android.util.Log;

import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.DataTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 12/14/17.
 */

public class MySQLExecute extends Thread {
    //region fields
    private Activity activity;
    private MySQLInfo info;
    private List<String> queries;
    private boolean isfinish;
    private boolean isok;
    private DataTable dataTable;
    private boolean isExec;
    //endregion

    //region constructors
    public MySQLExecute(MySQLInfo info, String query) {
        this.info = info;
        queries = new LinkedList<String>();
        queries.add(query);
        this.isExec = false;
    }

    public MySQLExecute(MySQLInfo info, List<String> queries) {
        this.info = info;
        this.queries = queries;
        this.isExec = true;
    }
    //endregion

    //region inner methods
    public void run() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(info.getUrl());
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            isok = execute(statement);
            connection.commit();
            statement.close();
            connection.close();
        } catch(SQLException ex) { //java.net.UnknownHostException
            isok = false;
            Log.e("Querys", Arrays.toString(queries.toArray()));
            ex.printStackTrace();
            connectionDialog();
            if(connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            isfinish = true;
        }
    }

    private void connectionDialog() {

    }

    private boolean execute(Statement statement) throws SQLException {
        boolean result = true;
        if (isExec) {
            for (String query : queries) {
                int count = statement.executeUpdate(query);
                result = count != 0;
            }
        } else {
            String query = queries.get(0);
            ResultSet resultSet = statement.executeQuery(query);
            fillDataTable(resultSet);
        }
        return result;
    }

    private void fillDataTable(ResultSet resultSet) throws SQLException {
        dataTable = new DataTable();
        dataTable.addNames(getColumnNames(resultSet));
        while (resultSet.next()) {
            dataTable.add(new DataRow(resultSet));
        }
    }

    private String[] getColumnNames(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();
        String[] names = new String[count];
        for (int i = 0; i < count; ++i) {
            names[i] = metaData.getColumnName(i + 1);
        }
        return names;
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region getters setters
    public MySQLInfo getInfo() {
        return info;
    }

    public void setInfo(MySQLInfo info) {
        this.info = info;
    }

    public List<String> getQueries() {
        return queries;
    }

    public void setQueries(List<String> queries) {
        this.queries = queries;
    }

    public boolean isfinish() {
        return isfinish;
    }

    public void setIsfinish(boolean isfinish) {
        this.isfinish = isfinish;
    }

    public boolean isok() {
        return isok;
    }

    public void setIsok(boolean isok) {
        this.isok = isok;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    //endregion
}
