package com.tangpj.calces.extensions

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

/**
 * Created by tang on 2018/6/29.
 */
class AppExt {
    String name
    String applicationId
    String dependMethod = "implementation"
    List<String> modules = new ArrayList<>()

    AppExt(String name){
        this.name = name
    }

    def name(String name){
        this.name = name
    }

    def applicationId(String applicationId){
        this.applicationId = applicationId
    }

    def dependMethod(String dependMethod){
        this.dependMethod = dependMethod
    }

    def modules(String... modules){
        this.modules.addAll(modules)
    }

    @Override
    String toString() {
        return "app = $name, applicationId = $applicationId, dependMethod = $dependMethod\n" +
                "modules: ${modules.isEmpty()? "is empty" : "$modules"}"
    }
}
