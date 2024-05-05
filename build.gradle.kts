// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.kotlin.serialization)
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlin) apply false

    // dependency injection
    alias(libs.plugins.daggerHilt) apply false

    // kpt for room
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}
