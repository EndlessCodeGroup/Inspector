// Root project build config
plugins {
    base
    id("com.github.ben-manes.versions") version "0.39.0"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.7.0"
}

// Common configurations for all subprojects
subprojects {
    apply(plugin = "commons")

    group = "ru.endlesscode.inspector"
    version = "0.11.0"
}

// Configuration for API implementations
val apiProject = project(":inspector-api")
configure(subprojects.filter { it.name.startsWith(project.name) } - apiProject) {

    // Finish configuring API first
    evaluationDependsOn(apiProject.path)

    // Use api as dependency
    dependencies {
        "api"(apiProject)
    }
}
