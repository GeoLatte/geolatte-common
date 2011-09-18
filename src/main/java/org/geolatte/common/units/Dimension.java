/*
 * This file is part of the GeoLatte project.
 *  *
 * GeoLatte is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GeoLatte is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2009 - 2010 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.common.units;

public class Dimension<U extends Number> {

    private final U width;

    private final U height;

    private Dimension(U width, U height){
        this.width = width;
        this.height = height;
    }

    public static <U extends Number> Dimension of(U width, U height){
        return new Dimension<U>(width, height);
    }

    public U getWidth(){
        return this.width;
    }

    public U getHeight(){
        return this.height;
    }

}
