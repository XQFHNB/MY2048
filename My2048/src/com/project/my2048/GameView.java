package com.project.my2048;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

public class GameView extends GridLayout {
	// 保險期間重寫三個構造方法，但是真的不知道，保險在哪裏呀
	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initGameView();
	}

	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initGameView();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initGameView();
	}

	/**
	 * 不管从哪一个构造 方法中进来都是要执行初始化方法，我们就将初始化方法设定为类的入口。
	 */
	private void initGameView() {
		/**
		 * 學習這裏是怎麼通過監聽觸摸事件進行分析的，還要根據它們的
		 */
		setColumnCount(4);// 直接指明是4列的
		setBackgroundColor(0xffbbada0);// 给整个游戏view上个底色
		setOnTouchListener(new OnTouchListener() {
			private float startX, startY, offsetX, offsetY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					offsetX = event.getX() - startX;
					offsetY = event.getY() - startY;
					// 這裏要注意的一點是，用戶在滑動的時候很有可能進行沒有標準的滑動，比如斜着滑動，因此我們就要事先判斷用戶的大致意圖
					// 即，在水平方向上滑動，還是在豎直方向上
					if (Math.abs(offsetX) > Math.abs(offsetY)) {// 水平方向上的移動大於豎直方向上的移動
																// 則判定水平滑動
						if (offsetX < -5) {// 爲什麼不直接剛0呢，爲了減小誤差
							// System.out.println("left");//
							// 至於怎麼判斷左右移動，聯繫view的座標就可以解決問題
							sweepLeft();
						} else if (offsetX > 5) {
							// System.out.println("right");
							sweepRight();
						}

					} else {// 這就是豎直方向上的移動了
						if (offsetY < -5) {
							// System.out.println("up");
							sweepUp();
						} else if (offsetY > 5) {
							// System.out.println("down");
							sweepDown();
						}
					}
					break;
				}
				return true;
			}
		});
	}

	/**
	 * 動態變化卡片的寬高等等
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		// 这里是设置每一个卡片的宽度，我们并没指定哪一种数据为宽，而是谁比较短，谁就是宽度，减10的目的是给屏幕留出10像素的距离来，除以4是我们的排列是4行4列
		int cardWidth = (Math.min(w, h) - 10) / 4;//
		addCards(cardWidth, cardWidth);// 我们的卡片设置成了正方形，
		startGame();//第一次的界面
	}

	private void addCards(int cardWidth, int cardHeighth) {
		Card c;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				c = new Card(getContext());
				cardMap[x][y] = c;// 将卡片记录下来
				c.setNum(0);
				addView(c, cardWidth, cardHeighth);// 这个方法非常典型，经常使用
			}
			// 一般在输出时这里换行，真是时光荏苒呀，想象当时C语言的时候，泪崩
		}
	}

	private void startGame() {
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				cardMap[x][y].setNum(0);
			}

		}
		addRandomNum();
		addRandomNum();

	}

	private void addRandomNum() {
		emptyPoints.clear();// 添加之前先清空
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (cardMap[x][y].getNum() <= 0) {// 倘若是空点，若不是空点的话我们就不用再改变数字了 、

					emptyPoints.add(new Point(x, y));
				}
			}
		}
		Point p = emptyPoints
				.remove((int) (Math.random() * emptyPoints.size()));// 注意强制类型的转换，.random（）是产生0-1之间的数，此时先取整的话会造成置零的
		cardMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
		// 情况
	}

	private void sweepLeft() {
		boolean merge = false;// 添加是否有动作的标记，也就是看看是不是要添加新的随机数，视觉上每次滑动都在添加新的随机数
		// 向左划的时候我们一行一行的扫，注意view的坐标与我们数学上的意识中有点相反
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				for (int x1 = x + 1; x1 < 4; x1++) {
					if (cardMap[x1][y].getNum() > 0) {// 说明x1,y这里不是空的
						if (cardMap[x][y].getNum() <= 0) {// x,y这里是空的，因此我们把后面的值转移到前面来，视觉上就是数字块向左移动了
							cardMap[x][y].setNum(cardMap[x1][y].getNum());
							cardMap[x1][y].setNum(0);// 右边的清空
							x--;// 这一行已经换了一次，然后再重新遍历一次
							merge = true;
						} else if (cardMap[x1][y].getNum() == cardMap[x][y]
								.getNum()) {// 倘若后面的数字与前面的相同，就进行合并
							// else if(cardMap[x1][y].equals(cardMap[x][y])
							cardMap[x][y].setNum(cardMap[x][y].getNum()
									+ cardMap[x1][y].getNum());
							cardMap[x1][y].setNum(0);// 右边的清空
							AtyMain.getAtymain().addScore(
									cardMap[x][y].getNum());
							merge = true;
						}
						break;// break才是对的，真是，在这里磨了半天，
					}

				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

	private void sweepRight() {
		boolean merge = false;
		for (int y = 0; y < 4; y++) {
			for (int x = 3; x >= 0; x--) {
				for (int x1 = x - 1; x1 >= 0; x1--) {
					if (cardMap[x1][y].getNum() > 0) {// 说明x1,y这里不是空的
						if (cardMap[x][y].getNum() <= 0) {// x,y这里是空的，因此我们把后面的值转移到前面来，视觉上就是数字块向左移动了
							cardMap[x][y].setNum(cardMap[x1][y].getNum());
							cardMap[x1][y].setNum(0);// 右边的清空
							x++;// 这一行已经换了一次，然后再重新遍历一次
							merge = true;
						} else if (cardMap[x1][y].getNum() == cardMap[x][y]
								.getNum()) {// 倘若后面的数字与前面的相同，就进行合并
							// else if(cardMap[x1][y].equals(cardMap[x][y])
							cardMap[x][y].setNum(cardMap[x][y].getNum()
									+ cardMap[x1][y].getNum());
							cardMap[x1][y].setNum(0);// 右边的清空
							AtyMain.getAtymain().addScore(
									cardMap[x][y].getNum());
							merge = true;
						}
						break;
					}

				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

	private void sweepUp() {
		boolean merge = false;
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				for (int y1 = y + 1; y1 < 4; y1++) {
					if (cardMap[x][y1].getNum() > 0) {// 说明x1,y这里不是空的
						if (cardMap[x][y].getNum() <= 0) {// x,y这里是空的，因此我们把后面的值转移到前面来，视觉上就是数字块向左移动了
							cardMap[x][y].setNum(cardMap[x][y1].getNum());
							cardMap[x][y1].setNum(0);// 右边的清空
							y--;// 这一行已经换了一次，然后再重新遍历一次
							merge = true;
						} else if (cardMap[x][y1].getNum() == cardMap[x][y]
								.getNum()) {// 倘若后面的数字与前面的相同，就进行合并
							// else if(cardMap[x1][y].equals(cardMap[x][y])
							cardMap[x][y].setNum(cardMap[x][y1].getNum()
									+ cardMap[x][y].getNum());
							cardMap[x][y1].setNum(0);// 右边的清空
							AtyMain.getAtymain().addScore(
									cardMap[x][y].getNum());
							merge = true;
						}
						break;
					}

				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

	private void sweepDown() {
		boolean merge = false;
		for (int x = 0; x < 4; x++) {
			for (int y = 3; y >= 0; y--) {
				for (int y1 = y - 1; y1 >= 0; y1--) {
					if (cardMap[x][y1].getNum() > 0) {// 说明x1,y这里不是空的
						if (cardMap[x][y].getNum() <= 0) {// x,y这里是空的，因此我们把后面的值转移到前面来，视觉上就是数字块向左移动了
							cardMap[x][y].setNum(cardMap[x][y1].getNum());
							cardMap[x][y1].setNum(0);// 右边的清空
							y++;// 这一行已经换了一次，然后再重新遍历一次
							merge = true;
						} else if (cardMap[x][y1].getNum() == cardMap[x][y]
								.getNum()) {// 倘若后面的数字与前面的相同，就进行合并
							// else if(cardMap[x1][y].equals(cardMap[x][y])
							cardMap[x][y].setNum(cardMap[x][y1].getNum()
									+ cardMap[x][y].getNum());
							cardMap[x][y1].setNum(0);// 右边的清空
							AtyMain.getAtymain().addScore(
									cardMap[x][y].getNum());
							merge = true;
						}
						break;
					}

				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

	private void checkComplete() {
		boolean complete = true;
		ALL: for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				if (cardMap[x][y].getNum() == 0
						|| (x > 0 && cardMap[x][y].equals(cardMap[x - 1][y]))
						|| (x < 3 && cardMap[x][y].equals(cardMap[x + 1][y]))
						|| (y > 0 && cardMap[x][y].equals(cardMap[x][y - 1]))
						|| (y < 3 && cardMap[x][y].equals(cardMap[x][y + 1]))) {
					complete = false;
					break ALL;
				}
			}
		}
		if (complete) {
			new AlertDialog.Builder(getContext()).setTitle("你好").setMessage("游戏结束了！").setPositiveButton("重来", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					startGame();
				}
			}).show();
		}
	}

	private Card[][] cardMap = new Card[4][4];
	private List<Point> emptyPoints = new ArrayList<Point>();// 说是为了让我们将数字存在里面方便取
}
