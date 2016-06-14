package com.project.my2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {
	private int num = 0;// 與卡片綁定的數字
	private TextView label;// 顯示數字的工具

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
		if (num<=0) {
			label.setText("");//初始将他们都设置为零，即在UI中呈现空卡片
		}
		else {
			label.setText(num + "");// 因为我们的setText（）方法是接收String类型的参数
		}
	}

	public Card(Context context) {
		super(context);
	
		label = new TextView(getContext());// 在構造方法裏面進行一個初始化,學習是如何在java源碼中寫UI的
		label.setTextSize(32);// 設置數字文本大小
		label.setGravity(Gravity.CENTER);//将view中的字放在中间
		label.setBackgroundColor(0x33ffffff);//给每个小卡片上个色，
		LayoutParams lp = new LayoutParams(-1, -1);// 爲什麼說佈局參數來控制會更加方便，-1-1的意思就是填充滿整個父級容器
		lp.setMargins(10, 10, 0, 0);//充分发挥了控制，解释了上一句的控制更加方便
//		lp.setMargins(left, top, right, bottom),为什么上面的语句没有将后面两个数字填满，仔细分析，若填好就是两个距离了
		addView(label, lp);
		setNum(0);//为什么这个要放在这里，而放在前面却不行，我想可能是因为，setNum()方法中还有setText()这个方法，这个方法是在写UI时给的，连view都没有肯定不可以
//		setText()了，因此要放到addView（）方法后面
		// TODO Auto-generated constructor stub
	}

	/**
	 * 對卡片設置一個判斷的方法
	 * 
	 * @param o
	 * @return
	 */
	public boolean equals(Card o) {
		// TODO Auto-generated method stub
		return getNum() == o.getNum();// 只用判斷兩個對象的綁定數字是否相同就可以看兩張卡片是不是可以進行摺疊
	}

}
