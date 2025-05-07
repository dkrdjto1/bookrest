package com.example.myapplication.db.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.vo.InterParkItemVo;
import com.example.myapplication.vo.ReadingVo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * SQLite 클래스
 */
public class SQLiteHelper {

    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase sqlLiteDatabase;
    private DatabaseHelper databaseHelper;
    private Context context;
    private String TAG = "SQL ";

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(SQLiteDataBase.CreateDB._CREATE_TABLE);
            db.execSQL(SQLiteDataBase.CreateDB._CREATE_TABLE_READING);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + SQLiteDataBase.CreateDB._TABLENAME);
            db.execSQL("DROP TABLE IF EXISTS " + SQLiteDataBase.CreateDB._TABLENAME_READING);
            db.execSQL(SQLiteDataBase.CreateDB._CREATE_TABLE);
            db.execSQL(SQLiteDataBase.CreateDB._CREATE_TABLE_READING);
            onCreate(db);
        }
    }

    public SQLiteHelper(Context context){
        this.context = context;
    }

    public SQLiteHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqlLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        databaseHelper.onCreate(sqlLiteDatabase);
    }

    public void onUpgrade(){
        databaseHelper.onUpgrade(sqlLiteDatabase, 0, 1);
    }

    public void close(){
        sqlLiteDatabase.close();
    }

    /**
     * itemId(키), title, description, coverSmallUrl, coverLargeUrl, publisher, author, link, isbn
     * @return
     */

    /**
     * 서재 추가
     * @param itemId
     * @param title
     * @param description
     * @param coverSmallUrl
     * @param coverLargeUrl
     * @param publisher
     * @param author
     * @param link
     * @param isbn
     * @return
     */
    public long insertColumn(String itemId, String title, String description, String coverSmallUrl, String coverLargeUrl,
                             String publisher, String author, String link, String isbn){
        DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String date = format.format(Calendar.getInstance().getTime());

        ContentValues values = new ContentValues();
        values.put(SQLiteDataBase.CreateDB.ITEMID, itemId);
        values.put(SQLiteDataBase.CreateDB.TITLE, title);
        values.put(SQLiteDataBase.CreateDB.DESCRIPTION, description);
        values.put(SQLiteDataBase.CreateDB.COVERSMALLURL, coverSmallUrl);
        values.put(SQLiteDataBase.CreateDB.COVERLARGEURL, coverLargeUrl);
        values.put(SQLiteDataBase.CreateDB.PUBLISHER, publisher);
        values.put(SQLiteDataBase.CreateDB.AUTHOR, author);
        values.put(SQLiteDataBase.CreateDB.LINK, link);
        values.put(SQLiteDataBase.CreateDB.ISBN, isbn);
        values.put(SQLiteDataBase.CreateDB.REGDATE, date);
        values.put(SQLiteDataBase.CreateDB.UPDDATE, "");

        Log.e(TAG, "서재 추가 " + itemId);
        return sqlLiteDatabase.insert(SQLiteDataBase.CreateDB._TABLENAME, null, values);
    }

    /**
     *  서재 전체 삭제
     * @return
     */
    public boolean deleteColumns(){

        Log.e(TAG, "서재 전체 삭제");
        return sqlLiteDatabase.delete(SQLiteDataBase.CreateDB._TABLENAME, null, null) > 0;
    }

    /**
     * 서재 삭제
     * @param itemId
     * @return
     */
    public boolean deleteColumn(String itemId){

        Log.e(TAG, "서재 삭제 " + itemId);
        return sqlLiteDatabase.delete(SQLiteDataBase.CreateDB._TABLENAME, "itemId=" + itemId, null) > 0;
    }

    /**
     * 독서 기록 저장
     * @param itemId
     * @param readingpagenumber
     * @return
     */
    public long insertReadingColumn(String itemId, String readingpagenumber, String memo, String alarm){
        DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String date = format.format(Calendar.getInstance().getTime());

        ContentValues values = new ContentValues();
        values.put(SQLiteDataBase.CreateDB.ITEMID, itemId);
        values.put(SQLiteDataBase.CreateDB.READINGDATE, date);
        values.put(SQLiteDataBase.CreateDB.READINGPAGENUMBER, readingpagenumber);
        values.put(SQLiteDataBase.CreateDB.MEMO, memo);
        values.put(SQLiteDataBase.CreateDB.ALARM, alarm);
        values.put(SQLiteDataBase.CreateDB.REGDATE, date);
        values.put(SQLiteDataBase.CreateDB.UPDDATE, date);

        Log.e(TAG, "독서기록 추가 " + itemId);

        return sqlLiteDatabase.insert(SQLiteDataBase.CreateDB._TABLENAME_READING, null, values);
    }

    /**
     * 독서 기록 수정
     * @param idx
     * @param itemId
     * @param readingpagenumber
     * @return
     */
    public boolean updateReadingColumn(String idx, String itemId, String readingpagenumber, String memo, String alarm){
        DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String date = format.format(Calendar.getInstance().getTime());

        ContentValues values = new ContentValues();
        values.put(SQLiteDataBase.CreateDB.IDX, idx);
        values.put(SQLiteDataBase.CreateDB.ITEMID, itemId);
        values.put(SQLiteDataBase.CreateDB.READINGDATE, date);
        values.put(SQLiteDataBase.CreateDB.READINGPAGENUMBER, readingpagenumber);
        values.put(SQLiteDataBase.CreateDB.MEMO, memo);
        values.put(SQLiteDataBase.CreateDB.ALARM, alarm);
        values.put(SQLiteDataBase.CreateDB.UPDDATE, date);

        Log.e(TAG, "독서기록 수정 " + itemId);

        return sqlLiteDatabase.update(SQLiteDataBase.CreateDB._TABLENAME_READING, values, "idx=" + idx + " and itemId=" + itemId, null) > 0;
    }

    /**
     * 알림 update
     * @param idx
     * @param itemId
     * @param alarm
     * @return
     */
    public boolean updateReadingAlarmColumn(String idx, String itemId, String alarm){
        ContentValues values = new ContentValues();
        values.put(SQLiteDataBase.CreateDB.IDX, idx);
        values.put(SQLiteDataBase.CreateDB.ITEMID, itemId);
        values.put(SQLiteDataBase.CreateDB.ALARM, alarm);

        Log.e(TAG, "독서기록 수정 " + itemId);

        return sqlLiteDatabase.update(SQLiteDataBase.CreateDB._TABLENAME_READING, values, "idx=" + idx + " and itemId=" + itemId, null) > 0;
    }

    /**
     * 독서 기록 삭제
     * @param idx
     * @return
     */
    public boolean deleteReadingColumn(String idx, String itemId){

        Log.e(TAG, "독서기록 삭제 " + idx);
        return sqlLiteDatabase.delete(SQLiteDataBase.CreateDB._TABLENAME_READING, "idx=" + idx + " and itemId=" + itemId, null) > 0;
    }

    /**
     * 독서 기록 itemId 별 삭제
     * @param itemId
     * @return
     */
    public boolean deleteReadingItemsColumn(String itemId){

        Log.e(TAG, "독서 기록 itemId 별 삭제 " + itemId);
        return sqlLiteDatabase.delete(SQLiteDataBase.CreateDB._TABLENAME_READING, "itemId=" + itemId, null) > 0;
    }

    /**
     * 독서기록 전체삭제
     * @return
     */
    public boolean deleteReadingColumns(){

        Log.e(TAG, "서재 전체 삭제");
        return sqlLiteDatabase.delete(SQLiteDataBase.CreateDB._TABLENAME_READING, null, null) > 0;
    }

    /**
     * 독서기록 탭에서 서재 목록 및 메모건수 조회
     * @return
     */
    public ArrayList<InterParkItemVo> selectReadingRecordColumns(){
        ArrayList<InterParkItemVo> items = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT "
                    + SQLiteDataBase.CreateDB.ITEMID + ","
                    + SQLiteDataBase.CreateDB.TITLE + ","
                    + SQLiteDataBase.CreateDB.DESCRIPTION + ","
                    + SQLiteDataBase.CreateDB.COVERSMALLURL + ","
                    + SQLiteDataBase.CreateDB.COVERLARGEURL + ","
                    + SQLiteDataBase.CreateDB.PUBLISHER + ","
                    + SQLiteDataBase.CreateDB.AUTHOR + ","
                    + SQLiteDataBase.CreateDB.LINK + ","
                    + SQLiteDataBase.CreateDB.ISBN + ","
                    + SQLiteDataBase.CreateDB.REGDATE + ","
                    + SQLiteDataBase.CreateDB.UPDDATE + ","
                    + "( SELECT count(1) FROM bookrest_reading where itemId = TBL.itemId ) as memoCount "
                    + " FROM bookrest TBL";

            cursor = sqlLiteDatabase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String itemId = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.ITEMID));
                    String title = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.TITLE));
                    String description = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.DESCRIPTION));
                    String coversmallurl = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.COVERSMALLURL));
                    String coverlargeurl = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.COVERLARGEURL));
                    String publisher = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.PUBLISHER));
                    String author = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.AUTHOR));
                    String link = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.LINK));
                    String isbn = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.ISBN));
                    String regdate = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.REGDATE));
                    String upddate = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.UPDDATE));
                    String memoCount = cursor.getString(cursor.getColumnIndex("memoCount"));

                    InterParkItemVo vo = new InterParkItemVo();
                    vo.setItemId(itemId);
                    vo.setTitle(title);
                    vo.setDescription(description);
                    vo.setCoverSmallUrl(coversmallurl);
                    vo.setCoverLargeUrl(coverlargeurl);
                    vo.setPublisher(publisher);
                    vo.setAuthor(author);
                    vo.setLink(link);
                    vo.setIsbn(isbn);
                    vo.setMemoCount(memoCount);
                    vo.setRegDate(regdate);
                    vo.setUpdDate(upddate);

                    items.add(vo);

                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e(TAG, "서재 목록 및 메모건수 조회 " + items.size());

        return items;
    }

    /**
     * 서재 전체 목록 조회
     * @return
     */
    public ArrayList<InterParkItemVo> selectColumns(){

        ArrayList<InterParkItemVo> items = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = sqlLiteDatabase.query(SQLiteDataBase.CreateDB._TABLENAME, null, null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String itemid = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.ITEMID));
                    String title = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.TITLE));
                    String description = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.DESCRIPTION));
                    String coversmallurl = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.COVERSMALLURL));
                    String coverlargeurl = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.COVERLARGEURL));
                    String publisher = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.PUBLISHER));
                    String author = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.AUTHOR));
                    String link = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.LINK));
                    String isbn = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.ISBN));
                    String regdate = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.REGDATE));
                    String upddate = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.UPDDATE));
                    
                    InterParkItemVo vo = new InterParkItemVo();
                    vo.setItemId(itemid);
                    vo.setTitle(title);
                    vo.setDescription(description);
                    vo.setCoverSmallUrl(coversmallurl);
                    vo.setCoverLargeUrl(coverlargeurl);
                    vo.setPublisher(publisher);
                    vo.setAuthor(author);
                    vo.setLink(link);
                    vo.setIsbn(isbn);
                    vo.setRegDate(regdate);
                    vo.setUpdDate(upddate);

                    items.add(vo);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e(TAG, "서재 전체 목록 조회 " + items.size());
        return items;
    }

    /**
     * 서재 조회 단건
     * @param itemId
     * @return
     */
    public InterParkItemVo selectColumn(String itemId){
        InterParkItemVo vo = new InterParkItemVo();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + SQLiteDataBase.CreateDB._TABLENAME + " WHERE " + SQLiteDataBase.CreateDB.ITEMID + "=" + itemId;
            cursor = sqlLiteDatabase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.TITLE));
                    String description = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.DESCRIPTION));
                    String coversmallurl = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.COVERSMALLURL));
                    String coverlargeurl = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.COVERLARGEURL));
                    String publisher = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.PUBLISHER));
                    String author = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.AUTHOR));
                    String link = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.LINK));
                    String isbn = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.ISBN));
                    String regdate = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.REGDATE));
                    String upddate = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.UPDDATE));

                    vo.setItemId(itemId);
                    vo.setTitle(title);
                    vo.setDescription(description);
                    vo.setCoverSmallUrl(coversmallurl);
                    vo.setCoverLargeUrl(coverlargeurl);
                    vo.setPublisher(publisher);
                    vo.setAuthor(author);
                    vo.setLink(link);
                    vo.setIsbn(isbn);
                    vo.setRegDate(regdate);
                    vo.setUpdDate(upddate);

                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.e(TAG, "서재 조회 단건 " + vo.getItemId());

        return vo;
    }

    /**
     * 독서기록 itemId 별 전체 목록 조회
     * @return
     */
    public ArrayList<ReadingVo> selectReadingColumns(String itemId){
        ArrayList<ReadingVo> items = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + SQLiteDataBase.CreateDB._TABLENAME_READING + " WHERE " + SQLiteDataBase.CreateDB.ITEMID + "=" + itemId;
            cursor = sqlLiteDatabase.rawQuery(query, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String idx = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.IDX));
                    String itemid = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.ITEMID));
                    String memo = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.MEMO));
                    String alarm = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.ALARM));
                    String readingdate = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.READINGDATE));
                    String readingpagenumber = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.READINGPAGENUMBER));
                    String regdate = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.REGDATE));
                    String upddate = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.UPDDATE));

                    ReadingVo vo = new ReadingVo();
                    vo.setItemId(itemid);
                    vo.setIdx(idx);
                    vo.setMemo(memo);
                    vo.setAlarm(alarm);
                    vo.setReadingdate(readingdate);
                    vo.setReadingpagenumber(readingpagenumber);
                    vo.setRegDate(regdate);
                    vo.setUpdDate(upddate);

                    items.add(vo);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e(TAG, "독서기록 itemId 별 전체 목록 조회 " + items.size());

        return items;
    }

    /**
     * 독서기록 Idx 조회 단건
     * @param idx
     * @return
     */
    public ReadingVo selectReadingColumn(String idx){
        ReadingVo vo = new ReadingVo();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + SQLiteDataBase.CreateDB._TABLENAME_READING + " WHERE " + SQLiteDataBase.CreateDB.IDX + "=" + idx;
            cursor = sqlLiteDatabase.rawQuery(query, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String itemid = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.ITEMID));
                    String memo = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.MEMO));
                    String alarm = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.ALARM));
                    String readingdate = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.READINGDATE));
                    String readingpagenumber = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.READINGPAGENUMBER));
                    String regdate = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.REGDATE));
                    String upddate = cursor.getString(cursor.getColumnIndex(SQLiteDataBase.CreateDB.UPDDATE));

                    vo.setItemId(itemid);
                    vo.setIdx(idx);
                    vo.setReadingdate(readingdate);
                    vo.setReadingpagenumber(readingpagenumber);
                    vo.setRegDate(regdate);
                    vo.setUpdDate(upddate);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e(TAG, "독서기록 Idx 조회 단건 " + vo.getItemId());

        return vo;
    }
}