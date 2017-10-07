/*
 * File: Breakout.java
 * -------------------
 * Name: Davit Gochitashvili
 * Section Leader: Davit Akopov
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1)
			* BRICK_SEP)
			/ NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	/** Speed of moving ball */
	private final static double DELAY = 7;

	/* Method: run() */
	/** Runs the BreakoutExtension program. */
	public void run() {
		startGame();
	}

	/*
	 * This method initializes game parts: draws paddle and ball;
	 */
	public void initialize() {
		drawPaddle();
		drawBall();
	}

	/*
	 * This method starts game NTURNS times (NTURNS can easily changed);
	 */

	private void startGame() {
		drawBricks();
		for (int k = 0; k < NTURNS; k++) {
			initialize();
			waitForClick();
			moveBall();
			if (BRICKS_REMIANING > 0) {
				remove(paddle);
				remove(ball);
			} else {
				break;
			}
		}
		finalResult();
	}

	/*
	 * This method just draws rows bricks of given colors;
	 */

	private void drawBricks() {
		for (int i = 0; i < NBRICKS_PER_ROW; i++) {
			for (int j = 0; j < NBRICK_ROWS; j++) {
				GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				int x = (BRICK_SEP * i + BRICK_WIDTH * i)
						+ WIDTH
						/ 2
						- ((BRICK_SEP + BRICK_WIDTH) * NBRICKS_PER_ROW - BRICK_SEP)
						/ 2;
				int y = BRICK_Y_OFFSET + BRICK_HEIGHT * j + j * BRICK_SEP;
				add(brick, x, y);
				setBricksColor(brick, j);
			}
		}
	}

	/*
	 * This method sets colors of bricks;
	 */

	private void setBricksColor(GRect brick, int j) {
		brick.setFilled(true);
		switch (j % 10) {
		case 0:
			brick.setColor(Color.RED);
			break;
		case 1:
			brick.setColor(Color.RED);
			break;
		case 2:
			brick.setColor(Color.ORANGE);
			break;
		case 3:
			brick.setColor(Color.ORANGE);
			break;
		case 4:
			brick.setColor(Color.YELLOW);
			break;
		case 5:
			brick.setColor(Color.YELLOW);
			break;
		case 6:
			brick.setColor(Color.GREEN);
			break;
		case 7:
			brick.setColor(Color.GREEN);
			break;
		case 8:
			brick.setColor(Color.CYAN);
			break;
		case 9:
			brick.setColor(Color.CYAN);
			break;
		}
	}

	/*
	 * This method draws black GRect type paddle;
	 */

	private void drawPaddle() {
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle, x, y);
		addMouseListeners();
	}

	/*
	 * This method makes move paddle when mouse moved left or right;
	 */

	public void mouseMoved(MouseEvent e) {
		if (e.getX() >= 0 && e.getX() < (WIDTH - PADDLE_WIDTH)) {
			paddle.setLocation(e.getX(), y);
		}
	}

	/*
	 * This method draws black GOval type ball;
	 */

	private void drawBall() {
		ball = new GOval(BALL_RADIUS * 2, BALL_RADIUS * 2);
		add(ball, WIDTH / 2 - BALL_RADIUS / 2, HEIGHT / 2 - BALL_RADIUS / 2);
		ball.setFilled(true);
	}

	/*
	 * This method moves ball in the window;
	 */

	private void touchWalls() {
		if (ball.getX() + 2 * BALL_RADIUS >= getWidth()) {
			vx = -vx;
		} else if (ball.getX() <= 0) {
			vx = -vx;
		} else if (ball.getY() <= 0) {
			vy = -vy;
		}
	}

	/*
	 * This method moves ball with random speed and direction; Also checks
	 * object and removes if needed;
	 */

	private void moveBall() {
		vy = +3.0;
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5))
			vx = -vx;
		while (true) {
			if (BRICKS_REMIANING == 0) {
				remove(ball);
				break;
			}
			ball.move(vx, vy);
			pause(DELAY);
			touchWalls();
			if (ball.getY() + 2 * BALL_RADIUS >= getHeight()) {
				break;
			}
			checkObject();
		}
	}

	/*
	 * This method returns object which was touched by ball;
	 */

	private GObject getCollider() {
		if (getElementAt(ball.getX(), ball.getY()) != null) {
			return getElementAt(ball.getX(), ball.getY());
		} else if (getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != null) {
			return getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
		} else if (getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS) != null) {
			return getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS);
		} else if (getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2
				* BALL_RADIUS) != null) {
			return getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2
					* BALL_RADIUS);
		}
		return null;
	}

	/*
	 * This method writes "VICTORY!" in center of canvas when player wins the
	 * game;
	 */

	private void winMessage() {
		AudioClip winSound = MediaTools.loadAudioClip("win.wav");
		GLabel win = new GLabel("VICTORY!");
		win.setColor(Color.blue);
		win.setFont("Times New Roman-36-BOLD");
		winSound.play();
		add(win, WIDTH / 2 - win.getWidth() / 2, HEIGHT / 2 - win.getAscent()
				/ 2);

	}

	/*
	 * This method writes "Game Over!" in center of canvas when player loses the
	 * game;
	 */
	private void loseMessage() {
		AudioClip gameover = MediaTools.loadAudioClip("gameover.wav");
		GLabel lose = new GLabel("Game Over!");
		lose.setColor(Color.RED);
		lose.setFont("Times New Roman-36-BOLD");
		gameover.play();
		add(lose, WIDTH / 2 - lose.getWidth() / 2,
				HEIGHT / 2 - lose.getAscent() / 2);

	}

	/*
	 * This method checks player wins or loses the game;
	 */

	private void finalResult() {
		if (BRICKS_REMIANING > 0) {
			loseMessage();
		} else {
			winMessage();
		}
	}

	/*
	 * This method check object which was collided by ball: If this object is
	 * brick removes it, if this object is paddle moves on opposite direction;
	 */

	private void checkObject() {
		GObject collider = getCollider();
		if (collider != paddle && collider != null) {
			BRICKS_REMIANING = BRICKS_REMIANING - 1;
			remove(collider);
			vy = -vy;
		} else if (collider == paddle) {
			if (ball.getY() + BALL_RADIUS * 2 > y) {
				vy = Math.abs(vy) * -1;
			} else {
				vy = -vy;
			}
		}
	}

	/** Paddle rectangle */
	private GRect paddle;

	/** Paddle X coordinate */
	private int x = WIDTH / 2 - PADDLE_WIDTH / 2;

	/** Paddle Y coordinate */
	private int y = HEIGHT - PADDLE_Y_OFFSET - 2 * PADDLE_HEIGHT;

	/** Ball oval */
	private GOval ball;

	/** Speed of ball */
	private double vx, vy;

	/** Random generator for X coordinate of the ball */
	private RandomGenerator rgen = new RandomGenerator();

	/** How many bricks are remaining */
	private int BRICKS_REMIANING = NBRICK_ROWS * NBRICKS_PER_ROW;

}