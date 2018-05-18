
/**
 *   Copyright (C) 2006 Nicolas Désy.  All rights reserved.
 *   
 *   This file is part of FlexGenerator
 *
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 *   
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *   Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package com.liguorien.flex.generator.utils;

/**
 * <p>Used to define the way to access a property on a XML element.</p>
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public enum NodeType {
    
    /**
     * <p>The property is an attribute. This is used for primitive types and 
     * String.</p>
     */
    ATTRIBUTE,
    
    /**
     * <p>The property is element. This is used for Entity classes, Array, Map 
     * and Collection.</p>
     */
    ELEMENT,
    
    /**
     * <p>The property is text element. This is used for primitive types and 
     * String.</p>
     */
    TEXT
}