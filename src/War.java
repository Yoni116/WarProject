import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class War extends Thread {
	private String name;
	private static Logger warLogger = Logger.getLogger("WarLogger");
	private boolean status;
	private List<MissileLauncher> missileLaunchers;
	private List<IronDome> ironDomes;
	private List<LauncherDestructor> launcherDestructors;

	public War(String name, List<MissileLauncher> launchers,
			List<IronDome> ironDomes,
			List<LauncherDestructor> launcherDestructors) {
		this.name = name;
		this.missileLaunchers = launchers;
		this.ironDomes = ironDomes;
		this.launcherDestructors = launcherDestructors;
		this.status = false;
		warLogger.setUseParentHandlers(false);
		warLogger.setLevel(Level.INFO);
		try {
			File logdir = new File("logs");
			if (!logdir.exists())
				logdir.mkdir();
			FileHandler fh = new FileHandler("logs/war_" + this.name + ".txt");
			fh.setFormatter(new WarFormatter());
			warLogger.addHandler(fh);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public War(String name) {
		this.name = name;
		this.missileLaunchers = new Vector<MissileLauncher>();
		this.ironDomes = new Vector<IronDome>();
		this.launcherDestructors = new Vector<LauncherDestructor>();
	}

	@Override
	public void run() {
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		while (status) {
			try {
				for (MissileLauncher missileLauncher : missileLaunchers) {
					missileLauncher.start();
				}
				for (IronDome ironDome : ironDomes) {
					ironDome.start();
				}
				for (LauncherDestructor launcherDestructor : launcherDestructors) {
					launcherDestructor.start();
				}
			} catch (IllegalThreadStateException e) {
			}
		}
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public void end() {
		status = false;
	}

	public String getWarName() {
		return name;
	}

	public boolean getStatus() {
		return status;
	}

	public List<IronDome> getIronDomes() {
		return ironDomes;
	}

	public List<LauncherDestructor> getLauncherDestructors() {
		return launcherDestructors;
	}

	public List<MissileLauncher> getMissileLaunchers() {
		return missileLaunchers;
	}
}
