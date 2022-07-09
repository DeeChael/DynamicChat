package net.deechael.dynamichat.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class EzEnum {

    private final Class<?> clazz;

    private Object object = null;

    private boolean created = false;

    public EzEnum(String className) {
        this(Ref.getClass(className));
    }

    public EzEnum(Class<?> clazz) {
        if (clazz == null) throw new IllegalArgumentException("Class not found");
        if (!clazz.isEnum()) throw new RuntimeException("Argument class is not a enum");
        this.clazz = clazz;
    }

    public void newInstance(String enumName) {
        if (!created) {
            object = valueOf(enumName);
            created = true;
        } else {
            throw new IllegalStateException("Instance has been created");
        }
    }

    public void setInstance(Object object) {
        if (!clazz.isInstance(object)) throw new IllegalArgumentException("Argument object doesn't extended class " + clazz.getName());
        this.object = object;
        created = true;
    }

    public Object getField(String fieldName) {
        if (!created) throw new IllegalStateException("Haven't create a new instance");
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Field \"" + fieldName + "\" not found");
        }
    }

    public <T> T getField(String fieldName, Class<T> typeClass) {
        if (!created) throw new IllegalStateException("Haven't create a new instance");
        Object object = getField(fieldName);
        if (!typeClass.isInstance(object)) throw new RuntimeException("Field type is not " + typeClass.getName());
        return typeClass.cast(object);
    }

    public Object getStaticField(String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Field \"" + fieldName + "\" not found");
        }
    }

    public <T> T getStaticField(String fieldName, Class<T> typeClass) {
        Object object = getStaticField(fieldName);
        if (!typeClass.isInstance(object)) throw new RuntimeException("Field type is not " + typeClass.getName());
        return typeClass.cast(object);
    }

    public void setField(String fieldName, Object object) {
        if (!created) throw new IllegalStateException("Haven't create a new instance");
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(this.object, object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Field \"" + fieldName + "\" not found");
        }
    }

    public void setStaticField(String fieldName, Object object) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Field \"" + fieldName + "\" not found");
        }
    }

    public Object invokeMethod(String methodName, Class<?>[] classes, Object[] arguments) {
        if (!created) throw new IllegalStateException("Haven't create a new instance");
        try {
            Method method = clazz.getDeclaredMethod(methodName, classes);
            if (Modifier.isStatic(method.getModifiers())) throw new IllegalAccessException("Method \"" + methodName + "\" is a static method");
            method.setAccessible(true);
            if (method.getReturnType().equals(void.class)) return null;
            return method.invoke(this.object, arguments);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Method \"" + methodName + "\" not found");
        } catch (InvocationTargetException | IllegalAccessException ignored) {
            return null;
        }
    }

    public Object invokeStaticMethod(String methodName, Class<?>[] classes, Object[] arguments) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, classes);
            if (!Modifier.isStatic(method.getModifiers())) throw new IllegalAccessException("Method \"" + methodName + "\" is not a static method");
            method.setAccessible(true);
            if (method.getReturnType().equals(void.class)) return null;
            return method.invoke(null, arguments);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Method \"" + methodName + "\" not found");
        } catch (InvocationTargetException | IllegalAccessException ignored) {
            return null;
        }
    }

    public boolean isExtended(Object object) {
        return clazz.isAssignableFrom(object.getClass());
    }

    public Object[] values() {
        return (Object[]) this.invokeStaticMethod("values", new Class[0], new Object[0]);
    }

    public EzEnum[] valuesAsEzEnum() {
        List<EzEnum> ezEnums = new ArrayList<>();
        for (Object object : values()) {
            EzEnum ezEnum = new EzEnum(this.clazz);
            ezEnum.setInstance(object);
            ezEnums.add(ezEnum);
        }
        return ezEnums.toArray(new EzEnum[0]);
    }

    public String name() {
        if (!created) throw new IllegalStateException("Haven't create a new instance");
        return (String) invokeMethod("name", new Class[0], new Object[0]);
    }

    public Object valueOf(String name) {
        return invokeStaticMethod("valueOf", new Class[] {String.class}, new Object[] {name});
    }

    public Object getInstance() {
        return this.object;
    }

    @Override
    public String toString() {
        if (!created) throw new IllegalStateException("Haven't create a new instance");
        return object.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof EzEnum) {
            EzEnum ezEnum = (EzEnum) object;
            if (!this.created && !ezEnum.created) return true;
            if (this.created && ezEnum.created) {
                return this.object.equals(ezEnum.object);
            } else {
                return false;
            }
        }
        return this.object.equals(object);
    }

    @Override
    public int hashCode() {
        if (!created) throw new IllegalStateException("Haven't create a new instance");
        return this.object.hashCode();
    }

    public Class<?> getInstanceEnum() {
        return clazz;
    }

}
