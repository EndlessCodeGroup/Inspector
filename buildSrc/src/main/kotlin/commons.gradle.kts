import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

apply(from = rootProject.file("gradle/dependencies.gradle"))

// Java version
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    jcenter()
}

dependencies {
    "implementation"(kotlin("stdlib-jdk8"))
    "implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        apiVersion = "1.3"
        languageVersion = "1.3"
    }
}
