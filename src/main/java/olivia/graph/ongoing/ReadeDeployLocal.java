package olivia.graph.ongoing;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;

import oliviaproject.util.file.FileUtils;

public class ReadeDeployLocal extends JFrame implements Serializable {
	
	public ReadeDeployLocal() {
	}
	public static void main(String[] args) throws IOException {
		InputStream inp = FileUtils.getFileFromResourceAsStream("/deploy/deployLocalConfig.properties");
		Properties p = new Properties();
		p.load(inp);
		servers=findServers(p);
		clients=findClients(p);
		findComponents(p);
		hosts=findHosts(components);

		
		for(String host: hosts)  {
			Set<Component>cps=sortByServer(components, host);
			print(cps);
			int memory=PictureUtil.calculateMemory(cps, host);
			mxGraphComponent graphComponent = PictureUtil.createPicture(cps, host, memory, false);
			ReadeDeployLocal frame = new ReadeDeployLocal();
			frame.getContentPane().add(graphComponent);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(400, 320);
			frame.setVisible(true);
		}
		for(String host: hosts)  {
			Set<Component>cps=sortByServer(components, host);
			checkPort(cps);
		}
		
	}

	
	private static Boolean  checkPort(Set<Component> cps) {
		Set <String>portOffsets=new HashSet<>();
		Boolean result=true;
		for(Component c: cps) {
			if(c.portOffset!=null && !portOffsets.contains(c.portOffset)|| c.portOffset==null) {
				portOffsets.add(c.portOffset);
			}else {
				
				result=false;
				System.out.println("port "+ c.portOffset +" is used twice");
			}
		
		}
	return result;		
	}

	private static Set<String> findHosts(Set<Component> cps) {
		Set <String>result=new HashSet<>();
		for(Component c: cps) {
				result.add(c.host);
		
		}
	return result;
	}

	private static Set<String> findClients(Properties p) {
		Set<String> servers = new HashSet<>();
				String value=(String) p.get("clients");
		String[] sv=value.split(",");
		List<String> l= Arrays.asList(sv);
		for (String s: l) {
			servers.add(s);
		}
		return servers;
	}
	private static Set<String> findServers(Properties p) {
		
		Set<String> servers = new HashSet<>();
				String value=(String) p.get("servers");
		String[] sv=value.split(",");
		List<String> l= Arrays.asList(sv);
		for (String s: l) {
			servers.add(s);
		}
		return servers;
	}

	static Set<Component> components = new HashSet<>();
	private static Set<String> hosts=new HashSet<>();
	static Set<String>servers=new HashSet<> ();
	static Set<String>clients=new HashSet<> ();
private static void findComponents(Properties p) {
	//Iterator it = p.elements().asIterator();
	Set<String> componentNames=new HashSet<>();

	Iterator it = p.keys().asIterator();
	while(it.hasNext()) {
		String x = (String)it.next();
		String nameComponent=findName(x);
		componentNames.add(nameComponent);
	}
	 it = componentNames.iterator();
	while(it.hasNext()) {
		String comp= (String) it.next();
		if(clients.contains(comp) || servers.contains(comp)) {
		Component component= createComponent (comp, p);
		
		components.add(component);
		}
	}
	
}
static void print(Set<Component> cps){
	 Iterator<Component> it = cps.iterator();
	while(it.hasNext()) {
		Component comp= it.next();
		System.out.println(comp);
	}}

	private static Set<Component> sortByServer(Set<Component> cps, String host) {
		Set <Component>components=new HashSet<>();
		for(Component c: cps) {
			if(c.host!=null && c.host.equals(host)) {
				components.add(c);
			}
		
		}
	return components;
}

	private static Component createComponent(String comp, Properties p) {
		Iterator it = p.keys().asIterator();
		Component component = new Component(comp);
		while (it.hasNext()) {
			String key = (String) it.next();
			boolean b = findArguments(comp, key);
			if (b) {
				String value = p.getProperty(key);
				component.addArg(key, value);
			}
		}

		return component;
	}

	private static boolean findArguments(String comp, String x) {
		return (x.startsWith(comp));
	}

	private static String findName(String key) {
		String[] column = key.split("\\.");
		if (column == null || column.length == 0)
			return "";
		return column[0];
	}

}
