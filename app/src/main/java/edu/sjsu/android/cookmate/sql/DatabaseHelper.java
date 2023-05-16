package edu.sjsu.android.cookmate.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.sjsu.android.cookmate.model.User;
import edu.sjsu.android.cookmate.model.Saved;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserManager.db";
    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String TABLE_RECIPE = "recipe";
    public static final String COLUMN_RECIPE_ID = "recipe_id";
    private static final String COLUMN_RECIPE_TITLE = "recipe_title";
    private static final String COLUMN_RECIPE_IMAGE = "recipe_image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Formulating query to create recipe table
        String CREATE_RECIPE_TABLE = "CREATE TABLE " + TABLE_RECIPE + "(" +
                COLUMN_RECIPE_ID + " INTEGER," +
                COLUMN_USER_ID + " INTEGER," +
                COLUMN_RECIPE_TITLE + " TEXT," +
                COLUMN_RECIPE_IMAGE + " TEXT, " +
                "CONSTRAINT PK_" + TABLE_RECIPE + " PRIMARY KEY (" + COLUMN_RECIPE_ID + ", " + COLUMN_USER_ID + ")" + ")";
        db.execSQL(CREATE_RECIPE_TABLE);

        // Formulating query to create user table
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USER_NAME + " TEXT UNIQUE," +
                COLUMN_USER_EMAIL + " TEXT," +
                COLUMN_USER_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        // Formulating queries to drop table
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
        db.execSQL(DROP_USER_TABLE);
        String DROP_RECIPE_TABLE = "DROP TABLE IF EXISTS " + TABLE_RECIPE;
        db.execSQL(DROP_RECIPE_TABLE);

        onCreate(db);

    }

    // Add User object to the database
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    // Add Recipe object to the database
    public void addRecipe(Saved saved) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, saved.getUserId());
        values.put(COLUMN_RECIPE_ID, saved.getRecipeId());
        values.put(COLUMN_RECIPE_IMAGE, saved.getRecipeImage());
        values.put(COLUMN_RECIPE_TITLE, saved.getRecipeTitle());
        db.insert(TABLE_RECIPE, null, values);
        db.close();
    }


    public void deleteRecipe(long recipeID, int userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_RECIPE, COLUMN_RECIPE_ID + " = ? AND " + COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(recipeID), String.valueOf(userID)});
        db.close();
    }

    public boolean checkRecipe(long recipe_id, int userID) {

        String[] columns = {
                COLUMN_RECIPE_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_RECIPE_ID + " = ? AND " + COLUMN_USER_ID + " = ?";

        // selection argument
        String[] selectionArgs = {String.valueOf(recipe_id), String.valueOf(userID)};

        Cursor cursor = db.query(TABLE_RECIPE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public int getUserId(String email, String password) {

        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int cursorCount = cursor.getCount();
        if (cursorCount > 0) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(COLUMN_USER_ID);
            if (columnIndex >= 0) {
                int userId = cursor.getInt(columnIndex);
                cursor.close();
                db.close();
                return userId;
            }
        }
        cursor.close();
        db.close();
        return -1;
    }
    public Cursor getAllSavedRecipes(int userId) {

        SQLiteDatabase database = getWritableDatabase();
        return database.query(TABLE_RECIPE, new String[]{
                        COLUMN_RECIPE_ID,
                        COLUMN_RECIPE_TITLE,
                        COLUMN_RECIPE_IMAGE},
                COLUMN_USER_ID + " = ?", new String[] {String.valueOf(userId)}, null, null, null);
    }
}