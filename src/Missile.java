import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Missile extends Thread {
	private static Logger logger = Logger.getLogger("WarLogger");
	private String id;
	private String destination;
	private long launchTime;
	private long flytime;
	private int damage;
	private MissileLauncher launcher;
	private boolean launched;

	public Missile(String id, String destination, int launchTime, int flyTime,
			int damage) {
		this.id = id;
		this.destination = destination;
		this.launchTime = launchTime * 1000;
		this.flytime = flyTime * 1000;
		this.damage = damage;
		this.launched = false;
		try {
			FileHandler fh = new FileHandler("logs/missile_" + id + ".txt",
					true);
			fh.setFilter(new ObjectFilter(this));
			fh.setFormatter(new WarFormatter());
			logger.addHandler(fh);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		fly();
	}

	private void fly() {
		try {
			sleep(launchTime);
		} catch (InterruptedException e) {
		}
		try {
			launcher.acquire();
			launcher.setHidden();
			logLaunch();
			launched = true;
			System.out.println(id + " start launch "
					+ System.currentTimeMillis() / 1000);
			sleep(flytime);
			launched = false;
			logHit();
		} catch (InterruptedException e) {
			System.out.println(id + " interrupted");
			logInterception();
		} finally {
			launcher.release();
		}
	}

	private void logLaunch() {
		logger.log(Level.SEVERE, "Missile " + id + " has started launching", this);
	}

	private void logHit() {
		logger.log(Level.SEVERE, "Missile " + id + " has hit the target and dealt "
				+ damage + " to " + destination, this);
	}

	private void logInterception() {
		logger.log(Level.INFO,"Missile " + id + " has been intercepted",this);
	}

	public boolean equals(String id) {
		if ((this.id).toLowerCase().equals(id.toLowerCase()))
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

	public boolean isLaunched() {
		return launched;
	}

	public String getID() {
		return id;
	}

}
