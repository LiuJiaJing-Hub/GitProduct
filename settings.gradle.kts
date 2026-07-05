pluginManagement {
    repositories {
        mavenLocal()
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/spring")
        maven("https://maven.aliyun.com/repository/spring-plugin")
        maven("https://maven.aliyun.com/repository/apache-snapshots")
        maven("https://maven.aliyun.com/repository/grails-core")
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
        maven("https://developer.huawei.com/repo/")
        maven("https://jitpack.io")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/spring")
        maven("https://maven.aliyun.com/repository/spring-plugin")
        maven("https://maven.aliyun.com/repository/apache-snapshots")
        maven("https://maven.aliyun.com/repository/grails-core")
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
        maven("https://developer.huawei.com/repo/")
        maven("https://jitpack.io")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        google()
        mavenCentral()
    }
}

rootProject.name = "app-video-user"
include(":app")
 
