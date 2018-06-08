package com.tangpj.calces

import org.gradle.api.DefaultTask

/**
 * Created by tang on 2018/6/7.
 */
class TestPluginTest extends DefaultTask{
    TestPluginTest(){
        super()
        dependsOn "lint"
    }
}
