import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Logger;

public class IronDome extends Thread {
    private String id;
    private TreeSet<Target> targets;
    private boolean isActive;
    private static Logger ironDomelogger = Logger.getLogger("WarLogger");

    public IronDome(String id) {
	this.id = id;
	this.targets = new TreeSet<>(new Comparator<Target>() {
	    @Override
	    public int compare(Target o1, Target o2) {
		return o1.getInterceptionTime() - o2.getInterceptionTime();
	    }
	});
	this.isActive = true;
    }

    public void addTarget(Missile m, int time) {
	this.targets.add(new Target(m, time));
    }

    @Override
    public void run() {
	while (isActive) {
	    try {
		Iterator<Target> it = targets.iterator();
		while (it.hasNext()) {
		    Target t=it.next();
		    ironDomeInterceptLog(t);
		    t.intercept();
		    targets.remove(t);		    
		}
	    } catch (Exception e) {
	    }
	}
    }

	private void ironDomeInterceptLog(Target t) {
		ironDomelogger.severe("IronDome " + id + " has started to try and intercept "+ ((Missile) t.getTarget()).getMid());
		
	}
}
