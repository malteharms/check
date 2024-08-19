// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.kotlin.serialization)
        classpath(libs.kotlin.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlin) apply false
    alias(libs.plugins.kotlinSerialization) apply false

    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
    alias(libs.plugins.androidLibrary) apply false
}
