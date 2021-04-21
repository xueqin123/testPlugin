package com.xue.plugins.manager.ann;

public class AnnotationManager {
    private static final String MY_ANNOTATION = "com.example.annotion.MyAnnotation";

    private static class SingleHolder {
        private static final AnnotationManager annotationManager = new AnnotationManager();
    }

    public static AnnotationManager getInstance() {
        return SingleHolder.annotationManager;
    }

    public void initAnnotation() {

    }
}
