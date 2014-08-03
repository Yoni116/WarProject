import java.util.List;
import java.util.Vector;


public class IronDome extends Thread {
	private String id;
	private Vector<Target> targets;
	private boolean isActive;
	
	public IronDome(String id) {
		this.id=id;
		this.targets=new Vector<>();
		this.isActive=true;
	}
	
	public void addTarget(Missile m, int time){
		this.targets.add(new Target(m, time));
	}
	@Override
	public void run() {
		while(isActive){
			try{
				targets.remove(0).intercept();
			}catch(Exception e){}
		}
	}
}
