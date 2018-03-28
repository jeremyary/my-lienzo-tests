package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.mediator.EventFilter;
import com.ait.lienzo.client.core.mediator.IEventFilter;
import com.ait.lienzo.client.core.mediator.Mediators;
import com.ait.lienzo.client.core.mediator.MousePanMediator;
import com.ait.lienzo.client.core.mediator.MouseWheelZoomMediator;
import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.MultiPathDecorator;
import com.ait.lienzo.client.core.shape.OrthogonalPolyLine;
import com.ait.lienzo.client.core.shape.wires.IControlHandleList;
import com.ait.lienzo.client.core.shape.wires.IDockingAcceptor;
import com.ait.lienzo.client.core.shape.wires.MagnetManager;
import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresMagnet;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.user.client.ui.FlowPanel;


public class ConnectorsAndParentsTests2 extends FlowPanel implements MyLienzoTest, HasMediators {

    private final IEventFilter[] zommFilters = new IEventFilter[] { EventFilter.CONTROL };
    private final IEventFilter[] panFilters = new IEventFilter[] { EventFilter.SHIFT };

    private Layer layer;
    private IControlHandleList m_ctrls;
    private WiresShape startEventShape;
    private Circle startEventCircle;

    public void test(Layer layer) {

        this.layer = layer;

        WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.getMagnetManager().setHotspotSize( 10 );
        initConnectionAcceptor( wires_manager );
        initDockingAcceptor( wires_manager, 20 );

        final double startX = 200;
        final double startY = 200;
        final double radius = 50;
        final double w = 100;
        final double h = 100;

        WiresShape parentShape = new WiresShape(new MultiPath().rect(0, 0, 800, 400).setStrokeColor( ColorName.YELLOW ));
        wires_manager.register( parentShape );
        parentShape.setDraggable( true );
        TestsUtils.addResizeHandlers( parentShape );
        parentShape.setLocation(new Point2D(50, 50));
        parentShape.getContainer().setUserData(ColorName.YELLOW.getColorString());
        wires_manager.getMagnetManager().createMagnets(parentShape);

        // Blue start event.
        MultiPath startEventMultiPath = new MultiPath().rect(0, 0, w, h).setStrokeColor( ColorName.BLUE );
        WiresShape startEventShape = new WiresShape(startEventMultiPath);
        wires_manager.register( startEventShape );
        Circle startEventCircle = new Circle(radius).setFillColor(ColorName.BLUE).setDraggable(false);
        startEventShape.setLocation(new Point2D(startX, startY));
        startEventShape.getContainer().setUserData(ColorName.BLUE.getColorString());
        startEventShape.addChild(startEventCircle, WiresLayoutContainer.Layout.CENTER);
        wires_manager.getMagnetManager().createMagnets( startEventShape );
        startEventShape.setDraggable( true );
        TestsUtils.addResizeHandlers( startEventShape );

        parentShape.add( startEventShape );

        // Green task node.
        WiresShape taskNodeShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor( ColorName.GREEN ));
        wires_manager.register( taskNodeShape );
        taskNodeShape.setDraggable( true );
        TestsUtils.addResizeHandlers( taskNodeShape );
        taskNodeShape.setLocation(new Point2D(startX + 200, startY));
        taskNodeShape.getContainer().setUserData(ColorName.GREEN.getColorString());
        wires_manager.getMagnetManager().createMagnets(taskNodeShape);

        parentShape.add( taskNodeShape );

        // Yellow task node.
        WiresShape task2NodeShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor( ColorName.YELLOW ));
        wires_manager.register( task2NodeShape );
        task2NodeShape.setDraggable( true );
        TestsUtils.addResizeHandlers( task2NodeShape );
        startEventShape.setLocation(new Point2D(startX + 200, startY + 300));
        task2NodeShape.getContainer().setUserData(ColorName.YELLOW.getColorString());
        wires_manager.getMagnetManager().createMagnets(task2NodeShape);

        // Red end event.
        WiresShape endEventShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setStrokeColor( ColorName.RED ));
        wires_manager.register( endEventShape );
        endEventShape.setDraggable( true );
        TestsUtils.addResizeHandlers( endEventShape );
        startEventShape.setLocation(new Point2D(startX + 700, startY));
        endEventShape.getContainer().setUserData(ColorName.RED.getColorString());
        wires_manager.getMagnetManager().createMagnets(endEventShape);


        // Connector from blue start event to green task node.
        connect(layer, startEventShape.getMagnets(), 3, taskNodeShape.getMagnets(), 7, wires_manager);
        // Connector from green task node to red end event
        connect(layer, taskNodeShape.getMagnets(), 3, endEventShape.getMagnets(), 7, wires_manager);
        // Connector from blue start event to yellow task node.
        connect(layer, startEventShape.getMagnets(), 3, task2NodeShape.getMagnets(), 7, wires_manager);

        addMediators( layer );
    }

    private void initDockingAcceptor( final WiresManager wires_manager, final int hotspotSize ) {
        wires_manager.setDockingAcceptor( new IDockingAcceptor() {
            @Override
            public boolean dockingAllowed( WiresContainer parent, WiresShape child ) {
                return true;
            }

            @Override
            public boolean acceptDocking( WiresContainer parent, WiresShape child ) {
                return true;
            }

            @Override
            public int getHotspotSize() {
                return hotspotSize;
            }
        } );
    }

    private void initConnectionAcceptor( final WiresManager wires_manager ) {



    }
    private void connect(Layer layer, MagnetManager.Magnets magnets0, int i0_1, MagnetManager.Magnets magnets1, int i1_1, WiresManager wiresManager)
    {

        WiresMagnet m0_1 = magnets0.getMagnet(i0_1);
        WiresMagnet m1_1 = magnets1.getMagnet(i1_1);

        double x0, x1, y0, y1;

        MultiPath head = new MultiPath();
        head.M(15, 20);
        head.L(0, 20);
        head.L(15/2, 0);
        head.Z();

        MultiPath tail = new MultiPath();
        tail.M(15, 20);
        tail.L(0, 20);
        tail.L(15/2, 0);
        tail.Z();

        OrthogonalPolyLine line;
        x0 = m0_1.getControl().getX();
        y0 = m0_1.getControl().getY();
        x1 = m1_1.getControl().getX();
        y1 = m1_1.getControl().getY();
        line = createLine(layer, 0, 0, x0, y0, (x0 + ((x1-x0)/2)), (y0 + ((y1-y0)/2)), x1, y1);
        final double hh = head.getBoundingBox().getHeight();
        line.setHeadOffset( hh );
        final double th = tail.getBoundingBox().getHeight();
        line.setTailOffset( th );

        WiresConnector connector = new WiresConnector(m0_1, m1_1, line,
                new MultiPathDecorator(head),
                new MultiPathDecorator(tail));

        wiresManager.register( connector );

        head.setStrokeWidth(5).setStrokeColor("#0000CC");
        tail.setStrokeWidth(5).setStrokeColor("#0000CC");
        line.setStrokeWidth(5).setStrokeColor("#0000CC");
    }

    private final OrthogonalPolyLine createLine( final Layer layer,
                                                 final double... points )
    {
        final OrthogonalPolyLine line = new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
        layer.add( line );
        return line;
    }

    private void addMediators( final Layer layer ) {
        final Mediators mediators = layer.getViewport().getMediators();
        mediators.push( new MouseWheelZoomMediator( zommFilters ) );
        mediators.push( new MousePanMediator( panFilters ) );
    }

    @Override
    public int compareTo(MyLienzoTest other) {
        return this.getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
    }
}