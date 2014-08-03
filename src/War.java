import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class War {
	private String name;
	private static Logger warLogger=Logger.getLogger("WarLogger");
	private List<MissileLauncher> missileLaunchers;
	private List<IronDome> ironDomes;
	private List<LauncherDestructor> launcherDestructors;
	private boolean warStatus = false;
	private static War war = null; //lo egyoni?
	static Scanner s = new Scanner(System.in);


	public static void main(String[] args) throws NoLauncherFoundException, SAXException,
														IOException, ParserConfigurationException {
		File src=new File("config.xml");
		int firstChoice;
		String warName;


		firstChoice = startMenu();

		System.out.println("Please insert a name for the war:");
		warName = s.next();



		switch(firstChoice){
		case 1:
			war=XMLParser.parseWar(warName, src);
			break;

		case 2:
			war = new War(warName);
			break;
		}

		war.start();

	}


	public War(String name,List<MissileLauncher> launchers,
			List<IronDome> ironDomes,List<LauncherDestructor> launcherDestructors) {
		this.name=name;
		this.missileLaunchers=launchers;
		this.ironDomes=ironDomes;
		this.launcherDestructors=launcherDestructors;
	}

	public War(String name){
		this.name = name;
		this.missileLaunchers = new Vector<MissileLauncher>();
		this.ironDomes = new Vector<IronDome>();
		this.launcherDestructors = new Vector<LauncherDestructor>();
	}

	public static int startMenu(){
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


	public void start() {

		warInventory();
		System.out.println();
		while(true){
			int choice = warMenu(warStatus);

			switch(choice){

			case 1:
				warInventory();
				break;
			case 2:
				addMissile();
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				war.warStatus = true;
				for (IronDome ironDome : ironDomes) {
					ironDome.start();
				}
				for (MissileLauncher missileLauncher : missileLaunchers) {
					missileLauncher.start();
				}
				for (LauncherDestructor launcherDestructor : launcherDestructors) {
					launcherDestructor.start();
				}
				break;

			}
		}
	}

	private void addMissile() {
		int launcherNumber;
		String missileid;
		String destination;
		int flyTime;
		int damage;
		for(int i=0;i<missileLaunchers.size();i++){
			System.out.println(i+" "+missileLaunchers.get(i));
		}
		System.out.println("Enter launcher number to add missile:");
		launcherNumber=s.nextInt();
		System.out.println("Enter missile id:");
		missileid=s.next();
		System.out.println("Enter destination:");
		destination=s.next();
		System.out.println("Enter flytime:");
		flyTime=s.nextInt();
		System.out.println("Enter damage:");
		damage=s.nextInt();
		missileLaunchers.get(launcherNumber).addMissile(
								new Missile(missileid, destination, 0, flyTime, damage));
	}


	public void warInventory(){
		System.out.println(war.name + " war inventory:");
		System.out.println(war.name + " has " + war.missileLaunchers.size() +" Missile Launchers");
		System.out.println(war.name + " has " + war.ironDomes.size() +" Iron Domes");
		System.out.println(war.name + " has " + war.launcherDestructors.size() +" Launcher Destructors\n");
	}

	public int warMenu(boolean warStatus){
		int choice;
		System.out.println("War " + war.name +" is currntly "+ (warStatus?"active":"inactive")+"\n");
		System.out.println("Please enter your next action:");
		System.out.println("1. Show war inventory");
		System.out.println("2. Add a new Missile to the war");
		System.out.println("3. Add a new Missile Launcher to the war");
		System.out.println("4. Add a new Iron Dome to the war");
		System.out.println("5. Add a new Luncher Destructor to the war");
		if(!warStatus)
		System.out.println("6. Run war with current invetory");
		choice = s.nextInt();

		return choice;
	}
}
