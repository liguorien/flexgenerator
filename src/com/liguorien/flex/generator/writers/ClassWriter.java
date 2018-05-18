
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

import com.liguorien.flex.generator.FlexGenerator;
import com.liguorien.flex.generator.FlexNode;
import com.liguorien.flex.generator.utils.NodeType;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 * 
 * @version 0.2
 * @author Nicolas Désy
 */
public interface ClassWriter {
   
    /**
     * Returns the indentation level of the class declaration
     * @return 
     *    An integer which represents the indentation level (ie. java=0, as3=1)
     */
    public int getClassIndentationLevel();
    
    /**
     * Writes code to access a property.
     * @param w 
     *     The writer used to output the result
     * @param g 
     *     The current generator.
     * @param varName 
     *     The name of variable where the property is accessed 
     * @param prop 
     *     The ProspectorDescriptor of the current property
     * @param prefix 
     *     The code to be written before generated code (can be null)
     * @param suffix 
     *     The code to be written after generated code (can be null)
     * @param type 
     *     The type of XML element for this property
     * @throws java.io.IOException 
     *     If an IO error occurs
     * @see com.liguorien.flex.generator.utils.NodeType
     */
    public void writeProperty(Writer w, FlexGenerator g, String varName,
            PropertyDescriptor prop, String prefix, String suffix,
            NodeType type) throws IOException;
    
    /**
     * Writes package declaration
     * @param w 
     *     The writer used to output the result
     * @param g 
     *     The current generator.
     * @param clazz 
     *     The current class
     * @param imports 
     *     A Set of import statement.
     * @throws java.io.IOException 
     *     If an IO error occurs
     */
    public void writePackageDeclaration(
            Writer w, FlexGenerator g, Class<?> clazz, Set<String> imports)
            throws IOException;
    
    /**
     * Writes package declaration
     * @param w 
     *     The writer used to output the result
     * @param g 
     *     The current generator.
     * @param className 
     *     The name of the generated class
     * @param subClass 
     *     The subclass of the generated class (can be null)
     * @param interfaces 
     *     The interfaces implemented by the generated class (can be null)
     * @throws java.io.IOException 
     */
    public void writeClassDeclaration(
            Writer w, FlexGenerator g, String className,
            String subClass, Set<String> interfaces) throws IOException;
    
    /**
     * Writes class's end declaration. Typically a closing curly brace.
     * @param w 
     *    The writer used to output the result
     * @param g 
     *    The current generator
     * @throws java.io.IOException 
     */
    public void writeClassEnd(Writer w, FlexGenerator g) throws IOException;
}
