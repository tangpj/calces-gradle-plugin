package com.tangpj.calces

import com.tangpj.calces.extensions.AppExtension
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by tang on 2018/6/12.
 */
class AppConfigPlugin implements Plugin<Project> {

    private static final String EXTENSION_NAME = "appConfig"

    private NamedDomainObjectCollection<AppExtension> appExtensionList

    @Override
    void apply(Project project) {
        NamedDomainObjectCollection<AppExtension> appExtensionList = project.container(AppExtension)
        project.extensions.add(EXTENSION_NAME, appExtensionList)
        configApp(project, appExtensionList)
    }

    static void configApp(Project project, NamedDomainObjectCollection<AppExtension> appExtensionList) {
        List<String> moduleList = new ArrayList<>()
        project.afterEvaluate {
            appExtensionList = project.extensions.getByName(EXTENSION_NAME) as NamedDomainObjectCollection<AppExtension>
            println appExtensionList
            checkoutModules(appExtensionList,moduleList)

        }

        initChildModules(moduleList, project)
        println moduleList
    }

    static void initChildModules(List<String> moduleList ,Project project){

        if (project.childProjects.isEmpty()){
            moduleList.add(project.toString()
                    .replace("project ","")
                    .replace('\'',''))
            return
        }
        project.childProjects.entrySet().forEach{
            initChildModules(moduleList, it.value)
        }

    }

    static void checkoutModules(NamedDomainObjectCollection<AppExtension> appExtensionList,
                                List<String> moduleList){
        Set<String> configSet = new HashSet<>()
        Set<String> modulesSet = new HashSet<>()
        if (moduleList != null){
            modulesSet.addAll(moduleList)
        }
        List<String> notFoundList = new ArrayList<>()
        appExtensionList.forEach{
            if (it.app != null && !it.app.isEmpty()){
                configSet.add(it.app)
            }else{
                configSet.add(":" + it.name)

            }
            if (it.modules != null){
                configSet.addAll(it.modules)
                it.modules.forEach{
                    if (!it.startsWith(":")){
                        throw new IllegalArgumentException(
                                "module name format error ,$it need to start whit ':' ")
                    }
                }
            }

        }
        configSet.forEach{
            if(!modulesSet.contains(it)){
                notFoundList.add(it)
            }
        }
        if (notFoundList.size() > 0){
            throw  new IllegalArgumentException(
                    "not fount modules = " + notFoundList
            )
        }
        println "build list = " + configSet
    }



}
