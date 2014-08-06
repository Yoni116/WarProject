import java.io.File;
import java.util.Scanner;



public class Program {
	private static Scanner s = new Scanner(System.in);

	public static void main(String[] args){
		File src = new File("config.xml");
		int firstChoice;
		int choice;
		String warName;
		War war = null;
		firstChoice = startMenu();
		System.out.println("Please insert a name for the war:");
		warName = s.next();
		switch (firstChoice) {
		case 1:
			try{
				war = XMLParser.parseWar(warName, src);
			} catch (Exception e){
				System.out.println("Unable to load war from " + src.getPath());
			}
			break;
		case 2:
			war = new War(warName);
			break;
		}
		war.start();
		while (true) {
			choice = warMenu(war);
			switch (choice) {
			case 1: {
				showWarInventory(war);
				break;
			}
			case 2: {
				addMissile(war);
				break;
			}
			case 3: {
				addMissileLauncher(war);
				break;
			}
			case 4: {
				addIronDome(war);
				break;
			}
			case 5: {
				addLauncherDestructor(war);
				break;
			}
			case 6: {
				if (!war.getStatus()) {
					synchronized (war) {
						war.setStatus(true);
						war.notify();
					}
				} else {
					war.end();
					showStatistics();
					System.exit(0);
				}
			}
			break;
			}
		}

	}

	private static void showStatistics() {
		// TODO Auto-generated method stub

	}

	private static void addMissileLauncher(War war) {
		// TODO Auto-generated method stub

	}

	private static void addIronDome(War war) {
		// TODO Auto-generated method stub

	}

	private static void addLauncherDestructor(War war) {
		// TODO Auto-generated method stub

	}

	private static int startMenu() {
		int choice;
		System.out.println("***************************\n");
		System.out.println("Welcome to the War Simulator\n");
		System.out.println("***************************\n");
		System.out.println("Here are your options:");
		System.out.println("1. Load War using XML file");
		System.out.println("2. Make a new war without a file");
		choice = s.nextInt();
		return choice;

	}

	private static int warMenu(War war) {
		int choice;
		System.out.println("War " + war.getWarName() + " is currently "
				+ (war.getStatus() ? "active" : "inactive") + "\n");
		System.out.println("Please enter your next action:");
		System.out.println("1. Show war inventory");
		System.out.println("2. Add a new Missile to the war");
		System.out.println("3. Add a new Missile Launcher to the war");
		System.out.println("4. Add a new Iron Dome to the war");
		System.out.println("5. Add a new Launcher Destructor to the war");
		if (!war.getStatus()) {
			System.out.println("6. Run war with current inventory");
		} else {
			System.out.println("6. Terminate war and show stats");
		}
		choice = s.nextInt();
		return choice;
	}

	private static void showWarInventory(War war) {
		System.out.println(war.getWarName() + " war inventory:");
		System.out.println(war.getWarName() + " has "
				+ war.getMissileLaunchers().size() + " Missile Launchers");
		System.out.println(war.getWarName() + " has "
				+ war.getIronDomes().size() + " Iron Domes");
		System.out.println(war.getWarName() + " has "
				+ war.getLauncherDestructors().size()
				+ " Launcher Destructors\n");
	}

	private static void addMissile(War war) {
		int launcherNumber;
		String missileid;
		String destination;
		int launchTime=0;
		int flyTime;
		int damage;
		System.out.println("\nAdd Missile:");
		for (int i = 0; i < war.getMissileLaunchers().size(); i++) {
			System.out
			.println((i + 1)
					+ ". "
					+ war.getMissileLaunchers().get(i)
					+ (war.getMissileLaunchers().get(i).isActive() ? " is Active!"
							: " is Inactive!"));
		}
		do {
			System.out.println("Enter active launcher number to add missile:");
			launcherNumber = s.nextInt()-1;
		} while (!war.getMissileLaunchers().get(launcherNumber).isActive());
		System.out.println("Enter missile id:");
		missileid = s.next();
		System.out.println("Enter destination:");
		destination = s.next();
		if(!war.getStatus()){
			System.out.println("Enter time to launch:");
			launchTime = s.nextInt();
		}
		System.out.println("Enter flytime:");
		flyTime = s.nextInt();
		System.out.println("Enter damage:");
		damage = s.nextInt();
		Missile m = new Missile(missileid, destination, launchTime, flyTime, damage);
		m.setLauncher(war.getMissileLaunchers().get(launcherNumber));
		war.getMissileLaunchers().get(launcherNumber).addMissile(m);
		System.out.println("Missile was added successfully");
		
	}

}
