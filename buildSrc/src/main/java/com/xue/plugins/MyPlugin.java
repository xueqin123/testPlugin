package com.xue.plugins;

import com.android.build.gradle.AppExtension;
import com.xue.plugins.log.LogUtil;
import com.xue.plugins.transform.MyTransform;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

public class MyPlugin implements Plugin<Project> {
    private static final String TAG = "MyPlugin";
    private Project project;
    public static Logger logger;

    @Override
    public void apply(Project target) {
        this.project = target;
        logger = project.getLogger();
        LogUtil.i(TAG, "apply start");
        AppExtension android = project.getExtensions().getByType(AppExtension.class);
        android.registerTransform(new MyTransform());
        project.beforeEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                LogUtil.i(TAG, "beforeEvaluate()");
            }
        });
        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                LogUtil.i(TAG, "afterEvaluate()");
            }
        });

    }
}
