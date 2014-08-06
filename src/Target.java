import java.util.logging.Level;
import java.util.logging.Logger;


public class Target extends Thread {
	private static Logger logger=Logger.getLogger("WarLogger");
	private Thread target;
	private Thread origin;
	private int interceptionTime;


	public Target(Missile m, int interceptionTime, IronDome ironD) {
		this.target = m;
		this.origin = ironD;
		this.interceptionTime = interceptionTime;
	}

	public Target(MissileLauncher ml, int destroyTime, LauncherDestructor ld) {
		this.target = ml;
		this.origin = ld;
		this.interceptionTime = destroyTime;
	}


	public void run(){
		intercept();
	}


	public void intercept() {

		if (target instanceof Missile) {

			try {
				sleep(interceptionTime * 1000);
				synchronized (origin) {
					logInterceptionTry();
					if (target.isAlive() && ((Missile)target).isLaunched()){
						target.interrupt();
						logInterception();
					}
					else {
						logFailedInterception();
					}
				}

			} catch (InterruptedException e) {
			} // undefined
		} else if (target instanceof MissileLauncher) {
			MissileLauncher ml = (MissileLauncher) target;
			try {
				Thread.sleep(interceptionTime * 1000);
			}
			catch (InterruptedException e) {
			}
			synchronized (origin) {
				if (!ml.isHidden()) {
					ml.setActive(false);
					logInterception();
				} else {
					logFailedInterception();
				}
			}
		}

	}

	private void logInterceptionTry() {
		logger.log(Level.INFO, "Iron dome " +
				((IronDome)origin).getID() + " is trying to intercept " + ((Missile)target).getID(),origin);
	}

	private void logInterception() {
		logger.log(Level.INFO, "Iron dome " +
				((IronDome)origin).getID() + " has intercepted " + ((Missile)target).getID(),origin);
	}

	private void logFailedInterception() {
		logger.log(Level.INFO, "Iron dome " +
				((IronDome)origin).getID() + " has failed to intercepted " + ((Missile)target).getID(),origin);
	}

	public int getInterceptionTime() {
		return interceptionTime;
	}

	public Thread getTarget() {
		return target;
	}

	@Override
	public String toString() {
		return "Target [target=" + target + ", interceptionTime="
				+ interceptionTime + "]";
	}

}
