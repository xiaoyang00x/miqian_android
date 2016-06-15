package com.miqian.mq.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.miqian.mq.encrypt.Encrypt;
import com.miqian.mq.entity.JpushInfo;

import java.util.ArrayList;
import java.util.List;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private SQLiteDatabase mDatabase = null;


    private static final String Jpush_INFO_TABLE_NAME = "jpush_info";
    private static final String Jpush_INFO_CREATE_KEY = " (id integer not null, key varchar(50) not null, value varchar(50) not null);";
    private static final String Jpush_INFO_CREATE_TABLE = "create table if not exists " + Jpush_INFO_TABLE_NAME + Jpush_INFO_CREATE_KEY;

    private static final String Jpush_TABLE_NAME = "jpush";
    private static final String Jpush_CREATE_KEY = " (id integer primary key autoincrement, userId varchar(50) not null, notice_id varchar(50) not null);";
    // private static final String Jpush_CREATE_KEY =
    // " (id integer primary key autoincrement, notice_id varchar(50) not null);";
    private static final String Jpush_CREATE_TABLE = "create table if not exists " + Jpush_TABLE_NAME + Jpush_CREATE_KEY;

    private static MyDataBaseHelper mInstance;

    public static synchronized MyDataBaseHelper getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new MyDataBaseHelper(mContext);
        }
        return mInstance;
    }

    public static synchronized MyDataBaseHelper getInstance() {
        return mInstance;
    }

    public MyDataBaseHelper(Context context) {
        super(context, "db", null, VERSION);
        if (mDatabase == null) {
            mDatabase = getWritableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Jpush_CREATE_TABLE);
        db.execSQL(Jpush_INFO_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    protected void finalize() throws Throwable {
        closeDatabase();
        super.finalize();
    }

    private SQLiteDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = getWritableDatabase();
        }
        return mDatabase;
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }
    }

    public void updateInfoTable(String tablename, String id, String key, String value) {
        SQLiteDatabase db = getDatabase();
        if (value == null) {
            return;
        }

        try {
            key = Encrypt.encrypt(key);
            value = Encrypt.encrypt(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        Cursor cursorTemp = db.query(tablename, null, "id=? and key=? ", new String[]{id, key}, null, null, null);
        if (cursorTemp.getCount() > 0) {
            values.put("value", value);
            db.update(tablename, values, "id=? and key=? ", new String[]{id, key});
        } else {
            values.put("id", id);
            values.put("key", key);
            values.put("value", value);
            db.insert(tablename, null, values);
        }
        cursorTemp.close();
    }

    public void inserInfoTable(String tablename, long id, String key, String value) {
        SQLiteDatabase db = getDatabase();
        if (value == null) {
            return;
        }
        try {
            key = Encrypt.encrypt(key);
            value = Encrypt.encrypt(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("key", key);
        values.put("value", value);
        db.insert(tablename, null, values);
    }

    public void delete(String tableName, JpushInfo jpushInfo) {
        SQLiteDatabase db = getDatabase();
        int id = db.delete(tableName, "notice_id" + "=?", new String[]{jpushInfo.getId()});
    }

    public void detetjpushInfo(JpushInfo jpushInfo) {
        delete(Jpush_TABLE_NAME, jpushInfo);
    }

    public void deleteall(String userId) {
        SQLiteDatabase db = getDatabase();
        db.delete(Jpush_TABLE_NAME, "userId" + "=?", new String[]{userId});
    }


    public void recordJpush(JpushInfo jpushInfo) {

        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.query(Jpush_TABLE_NAME, null, "userId=? and notice_id=?", new String[]{jpushInfo.getUserId(), jpushInfo.getId()}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String rowId = cursor.getString(0);
            updateInfoTable(Jpush_INFO_TABLE_NAME, rowId, "notice_id", jpushInfo.getId());
            updateInfoTable(Jpush_INFO_TABLE_NAME, rowId, "title", jpushInfo.getTitle());
            updateInfoTable(Jpush_INFO_TABLE_NAME, rowId, "content", jpushInfo.getContent());
            updateInfoTable(Jpush_INFO_TABLE_NAME, rowId, "uritype", jpushInfo.getUriType());
            updateInfoTable(Jpush_INFO_TABLE_NAME, rowId, "url", jpushInfo.getUrl());
            updateInfoTable(Jpush_INFO_TABLE_NAME, rowId, "state", jpushInfo.getState());
            updateInfoTable(Jpush_INFO_TABLE_NAME, rowId, "userId", jpushInfo.getUserId());
            updateInfoTable(Jpush_INFO_TABLE_NAME, rowId, "time", jpushInfo.getTime());
            updateInfoTable(Jpush_INFO_TABLE_NAME, rowId, "pushSource", jpushInfo.getPushSource());
            updateInfoTable(Jpush_INFO_TABLE_NAME, rowId, "token", jpushInfo.getToken());
            updateInfoTable(Jpush_INFO_TABLE_NAME, rowId, "ext", jpushInfo.getExt());
        } else {
            ContentValues values = new ContentValues();
            values.put("userId", jpushInfo.getUserId());
            values.put("notice_id", jpushInfo.getId());
            long rowId = db.insert(Jpush_TABLE_NAME, null, values);
            if (rowId > 0) {
                inserInfoTable(Jpush_INFO_TABLE_NAME, rowId, "notice_id", jpushInfo.getId());
                inserInfoTable(Jpush_INFO_TABLE_NAME, rowId, "title", jpushInfo.getTitle());
                inserInfoTable(Jpush_INFO_TABLE_NAME, rowId, "content", jpushInfo.getContent());
                inserInfoTable(Jpush_INFO_TABLE_NAME, rowId, "uritype", jpushInfo.getUriType());
                inserInfoTable(Jpush_INFO_TABLE_NAME, rowId, "url", jpushInfo.getUrl());
                inserInfoTable(Jpush_INFO_TABLE_NAME, rowId, "state", jpushInfo.getState());
                inserInfoTable(Jpush_INFO_TABLE_NAME, rowId, "userId", jpushInfo.getUserId());
                inserInfoTable(Jpush_INFO_TABLE_NAME, rowId, "time", jpushInfo.getTime());
                inserInfoTable(Jpush_INFO_TABLE_NAME, rowId, "pushSource", jpushInfo.getPushSource());
                inserInfoTable(Jpush_INFO_TABLE_NAME, rowId, "token", jpushInfo.getToken());
                inserInfoTable(Jpush_INFO_TABLE_NAME, rowId, "ext", jpushInfo.getExt());
            }
        }
        cursor.close();
    }


    public List<JpushInfo> getjpushInfo(String userId) {
        SQLiteDatabase db = getDatabase();
        ArrayList<JpushInfo> jpushArrayList = new ArrayList<JpushInfo>();
        JpushInfo jpushInfo = null;
        Cursor cursor = db.query(Jpush_TABLE_NAME, null, "userId=?", new String[]{userId}, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            jpushInfo = new JpushInfo();
            String rowId = cursor.getString(0);
            Cursor cursorTemp = db.query(Jpush_INFO_TABLE_NAME, null, "id=?", new String[]{rowId}, null, null, null);
            for (cursorTemp.moveToFirst(); !cursorTemp.isAfterLast(); cursorTemp.moveToNext()) {
                String key = cursorTemp.getString(1);
                String value = cursorTemp.getString(2);

                try {
                    key = Encrypt.decrypt(key);
                    value = Encrypt.decrypt(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (key.equals("title")) {
                    jpushInfo.setTitle(value);
                } else if (key.equals("content")) {
                    jpushInfo.setContent(value);
                } else if (key.equals("userId")) {
                    jpushInfo.setUserId(userId);
                } else if (key.equals("notice_id")) {
                    jpushInfo.setId(value);
                } else if (key.equals("uritype")) {
                    jpushInfo.setUriType(value);
                } else if (key.equals("url")) {
                    jpushInfo.setUrl(value);
                } else if (key.equals("state")) {
                    jpushInfo.setState(value);
                } else if (key.equals("time")) {
                    jpushInfo.setTime(value);
                } else if (key.equals("pushSource")) {
                    jpushInfo.setPushSource(value);
                }else if (key.equals("token")) {
                    jpushInfo.setToken(value);
                } else if (key.equals("ext")) {
                    jpushInfo.setExt(value);
                }
            }
            cursorTemp.close();
            jpushArrayList.add(jpushInfo);
        }
        cursor.close();
        return jpushArrayList;
    }

    public JpushInfo getJpushInfo(String notice_id) {
        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.query(Jpush_TABLE_NAME, null, "notice_id=?", new String[]{notice_id}, null, null, null);
        JpushInfo jpushInfo = null;
        if (cursor.getCount() > 0) {
            jpushInfo = new JpushInfo();
            cursor.moveToFirst();
            String rowId = cursor.getString(0);
            Cursor cursorTemp = db.query(Jpush_INFO_TABLE_NAME, null, "id=?", new String[]{rowId}, null, null, null);
            for (cursorTemp.moveToFirst(); !cursorTemp.isAfterLast(); cursorTemp.moveToNext()) {
                String key = cursorTemp.getString(1);
                String value = cursorTemp.getString(2);
                try {
                    key = Encrypt.decrypt(key);
                    value = Encrypt.decrypt(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (key.equals("title")) {
                    jpushInfo.setTitle(value);
                } else if (key.equals("content")) {
                    jpushInfo.setContent(value);
                } else if (key.equals("userId")) {
                    jpushInfo.setUserId(value);
                } else if (key.equals("notice_id")) {
                    jpushInfo.setId(value);
                } else if (key.equals("uritype")) {
                    jpushInfo.setUriType(value);
                } else if (key.equals("url")) {
                    jpushInfo.setUrl(value);
                } else if (key.equals("state")) {
                    jpushInfo.setState(value);
                } else if (key.equals("time")) {
                    jpushInfo.setTime(value);
                } else if (key.equals("pushSource")) {
                    jpushInfo.setPushSource(value);
                }else if (key.equals("token")) {
                    jpushInfo.setToken(value);
                }else if (key.equals("ext")) {
                    jpushInfo.setExt(value);
                }
            }
            cursorTemp.close();
        }
        cursor.close();
        return jpushInfo;
    }
}
