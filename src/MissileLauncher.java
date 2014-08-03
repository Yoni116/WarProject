import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Semaphore;


public class MissileLauncher extends Thread{
	private String id;
	private boolean isHidden;
	private boolean isActive;
	private List<Missile> missiles;
	private Semaphore launchpad;

	public MissileLauncher(String id,boolean isHidden) {
		this.id=id;
		this.isHidden=isHidden;
		this.missiles=new Vector<>();
		this.launchpad=new Semaphore(1);
		this.isActive=true;
	}

	public void addMissile(Missile m){
		missiles.add(m);
	}
	
	
	public String getLauncherId() {
		return this.id;
	}

	public Missile getMissileById(String id){
		for(Missile m : missiles){
			if (m.equals(id));
			return m;
		}
		return null;
	}

	@Override
	public void run() {
		while(isActive){
			if(missiles.size()>0)
				missiles.remove(0).start();
		}
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void acquire() throws InterruptedException {
		launchpad.acquire();
	}

	public void release() {
		launchpad.release();
	}

	public void setHidden() {
		if(isHidden){
			Random r=new Random();
			Thread counter=new Thread(new Runnable() {
				@Override
				public void run() {
					isHidden=false;
					try {
						sleep(r.nextInt(2000));
					} catch (InterruptedException e) {}
					isHidden=true;
				}
			});
			counter.start();
		}
	}

	@Override
	public String toString() {
		return "Launcher [id=" + id + ", isHidden=" + isHidden
				+ ", isActive=" + isActive	+ "]";
	}

	public void setActive(boolean active) {
		isActive=active;
	}

}
