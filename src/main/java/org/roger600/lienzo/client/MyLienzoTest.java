package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.GridLayer;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Line;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.FlowPanel;

public abstract class MyLienzoTest implements Comparable<MyLienzoTest> {

    public static final int DEFAULT_PANEL_WIDTH = 1200;
    public static final int DEFAULT_PANEL_HEIGHT = 900;

    LienzoPanel panel = null;
    Layer layer = null;
    FlowPanel testContainmentPanel;

    public abstract void test(final Layer layer);

    Layer getLayer() {
        if (null == layer) {
            layer = new Layer();
        }
        return layer;
    }

    LienzoPanel getPanel() {

        if (null == panel) {
            this.panel = new LienzoPanel(DEFAULT_PANEL_WIDTH,
                                         DEFAULT_PANEL_HEIGHT);

            applyBackgroundGridLayer(panel);
            getLayer().setTransformable(true);
            panel.add(getLayer());
        }
        return panel;
    }

    static void applyBackgroundGridLayer(final LienzoPanel panel) {

        Line line1 = new Line(0, 0, 0, 0)
                .setStrokeColor("#0000FF")
                .setAlpha(0.2);

        Line line2 = new Line(0, 0, 0, 0)
                .setStrokeColor("#00FF00")
                .setAlpha(0.2);
        line2.setDashArray(2, 2);

        GridLayer gridLayer = new GridLayer(100, line1, 25, line2);
        panel.setBackgroundLayer(gridLayer);
    }

    public int compareTo(MyLienzoTest o) {
        return this.getClass().getSimpleName().compareTo(o.getClass().getSimpleName());
    }

    public FlowPanel getTestContainmentPanel() {
        return testContainmentPanel;
    }

    public void setTestContainmentPanel(FlowPanel testContainmentPanel) {
        this.testContainmentPanel = testContainmentPanel;
    }
}
