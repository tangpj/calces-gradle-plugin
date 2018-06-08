package com.tangpj.calces

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

/**
 * Created by tang on 2018/6/7.
 */

class TestPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {

        //创建一个任务
        project.task('hello') {
            doLast {
                println  'aaa 你好'
            }
        }

    }

    @TaskAction
    def testClean(){
        System.out.println("==================")
        System.out.println("Test Clean Task")
        System.out.println("==================")
    }

}

