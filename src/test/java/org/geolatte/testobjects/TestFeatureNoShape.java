/*
 * This file is part of the GeoLatte project.
 *
 *     GeoLatte is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     GeoLatte is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010 - 2010 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.testobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple testfeature without a shape.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 20-apr-2010<br>
 * <i>Creation-Time</i>: 11:14:44<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
   public class TestFeatureNoShape {

        private String name;
        private int length;
        private List<String> owners;
        private int id;

        private SubObject subObject;

        public TestFeatureNoShape(String name, int length, String[] ownerNames, int idValue) {
            this(name, length, ownerNames, idValue, null, null);
        }

        public TestFeatureNoShape(String name, int length, String[] ownerNames, int idValue, String subObjectName) {
            this(name, length, ownerNames, idValue, subObjectName, null);
        }

        public TestFeatureNoShape(String name, int length, String[] ownerNames, int idValue, String subObjectName, String subSubObjectName) {
            this.name = name;
            id = idValue;
            owners = new ArrayList<String>();
            for (String ownerName : ownerNames) {
                owners.add(ownerName);
            }
            this.length = length;
            id = idValue;

            subObject = new SubObject();
            subObject.setName(subObjectName);
            subObject.getSubSubObject().setName(subSubObjectName);
        }

        public String getName() {
            return name;
        }

        public int getLength() {
            return length;
        }

        public List<String> getOwners() {
            return owners;
        }

        public int getId() {
            return id;
        }

        public SubObject getSubObject() {
        return subObject;
        }

        public void setSubObject(SubObject subObject) {
            this.subObject = subObject;
        }

        public class SubObject {

            private String name;
            private SubSubObject subSubObject;

            public SubObject() {
                subSubObject = new SubSubObject();
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public SubSubObject getSubSubObject() {
                return subSubObject;
            }
        }

        public class SubSubObject {

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }