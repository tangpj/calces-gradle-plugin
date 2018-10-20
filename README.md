<p align-"center">

<img src="https://raw.githubusercontent.com/DobbyTang/MarkdownRes/master/github/calces_logo.jpg" alt="Material Render Phone">

</p>

<h1 align="center"><a href="http://tangpj.com/" target="_blank">Calces</a></h1>

> 🍭wow Android辅助构建工具，机械性工作交给我来处理，把时间用在更有价值的工作上。

<p align="center">

<img alt="Version" src="https://img.shields.io/badge/version-1.2.42-brightgreen.svg"/>
<a href="https://plugins.gradle.org/plugin/calces.appconfig"><img alt="AppConfig" src="https://img.shields.io/badge/plugin-appConfig-blue.svg"/></a>
<a href="https://plugins.gradle.org/plugin/calces.modules"><img alt="Modules" src="https://img.shields.io/badge/plugin-modules-yellowgreen.svg"/></a>
<a href="https://plugins.gradle.org/plugin/calces.screen"><img alt="Modules" src="https://img.shields.io/badge/plugin-screen-%23B2EBF2.svg"/></a>
<a href="http://tangpj.com"><img alt="Author" src="https://img.shields.io/badge/author-Tangpj-ff69b4.svg"/></a>
<a href="http://groovy-lang.org/"><img alt="Gradle" src="https://img.shields.io/badge/groovy-2.4.12-orange.svg"/></a>
<a href="https://developer.android.com/studio/releases/gradle-plugin"><img alt="Gradle" src="https://img.shields.io/badge/build%3Agradle-3.2.1-green.svg"/></a>
</p>



## 目录

- [介绍](#介绍)
- [教程地址](#教程地址)
- [功能介绍](#功能介绍)
- [快速开始](#快速开始)
- [演示](#演示)
- [属性介绍](#属性介绍)
- [UPDATE](#UPDATE)
- [TODO](#TODO)
- [License](#License)



## 介绍
Android构建工具包，这个工具的主旨是使用脚本自动处理机械化的重复劳动，让开发者专注更有价值的工作。
目前提供自动组件化控制与屏幕适配插件。

- 组件化构建Gradle插件
提供Gradle配置App依赖的组件、配置组件是否能单独运行、实现多个依赖不同模块的App同时构建等。

- 屏幕适配插件
根据设计稿与配置，自动生成smallestWidth适配文件，让你从此不需要再关注屏幕适配。

## 教程地址
- 组件化插件
[Gradle自动实现Android组件化模块构建](https://tangpj.com/2018/07/22/calces-componentization/)

- 品目适配插件
[给你一个全自动的屏幕适配方案（基于SW方案）！—— 解放你和UI的双手](https://tangpj.com/2018/09/29/calces-screen/)

## 功能介绍

### 自动组件化构建插件
- 根据Gradle配置，自动实现Android组件的构建模式（application or library）
- 可配置多个依赖不同组件的App
- 自动要依赖需要的组件
- 自动配置组件独立运行（被依赖）时的AndroidManifest文件格式，即独立运行时自动配置启动Activity

### 屏幕适配插件
- 根据Gradle配置，自动生成你需要的smallestWidth适配文件


## 快速开始

### 自动组件化构建插件
1. 引入依赖库
   在Gradle 2.1及更高版本的插件构建脚本代码：
   在项目的build.gradle中

   ```
   buildscript {
       ...
   }
   plugins {
     id "calces.appconfig" version "1.2.42"
   }
   ```

   
   在较旧版本的Gradle中或需要动态配置的情况下的插件构建脚本代码：

   ```
      buildscript {
        repositories {
          maven {
            url "https://plugins.gradle.org/m2/"
          }
        }
        dependencies {
          classpath "gradle.plugin.com.tangpj.tools:calces:1.2.42"
        }
      }
      apply plugin: "calces.appConfig"
   ```


2. 在项目build.gradle配置AppConfig

   ```groovy
   appConfig {
       debugEnable true
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

   

3. 在modules（子模块）引入模块自动化构建插件 (注意：不需要手动配置com.android.library或com.android.application)

   ```groovy
   apply plugin: 'calces.modules'
   ```


### 屏幕适配插件

1. 引入依赖库
   在Gradle 2.1及更高版本的插件构建脚本代码：
   在项目的build.gradle中

   ```
   buildscript {
       ...
   }
   plugins {
     id "calces.screen" version "1.2.42"
   }
   apply plugin: "calces.screen"
   ```

   
   在较旧版本的Gradle中或需要动态配置的情况下的插件构建脚本代码：

   ```
      buildscript {
        repositories {
          maven {
            url "https://plugins.gradle.org/m2/"
          }
        }
        dependencies {
          classpath "gradle.plugin.com.tangpj.tools:calces:1.2.42"
        }
      }
      apply plugin: "calces.screen"
   ```


2. 在需要适配的组建上build.gradle配置Screen值
```groovy
screen{
    auto true
    dimens{
        designPx 375
        smallesWidths 360,480
    }
}
```

auto 是用来确认是否需要自动生成sw文件的，会影响编译时间，如果values没有更新的话建议设置为false。
除此之外，可以通过手动调用gradle任务来手动生成sw文件。


## 演示

[**Android-advanced-blueprint**](https://github.com/Tangpj/Android-advanced-blueprint) 


该开源项目包含了笔者关于Android进阶系列教程的示例代码，其中TodoCalces是该构建脚本的Demo。



## 属性介绍

### 自动组件化构建插件
**AppConfig**

- debugEnable, 布尔值
  是否开个debug模式，只有当debugEnbale为true时，modules的isRunAlone才能生效。即modoules只能在debug模式中独立启动
  
- apps, String列表
  app列表，控制当debug模式为false时的app数量与基础配置
  
- modules, String列表
  组件列表，配置需要实现自动组件化控制的组件列表
  

**App（apps里面的配置项）**

- name, String类型
  app的名字，需要和项目路径对应，如果不填写默认为该配置的名字（如配置名为app的话，name则为:name）。倒入规则和setting.gradle中的include规则保持一致
- modules, String列表
  需要依赖的组件列表，通过修改该属性实现依赖不同的组件
- dependMethod, String类型
  依赖的方法，默认为implementation，一般不需要配置该字段，除非有特殊需求
- applicationId, String类型
  动态填入applicationId。非特殊情况，建议为空
- applicationName, String类型
  配置启动Application（对应manifest中的application name属性）
- mainActivity, String类型
  配置启动Activity，为空则默认为AndroidManifest中的Activity。非特殊情况，建议为空


**Modules（modules里的配置项）**

- name, String类型
  与app中的name一致
- isRunAlone, 布尔值
  该组件能否独立启动
- applicationId, String类型
  独立启动时的applicationId

- applicationName, String类型
  配置启动Application（对应manifest中的application name属性）
- mainActivity, String类型
  独立启动的Activity
  
### 屏幕适配插件
**dimens( 自动化Sw屏幕适配方案配置 )**

- dimensFileName, String类型
  待转换dimensions文件名，默认为dimens(/res/values/dimens.xml)
- designPx, int类型
  设计稿width的px值，默认为375。需要根据设计稿的实际width值来确定
- scale, int类型
  取整方式，采用的事BigDecimal的Sacle值
- auto, 布尔值
  是否自动启动转换任务，设置为false可以避免每次构建时自动检查是否转换
  如果设置为false需要手动启动任务，任务名称为dimensCovert
  启动方法：./gradlew dimensCovert
- smallestWidths, int列表
  需要适配的sw值

**mipmap( 图片资源自动适配指定密度 )**
- folder, String类型
  待转换资源文件夹的文件夹名字，默认为mipmap，如果图片资源保存在drawable-xxx文件夹下，则设置为drawable
- designDensity, String
  待转换资源文件夹的密度，如xxxhdpi，会与folder组合成真正的文件夹名字。designDensity为xxxdpi, folder为mipmap时则待转换资源文件保存在mipmap-xxxhdpi路径下
- convertDensity, String列表
  需要转换的密度，只支持高密度图片转换为低密度图片。如待转换的图片密度为xxhdpi，则可以转换为xhdpi密度的图片，但是不能转换为xxxhdpi密度的图片
- auto, 布尔值
  作用与dimens的auto一样

## UPDATE

### Version 1.1.0

- 增加applicationName属性，用于配置启动application
- 不再直接对AndroidManifest进行修改，改成根据配置动态生成新的AndroidManifest的模式，生成目录：/app/calces
  建议在使用版本管理的时候忽略对calces文件夹的跟踪。
  
### Version 1.2.0 
- 增加ScreenPlugin屏幕适配插件
- 可指定设计稿尺寸width，根据设计稿width值生成对应适配文件
- 屏幕适配方案为smallestWidth方案

### Version 1.2.3 
- ScreenPlugin支持位图缩放功能
- 可以根据配置，实现高分辨率位图自动缩放为低分辨率位图

### Version 1.2.31 
- 修复ModulesPlugin无法编译Manifest BUG

### Version 1.2.42
- screen插件提供转换文件路径控制。
  dimens任务提供配置dimensFileName字段( 控制待转换dimensions文件的名称)
  mipmap任务提供配置folder字段( 控制图片资源保存路径，目的时支持采用drawable保存图片资源的项目)
- 更新com.android.tools.build:gradle插件版本到3.2.1


## TODO

- 位图自动压缩，减少安装包体积
- 组件版本管理支持
- 暂时还没想到...
  

## License

```
Copyright 2018 Tang.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```



