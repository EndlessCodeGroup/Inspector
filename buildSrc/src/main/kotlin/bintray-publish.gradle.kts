import com.jfrog.bintray.gradle.BintrayExtension
import internal.java
import java.util.*

apply(plugin = "maven-publish")
apply(plugin = "com.jfrog.bintray")

java {
    withSourcesJar()
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

configure<BintrayExtension> {
    user = extra["bintrayUser"] as String
    key = extra["bintrayApiKey"] as String
    setPublications("maven")

    pkg(closureOf<BintrayExtension.PackageConfig> {
        repo = "repo"
        name = "inspector"
        userOrg = "endlesscode"
        setLicenses("AGPL-v3")
        publicDownloadNumbers = true
        websiteUrl = "https://gitlab.com/endlesscodegroup/inspector"
        issueTrackerUrl = "https://gitlab.com/endlesscodegroup/inspector/issues"
        vcsUrl = "https://gitlab.com/endlesscodegroup/inspector.git"

        version(closureOf<BintrayExtension.VersionConfig> {
            name = project.version.toString()
            desc = "Inspector v${project.version}"
            released = Date().toString()
            vcsTag = "v${project.version}"
        })
    })
}
