/*
 * Copyright 2005-2018 Red Hat, Inc.
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

public enum COLOR {

    RED("CC0000"),
    BLUE("0000CC"),
    GREEN("00CC00"),
    BLACK("000000");

    private String hex;

    COLOR(String hex) {
        this.hex = hex;
    }

    public String hex() {
        return "#" + hex;
    }
}
