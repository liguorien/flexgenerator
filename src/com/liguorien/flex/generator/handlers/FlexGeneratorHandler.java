
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

package com.liguorien.flex.generator.handlers;

import com.liguorien.flex.generator.FlexGenerator;
import com.liguorien.flex.generator.utils.ClassType;
import java.beans.PropertyDescriptor;


/**
 * 
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public interface FlexGeneratorHandler {
    
    /**
     * 
     * @param clazz 
     */
    public void handleClassBegin(Class<?> clazz);
    
    /**
     * 
     * @param property 
     */
    public void handleProperty(PropertyDescriptor property);
    
    /**
     *
     */
    public void handleClassEnd(Class<?> clazz);
    
    /**
     * 
     * @param generator 
     */
    public void setGenerator(FlexGenerator generator);
    
    /**
     *
     */
    public Class<?> getCurrentClass();
    
    /**
     *
     */
    public ClassType getClassType();
        
}
