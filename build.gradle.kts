// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id ("org.jetbrains.kotlin.android") version("2.0.0") apply false
    id ("org.jetbrains.kotlin.plugin.compose") version("2.0.0") apply false
    id ("com.google.dagger.hilt.android") version ("2.51") apply false
    id ("com.google.devtools.ksp") version("2.0.0-1.0.21") apply false
    id ("androidx.room") version ("2.6.1") apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

buildscript {
    dependencies {
        classpath ("com.android.tools.build:gradle:8.5.2")
        classpath ("org.jetbrains.kotlin:kotlin-serialization:2.0.0")
    }
    repositories {
        google()
    }
}


