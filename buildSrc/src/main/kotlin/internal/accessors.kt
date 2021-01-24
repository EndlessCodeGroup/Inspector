package internal

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

internal fun Project.java(configure: JavaPluginExtension.() -> Unit) {
    extensions.configure(configure)
}

internal fun Project.kotlin(configure: KotlinProjectExtension.() -> Unit) {
    extensions.configure(configure)
}
