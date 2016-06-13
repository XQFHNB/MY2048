package com.project.my2048;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AtyMain extends Activity {
	private TextView tvScore;
	private int score = 0;
	private static AtyMain atymain = null;
/**
 * ����һ��ΪʲôҪ��������ΪʲôҪŪ��һ��AtyMain���ʵ����
 * �����������Ҫ������Ľ�������ʾ����������������Ҫһ�������ţ�����ͨ�������ķ�ʽ�õ������һ��ʵ��������ʾ��������������⣬����
 * ��û�����һ��ʵ�������������¾��������˼��
 */
	public AtyMain() {
		atymain = this;
	}

	public static AtyMain getAtymain() {
		return atymain;
	}

	public static void setAtymain(AtyMain atymain) {
		AtyMain.atymain = atymain;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aty_main);
		tvScore = (TextView) findViewById(R.id.tvScore);
	}

	public void clearScore() {
		score = 0;
		showScore();
	}

	public void showScore() {
		tvScore.setText(score + "");
	}

	public void addScore(int s) {
		score += s;
		showScore();
	}

}
