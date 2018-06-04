package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Picture;
import com.ait.lienzo.client.core.shape.SVGPath;
import com.ait.lienzo.shared.core.types.ColorName;
import org.roger600.lienzo.client.resources.LienzoTestsResources;

public class SVGTests extends MyLienzoTest {

    @Override
    public void test(Layer layer) {
        SVGPath path = getSVGPath();
        Picture picture = getSvgImage();
        layer.add(path);
        layer.add(picture.setX(200).setY(200));
    }

    private SVGPath getSVGPath() {
        final String rectPath = "M0,0 100,0 100,100 0,100Z";
        final SVGPath rectSvgPath = new SVGPath(rectPath)
                .setFillColor(ColorName.GREEN);
        return rectSvgPath;
    }

    private Picture getSvgImage() {
        final String uri = LienzoTestsResources.INSTANCE.rectangleSVG().getSafeUri().asString();
        final Picture picture = new Picture(uri);
        picture.setScale(0.5, 0.5);
        return picture;
    }
}
