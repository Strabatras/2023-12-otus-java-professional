import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id ("com.github.johnrengelman.shadow")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation ("com.google.guava:guava")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("hw01-gradle")
        archiveVersion.set("0.1")
        archiveClassifier.set("")
        manifest {
            attributes(mapOf("Main-Class" to "ru.otus.Main"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
