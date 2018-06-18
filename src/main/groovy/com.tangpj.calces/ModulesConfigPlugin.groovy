package com.tangpj.calces

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.tangpj.calces.extensions.AppExtension
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by tang on 2018/6/16.
 */
class ModulesConfigPlugin implements Plugin<Project> {

    private static final String PARENT_EXTENSION_NAME = "appConfig"

    @Override
    void apply(Project project) {
        NamedDomainObjectContainer<AppExtension> appExtensionList =
                project.parent.extensions[PARENT_EXTENSION_NAME] as NamedDomainObjectContainer<AppExtension>
        configModules(project,appExtensionList)
    }

    static void configModules(Project project, NamedDomainObjectContainer<AppExtension> appExtensionList){
        boolean isApp = false
        appExtensionList.forEach{
            if (it.name == project.name){
                isApp = true
                project.plugins.apply(AppPlugin)
            }
        }

        if (!isApp){
            project.plugins.apply(LibraryPlugin)
        }

    }
}
