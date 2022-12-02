/*
 |		___  ____
 |		|  \/  (_)
 |		| .  . |_ _ __ __ _  ___
 |		| |\/| | | '__/ _` |/ _ \
 |		| |  | | | | | (_| |  __/
 |		\_|  |_/_|_|  \__,_|\___|
 |
 |				Bot made by Not Alenya#8838
 |				This bot has been made for a school project
 |				It's still a bit buggy, but I'm definitely gonna
 |				patch it and provide a better version of it.
 */
package mirae;

import java.awt.Color;

import robocode.util.Utils;
import robocode.*;

public class Mirae extends Robot
{
	private byte moveDirection = 1;
	private double battleW;
	private double battleH;
	private double myX;
	private double myY;
	private double myHeading;
	private double gunHeading;
	private double radarHeading;
	private double enemyBearing;
	private double fireAngle;

	public void run() {
			initTank();

		while(true) {
			runScanner();
		}
	}


	// Set the default behavior of the tank
	public void initTank() {
		setColors(Color.white,Color.white,Color.white);
		setScanColor(Color.white);

		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		battleW =	getBattleFieldWidth();
		battleH = getBattleFieldHeight();

		myX = getX();
		myY = getY();

		System.out.println(battleW + ", " + battleH);
		System.out.println(myX + ", " + myY);
	}

	//on scanned robot, move the gun, radar and body while shotting at the enemy
	public void onScannedRobot(ScannedRobotEvent e) {

		myHeading = getHeading();
		gunHeading = getGunHeading();
		radarHeading = getRadarHeading();

		enemyBearing = e.getBearing();

		double radarTurn = myHeading + enemyBearing - radarHeading;
		turnRadarRight(2*Utils.normalRelativeAngle(radarTurn));

		fireAngle = myHeading+enemyBearing-gunHeading;

		turnGunRight(normalizeBearing(fireAngle));
		//normalized angles are worth it to use
		// in the worst case of non-normalized angles
		// the gun will turn 359 degres before reaching target
		// with normalized that goes from 0 to 180 and 0 to -180
		// you can aim quickly and shoot with a pretty good consistency
		fire(Math.min(400 / e.getDistance(), 3));
		// fire can go from 0 to 3 in power
		// velocity is calculated with this formula
		// Velocity = 20 − (3 ∗ power)
		// in order to reach a enemy that is far away, is better to use low power bullet

		runBody(e);
	}

	public void onHitByBullet(HitByBulletEvent e) {

	}

 /*
	| If a wall is touched move in the opposite direction, really simple strategy here
	*/
	public void onHitWall(HitWallEvent e) {
		back(20*moveDirection);
	}

 /*
	| Scanner need to be running at all time
	*/
	public void runScanner() {
		turnRadarRight(Double.POSITIVE_INFINITY);
	}

	public void runBody(ScannedRobotEvent e) {

	// always square off against our enemy
	turnRight(e.getBearing() + 90);

	// strafe by changing direction every 2 ticks
	// strafe strategy let us dodge many bullets
	if (getTime() % 2 == 0) {
		moveDirection *= -1;
		ahead(150 * moveDirection);
	}
	// If robot or wall is touched, invert the current speed to get away
	if(getVelocity() == 0) moveDirection *= -1;
}

 /*
	| normalize angles to make the aim more accurate and faster, since the gun turn 20 tick per second, we can't lose too much time on aiming
 	*/
	double normalizeBearing(double angle) {
		while (angle >  180) angle -= 360;
		while (angle < -180) angle += 360;
		return angle;
	}
}
