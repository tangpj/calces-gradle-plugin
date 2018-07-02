package com.tangpj.calces

import com.tangpj.calces.extensions.AppConfigExt
import com.tangpj.calces.extensions.AppExt
import com.tangpj.calces.extensions.LibraryExt
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by tang on 2018/6/12.
 */
class AppConfigPlugin implements Plugin<Project> {

    private static final String EXTENSION_NAME = "appConfig"

    @Override
    void apply(Project project) {
        AppConfigExt appConfigExt = new AppConfigExt(project)
        project.extensions.add(EXTENSION_NAME, appConfigExt)
        configApp(project)
    }

    void configApp(Project project) {
        List<String> moduleList = new ArrayList<>()
        NamedDomainObjectContainer<AppExt> appList
        AppConfigExt appConfigExt
        project.afterEvaluate {
            appConfigExt = project.extensions.getByName(EXTENSION_NAME) as AppConfigExt
            appList = appConfigExt.apps
            checkRepeat(appConfigExt)
            checkModules(appConfigExt,moduleList)

        }
        initChildModules(moduleList, project)
        println("project child modules: $moduleList")
    }

    void initChildModules(List<String> moduleList ,Project project){

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

    static void checkRepeat(AppConfigExt appConfigExt){
        Map<String,List<AppExt>> appGroupMap =
                appConfigExt.apps.groupBy{ it.name.startsWith(':') ? it.name : new String(":" + it.name)}

        appGroupMap.forEach{
            k,v ->
                if (v.size() > 1){
                    throw new IllegalArgumentException("app is repeat. app name: [$k]")
                }
        }

        Map<String,List<LibraryExt>> moduleGroupMap =
                appConfigExt.modules.groupBy{ it.name.startsWith(':') ? it.name : new String(":" + it.name)}

        moduleGroupMap.forEach{
            k,v ->
                if (v.size() > 1){
                    throw new IllegalArgumentException("modules is repeat. modules name: [$k]")
                }
        }

    }

    static void checkModules(AppConfigExt appConfigExt,
                             List<String> projectModules){
        Set<String> configSet = new HashSet<>()
        Set<String> modulesSet = new HashSet<>()
        if (projectModules != null){
            modulesSet.addAll(projectModules)
        }
        List<String> notFoundList = new ArrayList<>()

        List<String> appNameList = appConfigExt.apps
                .stream()
                .map{it.name.startsWith(':') ? it.name : new String(":" + it.name)}.collect()

        List<String> moduleNameList =
                appConfigExt.modules.
                        stream().
                        map{
                            String name = it.name.startsWith(':') ? it.name : new String(":" + it.name)
                            if (appNameList.contains(name)){
                                throw new IllegalArgumentException("$it.name already configured " +
                                        "as an application, please check appConfig")
                            }
                            name
                        }.
                        collect()

        println "moduleNameList = $moduleNameList"

        configSet.addAll(appNameList)
        configSet.addAll(moduleNameList)

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

        appConfigExt.apps.stream().forEach{ app ->
            app.modules.stream().forEach{
                if (! configSet.contains(it)){
                    throw  new IllegalArgumentException(
                            "appConfig error , can not find $app.name modules $it by project" )
                }
            }
        }

        println("modules: " + configSet)
    }



}
