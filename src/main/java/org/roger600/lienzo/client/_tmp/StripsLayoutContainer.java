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
package org.roger600.lienzo.client._tmp;

import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.layouts.base.DelegateLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.layouts.impl.GridLayoutContainer;

/***
 * @author jary@redhat.com
 */
public class StripsLayoutContainer extends DelegateLayoutContainer<StripsLayoutContainer> {

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }
    private static final double GRID_CELL_HEIGHT = 15;
    private static final double GRID_CELL_WIDTH = 15;

    private Orientation orientation;
    private final GridLayoutContainer gridLayoutContainer;
    private Double nextRow;
    private Double nextCol;

    public StripsLayoutContainer(final Orientation orientation, final double parentHeight, final double parentWidth) {
        this.orientation = orientation;
        this.nextRow = Math.ceil(parentHeight / GRID_CELL_HEIGHT);
        this.nextCol = Math.ceil(parentWidth / GRID_CELL_WIDTH);
        this.gridLayoutContainer = new GridLayoutContainer(nextRow.intValue() - 1,
                                                           nextCol.intValue() - 1);
    }

    public StripsLayoutContainer add(final WiresShape child) {

        if (orientation.equals(Orientation.VERTICAL)) {
            getDelegate().add(child, nextRow.intValue(), 1);
            nextRow += Math.ceil(child.getBoundingBox().getHeight() / GRID_CELL_HEIGHT);
            return this;

        } else {
            getDelegate().add(child, 1, nextCol.intValue());
            nextCol += Math.ceil(child.getBoundingBox().getWidth() / GRID_CELL_WIDTH);
            return this;
        }
    }

    @Override
    public GridLayoutContainer getDelegate() {
        return gridLayoutContainer;
    }
}
