package com.alan.xie.worknotes;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

import com.alan.xie.worknotes.common.Constant;

/**
 * @author alan.xie
 * @date 2015-1-7 ����4:05:04
 * @Description: TODO
 */
public class BaseActivity extends Activity {
	
	public SharedPreferences pref;
	public Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if(pref == null){
			pref = this.getSharedPreferences(Constant.SHARED, MODE_PRIVATE);
		}
		if(editor == null){
			editor = pref.edit();
		}
	}
	
	/**
	 * @author alan.xie
	 * @date 2015-1-7 ����4:04:42
	 * @Description: ����
	 * @param @param v
	 * @return void
	 */
	public void back(View v){
		this.finish();
	}
}