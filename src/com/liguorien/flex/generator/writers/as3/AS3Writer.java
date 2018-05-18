
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

import com.liguorien.flex.generator.FlexGenerator;
import com.liguorien.flex.generator.FlexNode;
import com.liguorien.flex.generator.utils.NodeType;
import com.liguorien.flex.generator.writers.*;
import com.liguorien.flex.generator.writers.as3.methods.GetEntityArray;
import com.liguorien.flex.generator.writers.as3.methods.GetInstance;
import com.liguorien.flex.generator.writers.as3.methods.GetEntityXMLList;
import com.liguorien.flex.generator.writers.as3.methods.GetXML;
import com.liguorien.flex.generator.writers.as3.methods.GetterSetter;
import com.liguorien.flex.generator.writers.as3.properties.BasicArrayProperty;
import com.liguorien.flex.generator.writers.as3.properties.E4XProperty;
import com.liguorien.flex.generator.writers.as3.properties.EntityArrayProperty;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class AS3Writer implements ClassWriter {
    
    public final static AS3Writer WRITER = new AS3Writer();  
    
    private final static ThreadLocal<ClassWriter> _contextWriter =
            new ThreadLocal<ClassWriter>();
    
    public static ClassWriter getContextWriter(){
        final ClassWriter cw = _contextWriter.get();
        if(cw != null){
            return cw;
        }
        return WRITER;
    }
    
    public static void setContextWriter(ClassWriter w){
        if(w == null){
            _contextWriter.remove();
        }else{
            _contextWriter.set(w);
        }
    }
    
    public int getClassIndentationLevel() {
        return 1;
    }
    
    /**
     * Returns the AS3 classname of a Java type.
     * @param type A Java class
     * @return The classname of the AS3 equivalent type
     */
    public static String getTypeName(Class type){
        
        if(type.isArray()){
            return "Array";
        }
        
        if (Map.class.isAssignableFrom(type)){
            return "Dictionary";
        }
        
        if (Collection.class.isAssignableFrom(type)){
            return "Array";
        }
        
        String typeName = _classMapping.get(type.getSimpleName());
        if(typeName == null){
            typeName = type.getSimpleName();
        }
        return typeName;
    }

    
    
    
    
    /**
     * @inheritDoc
     */
    public void writeProperty(Writer w, FlexGenerator g, String varName,
            PropertyDescriptor prop, String e4xPrefix, String e4xSuffix,
            NodeType type) throws IOException {
        
        w.write("\n");
        g.writeIndentation(w, 3);
        w.write(varName);
        w.write(".");
        w.write(prop.getName());
        w.write(" = ");
        
        if(e4xPrefix != null){
            w.write(e4xPrefix);
        }
        
        w.write("nod.");
        
        final Method getter = prop.getReadMethod();
        if(getter.isAnnotationPresent(FlexNode.class)){
            w.write(getter.getAnnotation(FlexNode.class).value());
        }else{
            w.write(prop.getName());
        }
        
        if(e4xSuffix != null){
            w.write(e4xSuffix);
        }
        
        w.write(";");
    }
    
    
    /**
     * @inheritDoc
     */
    public void writePackageDeclaration(
            Writer w, FlexGenerator g, Class<?> clazz, Set<String> imports)
            throws IOException {
        
        w.write("package ");
        w.write(g.getPackageName(clazz));
        g.writeCurlyBrace(w, 0);
        
        if(imports != null){
            w.write("\n");
            for(String imp : imports){
                g.writeIndentation(w, 1);
                w.write("import ");
                w.write(imp);
                w.write(";\n");
            }
        }
        
        w.write('\n');
    }
    
    
    /**
     * @inheritDoc
     */
    public void writeClassDeclaration(
            Writer w, FlexGenerator g, String className,
            String subClass, Set<String> interfaces) throws IOException {
        
        g.writeIndentation(w, 1);
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
        
        g.writeCurlyBrace(w, 1);
    }
    
    /**
     * @inheritDoc
     */
    public void writeClassEnd(Writer w, FlexGenerator g) throws IOException {
        w.write('\n');
        g.writeIndentation(w, 1);
        w.write("}\n}\n");
    }
    
    
    
    /**
     * Primitive/wrapper class mapping
     */
    private final static Map<String, String> _classMapping =
            new HashMap<String, String>();
    
    static {
        _classMapping.put("boolean", "Boolean");
        _classMapping.put("short", "int");
        _classMapping.put("int", "int");
        _classMapping.put("float", "Number");
        _classMapping.put("double", "Number");
        _classMapping.put("long", "Number");
        _classMapping.put("Boolean", "Boolean");
        _classMapping.put("Short", "int");
        _classMapping.put("Integer", "int");
        _classMapping.put("Float", "Number");
        _classMapping.put("Double", "Number");
        _classMapping.put("Long", "Number");
    }
    
}
