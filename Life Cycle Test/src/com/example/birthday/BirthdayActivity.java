package com.example.birthday;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class BirthdayActivity extends Activity {
	
	//年、月、日、年齢\
	private int year;
	private int month;
	private int day;
	private int age;
	
	//残り日数を表示するTextView
	private TextView dispView;
	//年齢を表示するTextView
	private TextView ageView;
	
	//デフォルトのメッセージ
	private static final String DEFAULT_MSG = "誕生日を設定してください";
	
	//プレファレンススファイル名
	private static final String PREFS_FILE = "MyPrefsFile";
	//プレファレンスのためのｷｰ
	private static final String YEAR = "YEAR";
	private static final String MONTH = "MONTH";
	private static final String DAY = "DAY";
	
	//「クリア」ボタンのイベントリスナー
	public class ClearButtonClickListener implements OnClickListener{

		public void onClick(View v) {
			//プレファンスをクリア
			SharedPreferences prefs = getSharedPreferences(PREFS_FILE,
					Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.clear();
			editor.commit();
			dispView.setText(DEFAULT_MSG);
			ageView.setText("");
			year = month = day = 0;
			}
		}
	//「誕生日の設定」ボタンのイベントリスナー
	public class SetButtonClickListener implements OnClickListener{
		
		public void onClick(View v) {
			//DatePickerDaialogを表示
			new DatePickerDialog(BirthdayActivity.this,
					new DatePickerDialog.OnDateSetListener(){
				
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth){
					BirthdayActivity.this.year = year;
					month = monthOfYear;
					day = dayOfMonth;
					showResult();
					//データを保存
					savePrefs();
				}	
			},1979,6,3).show();
		};
	}
		
	
	//結果を表示する
	private void showResult() {
		String dStr = String.format("%04d/%02d/%02d", year, month + 1, day);
		dispView.setText(dStr);
		
		//現在の日時を表すCalendarオブジェクトを生成
		Calendar now = Calendar.getInstance();
		
		//誕生日を管理するCalendarオブジェクトを生成
		Calendar birthday = (Calendar) now.clone();
		birthday.set(year, month, day);
		//年齢を求める
		age = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR) - 1;
		//birthdayを今年の誕生日に設定
		int thisYear = now.get(Calendar.YEAR);
		birthday.set(Calendar.YEAR, thisYear);
		
		if(now.after(birthday)){
			//誕生部がすぎていれば来年に
			birthday.add(Calendar.YEAR, 1);
			//年齢を1増やす
			age += 1;
		}else if(now.equals(birthday)){
			//今年が誕生日であれば年齢を1増やす
			age += 1;
		}
		//今年の誕生日までの日数を求める
		int diff = (int) ((birthday.getTimeInMillis() - now.getTimeInMillis())/
				(1000 * 60 * 60 * 24));
		String dispStr = "誕生日まで" + Integer.toString(diff) + "日で";
		//日数を表示する
		dispView.setText(dispStr);
		
		//年齢を表示する
		ageView.setText(age + 1 + "才");
		
	}
	
	//プレファレンスに保存
	private void savePrefs() {
			SharedPreferences prefs = getSharedPreferences(PREFS_FILE,
					Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt(YEAR, year);
			editor.putInt(MONTH, month);
			editor.putInt(DAY, day);
			editor.commit();
			
	}
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
        
        //[誕生日の設定]ボタンのイベントリスナーを設定
        Button setDateBtn = (Button) findViewById(R.id.setDateBtn);
        setDateBtn.setOnClickListener(new SetButtonClickListener());
      //[クリア]ボタンのイベントリスナーを設定
        Button clearBtn = (Button) findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new ClearButtonClickListener());
        
        //残り日数を表示するTextView
        dispView = (TextView) findViewById(R.id.dispView);
        //年齢を表示するTextView
        ageView = (TextView) findViewById(R.id.ageView);
        
        //プレファレンスからのデータの読み込み
        SharedPreferences prefs = getSharedPreferences(PREFS_FILE,
        		Activity.MODE_PRIVATE);
        year = prefs.getInt(YEAR, 0);
        month = prefs.getInt(MONTH, 0);
        day = prefs.getInt(DAY, 0);
    
    }

    @Override
	protected void onResume() {
		super.onResume();
		if(year != 0){
			//読み込んだ値を表示
			showResult();
		}else{
			//メッセージを表示
			dispView.setText(DEFAULT_MSG);
		}
	}


}
