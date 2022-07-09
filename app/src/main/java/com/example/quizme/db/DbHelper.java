package com.example.quizme.db;

import static com.example.quizme.db.AnswerContract.QuizEntry.ANSWER_ID;
import static com.example.quizme.db.AnswerContract.QuizEntry.QUESTION_ID;
import static com.example.quizme.db.AnswerContract.QuizEntry.TABLE_ANSWER;
import static com.example.quizme.db.QuizContract.QuizEntry.KEY_ANSWER;
import static com.example.quizme.db.QuizContract.QuizEntry.KEY_ID;
import static com.example.quizme.db.QuizContract.QuizEntry.KEY_OPTA;
import static com.example.quizme.db.QuizContract.QuizEntry.KEY_OPTB;
import static com.example.quizme.db.QuizContract.QuizEntry.KEY_OPTC;
import static com.example.quizme.db.QuizContract.QuizEntry.KEY_OPTD;
import static com.example.quizme.db.QuizContract.QuizEntry.KEY_QUES;
import static com.example.quizme.db.QuizContract.QuizEntry.TABLE_QUEST;
import static com.example.quizme.db.CategoryContract.CatEntry.CATEGORY;
import static com.example.quizme.db.CategoryContract.CatEntry.CAT_ID;
import static com.example.quizme.db.CategoryContract.CatEntry.TABLE_CATEGORY;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "triviaQuiz";
	// tasks table name

	private SQLiteDatabase dbase;
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		dbase=db;

		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_QUEST + " ("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_QUES + " TEXT,"
				+ KEY_OPTA + " TEXT,"
				+ KEY_OPTB + " TEXT,"
				+ KEY_OPTC + " TEXT,"
				+ KEY_OPTD + " TEXT,"
				+ CAT_ID + " INTEGER,"
				+ "  FOREIGN KEY(" + CAT_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + CAT_ID + ")"
				+ ");");

				db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY + " ("
				+ CAT_ID + " INTEGER PRIMARY KEY,"
				+ CATEGORY + " TEXT"
				+ ");");

				db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ANSWER + " ("
				+ ANSWER_ID + " INTEGER PRIMARY KEY,"
				+ QUESTION_ID + " INTEGER,"
				+ KEY_ANSWER + " TEXT,"
				+ "  FOREIGN KEY(" + QUESTION_ID + ") REFERENCES " + TABLE_QUEST + "(" + KEY_ID + ")"
				+ ");");



	}
	public void addQuestions(List<Question> questions)
	{
		for (Question question: questions) {
			this.addQuestion(question);
		}
	}

	public void addCategories(List<CategoryModel> categories)
	{

		for (CategoryModel category: categories) {
			this.addCategory(category);
		}
	}


	public void addAnswers(List<AnswerModel> answers)
	{

		for (AnswerModel answer: answers) {
			this.addAnswer(answer);
		}
	}




	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		// Drop older table if existed
		db.execSQL("drop table " + TABLE_QUEST);
		db.execSQL("drop table " + TABLE_CATEGORY);
		db.execSQL("drop table " + TABLE_ANSWER);

		onCreate(db);


	}


	public void addAnswer(AnswerModel answer) {
		//SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ANSWER, answer.getAnswer());
		values.put(ANSWER_ID, answer.getId());
		values.put(QUESTION_ID, answer.getIdQuestion());
		// Inserting Row
		dbase.insert(TABLE_ANSWER, null, values);
	}

	// Adding new question
	public void addQuestion(Question quest) {
		//SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_QUES, quest.getQuestion());
		values.put(KEY_ID, quest.getQuestionId());
		values.put(KEY_OPTA, quest.getOption1());
		values.put(KEY_OPTB, quest.getOption2());
		values.put(KEY_OPTC, quest.getOption3());
		values.put(KEY_OPTD, quest.getOption4());
		values.put(CAT_ID, quest.getCategoryId());
		// Inserting Row
		dbase.insert(TABLE_QUEST, null, values);		
	}
	public void addCategory(CategoryModel cat) {
		ContentValues values = new ContentValues();
		values.put(CAT_ID, cat.getCategoryId());
		values.put(CATEGORY, cat.getСategoryName());
		// Inserting Row
		dbase.insert(TABLE_CATEGORY, null, values);
	}


	public List<Question> getAllQuestions() {
		List<Question> quesList = new ArrayList<Question>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Question quest = new Question();
				quest.setCatId((Integer.parseInt(cursor.getString(6))));
				quest.setQuestion(cursor.getString(1));
				quest.setOption1(cursor.getString(2));
				quest.setOption2(cursor.getString(3));
				quest.setOption3(cursor.getString(4));
				quest.setOption4(cursor.getString(5));
				quest.setQuestionId(Integer.parseInt(cursor.getString(0)));

				quesList.add(quest);
			} while (cursor.moveToNext());
		}
		// return quest list
		return quesList;
	}

	public List<AnswerModel> getAllAnswers() {
		List<AnswerModel> answerModelList = new ArrayList<AnswerModel>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ANSWER;
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				AnswerModel answerModel = new AnswerModel();
				answerModel.setId((Integer.parseInt(cursor.getString(0))));
				answerModel.setIdQuestion(Integer.parseInt(cursor.getString(1)));
				answerModel.setAnswer(cursor.getString(2));
				answerModelList.add(answerModel);
			} while (cursor.moveToNext());
		}
		// return quest list
		return answerModelList;
	}

	public ArrayList<Question> getAllCatQuestions(int catId) {
		ArrayList<Question> quesList = new ArrayList<Question>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_QUEST+" WHERE "+CAT_ID+" = "+catId;
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Question quest = new Question();
				quest.setCatId((Integer.parseInt(cursor.getString(6))));
				quest.setQuestion(cursor.getString(1));
				quest.setOption1(cursor.getString(2));
				quest.setOption2(cursor.getString(3));
				quest.setOption3(cursor.getString(4));
				quest.setOption4(cursor.getString(5));
				quest.setQuestionId(Integer.parseInt(cursor.getString(0)));
				quesList.add(quest);
			} while (cursor.moveToNext());
		}
		// return quest list
		return quesList;
	}



	public ArrayList<AnswerModel> getAllAnswers(int questionId) {
		ArrayList<AnswerModel> answerModelArrayListList = new ArrayList<AnswerModel>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_ANSWER+" WHERE "+QUESTION_ID+" = "+questionId;
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				AnswerModel answer = new AnswerModel();
				answer.setId((Integer.parseInt(cursor.getString(0))));
				answer.setIdQuestion(Integer.parseInt(cursor.getString(1)));
				answer.setAnswer(cursor.getString(2));
				answerModelArrayListList.add(answer);
			} while (cursor.moveToNext());
		}
		// return quest list
		return answerModelArrayListList;
	}



	public ArrayList<CategoryModel> getAllCategories() {
		ArrayList<CategoryModel> catList = new ArrayList<>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				CategoryModel cat = new CategoryModel();
				cat.setCategoryId(cursor.getInt(0));
				cat.setСategoryName(cursor.getString(1));
				catList.add(cat);
			} while (cursor.moveToNext());
		}

		return catList;
	}

	public void deleteAllCategories() {

		String selectQuery = "delete FROM " + TABLE_CATEGORY;
		dbase = this.getWritableDatabase();
		dbase.execSQL(selectQuery);
	}

	public void deleteAllQuestions() {

		String selectQuery = "delete FROM " + TABLE_QUEST;
		dbase = this.getWritableDatabase();
		dbase.execSQL(selectQuery);
	}

	public void deleteAllAnswers() {

		String selectQuery = "delete FROM " + TABLE_ANSWER;
		dbase = this.getWritableDatabase();
		dbase.execSQL(selectQuery);
	}

}
