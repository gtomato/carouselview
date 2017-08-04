package com.gtomato.android.demo.carouselviewdemo;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.util.Log;
import android.view.Gravity;

import com.gtomato.android.ui.transformer.CoverFlowViewTransformer;
import com.gtomato.android.ui.transformer.FlatMerryGoRoundTransformer;
import com.gtomato.android.ui.transformer.InverseTimeMachineViewTransformer;
import com.gtomato.android.ui.transformer.LinearViewTransformer;
import com.gtomato.android.ui.transformer.ParameterizedViewTransformer;
import com.gtomato.android.ui.transformer.TimeMachineViewTransformer;
import com.gtomato.android.ui.transformer.WheelViewTransformer;
import com.gtomato.android.ui.widget.CarouselView;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author  sunny-chung
 */

public class CarouselParameters {
    public static final List<Class<? extends CarouselView.ViewTransformer>> TRANSFORMER_CLASSES = Arrays.asList(
            LinearViewTransformer.class,
            WheelViewTransformer.class,
            CoverFlowViewTransformer.class,
            TimeMachineViewTransformer.class,
            InverseTimeMachineViewTransformer.class,
            ParameterizedViewTransformer.class,
            FlatMerryGoRoundTransformer.class
    );

    private static final Map<String, String> PARAMETER_NAMES = new HashMap<String, String>() {{
        put("rotateDegree", "Rotation Angle (Degree)");
        put("numPies", "Number of Pies");
        put("farAlpha", "Alpha at Distant Point");
        put("farScale", "Scale at Distant Point");
    }};

    private static final Map<String, RangeInfo> PARAMETER_RANGES = new HashMap<String, RangeInfo>() {{
        // only "from" and "to" fields of RangeInfo are used
        put("yProjection", new RangeInfo<>(0.0, 90.0, 0, 0));
        put("numPies", new RangeInfo<>(1, Integer.MAX_VALUE, 0, 0));
        put("horizontalViewPort", new RangeInfo<>(0.0, 1.0, 0, 0));
        put("farAlpha", new RangeInfo<>(-1.0, 1.0, 0, 0));
    }};

    public static final Map<String, Integer> GRAVITY = Collections.unmodifiableMap(new LinkedHashMap<String, Integer>() {{
        put("LEFT", Gravity.LEFT);
        put("RIGHT", Gravity.RIGHT);
        put("TOP", Gravity.TOP);
        put("BOTTOM", Gravity.BOTTOM);
        put("CENTER", Gravity.CENTER);
        put("CENTER_HORIZONTAL", Gravity.CENTER_HORIZONTAL);
        put("CENTER_VERTICAL", Gravity.CENTER_VERTICAL);
        put("LEFT|BOTTOM", Gravity.LEFT | Gravity.BOTTOM);
        put("LEFT|CENTER_VERTICAL", Gravity.LEFT | Gravity.CENTER_VERTICAL);
        put("RIGHT|BOTTOM", Gravity.RIGHT | Gravity.BOTTOM);
        put("RIGHT|CENTER_VERTICAL", Gravity.RIGHT | Gravity.CENTER_VERTICAL);
    }});

    private static final double FLOAT_PARAMETER_DEFAULT_MAX_VALUE = 1.5f;
    private static final double FLOAT_PARAMETER_DEFAULT_MIN_VALUE = -1.5f;

    private static final int INT_PARAMETER_DEFAULT_MAX_VALUE = 10;
    private static final int INT_PARAMETER_DEFAULT_MIN_VALUE = -10;

    public static List<String> getTransformerNames() {
        List<String> names = new ArrayList<>();
        for (Class clazz : TRANSFORMER_CLASSES) {
            names.add(clazz.getSimpleName());
        }
        return names;
    }

    public static String getParameterName(String setterMethodName) {
        if (!setterMethodName.matches("set[A-Z].*")) {
            throw new IllegalArgumentException("not setter method");
        }
        String beanName = getBeanName(setterMethodName);
        String parameterName = PARAMETER_NAMES.get(beanName);
        if (parameterName == null) {
            // generate a default name
            StringBuilder sb = new StringBuilder();
            String methodNameSuffix = setterMethodName.substring(3);
            for (int i = 0, j = 0; i < methodNameSuffix.length(); ) {
                while (j < methodNameSuffix.length() && !isUpperCase(methodNameSuffix.charAt(j))) {
                    ++j;
                }
                if (j > i) {
                    if (sb.length() != 0) sb.append(' ');
                    sb.append(methodNameSuffix.substring(i, j));
                    i = j;
                }
                ++j;
            }
            parameterName = sb.toString();
            PARAMETER_NAMES.put(methodNameSuffix, parameterName);
        }
        return parameterName;
    }

    public static RangeInfo<Double> getParameterRangeInfoDouble(Method setterMethod) {

        double upper = FLOAT_PARAMETER_DEFAULT_MAX_VALUE, lower = FLOAT_PARAMETER_DEFAULT_MIN_VALUE;

        // should fail because FloatRange's retention policy is CLASS
        FloatRange annotatedRange = findAnnotation(setterMethod.getParameterAnnotations()[0], FloatRange.class);
        if (annotatedRange != null) {
            lower = annotatedRange.from();
            upper = annotatedRange.to();
            if (Double.isInfinite(lower)) lower = FLOAT_PARAMETER_DEFAULT_MIN_VALUE;
            if (Double.isInfinite(upper)) upper = FLOAT_PARAMETER_DEFAULT_MAX_VALUE;
        } else {
            RangeInfo<Double> range = PARAMETER_RANGES.get(getBeanName(setterMethod.getName()));
            if (range != null) {
                lower = range.from;
                upper = range.to;
                if (Double.isInfinite(lower)) lower = FLOAT_PARAMETER_DEFAULT_MIN_VALUE;
                if (Double.isInfinite(upper)) upper = FLOAT_PARAMETER_DEFAULT_MAX_VALUE;
            }
        }

        double diff = upper - lower;
        double interval;
        if (diff <= 3.0) {
            interval = 0.1;
        } else if (diff <= 6.0) {
            interval = 0.2;
        } else if (diff <= 15.0 ){
            interval = 0.5;
        } else {
            interval = 1.0;
        }

        if (annotatedRange != null) {
            if (!annotatedRange.fromInclusive()) {
                lower = annotatedRange.from() + interval;
                if (Double.isInfinite(lower)) lower = FLOAT_PARAMETER_DEFAULT_MIN_VALUE;
            }

            if (!annotatedRange.toInclusive()) {
                upper = annotatedRange.to() - interval;
                if (Double.isInfinite(upper)) upper = FLOAT_PARAMETER_DEFAULT_MAX_VALUE;
            }
        }

        int numIntervals = (int) Math.round((upper - lower) / interval);

        return new RangeInfo<>(lower, upper, interval, numIntervals);
    }

    public static RangeInfo<Integer> getParameterRangeInfoInt(Method setterMethod) {

        int upper = INT_PARAMETER_DEFAULT_MAX_VALUE, lower = INT_PARAMETER_DEFAULT_MIN_VALUE;

        // should fail because IntRange's retention policy is CLASS
        IntRange annotatedRange = findAnnotation(setterMethod.getParameterAnnotations()[0], IntRange.class);
        if (annotatedRange != null) {
            long from = annotatedRange.from(); // + (annotatedRange.fromInclusive() ? 0 : FLOAT_PARAMETER_EXCLUSIVE_OFFSET);
            long to = annotatedRange.to(); // - (annotatedRange.toInclusive() ? 0 : FLOAT_PARAMETER_EXCLUSIVE_OFFSET);
            lower = from > Integer.MIN_VALUE ? (int) from : INT_PARAMETER_DEFAULT_MIN_VALUE;
            upper = to < Integer.MAX_VALUE ? (int) to : INT_PARAMETER_DEFAULT_MAX_VALUE;
        } else {
            RangeInfo<Integer> range = PARAMETER_RANGES.get(getBeanName(setterMethod.getName()));
            if (range != null) {
                int from = range.from;
                int to = range.to;
                lower = from > Integer.MIN_VALUE ? from : INT_PARAMETER_DEFAULT_MIN_VALUE;
                upper = to < Integer.MAX_VALUE ? to : INT_PARAMETER_DEFAULT_MAX_VALUE;
            }
        }

        int diff = upper - lower;
        int interval;
        if (diff <= 30) {
            interval = 1;
        } else if (diff <= 60) {
            interval = 2;
        } else if (diff <= 150) {
            interval = 5;
        } else {
            interval = 10;
        }

        int numIntervals = (int) Math.floor(((double) upper - lower) / interval);

        return new RangeInfo<>(lower, upper, interval, numIntervals);
    }

    public static List<Method> getSetterMethods(Class<? extends CarouselView.ViewTransformer> clazz) {
        List<Method> result = new ArrayList<>();
        Pattern setterMethodPattern = Pattern.compile("set[A-Z].*");
        for (Method method : clazz.getMethods()) {
            if (setterMethodPattern.matcher(method.getName()).matches()) {
                result.add(method);
            }
        }
        return result;
    }

    public static Map<String, Method> getSetterMethodMap(Class<? extends CarouselView.ViewTransformer> clazz) {
        Map<String, Method> result = new HashMap<>();
        Pattern setterMethodPattern = Pattern.compile("set[A-Z].*");
        for (Method method : clazz.getMethods()) {
            if (setterMethodPattern.matcher(method.getName()).matches()) {
                result.put(getBeanName(method.getName()), method);
            }
        }
        return result;
    }

    public static <T extends CarouselView.ViewTransformer> Map<String, Number> getDefaultTransformerParameters(Class<T> clazz, List<Method> setterMethods) {
        Map<String, Number> results = new HashMap<>();
        T transformer;
        try {
            transformer = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        if (setterMethods == null) {
            setterMethods = getSetterMethods(clazz);
        }

        for (Method setterMethod : setterMethods) {
            Class parameterType = setterMethod.getParameterTypes()[0];
            String getterMethodName;
            if (parameterType == Boolean.class || parameterType == boolean.class) {
                getterMethodName = "is" + setterMethod.getName().substring(3);
            } else {
                getterMethodName = "get" + setterMethod.getName().substring(3);
            }

            try {
                Method getterMethod = clazz.getMethod(getterMethodName);

                Object result = getterMethod.invoke(transformer);

                if (result != null) {

                    String beanName = getBeanName(setterMethod.getName());

                    if (parameterType == Boolean.class || parameterType == boolean.class) {

                        results.put(beanName, ((boolean) result) ? (byte) 1 : (byte) 0);

                    } else if (parameterType == Float.class
                            || parameterType == float.class
                            || parameterType == Double.class
                            || parameterType == double.class) {

                        double castedResult = result instanceof Number ? ((Number) result).doubleValue() : (double) result;

                        if (!Double.isNaN(castedResult)) {

                            results.put(beanName, castedResult);

                        }

                    } else if (parameterType == Integer.class || parameterType == int.class) {

                        results.put(beanName, (Integer) result);

                    }

                }
            } catch (NoSuchMethodException e) {
                Log.w("CarouselViewDemo", "Cannot find getter method " + getterMethodName + " from " + clazz.getName());
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (ClassCastException e) {
                throw e;
            }
        }

        return results;
    }

    public static <T extends CarouselView.ViewTransformer> T createTransformer(Class<T> clazz, Map<String, Number> parameters) {
        T transformer;
        try {
            transformer = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        if (parameters != null) {
            Map<String, Method> setterMethods = getSetterMethodMap(clazz);
            for (Map.Entry<String, Number> param : parameters.entrySet()) {
                String beanName = param.getKey();
                Method setterMethod = setterMethods.get(beanName);
                Number paramValue = param.getValue();
                try {
                    if (paramValue instanceof Double) {
                        Class setterParamType = setterMethod.getParameterTypes()[0];
                        if (setterParamType == Float.class || setterParamType == float.class) {
                            setterMethod.invoke(transformer, param.getValue().floatValue());
                        } else {
                            setterMethod.invoke(transformer, param.getValue().doubleValue());
                        }
                    } else if (paramValue instanceof Integer) {
                        setterMethod.invoke(transformer, param.getValue().intValue());
                    } else if (paramValue instanceof Byte) {
                        setterMethod.invoke(transformer, param.getValue().byteValue() != 0);
                    } else {
                        throw new IllegalArgumentException("unknown parameter type " + paramValue.getClass());
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return transformer;
    }

    public static String getBeanName(String setterMethodName) {
        return setterMethodName.substring(3, 4).toLowerCase() + setterMethodName.substring(4);
    }

    private static boolean isUpperCase(char c) {
        return 'A' <= c && c <= 'Z';
    }

    private static <A extends Annotation> A findAnnotation(Annotation[] annotations, Class<A> type) {
        if (annotations == null) return null;
        for (Annotation annotation : annotations) {
            if (type.equals(annotation.annotationType())) {
                return (A) annotation;
            }
        }
        return null;
    }

    static class RangeInfo<T> {
        public T from, to, interval;
        public int numIntervals;

        public RangeInfo(T from, T to, T interval, int numIntervals) {
            this.from = from;
            this.to = to;
            this.interval = interval;
            this.numIntervals = numIntervals;
        }
    }

}
