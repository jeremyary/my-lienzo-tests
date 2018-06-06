package org.roger600.lienzo.client.ks;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.Point2D;
import com.google.gwt.user.client.ui.FlowPanel;
import org.roger600.lienzo.client.MyLienzoTest;
import org.roger600.lienzo.client.TestsUtils;
import org.roger600.lienzo.client._tmp.COLOR;
import org.roger600.lienzo.client._tmp.StripsLayoutContainer;

public class StripsLayoutContainerTests extends FlowPanel implements MyLienzoTest {

    private static final double STROKE_WIDTH = 3;

    private static final double PARENT_WIDTH = 250;
    private static final double PARENT_HEIGHT = 100;

    private static final double CHILD_WIDTH = 150;
    private static final double CHILD_HEIGHT = 50;

    private int childMargin = 0;

    public void test(final Layer layer) {

        WiresManager wiresManager = WiresManager.get(layer);
        wiresManager.setContainmentAcceptor(new IContainmentAcceptor() {
            @Override
            public boolean containmentAllowed(WiresContainer parent, WiresShape[] children) {
                return true;
            }

            @Override
            public boolean acceptContainment(WiresContainer parent, WiresShape[] children) {
                return true;
            }
        });

        // ------ CREATE A RED PARENT SHAPE WITH VERTICAL ORIENTATION AND ADD 3 CHILD SHAPES
        WiresShape verticalParent = createParent(StripsLayoutContainer.Orientation.VERTICAL,
                                                 COLOR.RED,
                                                 25,
                                                 25);

        StripsLayoutContainer verticalStripsLayoutContainer = (StripsLayoutContainer) verticalParent.getLayoutContainer();
        wiresManager.register(verticalParent);

        verticalStripsLayoutContainer.add(createChild(COLOR.BLUE));
        verticalStripsLayoutContainer.add(createChild(COLOR.GREEN));
        verticalStripsLayoutContainer.add(createChild(COLOR.BLACK));


        // ------ SAME, BUT WITH HORIZONTAL ORIENTATION
        WiresShape horizontalParent = createParent(StripsLayoutContainer.Orientation.HORIZONTAL,
                                                   COLOR.RED,
                                                   400,
                                                   100);

        StripsLayoutContainer horizontalStripsLayoutContainer = (StripsLayoutContainer) horizontalParent.getLayoutContainer();
        wiresManager.register(horizontalParent);

        horizontalStripsLayoutContainer.add(createChild(COLOR.BLUE));
        horizontalStripsLayoutContainer.add(createChild(COLOR.GREEN));
        horizontalStripsLayoutContainer.add(createChild(COLOR.BLACK));
    }

    private WiresShape createParent(StripsLayoutContainer.Orientation orientation, COLOR color, int posX, int posY) {
        return new WiresShape(new MultiPath()
                                      .rect(0,
                                            0,
                                            PARENT_WIDTH,
                                            PARENT_HEIGHT)
                                      .setStrokeWidth(STROKE_WIDTH)
                                      .setStrokeColor(color.hex())
                                      .setFillColor(color.hex().replaceAll("0", "6")),
                              new StripsLayoutContainer(orientation,
                                                        PARENT_HEIGHT,
                                                        PARENT_WIDTH))
                .setDraggable(true)
                .setLocation(new Point2D(posX,
                                         posY));
    }

    private WiresShape createChild(COLOR color) {
        return new WiresShape(new MultiPath()
                                      .rect(0,
                                            0,
                                            CHILD_WIDTH,
                                            CHILD_HEIGHT)
                                      .setStrokeWidth(STROKE_WIDTH)
                                      .setStrokeColor(color.hex())
                                      .setFillColor(color.hex().replaceAll("0", "6")))
                .setDraggable(true)
                .setLocation(new Point2D(100, childMargin += 100));
    }

    private void addResizeHandler(final WiresShape shape) {
        TestsUtils.addResizeHandlers(shape);
    }
}
