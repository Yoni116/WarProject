import java.util.Vector;


public class LauncherDestructor extends Thread {	
	public enum Type{
		ship,
		plane;
	}
	private static int idGenerator=1;
	private int id;
	private Type type;
	private Vector<Target> targets;
	
	
	public LauncherDestructor(Type type){
		this.id = idGenerator++;
		this.type=type;
		this.targets=new Vector<>();
	}


	public void addTarget(MissileLauncher l, int destructTime) {
		this.targets.add(new Target(l,destructTime, this));
	}
	
	
	@Override
	public void run() {
		while(true){
			synchronized(this){
				try {
					if(targets.size()==0)
						wait();
					targets.remove(0).intercept();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
