
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
import com.liguorien.flex.generator.writers.dom4j.Dom4jWriter;
import com.liguorien.flex.generator.writers.dom4j.methods.*;
import com.liguorien.flex.generator.writers.dom4j.properties.*;
import java.io.File;
import java.io.IOException;
import java.io.Writer;


/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class Dom4jBuilderHandler
        extends AbstractGeneratorHandler<Dom4jBuilderHandler> {
        
    private String _builderSuffix;
    
    /**
     * Create a new instance of Dom4jBuilderHandler
     */
    public Dom4jBuilderHandler(){
        this("Builder");
    }
    
    /**
     * Create a new instance of Dom4jBuilderHandler
     * @param builderSuffix 
     *     The suffix to be appended to the name of the generated classes.
     */
    public Dom4jBuilderHandler(String builderSuffix){
        super();
        
        _builderSuffix = builderSuffix;
        
        addPropertyWriter(
                new BasicProperty("String", null, null, 0));
        addPropertyWriter(
                new BasicProperty("int", "Integer.parseInt(", ")", 0));
        addPropertyWriter(
                new BasicProperty("Integer", "new Integer(", ")", 0));
        addPropertyWriter(
                new BasicProperty("boolean", "\"true\".equals(", ")", 0));
        addPropertyWriter(
                new BasicProperty("Boolean", "Boolean.valueOf(", ")", 0));
        addPropertyWriter(
                new BasicProperty("double", "Double.parseDouble(", ")", 0));
        addPropertyWriter(
                new BasicProperty("Double", "new Double(", ")", 0));
        addPropertyWriter(
                new BasicProperty("float", "Float.parseFloat(", ")", 0));
        addPropertyWriter(
                new BasicProperty("Float", "new Float(", ")", 0));
        addPropertyWriter(
                new BasicProperty("long", "Long.parseLong(", ")", 0));
        addPropertyWriter(
                new BasicProperty("Long", "new Long(", ")", 0));
        addPropertyWriter(
                new BasicProperty("short", "Short.parseShort(", ")", 0));
        addPropertyWriter(
                new BasicProperty("Short", "new Short(", ")", 0));
        addPropertyWriter(
                new CollectionProperty(5));
        addPropertyWriter(
                new MapProperty(5)); 
        addPropertyWriter(
                new ArrayProperty(5));    
        addPropertyWriter(
                new EntityProperty(5));    
        

        addMethodWriter(new GetInstance());
        addMethodWriter(GetElement.getDefault());
        addMethodWriter(new GetEntityCollection());     
        addMethodWriter(new GetArrayElement());
        addMethodWriter(new GetCollectionElement());
        addMethodWriter(new GetEntityMapElement());       
        addMethodWriter(new GetEntityArrayElement());       
        addMethodWriter(new GetEntityCollectionElement());
        
    }
    
    /**
     * @inheritDoc
     * @return {@link ClassType#SERVER_BUILDERS}
     */
    public ClassType getClassType(){
        return ClassType.SERVER_BUILDERS;
    }
  
    
    /**
     * Return the suffix to be appended to the name of the generated class.
     */
    public String getBuilderSuffix(){
        return _builderSuffix;
    }
    
    /**
     * @inheritDoc    
     */
    public File getOutputDirectory() {        
        return getGenerator().getJavaOutputDirectory();
    }
    
    /**
     * @inheritDoc
     */
    public void handleClassBegin(Class<?> clazz) {
        super.handleClassBegin(clazz, Dom4jWriter.getContextWriter(),
                getClassName(clazz).append(_builderSuffix).toString(), 
                null, null);
    }
    
  
    
    /**
     * @inheritDoc
     */
    protected void appendClassSuffix(StringBuilder buffer) {
        super.appendClassSuffix(buffer);
        buffer.append(_builderSuffix);
    }
    
    /**
     * @inheritDoc 
     */
    protected void appendExtension(StringBuilder buffer) {
        buffer.append(".java");
    }

    /**
     * @inheritDoc
     */
    public ClassWriter getClassWriter() {
        return Dom4jWriter.getContextWriter();
    }

    /**
     * @inheritDoc
     */
    public StringBuilder getClassName(Class clazz) {
        return new StringBuilder(clazz.getSimpleName());
    }
    
    /**
     * @inheritDoc
     */
    protected String getRelativeFilePath(Class clazz) {
        
        final StringBuilder buffer = new StringBuilder();
        buffer.
                append(getGenerator().getPackageName(clazz).replace('.', '/')).
                append('/');
        
        buffer.append(clazz.getSimpleName());
        buffer.append(getBuilderSuffix());
        appendExtension(buffer);
        
        return buffer.toString();
    }
    
    
    /**
     * @inheritDoc
     */
    public void writeClassName(Writer w, Class clazz,
            String classPrefix, String classSuffix) throws IOException {        
        w.write(clazz.getSimpleName());
    }
}
