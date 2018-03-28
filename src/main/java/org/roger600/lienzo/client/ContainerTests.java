package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.Point2D;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;


public class ContainerTests extends FlowPanel implements MyLienzoTest, HasMediators {

    private Layer layer;

    public void test(Layer layer) {
        this.layer = layer;
        WiresManager wires_manager = WiresManager.get(layer);

        IContainmentAcceptor acceptor = new IContainmentAcceptor()
        {
            @Override
            public boolean containmentAllowed(WiresContainer parent, WiresShape[] children)
            {
                return acceptContainment(parent, children);
            }

            @Override
            public boolean acceptContainment(WiresContainer parent, WiresShape[] children)
            {
                if (parent == null)
                {
                    GWT.log("acceptContainment - Parent is NULL");
                    return true;
                }
                GWT.log("acceptContainment - Parent is " + parent.toString());
                return true;
                //return !parent.getContainer().getUserData().equals(child.getContainer().getUserData());
            }
        };

        wires_manager.setContainmentAcceptor( acceptor );

        final double startX = 300;
        final double startY = 300;
        final double w = 100;
        final double h = 100;

        // Blue start event.
        MultiPath startEventMultiPath = new MultiPath().rect(0, 0, w, h).setStrokeColor("#000000");
        WiresShape startEventShape = new WiresShape(startEventMultiPath);
        wires_manager.register( startEventShape );
        startEventShape.setLocation(new Point2D(startX,startY));
        startEventShape.setDraggable(true).getContainer().setUserData("event");

        // Green task node.
        WiresShape taskNodeShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor("#00CC00"));
        wires_manager.register( taskNodeShape );
        taskNodeShape.setLocation(new Point2D(startX + 200,startY));
        taskNodeShape.setDraggable(true).getContainer().setUserData("task");

    }

    private void log(String message) {
        // GWT.log(message);
    }

    @Override
    public int compareTo(MyLienzoTest other) {
        return this.getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
    }
}