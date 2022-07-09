package net.deechael.dynamichat.util;


import java.lang.reflect.*;
public final class EzClass {

    private final Class<?> clazz;

    private Constructor<?> constructor = null;

    private Object object = null;

    private boolean created = false;

    public EzClass(String className) {
        this(Ref.getClass(className));
    }

    public EzClass(Class<?> clazz) {
        if (clazz == null) throw new IllegalArgumentException("Class not found");
        this.clazz = clazz;
    }

    public void setConstructor(Class<?>... classes) {
        try {
            this.constructor = clazz.getDeclaredConstructor(classes);
            this.constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find the constructor");
        }
    }

    public void newInstance(Object... objects) {
        if (!created) {
            if (constructor == null) {
                setConstructor();
                try {
                    object = constructor.newInstance();
                    created = true;
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Cannot find the constructor");
                }
                return;
            }
            try {
                object = constructor.newInstance(objects);
                created = true;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Cannot find the constructor");
            }
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
            Field field = this.clazz.getDeclaredField(fieldName);
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
            Field field = this.clazz.getDeclaredField(fieldName);
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
            Method method = this.clazz.getDeclaredMethod(methodName, classes);
            if (Modifier.isStatic(method.getModifiers())) throw new IllegalAccessException("Method \"" + methodName + "\" is a static method");
            method.setAccessible(true);
            Object object = method.invoke(this.object, arguments);
            if (method.getReturnType().equals(void.class)) return null;
            return object;
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Method \"" + methodName + "\" not found");
        } catch (InvocationTargetException | IllegalAccessException e) {
            return null;
        }
    }

    public Object invokeStaticMethod(String methodName, Class<?>[] classes, Object[] arguments) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, classes);
            if (!Modifier.isStatic(method.getModifiers())) throw new IllegalAccessException("Method \"" + methodName + "\" is not a static method");
            method.setAccessible(true);
            Object object = method.invoke(null, arguments);
            if (method.getReturnType().equals(void.class)) return null;
            return object;
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Method \"" + methodName + "\" not found");
        } catch (InvocationTargetException | IllegalAccessException e) {
            return null;
        }
    }

    public Object getFieldByType(Class<?> type) {
        if (!created) throw new IllegalStateException("Haven't create a new instance");
        Object object = null;
        for (Field field : this.clazz.getClass().getDeclaredFields()) {
            if (field.getType().equals(type)) {
                field.setAccessible(true);
                try {
                    object = field.get(this.object);
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return object;
    }

    public Object getStaticFieldByType(Class<?> type) {
        Object object = null;
        for (Field field : this.clazz.getDeclaredFields()) {
            if (field.getType().equals(type)) {
                field.setAccessible(true);
                try {
                    object = field.get(null);
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return object;
    }

    public boolean isExtended(Object object) {
        return clazz.isAssignableFrom(object.getClass());
    }

    public Object getInstance() {
        return this.object;
    }

    @Override
    public String toString() {
        if (!created) throw new IllegalStateException("Haven't create a new instance");
        return object.toString();
    }

    public Object[] createArray(int size) {
        return (Object[]) Array.newInstance(clazz, size);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof EzEnum) {
            EzClass ezClass = (EzClass) object;
            if (!this.created && !ezClass.created) return true;
            if (this.created && ezClass.created) {
                return this.object.equals(ezClass.object);
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

    public Class<?> getInstanceClass() {
        return clazz;
    }

}
