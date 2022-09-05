package olivia.graph.ongoing;
import java.util.HashMap;
import java.util.Map;

public class Component {
private static final String SEP = " ";
public Component(String name) {
	this.name=name;
	}
String name;
String host;
String portOffset;
String type;
String primaryServer;
Map<String, String> jvmArgs=new HashMap<>();
String[] jvmArgsKeys= new String[] {"-Xmx", "-Xms"};
public void addArg(String key, String value) {
String[] values = key.split("\\.");	
if(values==null || values.length<2) return;
String arg=values[1];
if(arg==null)return;


String argValue= value;
switch(arg){
	case "host":{
		this.host=argValue;
		break;
	}
	case "portOffset":{
		this.portOffset=argValue;
		break;
	}
	case "type":{
		this.type=argValue;
		break;
	}
	case "jvmArgs":{
		for(String s: jvmArgsKeys)
		if(argValue.startsWith(s)) {
		jvmArgs.put(s,argValue.replace(s, ""));
		}
		break;
	}

}
}
@Override
public String toString() {
	// TODO Auto-generated method stub
	return name+SEP+host+SEP+portOffset +SEP+ jvmArgs.get("-Xmx")+SEP+ jvmArgs.get("-Xms");
}
public int calculateXmxInG() {
	String xmx = jvmArgs.get("-Xmx");
	int result=0;
	if (xmx != null && xmx.contains("g")) {
		xmx = xmx.replace("g", "");
		result=Integer.valueOf(xmx);
	}else{
		result=1;
		};
	return result;

}
}
