import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Missile extends Thread {
	private String id;
	private String destination;
	private long launchTime;
	private long flytime;
	private int damage;
	private MissileLauncher launcher;

	public Missile(String id, String destination, int launchTime, int flyTime,
			int damage) {
		this.id = id;
		this.destination = destination;
		this.launchTime = launchTime * 1000;
		this.flytime = flyTime * 1000;
		this.damage = damage;
//		try {
//			FileHandler fh = new FileHandler("missile_" + id + ".txt"); // TODO tov?
//			fh.setFilter(new ObjectFilter(this));
//			Logger.getLogger("WarLogger").addHandler(fh);
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void run() {
		fly();
	}

	private void fly() {
		try {
			sleep(launchTime);
		} catch (InterruptedException e) {} // undefined scenario
		try {
			launcher.acquire();
			System.out.println(this + " lunched" );
			launcher.setHidden();
			sleep(flytime);
			System.out.println(this + " hit" );
			logHit();
		} catch (InterruptedException e) {
			logInterception();
			System.out.println(this + " intercepted" );
		} finally {
			launcher.release();
		}
	}

	private void logHit() {
		// TODO
	}

	private void logInterception() {
		// TODO
	}


	public boolean equals(String id) {
		if((this.id).equals(id))
			return true;
		else
			return false;

	}

	public int getLaunchTime() {
		return (int) (launchTime / 1000);
	}

	public void setLauncher(MissileLauncher launcher) {
		this.launcher = launcher;
	}

	@Override
	public String toString() {
		return "Missile [id=" + id + ", destination=" + destination
				+ ", flytime=" + flytime + ", damage=" + damage + "]";
	}
	
}
