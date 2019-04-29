package JavaSerializer;


import SyntacseForSerializing.SerializingSyntacse;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Serializer class
 * This is the special class that was made for bean classes
 *
 * @author Danilov Alexey
 */
public class JavaSerializer {
    
    /*-----------------------------------------------------Fields-----------------------------------------------------*/
    
    private Object _object;
    private SerializingSyntacse _serializingSyntacse;
    List<Object> objectList;
    List<String> idList;
    
    /*---------------------------------------------------Constructor--------------------------------------------------*/
    
    /**
     * Simple constructor
     *
     * @param o Object which should be serialized
     */
    public JavaSerializer(Object o) {
        if (o == null || o.getClass().isPrimitive())
            throw new NullPointerException("Parameter is incorrect");
        _object = o;
    }
    
    /*-----------------------------------------------------Methods----------------------------------------------------*/
    
    /**
     * Main method that do all work
     *
     * @param writer writer in file
     * @param s    syntacse of serialization
     */
    public void Make(FileWriter writer, SerializingSyntacse s)
        throws IntrospectionException, InvocationTargetException, IllegalAccessException, IOException {
    
        //syntacse that we would use
        if (s == null)
            throw new NullPointerException("SyntacseForSerializing was null");
        _serializingSyntacse = s;
    
    
        objectList = new ArrayList<>();
        idList = new ArrayList<>();
        
        
        //result of serializing
        _serializingSyntacse.header(writer, 0);
        nonPrimitiveToString(writer, "", _object, 0, -1);
        _serializingSyntacse.end(writer, 0);
    }
    
    /**
     * Method to make string about primitive variables
     *
     * @param name name of the parameter
     * @param o    parameter that we should convert to string
     * @param tabs numbers of tabs before description
     *
     * @return string with description of primitive
     */
    private void primitiveToString(FileWriter output, String name, Object o, int tabs, int index) throws IOException {
        _serializingSyntacse.primitive(o.getClass().getSimpleName(), name, o, output, tabs, index);
    }
    
    private void nonPrimitiveToString(FileWriter output, String name, Object o, int tabs, int index)
        throws IntrospectionException, InvocationTargetException, IllegalAccessException, IOException {
        for (int i = 0; i < objectList.size(); i++) {
            if (objectList.get(i) == o) {
                _serializingSyntacse.reference(o.getClass().getSimpleName(), name, idList.get(i), output, tabs, index);
                return;
            }
        }
        Class objectType = o.getClass();
        if (o.getClass().isArray()) {
            objectList.add(o);
            idList.add(objectType.getSimpleName() + "" + idList.size());
            _serializingSyntacse.arrayBegin(objectType.getName(), name, output,
                objectType.getSimpleName() + "" + idList.size(), tabs, index, ((Object[]) o).length);
            
            if (objectType == byte[].class) {
                for (int i = 0; i < ((byte[]) o).length; i++)
                    primitiveToString(output, "", ((byte[]) o)[i], tabs + 1, i);
            } else if (objectType == short[].class) {
                for (int i = 0; i < ((short[]) o).length; i++)
                    primitiveToString(output, "", ((short[]) o)[i], tabs + 1, i);
            } else if (objectType == int[].class) {
                for (int i = 0; i < ((int[]) o).length; i++)
                    primitiveToString(output, "", ((int[]) o)[i], tabs + 1, i);
            } else if (objectType == long[].class) {
                for (int i = 0; i < ((long[]) o).length; i++)
                    primitiveToString(output, "", ((long[]) o)[i], tabs + 1, i);
            } else if (objectType == char[].class) {
                for (int i = 0; i < ((char[]) o).length; i++)
                    primitiveToString(output, "", ((char[]) o)[i], tabs + 1, i);
            } else if (objectType == float[].class) {
                for (int i = 0; i < ((float[]) o).length; i++)
                    primitiveToString(output, "", ((float[]) o)[i], tabs + 1, i);
            } else if (objectType == double[].class) {
                for (int i = 0; i < ((double[]) o).length; i++)
                    primitiveToString(output, "", ((double[]) o)[i], tabs + 1, i);
            } else if (objectType == boolean[].class) {
                for (int i = 0; i < ((boolean[]) o).length; i++)
                    primitiveToString(output, "", ((boolean[]) o)[i], tabs + 1, i);
            } else if (objectType == String[].class) {
                for (int i = 0; i < ((String[]) o).length; i++)
                    primitiveToString(output, "", ((String[]) o)[i], tabs + 1, i);
            } else {
                for (int i = 0; i < ((Object[]) o).length; i++) {
                    nonPrimitiveToString(output, "", ((Object[]) o)[i], tabs + 1, i);
                }
            }
            _serializingSyntacse.arrayEnd(objectType.getName(), name, output, tabs, index);
        } else if (o instanceof Iterable) {
            objectList.add(o);
            idList.add(objectType.getSimpleName() + "" + idList.size());
            _serializingSyntacse.itarableBegin(objectType.getName(), name, output, objectType.getSimpleName() + "" +
                (idList.size() - 1), tabs, index);
            Iterator iterator = ((Iterable) o).iterator();
            while (iterator.hasNext()) {
                nonPrimitiveToString(output, "", iterator.next(), tabs + 1, index);
            }
            _serializingSyntacse.iterableEnd(objectType.getName(), name, output, tabs, index);
            
        } else {
            if (objectType == Byte.class) {
                primitiveToString(output, "",  o, tabs + 1, index);
            } else if (objectType == Short.class) {
                primitiveToString(output, "",  o, tabs + 1, index);
            } else if (objectType == Integer.class) {
                primitiveToString(output, "", o, tabs + 1, index);
            } else if (objectType == Long.class) {
                primitiveToString(output, "", o, tabs + 1, index);
            } else if (objectType == Character.class) {
                primitiveToString(output, "",  o, tabs + 1, index);
            } else if (objectType == Float.class) {
                primitiveToString(output, "", o, tabs + 1, index);
            } else if (objectType == Double.class) {
                primitiveToString(output, "", o, tabs + 1, index);
            } else if (objectType == Boolean.class) {
                primitiveToString(output, "", o, tabs + 1, index);
            } else {

                objectList.add(o);
                idList.add(objectType.getSimpleName() + "" + idList.size());

                _serializingSyntacse.nonPrimitiveBegin(objectType.getName(), name, output,
                    objectType.getSimpleName() + "" + (idList.size() - 1), tabs, -1);

                BeanInfo beanInfo = Introspector.getBeanInfo(o.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

                for (PropertyDescriptor pd : propertyDescriptors) {
                    if (pd.getReadMethod() != null && !"class".equals(pd.getDisplayName())) {

                        if (pd.getPropertyType().isPrimitive() || pd.getPropertyType().isAssignableFrom(String.class))
                            primitiveToString(
                                output, pd.getName(), pd.getReadMethod().invoke(o), tabs + 1, -1
                            );
                        else if (pd.getReadMethod().invoke(o) != null)
                            nonPrimitiveToString(output, pd.getName(), pd.getReadMethod().invoke(o),
                                tabs + 1, -1);
                    }
                }

                if (o == _object)
                    index = -2;

                _serializingSyntacse.nonPrimitiveEnd(objectType.getName(), name, output, tabs, index);
            }
        }
    }
}
