package olivia.graph.ongoing;
import java.util.Set;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class PictureUtil {
	public static int calculateMemory(Set<Component> components, String host) {

		int memory = 0;
		for (Component cp : components) {

			String xmx = cp.jvmArgs.get("-Xmx");
			Object v2 = null;
			if (xmx != null && xmx.contains("g")) {
				xmx = xmx.replace("g", "");
				memory += Integer.valueOf(xmx);
			} else {
				memory += 1; // default value.

			}
		}
		return memory;
	}

	public static mxGraphComponent createPicture(Set<Component> components, String host, int memory, boolean widthIsG) {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		int x = 20;
		int y = 20;
		int height = 50;
		
		try {
			mxCell v1 = (mxCell) graph.insertVertex(parent, null, host + "\n" + memory + "g", x, y, 30, 30);
			y = y + 20;
			x=x+100;
			for (Component cp : components) {
				int xwidth = 100;
				int yheight = 30;
				int width = xwidth;
				cp.calculateXmxInG();
				int memcp = cp.calculateXmxInG();
				if(widthIsG) {width = xwidth * memcp;
				y = y + height;
				}
				else {
					yheight=yheight*memcp;
					
				}
				mxCell v2 = (mxCell) graph.insertVertex(parent, null, cp.name + "\n" + memcp + "g", x, y, width,
						yheight);
				if(widthIsG) {
				}else {
					y=y+yheight;
					
				}
				graph.insertEdge(parent, null, "", v1, v2);
				System.out.println(v2.getStyle());
			}
		} finally {
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		return graphComponent;
	}

}
