package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.mediator.EventFilter;
import com.ait.lienzo.client.core.mediator.IEventFilter;
import com.ait.lienzo.client.core.mediator.Mediators;
import com.ait.lienzo.client.core.mediator.MousePanMediator;
import com.ait.lienzo.client.core.mediator.MouseWheelZoomMediator;
import com.ait.lienzo.client.core.shape.GridLayer;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Line;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.roger600.lienzo.client.ks.CardinalIntersectKSTests;
import org.roger600.lienzo.client.ks.MultiPathResizeTests;
import org.roger600.lienzo.client.ks.StripsLayoutContainerTests;
import org.roger600.lienzo.client.ks.WiresAlignDistroTests;
import org.roger600.lienzo.client.ks.WiresArrowsTests;
import org.roger600.lienzo.client.ks.WiresDockingTests;
import org.roger600.lienzo.client.ks.WiresResizesTests;
import org.roger600.lienzo.client.ks.WiresSquaresTests;

public class LienzoTests implements EntryPoint {

    public static final int WIDE = 1200;
    public static final int HIGH = 900;

    private final IEventFilter[] zommFilters = new IEventFilter[] { EventFilter.CONTROL };
    private final IEventFilter[] panFilters = new IEventFilter[] { EventFilter.SHIFT };

    private final static MyLienzoTest[] TESTS = new MyLienzoTest[] {
            new SelectionManagerTests(),
            // TODO: new TextWrapTests(),
            new AutoMagnetsConnectorsTests(),
            new CardinalIntersectSimpleTest(),
            new WiresDragHandlersTests(),
            new DragHandlersTests(),
            new SVGPicturesTests(),
            new ContainerTests(),
            new SVGTests(),
            new UXSVGTests(),
            new DragConstraintsTests(),
            // TODO: new FontTests(),
            new ImagesTests(),
            // TODO: new MultiPathShapesTests(),
            // TODO: new WiresRingTests(),
            // TODO: new BasicWiresShapesTests(),
            new GlyphPositionsAndScaleTests(),
            new TransformTests(),
            // TODO: new MagnetsAndCPsTests(),
            new BoundingBoxTests(),
            new WiresDragAndMoveTests(),
            new ShapeResizeTests(),
            new DragBoundsTests(),
            new LayoutContainerChildrenTests(),
            // TODO: new LayoutContainerChildrenTests2(),
            // TODO: new ChildRectangleResizeTests(),
            // TODO: new ChildCircleResizeTests(),
            // TODO: new StandaloneConnectorsTests(),
            // TODO: new ConnectionAndMagnetsTests(),
            // TODO: new ConnectionAcceptorsTests(),
            // TODO: new ConnectorsSelectionTests(),
            // TODO: new ConnectorsAndParentsTests(),
            // TODO: new ConnectorsAndParentsTests2(),
            new DeleteChildTests(),
            new DockingTests(),
            // TODO: new MarkConnectorTests(),
            new MediatorsTests(),
            new MediatorsTests2(),
            // TODO: new WiresTests(),
            new MultiPathAttributesChangedTests(),
            // TODO new CaseModellerContainmentTests(),
            // From Lienzo KS
            new WiresAlignDistroTests(),
            new CardinalIntersectKSTests(),
            new MultiPathResizeTests(),
            new WiresArrowsTests(),
            new WiresSquaresTests(),
            new WiresResizesTests(),
            new WiresDockingTests(),
            new StripsLayoutContainerTests()
    };

    private static final int MAX_BUTTONS_ROW = 7;
    private VerticalPanel mainPanel = new VerticalPanel();
    private VerticalPanel buttonsPanel = new VerticalPanel();
    private HorizontalPanel screenButtonsPanel = new HorizontalPanel();
    private HorizontalPanel buttonsRowPanel;
    private int buttonsPanelSize = 0;
    private FlowPanel testsPanel = new FlowPanel();

    public void onModuleLoad()
    {
        buttonsPanel.getElement().getStyle().setMargin( 10, Style.Unit.PX );

        RootPanel.get().add( mainPanel );

        for ( final MyLienzoTest test : TESTS ) {

            final Button button = new Button( test.getClass().getSimpleName() );
            button.addClickHandler( new ClickHandler() {
                @Override
                public void onClick( ClickEvent clickEvent ) {
                    createPanelForTest( test );
                }
            } );

            addButton( button );

        }

        mainPanel.add( buttonsPanel );
        mainPanel.add( screenButtonsPanel );
        mainPanel.add( testsPanel );

    }

    private void createPanelForTest( MyLienzoTest test ) {

        screenButtonsPanel.clear();
        testsPanel.clear();
        testsPanel.getElement().getStyle().setMargin( 10, Style.Unit.PX );
        testsPanel.getElement().getStyle().setBorderWidth( 1, Style.Unit.PX );
        testsPanel.getElement().getStyle().setBorderStyle( Style.BorderStyle.SOLID );
        testsPanel.getElement().getStyle().setBorderColor( "#000000" );

        final LienzoPanel panel = new LienzoPanel(WIDE,
                                                  HIGH);
        applyGrid( panel );
        final Layer layer = new Layer();

        testsPanel.add( panel );
        layer.setTransformable(true);
        panel.add(layer);

        if ( test instanceof HasButtons ) {
            ( ( HasButtons ) test ).setButtonsPanel( screenButtonsPanel );
        }

        if ( test instanceof HasMediators ) {
            addMediators( layer );
        }

        if ( test instanceof NeedsThePanel ) {
            ((NeedsThePanel) test).setLienzoPanel(panel);
        }

        test.test( layer );

        layer.draw();

    }

    private void addMediators( Layer layer ) {
        final Mediators mediators = layer.getViewport().getMediators();
        mediators.push( new MouseWheelZoomMediator( zommFilters ) );
        mediators.push( new MousePanMediator( panFilters ) );
    }

    private void addButton( final Button button ) {

        if ( buttonsPanelSize >= MAX_BUTTONS_ROW ) {

            buttonsPanelSize = 0;
            buttonsRowPanel = null;
        }


        if ( null == buttonsRowPanel ) {
            buttonsRowPanel = new HorizontalPanel();
            buttonsPanel.add( buttonsRowPanel );
        }

        buttonsRowPanel.add( button );
        buttonsPanelSize++;
    }

    private void applyGrid( final LienzoPanel panel ) {

        // Grid.
        Line line1 = new Line( 0, 0, 0, 0 )
                .setStrokeColor( "#0000FF" )
                .setAlpha( 0.2 );
        Line line2 = new Line( 0, 0, 0, 0 )
                .setStrokeColor( "#00FF00"  )
                .setAlpha( 0.2 );

        line2.setDashArray( 2,
                2 );

        GridLayer gridLayer = new GridLayer( 100, line1, 25, line2 );

        panel.setBackgroundLayer( gridLayer );
    }

}
