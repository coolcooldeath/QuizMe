package com.example.quizme.db;

import android.provider.BaseColumns;

public class CategoryContract {

    public static class CatEntry implements BaseColumns {
        public static final String TABLE_CATEGORY = "table_cat";
        // tasks Table Columns names
        public static final String CAT_ID = "catid";
        public static final String CATEGORY = "category";
    }
}
