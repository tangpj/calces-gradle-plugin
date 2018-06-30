package com.tangpj.calces

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.tangpj.calces.extensions.AppConfigExt
import com.tangpj.calces.extensions.AppExt
import com.tangpj.calces.extensions.ModuleExt
import com.tangpj.calces.utils.AppManifestStrategy
import com.tangpj.calces.utils.LibraryManifestStrategy
import com.tangpj.calces.utils.ManifestStrategy
import groovy.util.slurpersupport.GPathResult
import groovy.util.slurpersupport.NodeChild
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException

/**
 * Created by tang on 2018/6/16.
 */
class ModulesConfigPlugin implements Plugin<Project> {

    private static final String PARENT_EXTENSION_NAME = "appConfig"

    @Override
    void apply(Project project) {
        AppConfigExt appConfigExt = getAppConfigExtension(project)
        configModules(project, appConfigExt)
    }

    static void configModules(Project project, AppConfigExt appConfigExt){
        if (appConfigExt == null){
            throw new NullPointerException("can not find appConfig")
        }
        List<AppExt> filterList = appConfigExt.apps.stream()
                .filter{ (it.name.startsWith(':') ? it.name : new String(":" + it.name)).endsWith(project.name) }
                .skip(0).collect()

        if (filterList != null && filterList.size() > 0){
            AppExt appExt = filterList.get(0)
            AppPlugin appPlugin = project.plugins.apply(AppPlugin)
            appPlugin.extension.defaultConfig.setApplicationId(appExt.applicationId)
            dependModules(project, appExt, appConfigExt)
        }else if (!(appConfigExt.isDebugEnable() && modulesRunAlone(project,appConfigExt.modules))){
            project.plugins.apply(LibraryPlugin)
        }

    }

    static void dependModules(Project project, AppExt appExt, AppConfigExt appConfigExt){
        List<ModuleExt> moduleExtList = appConfigExt.modules.stream().filter{
            modules ->
                String modulesName = appExt.modules.stream().find{ it.contains(modules.name) }
                modulesName != null && !modulesName.isEmpty()
        }.skip(0).collect()

        if (appExt.modules != null && appExt.modules.size() > 0){
            List<String> modulesList = appExt.modules.stream()
                    .filter{appConfigExt.debugEnable ? (moduleExtList != null && !moduleExtList.get(0).isRunAlone) : true }
                    .map{
                         project.dependencies.add(appExt.dependMethod, project.project(it))
                         it
            }.collect()
            println("build app: [$appExt.name] , depend modules: $modulesList")
        }
    }

    AppConfigExt getAppConfigExtension(Project project){
        try{
            return project.parent.extensions.getByName(PARENT_EXTENSION_NAME) as AppConfigExt
        }catch (UnknownDomainObjectException ignored){
            if (project.parent != null){
                getAppConfigExtension(project.parent)
            }else {
                throw new UnknownDomainObjectException(ignored as String)
            }
        }
    }

    private static boolean modulesRunAlone(Project project, NamedDomainObjectContainer<ModuleExt> modules){
        List<ModuleExt> filterList = modules.stream().filter{ it.name.endsWith(project.name) }.skip(0).collect()
        if (filterList != null && filterList.size() > 0){
            ModuleExt moduleExt = filterList.get(0)
            def path = "${project.getBuildFile().getParent()}/src/main/AndroidManifest.xml"
            File manifestFile = new File(path)
            if (!manifestFile.getParentFile().exists() && !manifestFile.getParentFile().mkdirs()){
                println "Unable to find AndroidManifest and create fail, please manually create"
            }

            GPathResult manifest = new XmlSlurper().parse(manifestFile)
            if (moduleExt.isRunAlone){
                AppPlugin appPlugin = project.plugins.apply(AppPlugin)
                appPlugin.extension.defaultConfig.setApplicationId(moduleExt.runAloneId)

                println("build run alone modules: [$moduleExt.name]")
                if (!moduleExt.runAloneActivity.isEmpty()){
                    new AppManifestStrategy(path).resetManifest(moduleExt, manifest)
                }
            }else{
                new LibraryManifestStrategy(path).resetManifest(moduleExt, manifest)
            }
            return moduleExt.isRunAlone
        }
        return false


    }

}

