package com.mb.lab.banks.utils.exception;

import java.util.Collection;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class BusinessAssert {

    /**
     * Assert a boolean expression, throwing {@code BusinessException} if the test result is {@code false}.
     * 
     * <pre class="code">
     * BusinessAssert.isTrue(i &gt; 0, BusinessExceptionCode.INVALID_PARAM, &quot;The value must be greater than zero&quot;);
     * </pre>
     * 
     * @param expression
     *            a boolean expression
     * @param code
     *            the exception code to use if the assertion fails
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if expression is {@code false}
     */
    public static void isTrue(boolean expression, String code, String message) {
        if (!expression) {
            throw new BusinessException(code, message);
        }
    }

    /**
     * Assert a boolean expression, throwing {@code BusinessException} if the test result is {@code false}.
     * 
     * <pre class="code">
     * BusinessAssert.isTrue(i &gt; 0, &quot;The value must be greater than zero&quot;);
     * </pre>
     * 
     * @param expression
     *            a boolean expression
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if expression is {@code false}
     */
    public static void isTrue(boolean expression, String message) {
        isTrue(expression, BusinessExceptionCode.INVALID_PARAM, message);
    }

    /**
     * Assert a boolean expression, throwing {@code BusinessException} if the test result is {@code false}.
     * 
     * <pre class="code">
     * BusinessAssert.isTrue(i &gt; 0);
     * </pre>
     * 
     * @param expression
     *            a boolean expression
     * @throws BusinessException
     *             if expression is {@code false}
     */
    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    /**
     * Assert a boolean expression, throwing {@code BusinessException} if the test result is {@code true}.
     * 
     * <pre class="code">
     * BusinessAssert.notTrue(i &gt; 0, BusinessExceptionCode.INVALID_PARAM, &quot;The value must not be greater than zero&quot;);
     * </pre>
     * 
     * @param expression
     *            a boolean expression
     * @param code
     *            the exception code to use if the assertion fails
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if expression is {@code true}
     */
    public static void notTrue(boolean expression, String code, String message) {
        if (expression) {
            throw new BusinessException(code, message);
        }
    }

    /**
     * Assert a boolean expression, throwing {@code BusinessException} if the test result is {@code true}.
     * 
     * <pre class="code">
     * BusinessAssert.notTrue(i &gt; 0, &quot;The value must not be greater than zero&quot;);
     * </pre>
     * 
     * @param expression
     *            a boolean expression
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if expression is {@code true}
     */
    public static void notTrue(boolean expression, String message) {
        notTrue(expression, BusinessExceptionCode.INVALID_PARAM, message);
    }

    /**
     * Assert a boolean expression, throwing {@code BusinessException} if the test result is {@code true}.
     * 
     * <pre class="code">
     * BusinessAssert.notTrue(i &gt; 0);
     * </pre>
     * 
     * @param expression
     *            a boolean expression
     * @throws BusinessException
     *             if expression is {@code true}
     */
    public static void notTrue(boolean expression) {
        notTrue(expression, "[Assertion failed] - this expression must not be true");
    }

    /**
     * Assert that an object is not {@code null} .
     * 
     * <pre class="code">
     * BusinessAssert.notNull(clazz, BusinessExceptionCode.INVALID_PARAM, &quot;The class must not be null&quot;);
     * </pre>
     * 
     * @param object
     *            the object to check
     * @param code
     *            the exception code to use if the assertion fails
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if the object is {@code null}
     */
    public static void notNull(Object object, String code, String message) {
        if (object == null) {
            throw new BusinessException(code, message);
        }
    }

    /**
     * Assert that an object is not {@code null} .
     * 
     * <pre class="code">
     * BusinessAssert.notNull(clazz, &quot;The class must not be null&quot;);
     * </pre>
     * 
     * @param object
     *            the object to check
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if the object is {@code null}
     */
    public static void notNull(Object object, String message) {
        notNull(object, BusinessExceptionCode.INVALID_PARAM, message);
    }

    /**
     * Assert that an object is not {@code null} .
     * 
     * <pre class="code">
     * BusinessAssert.notNull(clazz);
     * </pre>
     * 
     * @param object
     *            the object to check
     * @throws BusinessException
     *             if the object is {@code null}
     */
    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    public static void notEmptyText(Object object, String code, String message) {
        isTrue(!StringUtils.isEmpty(object), code, message);
    }

    public static void notEmptyText(Object object, String message) {
        notEmptyText(object, BusinessExceptionCode.INVALID_PARAM, message);
    }

    public static void notEmptyText(Object object) {
        notEmptyText(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    /**
     * Assert that a collection has elements; that is, it must not be {@code null} and must have at least one element.
     * 
     * <pre class="code">
     * BusinessAssert.notEmpty(collection, BusinessExceptionCode.INVALID_PARAM, &quot;Collection must have elements&quot;);
     * </pre>
     * 
     * @param collection
     *            the collection to check
     * @param code
     *            the exception code to use if the assertion fails
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if the collection is {@code null} or has no elements
     */
    public static void notEmpty(Collection<?> collection, String code, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(code, message);
        }
    }

    /**
     * Assert that a collection has elements; that is, it must not be {@code null} and must have at least one element.
     * 
     * <pre class="code">
     * BusinessAssert.notEmpty(collection, &quot;Collection must have elements&quot;);
     * </pre>
     * 
     * @param collection
     *            the collection to check
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if the collection is {@code null} or has no elements
     */
    public static void notEmpty(Collection<?> collection, String message) {
        notEmpty(collection, BusinessExceptionCode.INVALID_PARAM, message);
    }

    /**
     * Assert that a collection has elements; that is, it must not be {@code null} and must have at least one element.
     * 
     * <pre * class="code"> BusinessAssert.notEmpty(collection, &quot;Collection must have elements&quot;);
     * </pre>
     *
     * @param collection
     *            the collection to check
     * @throws BusinessException
     *             if the collection is {@code null} or has no elements
     */
    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
    }

    /**
     * Assert that a Map has entries; that is, it must not be {@code null} and must have at least one entry.
     * 
     * <pre * class="code"> BusinessAssert.notEmpty(map, &quot;Map must have entries&quot;);
     * </pre>
     *
     * @param map
     *            the map to check
     * @param code
     *            the exception code to use if the assertion fails
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if the map is {@code null} or has no entries
     */
    public static void notEmpty(Map<?, ?> map, String code, String message) {
        if (CollectionUtils.isEmpty(map)) {
            throw new BusinessException(code, message);
        }
    }

    /**
     * Assert that a Map has entries; that is, it must not be {@code null} and must have at least one entry.
     * 
     * <pre * class="code"> BusinessAssert.notEmpty(map, &quot;Map must have entries&quot;);
     * </pre>
     *
     * @param map
     *            the map to check
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if the map is {@code null} or has no entries
     */
    public static void notEmpty(Map<?, ?> map, String message) {
        notEmpty(map, BusinessExceptionCode.INVALID_PARAM, message);
    }

    /**
     * Assert that a Map has entries; that is, it must not be {@code null} and must have at least one entry.
     * 
     * <pre * class="code"> BusinessAssert.notEmpty(map);
     * </pre>
     *
     * @param map
     *            the map to check
     * @throws BusinessException
     *             if the map is {@code null} or has no entries
     */
    public static void notEmpty(Map<?, ?> map) {
        notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
    }

    /**
     * Assert that the given collection contain the given object.
     * 
     * <pre class="code">
     * BusinessAssert.contain(customers, &quot;Bob&quot;, BusinessExceptionCode.INVALID_PARAM, &quot;Customers must contain 'Bob'&quot;);
     * </pre>
     * 
     * @param collection
     *            the collection to search
     * @param object
     *            the object to find within the collection
     * @param code
     *            the exception code to use if the assertion fails
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if the collection is {@code null} or has no entries or does not contain given object
     */
    public static void contain(Collection<?> collection, Object object, String code, String message) {
        if (collection == null || collection.isEmpty() || object == null || !collection.contains(object)) {
            throw new BusinessException(code, message);
        }
    }

    /**
     * Assert that the given collection contain the given object.
     * 
     * <pre class="code">
     * BusinessAssert.contain(customers, &quot;Bob&quot;, &quot;Customers must contain 'Bob'&quot;);
     * </pre>
     * 
     * @param collection
     *            the collection to search
     * @param object
     *            the object to find within the collection
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if the collection is {@code null} or has no entries or does not contain given object
     */
    public static void contain(Collection<?> collection, Object object, String message) {
        contain(collection, object, BusinessExceptionCode.INVALID_PARAM, message);
    }

    /**
     * Assert that the given collection contain the given object.
     * 
     * <pre class="code">
     * BusinessAssert.contain(customers, &quot;Bob&quot;);
     * </pre>
     * 
     * @param collection
     *            the collection to search
     * @param object
     *            the object to find within the collection
     * @throws BusinessException
     *             if the collection is {@code null} or has no entries or does not contain given object
     */
    public static void contain(Collection<?> collection, Object object) {
        contain(collection, object, "[Assertion failed] - this collection must not be empty; it must contain given object");
    }

    /**
     * Assert that two objects are equal.
     * 
     * <pre class="code">
     * BusinessAssert.equals(expected, actual, BusinessExceptionCode.INVALID_PARAM, &quot;Expected object must equals with actual object&quot;);
     * </pre>
     * 
     * @param collection
     *            the collection to search
     * @param object
     *            the object to find within the collection
     * @param code
     *            the exception code to use if the assertion fails
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if the expected object not equals with actual object
     */
    public static void equals(Object expected, Object actual, String code, String message) {
        if (expected == null && actual == null)
            return;
        if (expected != null && expected.equals(actual))
            return;
        throw new BusinessException(code, message);
    }

    /**
     * Assert that two objects are equal.
     * 
     * <pre class="code">
     * BusinessAssert.equals(expected, actual, &quot;Expected object must equals with actual object&quot;);
     * </pre>
     * 
     * @param collection
     *            the collection to search
     * @param object
     *            the object to find within the collection
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if the expected object not equals with actual object
     */
    public static void equals(Object expected, Object actual, String message) {
        equals(expected, actual, BusinessExceptionCode.INVALID_PARAM, message);
    }

    /**
     * Assert that two objects are equal.
     * 
     * <pre class="code">
     * BusinessAssert.equals(expected, actual);
     * </pre>
     * 
     * @param collection
     *            the collection to search
     * @param object
     *            the object to find within the collection
     * @throws BusinessException
     *             if the expected object not equals with actual object
     */
    public static void equals(Object expected, Object actual) {
        equals(expected, actual, "[Assertion failed] - expected object not equals with actual object");
    }

    /**
     * Assert that two string are equal.
     * 
     * <pre class="code">
     * BusinessAssert.equals(&quot;Bob&quot;, &quot;James&quot;, BusinessExceptionCode.INVALID_PARAM, &quot;'Bob' not equals with 'James'&quot;);
     * </pre>
     * 
     * @param collection
     *            the collection to search
     * @param object
     *            the object to find within the collection
     * @param code
     *            the exception code to use if the assertion fails
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if the expected string not equals with actual string
     */
    public static void equals(String expected, String actual, String code, String message) {
        if (expected == null && actual == null)
            return;
        if (expected != null && expected.equals(actual))
            return;
        throw new BusinessException(code, message);
    }

    /**
     * Assert that two string are equal.
     * 
     * <pre class="code">
     * BusinessAssert.equals(&quot;Bob&quot;, &quot;James&quot;, &quot;'Bob' not equals with 'James'&quot;);
     * </pre>
     * 
     * @param collection
     *            the collection to search
     * @param object
     *            the object to find within the collection
     * @param message
     *            the exception message to use if the assertion fails
     * @throws BusinessException
     *             if the expected string not equals with actual string
     */
    public static void equals(String expected, String actual, String message) {
        equals(expected, actual, BusinessExceptionCode.INVALID_PARAM, message);
    }

    /**
     * Assert that two string are equal.
     * 
     * <pre class="code">
     * BusinessAssert.equals(&quot;Bob&quot;, &quot;James&quot;);
     * </pre>
     * 
     * @param collection
     *            the collection to search
     * @param object
     *            the object to find within the collection
     * @throws BusinessException
     *             if the expected string not equals with actual string
     */
    public static void equals(String expected, String actual) {
        equals(expected, actual, "[Assertion failed] - expected string not equals with actual string");
    }
    
    public static void throwHere(String code, String message) {
        throw new BusinessException(code, message);
    }

}
