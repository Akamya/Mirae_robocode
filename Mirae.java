package mirae;

import java.util.HashMap;

import robocode.*;
import robocode.util.Utils;
import java.awt.Color;

/**
 * Mirae - a robot by Akamya
 */

public class Mirae extends Robot
{
	
	private Robot enemyBot;
	private HashMap<String, Robot> enemies;

	public void run() {
	
		initTank();

		while(true) {
			runScanner();
			runGun();
		}
	}

	/*Init the tank basic properties*/
	public void initTank() {
		setColors(Color.gray,Color.gray,Color.gray);
		setScanColor(Color.gray);
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		
		enemyBot = new Robot();
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		double radarTurn = getHeading() + e.getBearing() - getRadarHeading();
		turnRadarRight(2*Utils.normalRelativeAngle(radarTurn));
	}

	public void onHitByBullet(HitByBulletEvent e) {
	}
	
	public void onHitWall(HitWallEvent e) {

		back(20);
	}
	
	/*run the scanner*/	
	public void runScanner() {
		turnRadarRight(Double.POSITIVE_INFINITY*180/Math.PI);
		/*do {
			scan(); 
		} while(true);*/
	}	
	
	public void runGun() {
		if(enemyBot == null) {
		
		} else {
			System.out.println(enemyBot.getName());		
		}
	}
	
	public void runBody() {
		
	}
}
