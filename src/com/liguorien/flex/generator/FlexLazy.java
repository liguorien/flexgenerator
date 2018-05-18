
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Used to define a property as lazy. There is 4 possible lazy modes :</p>
 * <ul>
 *   <li>{@link #serverRead}</li>
 *   <li>{@link #serverWrite}</li>
 *   <li>{@link #clientRead}</li>
 *   <li>{@link #clientWrite}</li>
 * </ul>
 * <p>The default value for each mode is <em>true</em>. If this annotation is 
 * not declared on a property, then the value will be <em>false</em> for 
 * each modes.</p>
 * <p>In the following exemple, the "contacts" property will be sent to 
 * the client, the client will read it, but the client won't return it 
 * to the server.</p>
 * <pre>
 *    public class Person  {
 *
 *        private int _id;
 *        private String _name;
 *        private Person[] _contacts;
 *
 *         
 *        &#064;FlexLazy(serverWrite=false, clientRead=false)
 *        public Person[] getContacts(){
 *           return _contacts;
 *        }
 *        public void setContacts(Person[] contacts){
 *           _contact = contacts;
 *        }
 *
 *        public int getId() {
 *            return _id;
 *        }
 *        public void setId(int id) {
 *            _id = id;
 *        }
 *
 *        public String getName() {
 *            return _name;
 *        }
 *        public void setName(String name) {
 *            _name = name;
 *        }
 *    }
 * </pre>
 *
 * @see FlexGenerator#isLazy
 * @version 0.2
 * @author Nicolas Désy
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface FlexLazy {
    
    /**
     * <p>Indicates when the server reads data from the client. </p>
     * <p>If the value is set to <em>true</em>, then the server 
     * won't try to read this property from the client.</p>
     */
    boolean serverRead() default true;
    
    /**
     * <p>Indicates when the server writes data to the client</p>
     * <p>If the value is set to <em>true</em>, then the server 
     * won't try to write this property to the client.</p>
     */
    boolean serverWrite() default true;    
    
    /**
     * <p>Indicates when the client reads data from the server</p>
     * <p>If the value is set to <em>true</em>, then the client 
     * won't try to read this property from the server.</p>
     */
    boolean clientRead() default true;
    
    /**
     * <p>Indicates when the client writes data to the server</p>
     * <p>If the value is set to <em>true</em>, then the client 
     * won't try to write this property to the server.</p>
     */
    boolean clientWrite() default true;
}
