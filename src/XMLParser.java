import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {

	public static War parseWar(String name, File src) throws NoLauncherFoundException, SAXException, IOException, ParserConfigurationException {
		List<MissileLauncher> launchers = null;
		List<IronDome> ironDomes = null;
		List<LauncherDestructor> destructors = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(src);
			launchers = readMissileLaunchers(doc);
			ironDomes = readIronDomes(doc, launchers);
			destructors = readLauncherDestructors(doc, launchers);		
		} catch(NoMissileFoundException e) {
		}
		
		return new War(name, launchers, ironDomes, destructors);
	}

	/**
	 * Reads missile launchers from XML file
	 * 
	 * @param Source
	 *            file to read from
	 * @return Vector of missile launchers
	 */
	private static List<MissileLauncher> readMissileLaunchers(Document doc) {
		Vector<MissileLauncher> v = new Vector<>();
		NodeList launcherlist = doc.getElementsByTagName("launcher");
		int numberOfLaunchers = launcherlist.getLength();
		for (int i = 0; i < numberOfLaunchers; i++) {
			Node launcher = launcherlist.item(i);
			Element l = (Element) launcher;
			String lid = l.getAttribute("id");
			boolean isHidden = (l.getAttribute("isHidden").toLowerCase()
					.equals("true")) ? true : false;
			MissileLauncher ml = new MissileLauncher(lid, isHidden);
			NodeList missilelist = l.getElementsByTagName("missile");
			readMissiles(ml, missilelist);
			v.add(ml);
		}
		return v;
	}

	/**
	 * Used by readMissileLaunchers. Reads missiles from file.
	 * 
	 * @param Source
	 *            file to read from
	 * @return Vector of missiles
	 */
	private static void readMissiles(MissileLauncher ml, NodeList missilelist) {
		int numberOfMissiles = missilelist.getLength();
		for (int j = 0; j < numberOfMissiles; j++) {
			Element m = (Element) missilelist.item(j);
			String id = m.getAttribute("id");
			String destination = m.getAttribute("destination");
			int launchTime = Integer.valueOf(m.getAttribute("launchTime"));
			int flyTime = Integer.valueOf(m.getAttribute("flyTime"));
			int damage = Integer.valueOf(m.getAttribute("damage"));
			Missile mis;
			ml.addMissile(mis = new Missile(id, destination, launchTime,
					flyTime, damage));
			mis.setLauncher(ml);
		}
	}

	/**
	 * Reads iron domes from file.
	 * 
	 * @param Source
	 *            file to read from
	 * @return
	 * @throws NoMissileFoundException 
	 */
	private static List<IronDome> readIronDomes(Document doc,
			List<MissileLauncher> targets) throws NoMissileFoundException {
		Vector<IronDome> v = new Vector<>();
		String targetId = null;
		int destructTime = 0;

		Node mdList = doc.getElementsByTagName("missileDestructors").item(0);
		NodeList ironDomeList = ((Element)mdList).getElementsByTagName("destructor");
		int numberOfIronDomes = ironDomeList.getLength();
		for (int i = 0; i < numberOfIronDomes; i++) {
			Node iron = ironDomeList.item(i);
			Element irond = (Element) iron;
			String id = irond.getAttribute("id");
			IronDome ironD = new IronDome(id);
			NodeList targetedMissile = irond
					.getElementsByTagName("destructdMissile");
			int numberOfTargets = targetedMissile.getLength();
			for (int j = 0; j < numberOfTargets; j++) {
				Node target = targetedMissile.item(j);
				Element targetM = (Element) target;
				targetId = targetM.getAttribute("id");
				destructTime = Integer.valueOf(targetM
						.getAttribute("destructAfterLaunch"));
				assignMissileTargets(ironD, targets, targetId, destructTime);
			}
			v.add(ironD);

		}
		return v;
	}

	private static List<LauncherDestructor> readLauncherDestructors(
			Document doc, List<MissileLauncher> targets) throws NoLauncherFoundException {
		Vector<LauncherDestructor> v = new Vector<>();
		Node ldlist = doc.getElementsByTagName("missileLauncherDestructors").item(0);
		NodeList launcherDestructors = ((Element) ldlist).getElementsByTagName("destructor");
		int numberOfLauncherDestructor = launcherDestructors.getLength();
		String targetId;
		int destructTime;
		for (int i = 0; i < numberOfLauncherDestructor; i++) {
			Node ld = launcherDestructors.item(i);
			Element eld = (Element) ld;
			String type = eld.getAttribute("type");
			LauncherDestructor destructor = new LauncherDestructor(
					LauncherDestructor.Type.valueOf(type));
			NodeList targetedLaunchers = eld
					.getElementsByTagName("destructedLanucher");
			int numberOfTargets = targetedLaunchers.getLength();
			for (int j = 0; j < numberOfTargets; j++) {
				Node target = targetedLaunchers.item(j);
				Element targetL = (Element) target;
				targetId = targetL.getAttribute("id");
				destructTime = Integer.valueOf(targetL.getAttribute("destructTime"));
				assignLauncherTargets(destructor, targets, targetId, destructTime);
			}

			v.add(destructor);
		}

		return v;
	}

	private static void assignLauncherTargets(LauncherDestructor destructor,
			List<MissileLauncher> targets, String targetId, int destructTime) throws NoLauncherFoundException {
		
		for (int i = 0; i < targets.size() ; i++) {
			MissileLauncher l=targets.get(i);
			if(l.getLauncherId().equals(targetId)){
				destructor.addTarget(l,destructTime);
				return;
			}
			
		}
		throw new NoLauncherFoundException();
		
	}

	/**
	 * Used by readIronDomes and LuncherDestructors. searches for the targets in
	 * the launchers and assign them.
	 * 
	 * @param Source
	 *            file to read from
	 * @throws NoMissileFoundException 
	 */
	private static void assignMissileTargets(IronDome ironD,
			List<MissileLauncher> targets, String targetId, int destructTime) throws NoMissileFoundException {
		for(MissileLauncher l:targets){
			Missile m;
			if((m=l.getMissileById(targetId)) != null){
				ironD.addTarget(m, destructTime);
				return;
			}
		}
		throw new NoMissileFoundException();
	}

}
