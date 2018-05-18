
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

import com.liguorien.flex.generator.utils.TransientMode;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Used to indicates to the generator that a class or a property
 * should not be convert to actionscript.</p>
 * <p>It can be used on a class declaration or on a getter method.</p>
 *
 * <p><b>Exemple :</b></p>
 * <pre>
 *    // tell to the generator to skip this class
 *    &#064;FlexTransient
 *    public class SomeUtilityClass  {
 *
 *    }
 *
 *
 *    public class MyEntity {
 *      
 *        private String _name;
 *
 *        public String getName(){
 *            return _name;
 *        }
 *        public void setName(String name){
 *            _name = name;
 *        }
 *
 * 
 *
 *        private int _serverSideOnlyProperty;
 *
 *
 *   // tell to the generator to skip this property
 *        &#064;FlexTransient
 *        public int getServerSideOnlyProperty(){
 *             return _serverSideOnlyProperty;
 *        }
 *        public void setServerSideOnlyProperty(int value){
 *             _serverSideOnlyProperty = value;
 *        }
 *    }
 * </pre>
 *
 * @version 0.2
 * @author Nicolas Désy
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})

public @interface FlexTransient {    
    
    public TransientMode value() default TransientMode.PUBLIC;
}

