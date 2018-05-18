
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

import com.liguorien.flex.generator.utils.ClassType;
import com.liguorien.flex.generator.writers.ClassWriter;
import com.liguorien.flex.generator.writers.as3.AS3Writer;
import com.liguorien.flex.generator.writers.as3.methods.GetBasicXML;
import com.liguorien.flex.generator.writers.as3.methods.GetEntityArray;
import com.liguorien.flex.generator.writers.as3.methods.GetBasicXMLList;
import com.liguorien.flex.generator.writers.as3.methods.GetEntityMap;
import com.liguorien.flex.generator.writers.as3.methods.GetEntityMapXML;
import com.liguorien.flex.generator.writers.as3.methods.GetEntityXMLList;
import com.liguorien.flex.generator.writers.as3.methods.GetInstance;
import com.liguorien.flex.generator.writers.as3.methods.GetXML;
import com.liguorien.flex.generator.writers.as3.properties.BasicArrayProperty;
import com.liguorien.flex.generator.writers.as3.properties.BasicMapProperty;
import com.liguorien.flex.generator.writers.as3.properties.E4XProperty;
import com.liguorien.flex.generator.writers.as3.properties.EntityArrayProperty;
import com.liguorien.flex.generator.writers.as3.properties.EntityMapProperty;
import com.liguorien.flex.generator.writers.as3.properties.EntityProperty;
import com.liguorien.flex.generator.writers.as3.properties.StringProperty;
import java.io.File;

/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class FlexModelBuilderHandler extends
        AbstractGeneratorHandler<FlexModelBuilderHandler> {
  
    
    private String _builderSuffix;
    
    
    public FlexModelBuilderHandler(){
        this("Builder");
    }
    
    /**
     * Creates a new instance of FlexModelBuilderHandler
     */
    public FlexModelBuilderHandler(String builderSuffix){
        super();
        _builderSuffix = builderSuffix;    
        
        addPropertyWriter(
                new E4XProperty("int", "int(", ")", 0));
        addPropertyWriter(
                new E4XProperty("Boolean", "(", " == \"true\")", 0));
        addPropertyWriter(
                new E4XProperty("Number", "Number(", ")", 0));
        addPropertyWriter(
                new E4XProperty("Array", "", " == \"true\")", 0));
        addPropertyWriter(
                new StringProperty());
        addPropertyWriter(
                new EntityArrayProperty());
        addPropertyWriter(
                new BasicArrayProperty());
        addPropertyWriter(
                new EntityProperty());
        addPropertyWriter(
                new EntityMapProperty());
        addPropertyWriter(
                new BasicMapProperty());
        
        addMethodWriter(
                new GetInstance());
        addMethodWriter(
                GetXML.getDefault());
        addMethodWriter(
                new GetBasicXML()); 
        addMethodWriter(
                new GetEntityArray());
        addMethodWriter(
                new GetEntityXMLList());
        addMethodWriter(
                new GetBasicXMLList());
        addMethodWriter(
                new GetEntityMap());
        addMethodWriter(
                new GetEntityMapXML());
        
    }
   
    
    /**
     *
     */
    public File getOutputDirectory() {
        return getGenerator().getFlexOutputDirectory();
    }
    
    /**
     *
     */
    public String getBuilderSuffix(){
        return _builderSuffix;
    }
    
    public ClassType getClassType(){
        return ClassType.CLIENT_BUILDERS;
    }
    
    /**
     *
     */
    public void handleClassBegin(Class<?> clazz) {          
        super.handleClassBegin(clazz, AS3Writer.getContextWriter(), 
                getClassName(clazz).append(_builderSuffix).toString(), 
                null, null);
    }
    
    /**
     *
     */
    public void handleClassEnd(Class<?> clazz) {
       super.handleClassEnd(clazz, AS3Writer.getContextWriter());
    }
    
    /**
     *
     */
    protected void appendClassSuffix(StringBuilder buffer) {
        super.appendClassSuffix(buffer);
        buffer.append(_builderSuffix);
    }
    
    /**
     *
     */
    protected void appendExtension(StringBuilder buffer) {
        buffer.append(".as");
    }
    
    public ClassWriter getClassWriter() {
        return AS3Writer.getContextWriter();
    }
}
