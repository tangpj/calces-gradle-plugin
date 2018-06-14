package com.tangpj.calces

import com.android.build.gradle.AppPlugin
import com.tangpj.calces.extensions.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by tang on 2018/6/12.
 */
class AppConfigPlugin implements Plugin<Project> {

    private static final String EXTENSION_NAME = "appConfig"

    @Override
    void apply(Project project) {
        def extensions = project.container(AppExtension)
        project.extensions.add(EXTENSION_NAME, extensions)
        configApp(project)
    }

    void configApp(Project project) {
        AppPlugin
        project.afterEvaluate {

            def appConfig = project.extensions.getByName(EXTENSION_NAME)
            println appConfig

        }
    }



}
