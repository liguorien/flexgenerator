
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
 * <p>Used to define property's node name in the generated XML.</p>
 * <p>If the declared name begins with @, the property will be an attribute.
 * Otherwise it will be an element.  Note that Entity classes, Array,
 * Collection and Map cannot be used as attribute.</p>
 *
 * <p>This annotation can be used on the class or on the getter method.
 * If the annotation is not present, then the name of the class or the property
 * will be used as the name of the node. It means that each properties will
 * be a nested element by default.</p>
 *
 * <p><b>Exemple :</b></p>
 * <pre>
 *    public class Person  {
 *
 *        private int _id;
 *        private String _name;
 *        private Person[] _contacts;
 *        private List&lt;Person&gt; _otherContacts;
 *
 *
 *        &#064;FlexNode("@id")
 *        public int getId() {
 *            return _id;
 *        }
 *        public void setId(int id) {
 *            _id = id;
 *        }
 *
 *
 *        &#064;FlexNode("@name")
 *        public String getName() {
 *            return _name;
 *        }
 *        public void setName(String name) {
 *            _name = name;
 *        }
 *
 *
 *        public Person[] getContacts(){
 *           return _contacts;
 *        }
 *        public void setContacts(Person[] contacts){
 *           _contacts = contacts;
 *        }
 *
 *
 *        &#064;FlexNode("others-contacts")
 *        &#064;FlexList(Person.class)
 *        public List&lt;Person&gt; getOtherContacts(){
 *           return _otherContacts;
 *        }
 *        public void setOtherContacts(List&lt;Person&gt; contacts){
 *           _otherContacts = contacts;
 *        }
 *    }
 * </pre>
 *
 * <p>After running the generator, you can now use the <b>PersonBuilder</b> 
 * class.</p>
 *
 * <pre>
 *  static int ids = 1;
 *    
 *  static Person getPerson(String name, Person[] contacts){
 *        Person p = new Person();
 *        p.setId(ids++);
 *        p.setName(name);
 *        p.setContacts(contacts);
 *        return p;
 *  }
 *   
 *  public static void main(String[] args) {
 *       
 *       // construct "Bob" and his friends
 *       Person bob = getPerson("Bob", new Person[]{
 *          getPerson("Bill", null),
 *          getPerson("Tedd", null),
 *          getPerson("Joey", null)
 *       });
 *       
 *       bob.setOtherContacts(Arrays.asList(new Person[]{
 *           getPerson("Serge", new Person[]{
 *               getPerson("Sergine", null)
 *           }),
 *           getPerson("Alain", null),
 *           getPerson("Paul", null)
 *       }));
 *       
 *       
 *       // generate a dom4j element from "Bob" instance
 *       Element bobElement = PersonBuilder.getElement(bob);       
 *      
 *       // output the XML to the console
 *       new XMLWriter(System.out).write(bobElement);       
 *  }
 * </pre>
 *
 * <p><b>Generated XML :</b></p>
 * <pre>
 *  &lt;Person id="4" name="Bob"&gt;
 *	&lt;contacts&gt;
 * 		&lt;Person id="1" name="Bill"/&gt;
 *		&lt;Person id="2" name="Tedd"/&gt;
 *		&lt;Person id="3" name="Joey"/&gt;
 *	&lt;/contacts&gt;
 *	&lt;others-contacts&gt;
 *		&lt;Person id="6" name="Serge"&gt;
 *			&lt;contacts&gt;
 *				&lt;Person id="5" name="Sergine"/&gt;
 *			&lt;/contacts&gt;
 *		&lt;/Person&gt;
 *		&lt;Person id="7" name="Alain"/&gt;
 *		&lt;Person id="8" name="Paul"/&gt;
 *	&lt;/others-contacts&gt;
 *   &lt;/Person&gt;
 * </pre>
 * 
 *
 * @version 0.2
 * @author Nicolas Désy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})

public @interface FlexNode {
    
    /**
     * <p>If the value begins with @, then the property will be an attribute.
     * Otherwise it will be an element.  Note that Entity classes, Array,
     * Collection and Map cannot be used as attribute.</p>
     */
    String value();
}
