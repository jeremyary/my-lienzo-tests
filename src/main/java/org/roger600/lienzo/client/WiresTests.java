package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.IContainer;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.OrthogonalPolyLine;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.IConnectionAcceptor;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.IControlHandleList;
import com.ait.lienzo.client.core.shape.wires.MagnetManager;
import com.ait.lienzo.client.core.shape.wires.WiresConnection;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresMagnet;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.WiresMoveEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresMoveHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeEndEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeEndHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStartEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStartHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStepEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStepHandler;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;


public class WiresTests extends MyLienzoTest implements HasMediators {

    private Layer layer;
    private IControlHandleList m_ctrls;
    private WiresShape startEventShape;
    private Circle startEventCircle;

    public void test(Layer layer) {
        this.layer = layer;
        WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.setConnectionAcceptor(new IConnectionAcceptor() {
            @Override
            public boolean acceptHead(WiresConnection head, WiresMagnet magnet) {
                WiresConnection tail = head.getConnector().getTailConnection();

                WiresMagnet m = tail.getMagnet();

                if (m == null)
                {
                    return true;
                }
                if (magnet == null)
                {
                    return true;
                }
                return accept(magnet.getMagnets().getGroup(), tail.getMagnet().getMagnets().getGroup());
            }

            @Override
            public boolean acceptTail(WiresConnection tail, WiresMagnet magnet) {
                WiresConnection head = tail.getConnector().getHeadConnection();

                WiresMagnet m = head.getMagnet();

                if (m == null)
                {
                    return true;
                }
                if (magnet == null)
                {
                    return true;
                }
                return accept(head.getMagnet().getMagnets().getGroup(), magnet.getMagnets().getGroup());
            }

            @Override
            public boolean headConnectionAllowed(WiresConnection head, WiresShape shape) {
                WiresConnection tail = head.getConnector().getTailConnection();
                WiresMagnet m = tail.getMagnet();

                if (m == null)
                {
                    return true;
                }
                if (shape == null)
                {
                    return true;
                }

                return accept(shape.getContainer(), tail.getMagnet().getMagnets().getGroup());
            }

            @Override
            public boolean tailConnectionAllowed(WiresConnection tail, WiresShape shape) {
                WiresConnection head = tail.getConnector().getHeadConnection();

                WiresMagnet m = head.getMagnet();

                if (m == null)
                {
                    return true;
                }
                if (shape == null)
                {
                    return true;
                }
                return accept(head.getMagnet().getMagnets().getGroup(), shape.getContainer());
            }

            private boolean accept(final IContainer head, final IContainer tail)
            {
                log("Accept [head=" + head.getUserData() + "] [tail=" + tail.getUserData() + "]");
                final String headData = (String) head.getUserData();
                final String tailData = (String) tail.getUserData();
                if ( "event".equals(headData) && "event".equals(tailData) )
                {
                    return false;
                }
                return true;
            }
        });

        wires_manager.setContainmentAcceptor(new IContainmentAcceptor()
        {
            @Override
            public boolean containmentAllowed(WiresContainer parent, WiresShape[] children)
            {
                return acceptContainment(parent, children);
            }

            @Override
            public boolean acceptContainment(WiresContainer parent, WiresShape[] children)
            {
                if (parent.getParent() == null)
                {
                    return true;
                }
                return !parent.getContainer().getUserData().equals(children[0].getContainer().getUserData());
            }
        });

        final double startX = 300;
        final double startY = 300;
        final double radius = 50;
        final double w = 100;
        final double h = 100;

        // Blue start event.
        MultiPath startEventMultiPath = new MultiPath().rect(0, 0, w, h).setStrokeColor("#000000");
        startEventShape = new WiresShape( startEventMultiPath );
        wires_manager.register(startEventShape);
        startEventCircle = new Circle(radius).setFillColor("#0000CC").setDraggable(false);
        startEventShape.setLocation(new Point2D(startX, startY));
        startEventShape.getContainer().setUserData("event");
        startEventShape.addChild(startEventCircle, WiresLayoutContainer.Layout.TOP);
        wires_manager.getMagnetManager().createMagnets( startEventShape );
        // startEventShape.addChild(new Rectangle(50, 50).setX(0).setY(0).setFillColor(ColorName.BLACK), WiresPrimitivesContainer.Layout.LEFT);
        // ( (WiresLayoutContainer) startEventShape.getGroup()).add(startEventCircle, WiresLayoutContainer.Layout.CENTER);

        // Green task node.
        WiresShape taskNodeShape = new WiresShape( new MultiPath().rect(0, 0, w, h).setFillColor("#00CC00") );
        wires_manager.register(taskNodeShape);
        taskNodeShape.setLocation(new Point2D(startX + 200, startY));
        taskNodeShape.getContainer().setUserData("task");
        wires_manager.getMagnetManager().createMagnets( taskNodeShape );

        // Yellow task node.
        WiresShape task2NodeShape = new WiresShape( new MultiPath().rect(0, 0, w, h).setFillColor("#FFEB52") );
        wires_manager.register(task2NodeShape);
        task2NodeShape.setLocation(new Point2D(startX + 200, startY + 300));
        task2NodeShape.getContainer().setUserData("task");
        wires_manager.getMagnetManager().createMagnets( task2NodeShape );

        // Red end event.
        WiresShape endEventShape = new WiresShape( new MultiPath().rect(0, 0, w, h).setStrokeColor("#FFFFFF") );
        wires_manager.register(endEventShape);
        endEventShape.setLocation(new Point2D(startX + 400, startY));
        endEventShape.getContainer().setUserData("event");
        wires_manager.getMagnetManager().createMagnets( endEventShape );

        // Connector from blue start event to green task node.
        connect(layer, startEventShape.getMagnets(), 3, taskNodeShape.getMagnets(), 7, wires_manager, true, false);
        // Connector from green task node to red end event
        connect(layer, taskNodeShape.getMagnets(), 3, endEventShape.getMagnets(), 7, wires_manager, true, false);
        // Connector from blue start event to yellow task node.
        connect(layer, startEventShape.getMagnets(), 3, task2NodeShape.getMagnets(), 7, wires_manager, true, false);

        startEventShape.setDraggable(true).addWiresMoveHandler( new WiresMoveHandler() {
            @Override
            public void onShapeMoved( WiresMoveEvent event ) {
                log( "onShapeMoved [x=" + event.getX() + ", y=" + event.getY()+ "]" );
            }
        } );

        TestsUtils.addResizeHandlers( startEventShape );

        startEventShape.addWiresResizeStartHandler( new WiresResizeStartHandler() {
            @Override
            public void onShapeResizeStart( final WiresResizeStartEvent event ) {
                log( "onShapeResizeStart [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
            }
        } );

        startEventShape.addWiresResizeStepHandler( new WiresResizeStepHandler() {
            @Override
            public void onShapeResizeStep( WiresResizeStepEvent event ) {
                log( "onShapeResizeStep [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
            }
        } );

        startEventShape.addWiresResizeEndHandler( new WiresResizeEndHandler() {
            @Override
            public void onShapeResizeEnd( WiresResizeEndEvent event ) {
                log( "onShapeResizeEnd [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
            }
        } );

        addButton(layer);
        addButton2(layer);
    }

    private double getCircleRadius() {
        final double w = startEventShape.getContainer().getBoundingBox().getWidth();
        return w / 2;
    }

    private Rectangle button;

    private void addButton(final Layer layer) {
        button = new Rectangle(50, 50).setFillColor(ColorName.BLACK);
        button.addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {

                // startEventShape.getGroup().setX(100).setY(100);

                logButton(button);
                button.setScale(2, 2);
                layer.batch();
                logButton(button);
                button.setX(100);
                button.setY(100);
            }
        });
        layer.add(button);
    }

    private void addButton2(final Layer layer) {
        final Rectangle button2 = new Rectangle(50, 50).setX(0).setY(600).setFillColor(ColorName.BLACK);
        button2.addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                logButton(button);
                button.setWidth(button.getWidth() + 1);
                button.setHeight(button.getHeight() + 1);
                logButton(button);
            }
        });
        layer.add(button2);
    }

    private void logButton(Rectangle button) {
        GWT.log("Button [x=" + button.getX() + ", y=" + button.getY()
                + ", w=" + button.getWidth() + ", h=" + button.getHeight() + "]");
    }

    private void connect(Layer layer, MagnetManager.Magnets headMagnets, int headMagnetsIndex, MagnetManager.Magnets tailMagnets, int tailMagnetsIndex, WiresManager wires_manager,
                         final boolean tailArrow, final boolean headArrow)
    {
        WiresMagnet m0_1 = headMagnets.getMagnet(headMagnetsIndex);

        WiresMagnet m1_1 = tailMagnets.getMagnet(tailMagnetsIndex);

        double x0 = m0_1.getControl().getX();

        double y0 = m0_1.getControl().getY();

        double x1 = m1_1.getControl().getX();

        double y1 = m1_1.getControl().getY();

        OrthogonalPolyLine line = createLine(x0, y0, (x0 + ((x1 - x0) / 2)), (y0 + ((y1 - y0) / 2)), x1, y1);

       /* OLD CONNECTORS STUFF

            WiresConnector connector = wires_manager.createConnector(m0_1, m1_1, line,
                headArrow ? new SimpleArrow(20, 0.75) : null,
                tailArrow ? new SimpleArrow(20, 0.75) : null);

        connector.getDecoratableLine().setStrokeWidth(5).setStrokeColor("#0000CC");*/
    }

    private final OrthogonalPolyLine createLine(final double... points)
    {
        return new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
    }

    private void log(String message) {
        // GWT.log(message);
    }
}