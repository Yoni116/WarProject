import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Logger;

public class IronDome extends Thread {
	private String id;
	private TreeSet<Target> targets;
	private boolean isActive;
	;

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
		this.targets.add(new Target(m, time,this));
	}

	@Override
	public void run() {
		while (isActive) {
			try {
				Iterator<Target> it = targets.iterator();
				while (it.hasNext()) {
					Target t=it.next();
					t.start();
					targets.remove(t);		    
				}
			} catch (Exception e) {
			}
		}
	}
	



}

