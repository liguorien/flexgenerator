
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

import com.liguorien.flex.generator.FlexTransient;
import com.liguorien.flex.generator.utils.TransientMode;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class PropertyDescriptorProxy extends PropertyDescriptor {
    
    public final static String TRANSIENT_PROPERTY = "type";
    
    private Class<?> _type;
    
    public PropertyDescriptorProxy(Class<?> type) throws IntrospectionException{
        super(TRANSIENT_PROPERTY, PropertyDescriptorProxy.class);
        _type = type;
    }
    
    public synchronized Class<?> getPropertyType() {
        return _type;
    }
    
    @FlexTransient(TransientMode.INTERNAL)
    public Class<?> getType(){
        return _type;
    }
    
    public void setType(Class<?> type){
        _type = type;
    }
}