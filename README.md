
 \<h1 align="center"\>\<a href="http://tangpj.com" target="_blank"\>Calces\</a\>\</h1\>

<p align="center">

<img alt="Version" src="https://img.shields.io/badge/version-1.0.1--SNAPSHOT-brightgreen.svg"/>
<a href="https://plugins.gradle.org/plugin/calces.appconfig"><img alt="AppConfig" src="https://img.shields.io/badge/plugin-appConfig-blue.svg"/></a>
<a href="https://plugins.gradle.org/plugin/calces.modules"><img alt="Modules" src="https://img.shields.io/badge/plugin-modules-yellowgreen.svg"/></a>
<a href="http://tangpj.com"><img alt="Author" src="https://img.shields.io/badge/author-Tangpj-ff69b4.svg"/></a>
<a href="http://groovy-lang.org/"><img alt="Gradle" src="https://img.shields.io/badge/groovy-2.4.12-orange.svg"/></a>
<a href="https://developer.android.com/studio/releases/gradle-plugin"><img alt="Gradle" src="https://img.shields.io/badge/build%3Agradle-3.1.3-green.svg"/></a>
</p>



## 介绍

Android组件化构建Gradle插件，能够通过Gradle配置App依赖的组件、配置组件是否能单独运行、实现多个依赖不同模块的App同时构建等。

实现功能：

- 根据Gradle配置，自动实现Android组件的构建模式（application or library）
- 自动配置组件独立运行（被依赖）时的AndroidManifest文件格式，即独立运行时自动配置启动Activity
- 配置App需要依赖的Modules，并能够同时构建多个不同依赖的App

## 快速开始

1. 引入依赖库

   ```groovy
   buildscript {
     repositories {
       maven {
         url "https://plugins.gradle.org/m2/"
       }
     }
     dependencies {
       classpath "gradle.plugin.com.tangpj.tools:calces:1.0.1-SNAPSHOT"
     }
   }
    
   ```

   

2. 项目build.gradle配置AppConfig

   ```groovy
   apply plugin: "calces.appconfig"
   
   apps {
           app1 {
               mainActivity "com.xxx.MainActivity1"
               modules ':modules1',
                       ':modules2'
           }
   
             app2 {
               mainActivity "com.xxx.MainActivity2"
               modules ':modules1'
           }
       }
   
       modules {
           modules1 {
               applicationId "com.xxxx.modules1"
               mainActivity ".Modules1Activity"
               isRunAlone true
           }
   
            modules2 {
               applicationId "com.xxxx.modules2"
               mainActivity ".Modules2Activity"
               isRunAlone true
           }
   
   
       }
   }
   ```

   

3. modules配置 (注意：不需要手动配置com.android.library或com.android.application)

   ```groovy
   apply plugin: 'calces.modules'
   ```

   



