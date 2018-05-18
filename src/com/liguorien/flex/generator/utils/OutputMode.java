
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
 * <p>Used to define different output 
 * modes available to the FlexModelGenerator</p>
 * <p>The current modes are : </p>
 * <ul>
 *    <li>{@link #FILE}</li>
 *    <li>{@link #CONSOLE}</li>
 * </ul>
 *
 * @see FlexGenerator#setOutputMode
 * @version 0.2
 * @author Nicolas Désy
 * @author Nicolas Désy
 */
public enum OutputMode {
    
    /**
     * <p>The generator will output the result 
     * directly into actionscript file.</p>
     * <p>You must set the output directory of the 
     * FlexModelGenerator to use this mode</p>
     * <p><b>WARNING : </b> if the actionscript file already exists, it will
     * be overwritten. So be careful with this mode and use it only to generate 
     * the skeleton of the actionscript classes</p>
     * @see FlexGenerator#setOutputMode
     * @see FlexGenerator#setFlexOutputDirectory
     * @see FlexGenerator#setJavaOutputDirectory
     */
    FILE,
    
    /**
     * <p>The generator will output the result 
     * directly to the system output.</p>
     * @see FlexGenerator#setOutputMode
     */
    CONSOLE
}
