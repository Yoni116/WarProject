
public class Target{
	private Thread target;
	private int interceptionTime;

	public Target(Missile m,int interceptionTime) {
		this.target=m;
		this.interceptionTime=interceptionTime;
	}

	public Target(MissileLauncher ml,int destroyTime){
		this.target=ml;
		this.interceptionTime=destroyTime;
	}

	public void intercept() {
		if(target instanceof Missile){
			try{
				Thread.sleep(interceptionTime*1000);
				if(target.isAlive()) //TODO
					target.interrupt();
				else{

				}
			}catch(InterruptedException e){} //undefined
		} else if(target instanceof MissileLauncher) {
			MissileLauncher ml=(MissileLauncher) target;
			try {
				Thread.sleep(interceptionTime*1000);
			} catch (InterruptedException e) {}
			if(!ml.isHidden()){
				ml.setActive(false);
				//TODO log success
			} else {
				//TODO log fail
			}
		}
	}


}
