
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
import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;

/**
 * Defines a writer who generates an entire method. 
 *  
 * @version 0.2
 * @author Nicolas Désy
 */
public interface MethodWriter<T extends FlexGeneratorHandler> {
    
    /**
     * Determines if this writer accepts a class.
     * @param clazz The class to check
     * @return a boolean which indicated if the writer accepts the class
     */
    public boolean acceptClass(Class<?> clazz);
    
    
    /**
     * Writes method signature and body to the writer
     * @param w 
     *     The Writer
     * @param g 
     *     The current generator
     * @param handler 
     *     The current handler
     * @param clazz 
     *     The current clazz
     * @throws java.io.IOException 
     */
    public void writeMethod(Writer w,
            FlexGenerator g, T handler, Class<?> clazz) throws IOException;
    
    
    /**
     * Returns the priority for this writer. Writer with the highest value will
     * be the first executed.
     * @return The priority for this writer
     */
    public int getPriority();
    
    
    /**
     * Comparator used to sort a List of MethodWriter
     */
    public final static Comparator<MethodWriter> COMPARATOR = 
            new Comparator<MethodWriter>(){        
        public int compare(MethodWriter o1, MethodWriter o2) {
            if(o1.getPriority() > o2.getPriority()) return -1;
            if(o1.getPriority() == o2.getPriority()) return 0;
            return 1;           
        }
    };
}
