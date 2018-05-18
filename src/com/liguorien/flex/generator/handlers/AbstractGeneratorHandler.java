
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
import com.liguorien.flex.generator.writers.MethodWriter;
import com.liguorien.flex.generator.FlexNode;
import com.liguorien.flex.generator.utils.OutputMode;
import com.liguorien.flex.generator.writers.PropertyWriter;
import com.liguorien.flex.generator.writers.ClassWriter;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>Basic implementation of {@link FlexGeneratorHandler}.  The basic use of 
 * this class is to extends it to create a new type of generated code.</p>
 * <p>The behavior of this handler can easily be customized. It provides a List 
 * of {@link MethodWriter}, whichs contains a writer for each generated methods.

 * @version 0.2
 * @author Nicolas Désy
 */
public abstract class AbstractGeneratorHandler<T extends FlexGeneratorHandler>
        implements FlexGeneratorHandler {
    
    /**
     * contains the default property writers for this handler
     */
    private final List<PropertyWriter<T>> _propertyWriters =
            new ArrayList<PropertyWriter<T>>();
    
    /**
     * contains the method writers for this handler
     */
    private final List<MethodWriter<T>> _methodWriters =
            new ArrayList<MethodWriter<T>>();
    
    /**
     * contains the current context method writers for this handler
     */
    private Set<MethodWriter<T>> _contextMethodWriters =
            new HashSet<MethodWriter<T>>();
    
    /**
     * contains old context method writers
     */
    private Set<MethodWriter<T>> _oldContextMethodWriters =
            new HashSet<MethodWriter<T>>();
    
    /**
     * contains the currents import statements
     */
    private final Set<String> _imports = new TreeSet<String>();
    
    /**
     * contains the current attributes
     */
    private List<PropertyDescriptor> _attributes;
    
    /**
     * contains the current elements
     */
    private List<PropertyDescriptor> _elements;
    
    private String _className;
    private String _subClass;
    private Set<String> _interfaces;
    private Class<?> _clazz;
    private String _distPackage = null;
    
    /**
     * Creates a new instance of AbstractGeneratorHandler
     */
    public AbstractGeneratorHandler() {
    }
    
    /**
     * Add a import statement for the current class.
     * @param str The fully qualified name of the class
     */
    public void addImport(String str){
        _imports.add(str);
    }
    
    /**
     * <p>Returns a Set which contains the import statement for the current 
     * class</p>
     * @return a Set which contains the import statement for the current 
     * class
     */
    public Set<String> getImports(){
        return _imports;
    }
    
    /**
     * <p>Clear the import statements</p>
     */
    public void clearImports(){
        _imports.clear();
    }
   
    /**
     * <p>Clear and reset the import statements.  Subclass can override to
     * add default import. It will called between each class.</p>
     */
    public void resetImports(){
        clearImports();
    }
    
    /**
     * <p>Get the {@link ClassWriter} of this handler.</p>
     * @return The {@link ClassWriter} of this handler.
     * @see ClassWriter
     */
    public abstract ClassWriter getClassWriter();
    
    /**
     * <p>Get the {@link ClassType} for this handler.</p>
     * @return The {@link ClassType} of this handler.
     * @see ClassType
     */
    public abstract ClassType getClassType();
    
    /**
     * <p>Basic implementation of 
     * {@link FlexGeneratorHandler#handleClassBegin} but with a different 
     * signature.  Should be called by subclass in their own implementation of
     * {@link FlexGeneratorHandler#handleClassBegin}.</p>
     * @param 
     *     clazz The current class 
     * @param 
     *     cw The current {@link ClassWriter}
     * @param 
     *     className The name of the generated class
     * @param 
     *     subClass The subclass of the generated (can be null)
     * @param 
     *     interfaces A Set of interface which are implemented  
     *     by the generated class
     * @see FlexGeneratorHandler#handleClassBegin
     */
    protected void handleClassBegin(
            Class<?> clazz, ClassWriter cw, String className, String subClass,
            Set<String> interfaces){
        
        _clazz = clazz;
        initWriter(clazz);
        _attributes = new ArrayList<PropertyDescriptor>();
        _elements = new ArrayList<PropertyDescriptor>();
        _className = className;
        _subClass = subClass;
        _interfaces = interfaces;
        _contextMethodWriters.clear();
        _oldContextMethodWriters.clear();
    }
    
    /**
     * <p>Basic implementation of 
     * {@link FlexGeneratorHandler#handleProperty}</p>
     * <p>This implementation check if the property will an attribute or an 
     * element in the generated XML.  The property descripator can later be 
     * accessed by {@link #getAttributes} and {@link #getElements}.</p>
     * @param property The property descriptor to handle
     */
    public void handleProperty(PropertyDescriptor property) {
        final Method getter = property.getReadMethod();
        if(getter.isAnnotationPresent(FlexNode.class) &&
                getter.getAnnotation(FlexNode.class).value().indexOf("@") == 0){
            
            _attributes.add(property);
        }else{
            _elements.add(property);
        }
    }    
    
    /**
     * <p>Write the document for the current generated class.</p>
     * <p>The default behavior is to invoke 
     * {@link FlexGenerator#writeDocumentation}.</p>
     * <p>Can be overrided to change the behavior.</p>
     * @param w The writer used to write the documentation.
     */
    protected void writeDocumentation(Writer w){
        getGenerator().writeDocumentation(w, getClass(),
                getClassWriter().getClassIndentationLevel(), _className,
                getClassName(getCurrentClass()).toString(), new Date());
    }
    
    /**
     * <p>Basic implementation of 
     * {@link FlexGeneratorHandler#handleClassEnd}</p>     
     * <p>The default behavior is to invoke 
     * {@link #handleClassEnd(Class clazz, ClassWriter cw)} using 
     * {@link #getClassWriter} as the ClassWriter.</p>
     * @param clazz The current class
     */
    public void handleClassEnd(Class<?> clazz) {
        handleClassEnd(clazz, getClassWriter());
    }
    
    /**
     * <p>This is where the class is actually generated.</p>   
     * @param clazz The current class
     * @param cw The {@link ClassWriter} used to generated the class.
     */
    protected void handleClassEnd(Class<?> clazz, ClassWriter cw) {
        
        final Writer w = getWriter();
        final FlexGenerator g = getGenerator();
        
        try {
            
            /**
             * write methods into a temporary buffer before writing import
             * declarations
             */
            final StringWriter sw = new StringWriter();
                  
            g.writeMethods(sw, this, getMethodWriters(), clazz);
            
            while(_contextMethodWriters.size() > 0){
                
                final Set<MethodWriter<T>> cmw = _contextMethodWriters;
                _contextMethodWriters = new HashSet<MethodWriter<T>>();
                
                g.writeMethods(sw, this, cmw, clazz);
                
                _oldContextMethodWriters.addAll(cmw);
                
                final Iterator<MethodWriter<T>> it = 
                        _contextMethodWriters.iterator();
                
                while(it.hasNext()){                
                    if(_oldContextMethodWriters.contains(it.next())){
                        it.remove();
                    }
                }
            }
            
            
            
            // write package declaration
            cw.writePackageDeclaration(w, g, clazz, getImports());
            w.flush();
            
            writeDocumentation(w);
            
            // write class declaration
            cw.writeClassDeclaration(w, g, _className, _subClass, _interfaces);
            w.flush();
            
            // transfer the temporary method buffer into the main writer
            w.write(sw.toString());          
            w.flush();
            
            // write class end
            cw.writeClassEnd(w, g);
            w.flush();
            
            
            w.close();
            
            if(g.getOutputMode() == OutputMode.CONSOLE){
                System.out.println(w.toString());
            }
            
            resetImports();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * <p>Add a {@link MethodWriter} to this handler.</p>
     * @param writer A {@link MethodWriter} instance.
     */
    public void addMethodWriter(MethodWriter<T> writer){
        _methodWriters.add(writer);
        Collections.sort(_methodWriters,
                MethodWriter.COMPARATOR);
    }
    
    /**
     * <p>Add a new {@link MethodWriter} for the current class only.</p>
     * @param writer A {@link MethodWriter} instance.
     */
    public void addContextMethodWriter(MethodWriter<T> writer){
        _contextMethodWriters.add(writer);      
    }
    
    /**
     * <p>Add a {@link PropertyWriter} to this handler.</p>
     * @param writer A {@link PropertyWriter} instance.
     */
    public void addPropertyWriter(PropertyWriter<T> writer){
        _propertyWriters.add(writer);
        Collections.sort(_propertyWriters,
                PropertyWriter.COMPARATOR);
    }
    
    /**
     * <p>Returns the method writers of this handler.</p>
     * @return A List of {@link PropertyWriter} instances.
     */
    public List<MethodWriter<T>> getMethodWriters(){
        return _methodWriters;
    }
    
    /**
     * <p>Returns the method writers which are been added for the current class
     * </p>
     * @return A Set of {@link MethodWriter} instances.
     */
    public Set<MethodWriter<T>> getContextMethodWriters(){
        return _contextMethodWriters;
    }
    
    /**
     * <p>Returns the default property writers of this handler.</p>
     * @return A List of {@link PropertyWriter} instances.
     */
    public List<PropertyWriter<T>> getPropertyWriters(){
        return _propertyWriters;
    }
    
    
    /**
     * <p>Returns a List of PropertyDescriptor which will be XML attributes.</p>
     * @return A List of PropertyDescriptor.
     */
    public List<PropertyDescriptor> getAttributes(){
        return _attributes;
    }
    
    /**
     * <p>Returns a List of PropertyDescriptor which will be XML elements.</p>
     * @return A List of PropertyDescriptor.
     */
    public List<PropertyDescriptor> getElements(){
        return _elements;
    }
    
    /**
     * <p>Return the output directory of this handler</p>
     * @return A File object which represent the root of output directory
     */
    public abstract File getOutputDirectory();
    
    /**
     * <p>Appends a prefix to name of the generated class. The default behavior
     * is to append (if not null) value of {@link FlexGenerator#getClassPrefix}.
     * </p>
     * @param buffer The buffer which the prefix must be appended.
     */
    protected void appendClassPrefix(StringBuilder buffer) {
        if(getGenerator().getClassPrefix() != null){
            buffer.append(getGenerator().getClassPrefix());
        }
    }
    
    /**
     * <p>Appends a suffix to name of the generated class. The default behavior
     * is to append (if not null) value of {@link FlexGenerator#getClassSuffix}.
     * </p>
     * @param buffer The buffer which the suffix must be appended.
     */
    protected void appendClassSuffix(StringBuilder buffer) {
        if(getGenerator().getClassSuffix() != null){
            buffer.append(getGenerator().getClassSuffix());
        }
    }
    
    /**
     * <p>Appends the extension of the generated file.</p>
     * @param buffer The buffer which the extension must be appended.
     */
    protected abstract void appendExtension(StringBuilder buffer);
    
    
    /**
     * <p>Return the current Entity class.</p>
     * @return The current entity class.
     */
    public Class<?> getCurrentClass(){
        return _clazz;
    }
    
    
    /**
     * <p>Writes the name of the generated class. It's used by the 
     * {@link MethodWriter} who need a reference to the class.</p>
     * @param w The writer used to write the classname.
     * @param clazz An entity class
     * @param classPrefix The prefix of the classname (can be null)
     * @param classSuffix The suffix of the classname (can be null)
     * @throws java.io.IOException If an IO error occurs.
     */
    public void writeClassName(Writer w, Class clazz,
            String classPrefix, String classSuffix) throws IOException {
        
        if(classPrefix != null){
            w.write(classPrefix);
        }
        
        w.write(clazz.getSimpleName());
        
        if(classSuffix != null){
            w.write(classSuffix);
        }
    }
    
    /**
     * <p>Get the name of the generated class. It's used by the 
     * {@link MethodWriter} who need a reference to the class.</p>    
     * @param clazz An entity class
     * @return A StringBuilder instance.    
     */
    public StringBuilder getClassName(Class clazz) {
        
        final StringBuilder buffer = new StringBuilder();
        final FlexGenerator g = getGenerator();
        
        if(g.getClassPrefix() != null){
            buffer.append(g.getClassPrefix());
        }
        
        buffer.append(clazz.getSimpleName());
        
        if(g.getClassSuffix() != null){
            buffer.append(g.getClassSuffix());
        }
        
        return buffer;
    }
    
    /**
     * <p>Returns a file path relative to the output directory.</p>
     * @param clazz An entity class
     * @return A filepath relative to the output directory.
     */
    protected String getRelativeFilePath(Class clazz) {
        
        final StringBuilder buffer = new StringBuilder();
        buffer.
                append(getGenerator().getPackageName(clazz).replace('.', '/')).
                append('/');
   
        appendClassPrefix(buffer);
        buffer.append(clazz.getSimpleName());
        appendClassSuffix(buffer);
        appendExtension(buffer);
        
        return buffer.toString();
    }
    
    /**
     * <p>Init writer used to output the generated classes.</p>
     * @param clazz The current entity class.
     */
    protected void initWriter(Class<?> clazz){
        
        switch(getGenerator().getOutputMode()){
            
            case CONSOLE :
                setWriter(new StringWriter());
                break;
                
            case FILE :
                
                final String relativePath = getRelativeFilePath(clazz);
                
                try {
                    
                    final File file =
                            new File(getOutputDirectory(), relativePath);
                    
                    if(!file.getParentFile().exists()){
                        file.getParentFile().mkdirs();
                    }
                    
                    setWriter(new FileWriter(file));
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
        }
    }
    
    
    
    
    
    /**
     * Holds value of property _writer.
     */
    private Writer _writer;
    
    /**
     * Getter for property writer.
     * @return Value of property writer.
     */
    public Writer getWriter() {
        return _writer;
    }
    
    /**
     * Setter for property writer.
     * @param writer New value of property writer.
     */
    public void setWriter(Writer writer) {
        _writer = writer;
    }
    
    /**
     * Holds value of property _generator.
     */
    private FlexGenerator _generator;
    
    /**
     * Getter for property generator.
     * @return Value of property generator.
     */
    public FlexGenerator getGenerator() {
        return _generator;
    }
    
    /**
     * Setter for property generator.
     * @param generator New value of property generator.
     */
    public void setGenerator(FlexGenerator generator) {
        _generator = generator;
    }
}
