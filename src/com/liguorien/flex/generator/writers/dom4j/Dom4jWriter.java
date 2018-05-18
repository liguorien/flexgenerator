
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

package com.liguorien.flex.generator.writers.dom4j;

import com.liguorien.flex.generator.FlexGenerator;
import com.liguorien.flex.generator.FlexNode;
import com.liguorien.flex.generator.utils.NodeType;
import com.liguorien.flex.generator.writers.ClassWriter;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class Dom4jWriter implements ClassWriter {
    
    public final static ClassWriter WRITER = new Dom4jWriter();
    
    private final static ThreadLocal<ClassWriter> _contextWriter =
            new ThreadLocal<ClassWriter>();
    
    /**
     *  
     */
    public static ClassWriter getContextWriter(){
        final ClassWriter cw = _contextWriter.get();
        return (cw == null) ? WRITER : cw;
    }
    
    /**
     * 
     * @param w 
     */
    public static void setContextWriter(ClassWriter w){
        if(w == null){
            _contextWriter.remove();
        }else{
            _contextWriter.set(w);
        }
    }
    
    
    /**
     * 
     * @param w 
     * @param g 
     * @param varName 
     * @param prop 
     * @param prefix 
     * @param suffix 
     * @param type 
     * @throws java.io.IOException 
     */
    public void writeProperty(Writer w, FlexGenerator g, String varName,
            PropertyDescriptor prop, String prefix, String suffix,
            NodeType type)
            throws IOException {
        
        final Method setter = prop.getWriteMethod();
        if(setter == null){
            return;
        }
        
        final Method getter = prop.getReadMethod();
        
        if(type == null){
            type = NodeType.TEXT;
        }
        
        String nodeName = prop.getName();
        
        if(getter.isAnnotationPresent(FlexNode.class)){
            nodeName = getter.getAnnotation(FlexNode.class).value();
            if(nodeName.indexOf("@") == 0){
                type = NodeType.ATTRIBUTE;
                nodeName = nodeName.substring(1);
            }
        }
        
        
        w.write("\n");
        g.writeIndentation(w, 2);
        w.write(varName);
        w.write(".");
        w.write(setter.getName());
        w.write("(");
        
        if(prefix != null){
            w.write(prefix);
        }
        
        switch(type){
            case ATTRIBUTE :
                w.write("el.attributeValue(\"");
                break;
                
            case ELEMENT :
                w.write("el.element(\"");
                break;
                
            case TEXT :
                w.write("el.elementTextTrim(\"");
                break;
        }
        
        w.write(nodeName);
        w.write("\")");
        
        if(suffix != null){
            w.write(suffix);
        }
        
        w.write(");");
    }
    
    
    /**
     *
     * @param w
     * @param gen
     * @param clazz
     * @param imports
     * @throws java.io.IOException
     */
    public void writePackageDeclaration(
            Writer w, FlexGenerator gen,
            Class<?> clazz, Set<String> imports) throws IOException {
        
        w.write("package ");
        w.write(gen.getPackageName(clazz));
        w.write(";");
        
        if(imports != null){
            w.write("\n");
            for(String imp : imports){
                w.write("\nimport ");
                w.write(imp);
                w.write(";");
            }
        }
        
        w.write("\n\n");
    }
    
    
    /**
     *
     * @param w
     * @param gen
     * @param className
     * @param subClass
     * @param interfaces
     * @throws java.io.IOException
     */
    public void writeClassDeclaration(
            Writer w, FlexGenerator gen, String className,
            String subClass, Set<String> interfaces) throws IOException {
        
       
        w.write("public class ");
        w.write(className);
        
        if(subClass != null){
            w.write(" extends ");
            w.write(subClass);
        }
        
        if(interfaces != null && interfaces.size() > 0){
            
            w.write(" implements");
            boolean first = true;
            
            for(String it : interfaces){
                if(first){
                    first = false;
                    w.write(' ');
                }else{
                    w.write(", ");
                }
                w.write(it);
            }
        }
        
        gen.writeCurlyBrace(w, 0);
        w.write('\n');
    }
    
    /**
     * 
     * @param w 
     * @param gen 
     * @throws java.io.IOException 
     */
    public void writeClassEnd(Writer w, FlexGenerator gen) throws IOException {
        w.write("\n}\n");
    }
    
    /**
     * Primitive/wrapper class mapping
     */
    private final static Map<String, String> _classMapping =
            new HashMap<String, String>();
    
    static {
        _classMapping.put("boolean", "Boolean");
        _classMapping.put("short", "Short");
        _classMapping.put("int", "Integer");
        _classMapping.put("float", "Float");
        _classMapping.put("double", "Double");
        _classMapping.put("long", "Long");
    }
    
    /**
     * Return the wrapper class of a primitive.
     * @param simpleName 
     * @return The wrapper class name
     */
    public static String getWrapperName(String simpleName){
        final String result = _classMapping.get(simpleName);
        if(result == null){
            return simpleName;
        }
        return result;
    }

    /**
     * @inheritDoc
     */
    public int getClassIndentationLevel() {
        return 0;
    }
    
    
}
