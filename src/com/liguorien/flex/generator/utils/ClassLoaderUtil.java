
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

package com.liguorien.flex.generator.utils;

import com.liguorien.flex.generator.FlexGenerator;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <p>Provides some utility methods to find resources
 * through several classloaders.</p>
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public final class ClassLoaderUtil {
    
    /**
     * <p>Finds a resource through several classloaders. The search sequence is
     * the following :</p>
     * <ul>
     *  <li>The current thread context ClassLoader</li>
     *  <li>The System ClassLoader</li>
     *  <li>The ClassLoader who loaded FlexModelGenerator</li>
     * </ul>
     * @return resource's URL or null if the resource cannot be found
     */
    public static URL findResource(String name){
        
        
        URL url = null;
        
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        
        if(loader != null){
            url = loader.getResource(name);
        }
        
        if(url == null){
            
            loader = ClassLoader.getSystemClassLoader();
            url = loader.getResource(name);
            
            if(url == null){
                loader = ClassLoaderUtil.class.getClassLoader();
                url = loader.getResource(name);
            }
        }
        
        return url;
    }
    
    
    /**
     * <p>Finds a resource through several classloaders. The search sequence is
     * the following :</p>
     * <ul>
     *  <li>The current thread context ClassLoader</li>
     *  <li>The System ClassLoader</li>
     *  <li>The ClassLoader who loaded FlexModelGenerator</li>
     * </ul>
     * @return a Class instance or null if the class cannot be found
     */
    public static Class<?> loadClass(String name){
        
        try {
            return Thread.currentThread().
                    getContextClassLoader().loadClass(name);
        } catch (ClassNotFoundException ex) {
        } catch (NullPointerException ex) {}
        
        try {
            return ClassLoader.getSystemClassLoader().loadClass(name);
        } catch (ClassNotFoundException ex) {}
        
        try {
            return ClassLoaderUtil.class.getClassLoader().loadClass(name);
        } catch (ClassNotFoundException ex) {}
        
        return null;
    }
    
    
    /**
     * <p>Finds a ResourceBundle through several classloaders. 
     * The search sequence is the following :</p>
     * <ul>
     *  <li>The current thread context ClassLoader</li>
     *  <li>The System ClassLoader</li>
     *  <li>The ClassLoader who loaded FlexModelGenerator</li>
     * </ul>
     * @return a ResourceBundle instance or null if the bundle cannot be found
     */
    public static ResourceBundle getBundle(String name){
        
        final Locale locale = getLocale();
        
        try {
            return ResourceBundle.getBundle(name, locale,
                    Thread.currentThread().getContextClassLoader());
        } catch (MissingResourceException ex) {}
        
        try {
            return ResourceBundle.getBundle(name, locale,
                    ClassLoader.getSystemClassLoader());
        } catch (MissingResourceException ex) {}
        
        try {
            return ResourceBundle.getBundle(name, locale,
                    ClassLoaderUtil.class.getClassLoader());
        } catch (MissingResourceException ex) {}
        
        return null;
    }
    
    
    /**
     * <p>Searchs for a String resource into a ResourceBundle 
     * through several classloaders. 
     * The search sequence is the following :</p>
     * <ul>
     *  <li>The current thread context ClassLoader</li>
     *  <li>The System ClassLoader</li>
     *  <li>The ClassLoader who loaded FlexModelGenerator</li>
     * </ul>
     * @return a String or null if the resource cannot be found
     */
    public static String getString(String bundleName, String key){
        
        final ResourceBundle bundle = getBundle(bundleName);
        
        if(bundle == null){
            return null;
        }
        
        if(containsKey(bundle, key)){
            return bundle.getString(key);
        }
        
        return null;
    }
    
    
    /**
     * Determines if a ResourceBundle contains a given key.
     * @param b The ResourceBundle
     * @param key The key to check
     * @return A boolean value which indicates if the bundle contains the key.
     */
    public static boolean containsKey(ResourceBundle b, String key) {
	final Enumeration<String> e = b.getKeys();
        while(e.hasMoreElements()){
            if(e.nextElement().equals(key)){
                return true;
            }
        }
	return false;
    }
    
    
    /**
     * <p>Get the Locale of the current context FlexGenerator instance.</p>
     * <p>To Locale of the generator can be changed with  
     * {@link FlexGenerator#setLocale}.  The default value is 
     * {@link Locale#getDefault()}</p>
     * @return A local instance. 
     */
    public static Locale getLocale(){
        return FlexGenerator.getContext().getGenerator().getLocale();
    }
}
