package com.asm.plugin.autotrack;


import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class AutoTrackPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        System.out.println("========================");
        System.out.println("hello AutoTrackPlugin gradle plugin!");
        System.out.println("========================");
        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        appExtension.registerTransform(new InkeAutoTrackTransform());
    }
}
