
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

package com.liguorien.flex.generator.writers.as3;

/**
 * @version 0.2
 * @author Nicolas Désy
 */
public abstract class ContextMethodWriter extends BuilderMethodWriter {
    
    private String _typeName;
    
    /** Creates a new instance of ContextMethodWriter */
    public ContextMethodWriter(String typeName) {
        _typeName = typeName;
    }
    
    public String getTypeName(){
        return _typeName;
    }
    
    public boolean equals(Object obj) {
        if(obj instanceof ContextMethodWriter){
            final ContextMethodWriter o = (ContextMethodWriter)obj;
            return _typeName.equals(o._typeName) &&
                    getClass().equals(o.getClass());
        }
        return false;
    }
    
    public int hashCode(){
        return _typeName.hashCode();
    }
}
