// Root project build config
plugins {
    `base`
    `bintray-credentials`
    id("com.github.ben-manes.versions") version "0.36.0"
}

description = "Catch and report all exceptions"

// Common configurations for all subprojects
subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "commons")

    group = "ru.endlesscode.inspector"
    description = rootProject.description
    base.archivesBaseName = name

    version = "0.10.1"
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
