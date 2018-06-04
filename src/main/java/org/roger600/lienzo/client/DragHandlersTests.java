package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeDragEndEvent;
import com.ait.lienzo.client.core.event.NodeDragEndHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Panel;

public class DragHandlersTests extends MyLienzoTest implements HasButtons {

    public void test(Layer deprecatedParam) {

        Rectangle rectangle = new Rectangle(100, 100)
                .setDraggable(true)
                .setX(20)
                .setY(20)
                .setStrokeWidth(1d)
                .setFillColor(ColorName.BLACK);

//        rectangle.addNodeDragEndHandler( h1 );
//        rectangle.addNodeDragEndHandler( h2 );

        getLayer().add(rectangle);
    }

    NodeDragEndHandler h1 = new NodeDragEndHandler() {
        @Override
        public void onNodeDragEnd(NodeDragEndEvent nodeDragEndEvent) {
            GWT.log("H1");
        }
    };

    NodeDragEndHandler h2 = new NodeDragEndHandler() {
        @Override
        public void onNodeDragEnd(NodeDragEndEvent nodeDragEndEvent) {
            GWT.log("H2");
        }
    };

    @Override
    public void setButtonsPanel(Panel panel) {

    }
}