package com.mb.lab.banks.utils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class AnnotatedClassScanner {

    private static final String RESOURCE_PATTERN = "/**/*.class";

    private final String[] packagesToScan;

    private final TypeFilter[] typeFilters;

    private final ResourcePatternResolver resourcePatternResolver;

    public AnnotatedClassScanner(ClassLoader classLoader, String[] basePackages, Class<? extends Annotation>[] anotationClasses) {
        Assert.notEmpty(basePackages, "'basePackages' must not be empty");
        Assert.notEmpty(anotationClasses, "'anotationClasses' must not be empty");
        this.resourcePatternResolver = new PathMatchingResourcePatternResolver(classLoader);
        this.packagesToScan = basePackages;
        this.typeFilters = getTypeFilters(anotationClasses);
    }

    public AnnotatedClassScanner(ClassLoader classLoader, Class<?>[] basePackageClasses, Class<? extends Annotation>[] anotationClasses) {
        Assert.notEmpty(basePackageClasses, "'basePackageClasses' must not be empty");
        Assert.notEmpty(anotationClasses, "'anotationClasses' must not be empty");
        this.resourcePatternResolver = new PathMatchingResourcePatternResolver(classLoader);
        this.packagesToScan = getPackagesToScan(basePackageClasses);
        this.typeFilters = getTypeFilters(anotationClasses);
    }

    /**
     * Scan the packages for classes marked with provided annotations.
     */
    public Set<AnnotatedClassMetadata> scanPackages() throws IllegalArgumentException {
        try {
            Set<AnnotatedClassMetadata> candidates = new LinkedHashSet<AnnotatedClassMetadata>();
            for (String packageToScan : this.packagesToScan) {
                String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(packageToScan) + RESOURCE_PATTERN;
                Resource[] resources = this.resourcePatternResolver.getResources(pattern);
                MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
                for (Resource resource : resources) {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    if (isMatchedClass(metadataReader, metadataReaderFactory)) {
                        String className = metadataReader.getClassMetadata().getClassName();
                        Class<?> matchedClass = this.resourcePatternResolver.getClassLoader().loadClass(className);
                        candidates.add(new AnnotatedClassMetadata(matchedClass, metadataReader));
                    }
                }
            }
            return candidates;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to scan classpath for unlisted classes", ex);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Failed to load annotated classes from classpath", ex);
        }
    }

    protected boolean isMatchedClass(MetadataReader reader, MetadataReaderFactory factory) throws IOException {
        for (TypeFilter filter : typeFilters) {
            if (filter.match(reader, factory) && !reader.getClassMetadata().isInterface()) {
                return true;
            }
        }
        return false;
    }

    /**
     * The classes whose packages should be scanned for annotations.
     */
    private String[] getPackagesToScan(Class<?>[] basePackageClasses) {
        Set<String> packages = new HashSet<String>();
        for (Class<?> type : basePackageClasses) {
            packages.add(ClassUtils.getPackageName(type));
        }
        return packages.toArray(new String[0]);
    }

    private TypeFilter[] getTypeFilters(Class<? extends Annotation>[] anotationClasses) {
        List<TypeFilter> filters = new ArrayList<TypeFilter>(anotationClasses.length);
        for (Class<? extends Annotation> type : anotationClasses) {
            filters.add(new AnnotationTypeFilter(type, false));
        }
        return filters.toArray(new TypeFilter[anotationClasses.length]);
    }

}
