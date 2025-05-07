package com.example.myapplication.db.sqllite;

import android.provider.BaseColumns;

/**
 * SQLite Create DataBase
 */
public final class SQLiteDataBase {

    /**
     * itemId(키), title, description, coverSmallUrl, coverLargeUrl, publisher, author, link, isbn 조회
     */
    public static final class CreateDB implements BaseColumns {
        public static final String ITEMID = "itemId";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String COVERSMALLURL = "coverSmallUrl";
        public static final String COVERLARGEURL = "coverLargeUrl";
        public static final String PUBLISHER = "publisher";
        public static final String AUTHOR = "author";
        public static final String LINK = "link";
        public static final String ISBN = "isbn";
        public static final String REGDATE = "regDate";
        public static final String UPDDATE = "updDate";
        public static final String _TABLENAME = "bookrest";
        public static final String _CREATE_TABLE = "create table if not exists " + _TABLENAME + "("
                + ITEMID +" text primary key, "
                + TITLE +" text , "
                + DESCRIPTION +" text , "
                + COVERSMALLURL +" text , "
                + COVERLARGEURL +" text , "
                + PUBLISHER +" text , "
                + AUTHOR +" text , "
                + LINK +" text , "
                + ISBN +" text , "
                + REGDATE +" text , "
                + UPDDATE +" text );";

        public static final String IDX = "idx";
        public static final String READINGDATE = "readingDate"; // 독서기록 일시
        public static final String MEMO = "memo"; // 독서기록 메모
        public static final String ALARM = "alarm"; // 알림
        public static final String READINGPAGENUMBER = "readingPageNumber"; // 독서기록 읽은 페이지 번호 기록
        public static final String _TABLENAME_READING = "bookrest_reading";
        public static final String _CREATE_TABLE_READING = "create table if not exists " + _TABLENAME_READING + "("
                + IDX + " integer primary key autoincrement, "
                + ITEMID +" text , "
                + READINGDATE +" text , "
                + READINGPAGENUMBER +" text , "
                + ALARM +" text , "
                + MEMO +" text , "
                + REGDATE +" text , "
                + UPDDATE +" text );";
    }
}
