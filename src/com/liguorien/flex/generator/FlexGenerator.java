
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

package com.liguorien.flex.generator;

import com.liguorien.flex.generator.handlers.FlexGeneratorHandler;
import com.liguorien.flex.generator.utils.ClassLoaderUtil;
import com.liguorien.flex.generator.utils.GeneratorContext;
import com.liguorien.flex.generator.utils.OutputMode;
import com.liguorien.flex.generator.utils.LazyMode;
import com.liguorien.flex.generator.utils.ClassType;
import com.liguorien.flex.generator.utils.PropertyDescriptorProxy;
import com.liguorien.flex.generator.utils.TransientMode;
import com.liguorien.flex.generator.writers.MethodWriter;
import com.liguorien.flex.generator.writers.PropertyWriter;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * <p>Main class of FlexGenerator.</p>
 * <p>The purpose of this class is to generate classes based on the properties
 * defined on a JavaBean.</p>
 * <p>The generator can easily be customized by creating a 
 * {@link FlexGeneratorHandler} for each new type of generated classes.</p>
 * <p>Here is a little exemple on how to use this class :</p>
 * <pre>
 * FlexGenerator generator = new FlexGenerator();
 * 
 * // add an handler to generate code which will 
 * // read and create dom4j element from Java entities
 * g.addHandler(Dom4jBuilderHandler.getDefault());
 * 
 * // add an handler to generate code which will be
 * // the ActionScript version of the JavaEntity
 * g.addHandler(FlexModelHandler.getDefault());
 * 
 * // add an handler to generated code which will 
 * // read and create XML object from an ActionScript entities
 * g.addHandler(FlexModelBuilderHandler.getDefault());
 * 
 * // set formatting options
 * generator.setNewLineBeforeCurlyBrace(false);
 * generator.setUsingUnderscore(true);
 * 
 * // indicate to the generator where generate the actionscript and java files
 * generator.setOutputMode(OutputMode.FILE);
 * generator.setFlexOutputDirectory("./src/flex/");
 * generator.setJavaOutputDirectory("./src/java/");
 * 
 * 
 * // generate a single class
 * generator.generate(MyEntity.class);
 * 
 * // generate an entire package
 * generator.generate("com.liguorien.myproject.entities");
 * </pre>
 * 
 * 
 * @author Nicolas Désy
 * @version 0.2
 * @see FlexBindable
 * @see FlexDefault
 * @see FlexTransient
 * @see FlexLazy
 * @see FlOutputModelexMap
 * @see FlexNode
 * @see FlexSet
 * @see FlexTransient
 * @see com.liguorien.flex.generator.utils.OutputMode
 * @see com.liguorien.flex.generator.handlers.FlexGeneratorHandler
 * @see com.liguorien.flex.generator.handlers.AbstractGeneratorHandler
 * @see com.liguorien.flex.generator.handlers.Dom4jBuilderHandler
 * @see com.liguorien.flex.generator.handlers.FlexModelHandler
 * @see com.liguorien.flex.generator.handlers.FlexModelBuilderHandler
 */
public class FlexGenerator {
    
    private File _outputDirectory = null;
    
    private OutputMode _outputMode = OutputMode.CONSOLE;
    
    private final Map<ClassType, String> _packages =
            new HashMap<ClassType, String>();
    
    private boolean _usingUnderscore = true;
    
    private boolean _newLineBeforeCurlyBrace = false;
    
    private String _classSuffix;
    
    private String _classPrefix;
    
    private String _indentationString = "    ";
    
    private final List<FlexGeneratorHandler> _handlers =
            new ArrayList<FlexGeneratorHandler>();
    
    private final Set<Class<?>> _classes = new HashSet<Class<?>>();
    
    /** Creates a new instance of FlexGenerator */
    public FlexGenerator(){
        setPackage(ClassType.CLIENT_ENTITIES, ".");
        setPackage(ClassType.CLIENT_BUILDERS, ".");
        setPackage(ClassType.SERVER_BUILDERS, ".");
    }
    
    /**
     * <p>Set the destination package for a given {@link ClassType}.</p>
     * <p>By default, the path will be the same as the Java entity class. If the 
     * path begins with a slash(/), then the output package will be to this 
     * absolute path.  If the slash is omitted, the package is relative to
     * the Java entity package.</p>
     * <p><b>Exemple :</b></p>
     * <p>Let's supose that we are generating classes for the entity 
     * <code>com.liguorien.myapp.entities.MyEntity</code></p>
     * <table class="infotable" border="1">
     *   <tr>
     *     <td>g.setPackage(ClassType.CLIENT_BUILDERS, <b>"builder"</b>)</td>
     *     <td>com.mycompany.myapp.entities.builder.MyEntityBuilder</td>
     *   </tr>
     *   <tr>
     *     <td>g.setPackage(ClassType.CLIENT_BUILDERS, <b>"../builder"</b>)</td>
     *     <td>com.mycompany.myapp.builder.MyEntityBuilder</td>
     *   </tr>
     *   <tr>
     *     <td>g.setPackage(ClassType.CLIENT_BUILDERS, 
     *      <b>"/org.otherproject.builder"</b>)</td>
     *     <td>org.otherproject.builder.MyEntityBuilder</td>
     *   </tr>
     * </table>
     * @param type A {@link ClassType}
     * @param path The path used to generated the classes.
     */
    public void setPackage(ClassType type, String path){
        _packages.put(type, path);
    }
    
    /**
     * <p>Add an handler to this generator.</p>
     * @param handler A {@link FlexGeneratorHandler} instance.
     */
    public void addHandler(FlexGeneratorHandler handler){
        _handlers.add(handler);
        handler.setGenerator(this);
    }
    
    /**
     * <p>Determine if the class sent is a generated class.</p>
     * @param clazz A class
     * @return A boolean which indicates if the class will be generated in this
     * session.
     */
    public boolean isGeneratedClass(Class<?> clazz){
        return _classes.contains(clazz);
    }
    
    /**
     * <p>Proceed to generation.</p>
     */
    public void generate(){
        
        final GeneratorContext context = new GeneratorContext();
        context.setGenerator(this);
        
        _contextHandlers.set(context);
        
        try {
            for(Class clazz : _classes){
                _generate(clazz, context);
            }
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Add a single class
     * @param clazz A class which represents a Java entity
     */
    public void addClass(Class<?> clazz){
        if(clazz != null && !clazz.isAnnotationPresent(FlexTransient.class)){
            _classes.add(clazz);
        }
    }
    
    /**
     * <p>Add an entire package.
     * Every classes in the package will be processed</p>
     * <p>Classes can be excluded with the {@link FlexTransient} annotation.</p>
     * @param packageName The name of the package
     * (ie. "com.liguorien.myproject.entities")
     * @see FlexTransient
     */
    public void addPackage(String packageName){
        
        final String name = packageName.replace('.', '/');
        final URL url = ClassLoaderUtil.findResource(name);
        
        if(url == null){
            throw new IllegalArgumentException(
                    "The package '" + name + "' cannot be found");
        }
        
        try {
            
            final File packFolder = new File(
                    URLDecoder.decode(url.getFile(), "ISO-8859-1"));
            
            if (packFolder.exists()) {
                
                for(String fileName : packFolder.list()){
                    
                    if (fileName.endsWith(".class")) {
                        addClass(ClassLoaderUtil.loadClass(packageName
                                + '.' +
                                fileName.substring(0, fileName.length() - 6)
                                ));
                    }
                }
            }
            
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }
    
    
    /**
     * <p>Iterates over the propertyDescriptor of the class and delegate the 
     * work to the handlers.</p>
     * @param clazz The class to generate
     * @param context The current context
     * @throws java.beans.IntrospectionException 
     *    If an introspection error occurs
     * @throws java.io.IOException
     *    If an IO error occurs
     */
    private void _generate(Class<?> clazz, GeneratorContext context)
    throws IntrospectionException, IOException {
        
        for(FlexGeneratorHandler handler : _handlers){
            context.setHandler(handler);
            handler.handleClassBegin(clazz);
        }
        
        final BeanInfo info = Introspector.getBeanInfo(clazz);
        for(PropertyDescriptor prop : info.getPropertyDescriptors()){
            
            // be sure to skip the 'class' and FlexTransient property
            if("class".equals(prop.getName()) || prop.getReadMethod().
                    isAnnotationPresent(FlexTransient.class)){
                continue;
            }
            for(FlexGeneratorHandler handler : _handlers){
                context.setHandler(handler);
                handler.handleProperty(prop);
            }
        }
        
        for(FlexGeneratorHandler handler : _handlers){
            context.setHandler(handler);
            handler.handleClassEnd(clazz);
        }
    }
    
    
    
    /**
     * <p>Set the output directory used by the generator. It should be the
     * classpath of your flex projet.</p>
     * @param dir A File object which represents the classpath
     * of your flex project
     */
    public void setFlexOutputDirectory(File dir){
        _outputDirectory = dir;
    }
    
    /**
     * <p>Set the output directory used by the generator. It should be the
     * classpath of your flex projet.</p>
     * @param dir A String which represents the classpath of your flex project
     */
    public void setFlexOutputDirectory(String dir){
        setFlexOutputDirectory(new File(dir));
    }
    
    /**
     * <p>Returns the Flex output directory</p>
     * @return A File object
     */
    public File getFlexOutputDirectory(){
        return _outputDirectory;
    }
    
    
    /**
     * <p>Indicates if an underscore(_) should be added to the private field
     * name.</p>
     * @return Value of property usingUnderscore.
     */
    public boolean isUsingUnderscore() {
        return _usingUnderscore;
    }
    
    /**
     * <p>Indicates if an underscore(_) should be added to the private field
     * name.</p>
     * <p>The default value is true.</p>
     * @param usingUnderscore New value of property usingUnderscore.
     */
    public void setUsingUnderscore(boolean usingUnderscore) {
        _usingUnderscore = usingUnderscore;
    }
    
    
    /**
     * <p>Indicates if a new line should be added between package/class/method
     * declaration and the curly brace.</p>
     * @return Value of property newLineBeforeCurlyBrace.
     */
    public boolean isNewLineBeforeCurlyBrace() {
        return _newLineBeforeCurlyBrace;
    }
    
    /**
     * <p>Indicates if a new line should be added between package/class/method
     * declaration and the curly brace.</p>
     * <p>The default value is false.</p>
     * @param newLineBeforeCurlyBrace
     *     New value of property newLineBeforeCurlyBrace.
     */
    public void setNewLineBeforeCurlyBrace(boolean newLineBeforeCurlyBrace) {
        _newLineBeforeCurlyBrace = newLineBeforeCurlyBrace;
    }
    
    
    /**
     * Return the current output mode.
     * @return Value of property outputMode.
     */
    public OutputMode getOutputMode() {
        return _outputMode;
    }
    
    /**
     * <p>Set the output mode that will be used by the generator.
     * The default value is {@link OutputMode#CONSOLE}.</p>
     * 
     * 
     * @param outputMode The output mode.
     * @see OutputMode
     */
    public void setOutputMode(OutputMode outputMode) {
        _outputMode = outputMode;
    }
    
    /**
     * Get the class prefix
     * @return Value of property classPrefix.
     */
    public String getClassPrefix() {
        return _classPrefix;
    }
    
    /**
     * <p>Assign a prefix which will be added to the AS3 class name.</p>
     * <p>For example, if you have a class "Person" and the prefix
     * is "Model",  the AS3 class will be named "ModelPerson".</p>
     * @param classPrefix The prefix.
     */
    public void setClassPrefix(String classPrefix) {
        _classPrefix = classPrefix;
    }
    
    /**
     * Get the class suffix
     * @return Value of property classSuffix.
     */
    public String getClassSuffix() {
        return _classSuffix;
    }
    
    /**
     * <p>Assign a suffix which will be added to the AS3 class name.</p>
     * <p>For example, if you have a class "Person" and the suffix
     * is "Model",  the AS3 class will be named "PersonModel".</p>
     * @param classSuffix The suffix.
     */
    public void setClassSuffix(String classSuffix) {
        _classSuffix = classSuffix;
    }
    
    
    /**
     * Holds value of property _javaOutputDirectory.
     */
    private File _javaOutputDirectory;
    
    /**
     * Getter for property javaOutputDirectory.
     * @return Value of property javaOutputDirectory.
     */
    public File getJavaOutputDirectory() {
        return _javaOutputDirectory;
    }
    
    /**
     * Setter for property javaOutputDirectory.
     * @param dir New value of property javaOutputDirectory.
     */
    public void setJavaOutputDirectory(File dir) {
        _javaOutputDirectory = dir;
    }
    
    /**
     * Setter for property javaOutputDirectory.
     * @param dir New value of property javaOutputDirectory.
     */
    public void setJavaOutputDirectory(String dir) {
        setJavaOutputDirectory(new File(dir));
    }
    
    
    
    /**
     * <p>Determines if a property is lazy.</p>
     * @param prop The property to check
     * @param mode The LazyMode expected
     * @return a boolean value
     * @see FlexLazy
     * @see LazyMode
     */
    public static boolean isLazy(PropertyDescriptor prop, LazyMode mode){
        
        if(mode == null){
            return false;
        }
        
        final Method getter = prop.getReadMethod();
        if(!getter.isAnnotationPresent(FlexLazy.class)){
            return false;
        }
        
        final FlexLazy lazy = getter.getAnnotation(FlexLazy.class);
        
        switch(mode){
            case SERVER_WRITE :
                return lazy.serverWrite();
                
            case SERVER_READ :
                return lazy.serverRead();
                
            case CLIENT_READ :
                return lazy.clientRead();
                
            case CLIENT_WRITE :
                return lazy.clientWrite();
        }
        
        return false;
    }
    
    
    /**
     * <p>Returns the package name of the generated class.</p>
     * @param clazz The current generated class.
     * @return The package name of the generated class.
     * @throws IllegalArgumentException If the generator is misconfigurated.
     */
    public String getPackageName(Class<?> clazz){
        
        final ClassType type =
                FlexGenerator.getContext().getHandler().getClassType();
        
        String decl = null;
        
        if(clazz.isAnnotationPresent(FlexPackage.class)){
            
            final FlexPackage pack = clazz.getAnnotation(FlexPackage.class);
            
            switch(type){
                case CLIENT_BUILDERS :
                    decl = pack.clientBuilders();
                    break;
                case CLIENT_ENTITIES :
                    decl = pack.clientEntities();
                    break;
                case SERVER_BUILDERS :
                    decl = pack.serverBuilders();
                    break;
            }
        }
        
        if(decl == null || "".equals(decl)){
            decl = _packages.get(type);
            if(decl == null || "".equals(decl)){
                throw new IllegalArgumentException(
                        "@FlexPackage cannot be null or an empty String");
            }
        }
        
        String packName = clazz.getPackage().getName();
        
        if(decl.charAt(0) == '/'){
            return decl.substring(1);
        }
        
        if(decl.charAt(0) == '.' && decl.length() == 1){
            return packName;
        }
        
        while(
                decl.charAt(0) == '.' &&
                decl.charAt(1) == '.' &&
                decl.charAt(2) == '/') {
            
            packName = packName.substring(0, packName.lastIndexOf("."));
            decl = decl.substring(3);
        }
        
        return packName + '.' + decl;
    }
    
    
    
    /**
     * <p>Write a List of properties to a given Writer.</p>
     * @param w
     *     The Writer used to write the properties.
     * @param g 
     *     The current FlexGenerator instance.
     * @param handler
     *     The current FlexGeneratorHandler.
     * @param varName
     *     The name of the instance where the properties are accessed.
     * @param props
     *     A List of PropertyDescriptor
     * @param writers
     *     A List of {@link PropertyWriter} used to write the code to 
     *     access the property.
     * @param mode
     *     The {@link LazyMode} for the context where properties will be 
     *     accessed.
     * @throws java.io.IOException
     *     If an IO error occurs.
     */
    public static <T extends FlexGeneratorHandler> void writeProperties(
            Writer w,
            FlexGenerator g,
            T handler,
            String varName,
            List<PropertyDescriptor> props,
            List<PropertyWriter<T>> writers,
            LazyMode mode)
            throws IOException {
        
        for(PropertyDescriptor prop : props){
            FlexGenerator.writeProperty(
                    w, g, handler, varName, prop, writers, mode);
        }
    }
    
    /**
     * <p>Write a single property to a given Writer.</p>
     * @param w
     *     The Writer used to write the property.
     * @param g 
     *     The current FlexGenerator instance.
     * @param handler
     *     The current FlexGeneratorHandler.
     * @param varName
     *     The name of the instance where the property is accessed.
     * @param prop
     *     A PropertyDescriptor
     * @param writers
     *     A List of {@link PropertyWriter} used to write the code to 
     *     access the property.
     * @param mode
     *     The {@link LazyMode} for the context where the property will be 
     *     accessed.
     * @throws java.io.IOException
     *     If an IO error occurs.
     */
    public static <T extends FlexGeneratorHandler> void writeProperty(
            Writer w,
            FlexGenerator g,
            T handler,
            String varName,
            PropertyDescriptor prop,
            List<PropertyWriter<T>> writers,
            LazyMode mode)
            throws IOException {
        
        if(FlexGenerator.isLazy(prop, mode)){
            return;
        }
        
        final Method getter = prop.getReadMethod();
        if(getter.isAnnotationPresent(FlexTransient.class)){
            
            TransientMode t =
                    getter.getAnnotation(FlexTransient.class).value();
            
            if(t == TransientMode.PUBLIC){
                return;
            }
            
            if(prop.getName().equals(
                    PropertyDescriptorProxy.TRANSIENT_PROPERTY)){
                return;
            }
        }
        
        for(PropertyWriter<T> writer : writers){
            if(writer.acceptProperty(prop, handler)){
                writer.writeProperty(w, g, handler, varName, prop);
                return;
            }
        }
    }
    
    /**
     * <p>Iterates over a List of {@link MethodWriter} and let them write
     * code if they accept to class sent in parameters.</p>
     * @param w The writer used to write the generated code.
     * @param handler The current handler.
     * @param methods A List of {@link MethodWriter}.
     * @param clazz The current class.
     * @throws java.io.IOException If an IO error occurs.
     */
    public <T extends FlexGeneratorHandler> void writeMethods(
            Writer w,
            T handler,
            Collection<MethodWriter<T>> methods,
            Class<?> clazz)
            throws IOException {
        
        for(MethodWriter<T> mw : methods){
            if(mw.acceptClass(clazz)){
                mw.writeMethod(w, this, handler, clazz);
                w.flush();
            }
        }
    }
    
    /**
     * Write indentation to a writer
     * @param w A Writer instance
     * @param indentationCount The indentation count.
     * @throws java.io.IOException If an IO error occurs.
     */
    public void writeIndentation(Writer w, int indentationCount)
    throws IOException {
        for (int i = 0; i < indentationCount; i++) {
            w.write(_indentationString);
        }
    }
    
    /**
     * Get the indentation string for a given indentation count
     * @param count The indentation count.
     * @return An indentation String.
     */
    public String getIndentation(int count){
        final StringBuilder buffer =
                new StringBuilder(_indentationString.length() * count);
        for (int i = 0; i < count; i++) {
            buffer.append(_indentationString);
        }
        return buffer.toString();
    }
    
    /**
     * <p>Write an opening curly brace. It check in config if a new line must be 
     * added before the curly.</p>
     * @param w A Writer
     * @param indentationCount The current indentation level
     * @throws java.io.IOException If an IO error occurs.
     * @see #setNewLineBeforeCurlyBrace
     */
    public void writeCurlyBrace(Writer w, int indentationCount)
    throws IOException {
        if(isNewLineBeforeCurlyBrace()){
            w.write("\n");
            writeIndentation(w, indentationCount);
            w.write("{\n");
        }else{
            w.write(" {\n");
        }
    }
    
    
    /**
     * <p>Return the node name of a property.</p>
     * @param prop A PropertyDescriptor.
     * @return The node name.
     */
    public static String getNodeName(PropertyDescriptor prop){
        final Method getter = prop.getReadMethod();
        if(getter.isAnnotationPresent(FlexNode.class)){
            final String name = getter.getAnnotation(FlexNode.class).value();
            if(name.indexOf("@") == 0){
                return name.substring(1);
            }
            return name;
        }
        return prop.getName();
    }
    
    
    /**
     * <p>Return the node name of an entity class.</p>
     * @param clazz An entity class.
     * @return The node name.
     */
    public static String getNodeName(Class<?> clazz){
        if(clazz.isAnnotationPresent(FlexNode.class)){
            final String name = clazz.getAnnotation(FlexNode.class).value();
            if(name.indexOf("@") == 0){
                throw new IllegalArgumentException("A class cannot be an " +
                        "attribute node. (" + clazz.getName() + ")");
            }
            return name;
        }
        return clazz.getSimpleName();
    }
    
    
    /**
     * Getter for property indentationString.
     * @return Value of property indentationString.
     */
    public String getIndentationString() {
        return _indentationString;
    }
    
    /**
     * Setter for property indentationString.
     * @param indentationString New value of property indentationString.
     */
    public void setIndentationString(String indentationString) {
        _indentationString = indentationString;
    }
    
    
    /**
     * Write a JavaDoc-styled comment.
     * @param w A Writer
     * @param comment The comment to write
     * @param indentation The current indentation level
     */
    public void writeComment(Writer w, String comment, int indentation){
        
        try {
            
            writeIndentation(w, indentation);
            w.write("/**\n");
            
            for(String line : comment.split("\n")){
                writeIndentation(w, indentation);
                w.write(" * ");
                w.write(line);
                w.write("\n");
            }
            
            writeIndentation(w, indentation);
            w.write(" */\n");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    private final static ThreadLocal<GeneratorContext>
            _contextHandlers = new ThreadLocal<GeneratorContext>();
    
    /*
     * Returns the generator context for the current Thread. 
     * @return A {@link GeneratorContext} instance.
     */
    public static GeneratorContext getContext(){
        return _contextHandlers.get();
    }
    
    /**
     * <p>Write internationalized document for a given class.</p>
     * @param w A Writer
     * @param clazz The target class
     * @param indentation The current indentation level
     * @param params String params sent to the MessageFormat
     */
    public void writeDocumentation(
            Writer w, Class<?> clazz, int indentation, Object ... params){
        
        final String doc = ClassLoaderUtil.getString(
                clazz.getPackage().getName() + ".documentation",
                clazz.getSimpleName() + ".doc"
                );
        
        if(doc != null){
            writeComment(w, MessageFormat.format(doc, params), indentation);
        }
    }
    
            
    
    /**
     * Write the name of the method of a given class.
     * @param w A Writer
     * @param clazz The target class
     * @param params The parameters sent to the MessageFormat
     * @throws java.io.IOException 
     */
    public void writeMethodName(
            Writer w, Class<?> clazz, Object ... params) throws IOException {
        
        final String name = ClassLoaderUtil.getString(
                clazz.getPackage().getName() + ".documentation",
                clazz.getSimpleName() + ".name"
                );
        
        if(name != null){
            w.write(MessageFormat.format(name, params));
        }else{
            throw new NullPointerException(
                    "Method '"+ clazz.getSimpleName()
                    + "' missing in ResourceBundle");
        }
    }
    
  
    
    /**
     * <p>Writes the result of {@link #writeMethodName} into a StringWriter
     * and return the result as a String.</p>
     * @param clazz The target class
     * @param params The parameters sent to the MessageFormat
     * @return The method name
     */
    public String getMethodName(Class<?> clazz, Object ... params){
        final StringWriter sw = new StringWriter();
        try {
            writeMethodName(sw, clazz, params);
        } catch (IOException ex) {}
        return sw.toString();
    }
    
    /**
     * Holds value of property _locale.
     */
    private Locale _locale = Locale.getDefault();
    
    /**
     * Getter for property locale.
     * @return Value of property locale.
     */
    public Locale getLocale() {
        return _locale;
    }
    
    /**
     * Setter for property locale.
     * @param locale New value of property locale.
     */
    public void setLocale(Locale locale) {
        _locale = locale;
    }
}
