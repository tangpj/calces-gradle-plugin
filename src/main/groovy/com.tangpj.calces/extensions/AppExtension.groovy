package com.tangpj.calces.extensions

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Project

/**
 * Created by tang on 2018/6/14.
 */
class AppExtension {
    String name
    String applicationId
    String modules

    AppExtension(String name){
        this.name = name
    }

    def applicationId(String applicationId){
        this.applicationId = applicationId
    }

    def modules(String modules){
        this.modules = modules
    }

    @Override
    String toString() {
        return "$name, applicationId = $applicationId , modules = $modules"
    }
}
