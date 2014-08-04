

public class Target {
    private Thread target;
    private int interceptionTime;
    private static int missileCumulativeTime=0;

    public Target(Missile m, int interceptionTime) {
	this.target = m;
	this.interceptionTime = interceptionTime;
    }

    public Target(MissileLauncher ml, int destroyTime) {
	this.target = ml;
	this.interceptionTime = destroyTime;
    }

    public void intercept() {
	if (target instanceof Missile) {
	    try {
	    	if(interceptionTime>0){
	    		missileCumulativeTime+=interceptionTime;
	    		Thread.sleep((missileCumulativeTime-interceptionTime) * 1000);
	    	}
	    	else
	    		Thread.sleep(interceptionTime * 1000);
		
		    System.out.println(((Missile)target).getMid() + " trying to intercept " + System.currentTimeMillis()/1000);
		if (target.isAlive() && ((Missile)target).isLaunched()){
		    target.interrupt();
		    logInterception();
		}
		else {
		    logFailedInterception();
		}
	    } catch (InterruptedException e) {
	    } // undefined
	} else if (target instanceof MissileLauncher) {
	    MissileLauncher ml = (MissileLauncher) target;
	    try {
		Thread.sleep(interceptionTime * 1000);
	    } catch (InterruptedException e) {
	    }
	    if (!ml.isHidden()) {
		ml.setActive(false);
		logInterception();
	    } else {
		logFailedInterception();
	    }
	}
    }

    private void logInterception() {

    }

    private void logFailedInterception() {
	
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
