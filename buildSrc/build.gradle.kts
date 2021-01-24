plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("gradle-plugin", version = "1.4.21"))
    implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
}
