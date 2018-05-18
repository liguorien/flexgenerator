
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

package com.liguorien.flex.generator.writers;

import com.liguorien.flex.generator.*;
import com.liguorien.flex.generator.handlers.FlexGeneratorHandler;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;

/**
 * Defines a writer who generates code to access a property. 
 * @version 0.2
 * @author Nicolas Désy
 */
public interface PropertyWriter<T extends FlexGeneratorHandler> {
    
    /**
     * Returns the priority for this writer. Writer with the highest value will
     * be the first executed.
     * @return 
     *     The priority for this writer
     */
    public int getPriority();
    
    /**
     * Determines if this writer accepts to write the property
     * @param prop 
     *     The property to check
     * @param handler 
     *     The current generator handler
     * @return 
     *     A boolean value which indicates if the writer accepts to 
     *    write the property 
     */
    public boolean acceptProperty(PropertyDescriptor prop, T handler);
    
    /**
     * Writes code to access a property.
     * @param w 
     *     The Writer
     * @param g 
     *     The current generator
     * @param handler 
     *     The current handler
     * @param varName 
     *     The name of variable where the property is accessed 
     * @param prop 
     *     The ProspectorDescriptor of the current property
     * @throws java.io.IOException 
     */
    public void writeProperty(Writer w, FlexGenerator g, T handler, 
            String varName, PropertyDescriptor prop) throws IOException;    
    
    
    
    /**
     * Comparator used to sort a List of PropertyWriter
     */
    public final static Comparator<PropertyWriter> COMPARATOR =
            new Comparator<PropertyWriter>() {
        public int compare(PropertyWriter o1, PropertyWriter o2) {
            if(o1.getPriority() > o2.getPriority()) return -1;
            if(o1.getPriority() == o2.getPriority()) return 0;
            return 1;
        }
    };
}
