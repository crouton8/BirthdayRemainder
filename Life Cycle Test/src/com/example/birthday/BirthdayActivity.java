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
	
	//�N�A���A���A�N��\
	private int year;
	private int month;
	private int day;
	private int age;
	
	//�c�������\������TextView
	private TextView dispView;
	//�N���\������TextView
	private TextView ageView;
	
	//�f�t�H���g�̃��b�Z�[�W
	private static final String DEFAULT_MSG = "�a������ݒ肵�Ă�������";
	
	//�v���t�@�����X�X�t�@�C����
	private static final String PREFS_FILE = "MyPrefsFile";
	//�v���t�@�����X�̂��߂̷�
	private static final String YEAR = "YEAR";
	private static final String MONTH = "MONTH";
	private static final String DAY = "DAY";
	
	//�u�N���A�v�{�^���̃C�x���g���X�i�[
	public class ClearButtonClickListener implements OnClickListener{

		public void onClick(View v) {
			//�v���t�@���X���N���A
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
	//�u�a�����̐ݒ�v�{�^���̃C�x���g���X�i�[
	public class SetButtonClickListener implements OnClickListener{
		
		public void onClick(View v) {
			//DatePickerDaialog��\��
			new DatePickerDialog(BirthdayActivity.this,
					new DatePickerDialog.OnDateSetListener(){
				
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth){
					BirthdayActivity.this.year = year;
					month = monthOfYear;
					day = dayOfMonth;
					showResult();
					//�f�[�^��ۑ�
					savePrefs();
				}	
			},1979,6,3).show();
		};
	}
		
	
	//���ʂ�\������
	private void showResult() {
		String dStr = String.format("%04d/%02d/%02d", year, month + 1, day);
		dispView.setText(dStr);
		
		//���݂̓�����\��Calendar�I�u�W�F�N�g�𐶐�
		Calendar now = Calendar.getInstance();
		
		//�a�������Ǘ�����Calendar�I�u�W�F�N�g�𐶐�
		Calendar birthday = (Calendar) now.clone();
		birthday.set(year, month, day);
		//�N������߂�
		age = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR) - 1;
		//birthday�����N�̒a�����ɐݒ�
		int thisYear = now.get(Calendar.YEAR);
		birthday.set(Calendar.YEAR, thisYear);
		
		if(now.after(birthday)){
			//�a�����������Ă���Η��N��
			birthday.add(Calendar.YEAR, 1);
			//�N���1���₷
			age += 1;
		}else if(now.equals(birthday)){
			//���N���a�����ł���ΔN���1���₷
			age += 1;
		}
		//���N�̒a�����܂ł̓��������߂�
		int diff = (int) ((birthday.getTimeInMillis() - now.getTimeInMillis())/
				(1000 * 60 * 60 * 24));
		String dispStr = "�a�����܂�" + Integer.toString(diff) + "����";
		//������\������
		dispView.setText(dispStr);
		
		//�N���\������
		ageView.setText(age + 1 + "��");
		
	}
	
	//�v���t�@�����X�ɕۑ�
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
        
        //[�a�����̐ݒ�]�{�^���̃C�x���g���X�i�[��ݒ�
        Button setDateBtn = (Button) findViewById(R.id.setDateBtn);
        setDateBtn.setOnClickListener(new SetButtonClickListener());
      //[�N���A]�{�^���̃C�x���g���X�i�[��ݒ�
        Button clearBtn = (Button) findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new ClearButtonClickListener());
        
        //�c�������\������TextView
        dispView = (TextView) findViewById(R.id.dispView);
        //�N���\������TextView
        ageView = (TextView) findViewById(R.id.ageView);
        
        //�v���t�@�����X����̃f�[�^�̓ǂݍ���
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
			//�ǂݍ��񂾒l��\��
			showResult();
		}else{
			//���b�Z�[�W��\��
			dispView.setText(DEFAULT_MSG);
		}
	}


}
