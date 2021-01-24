import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

internal fun Project.java(configure: JavaPluginExtension.() -> Unit) {
    extensions.configure(configure)
}
