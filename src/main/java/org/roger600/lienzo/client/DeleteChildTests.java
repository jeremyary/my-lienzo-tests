package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;

public class DeleteChildTests extends MyLienzoTest {

    public void test(Layer _layer) {
        final Layer layer = _layer;
        final WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.setContainmentAcceptor(new IContainmentAcceptor() {
            
            @Override
            public boolean containmentAllowed(WiresContainer parent, WiresShape[] children) {
                return true;
            }

            @Override
            public boolean acceptContainment(WiresContainer parent, WiresShape[] children) {
                return true;
            }
        });
        
        MultiPath parentMultiPath = new MultiPath().rect(0, 0, 300, 300).setStrokeColor("#000000");
        final WiresShape parentShape = new WiresShape(parentMultiPath);
        wires_manager.register( parentShape );
        parentShape.setDraggable(true);
        wires_manager.getMagnetManager().createMagnets(parentShape);

        MultiPath childMultiPath = new MultiPath().rect(0, 0, 100, 100).setStrokeColor("#CC0000");
        final WiresShape childShape = new WiresShape(childMultiPath);
        wires_manager.register( childShape );
        childShape.setDraggable(true).setLocation(new Point2D(500, 0));
        wires_manager.getMagnetManager().createMagnets(childShape);


        Rectangle button = new Rectangle(50,50).setFillColor(ColorName.BLUE);
        button.setX(500).setY(500);
        button.addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(final NodeMouseClickEvent event) {
                GWT.log("Deregistering shape " + childShape);
                wires_manager.deregister(childShape);
                layer.batch();
            }
        });
        layer.add(button);
    }
}
