/*
 * Copyright 2005-2017 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.growable.GrowableLienzoPanel;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/***
 * @author jary@redhat.com
 */
public class GrowableLienzoPanelTests implements EntryPoint {

    public static final int SHAPE_DIMENSION = 50;

    @Override
    public void onModuleLoad() {

        GrowableLienzoPanel growableLienzoPanel =
                new GrowableLienzoPanel(1000, 1000, 300, 300);

        WiresShape dragShape = new WiresShape(
                new MultiPath()
                        .setFillColor(ColorName.RED)
                        .rect(100, 50, SHAPE_DIMENSION, SHAPE_DIMENSION));

        growableLienzoPanel.addShape(dragShape, true);

        int count = 0;
        for (int y = 100; y <= 150; y += 10) {
            for (int x = 0; x < 50; x += 10) {
                count++;
                growableLienzoPanel.addShape(new WiresShape(
                                                     new MultiPath()
                                                             .setFillColor(ColorName.BLUE)
                                                             .rect(x, y, 8, 8)),
                                             false);
            }
        }
        GWT.log(count + " static test squares added");
    }
}