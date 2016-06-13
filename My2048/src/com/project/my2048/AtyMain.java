package com.project.my2048;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AtyMain extends Activity {
	private TextView tvScore;
	private int score = 0;
	private static AtyMain atymain = null;
/**
 * 解释一下为什么要这样做，为什么要弄出一个AtyMain类的实例来
 * 大概是我们需要在主活动的界面上显示分数，但是我们需要一个传送门，可以通过这样的方式拿到主活动的一个实例进行显示，（可以这样理解，但是
 * 并没有造出一个实例来，反正大致就是这个意思）
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
