plugins {
    publish
}

description = "Report problems to Discord"

dependencies {
    implementation(fuel.self)
    implementation(fuel.coroutines)
}
