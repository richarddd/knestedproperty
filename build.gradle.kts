import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.3.40"
val junitPlatformVersion = "1.5.0"
val spekVersion = "2.0.5"

plugins {
    java
    kotlin("jvm") version "1.3.40"
}

group = "se.davison.knestedpropery"
version = "1.0-SNAPSHOT"



repositories {
    mavenCentral()
    maven {
        url = uri("https://dl.bintray.com/spekframework/spek")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(kotlin("reflect", kotlinVersion))
    testImplementation(kotlin("test", kotlinVersion))

    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion") {
        exclude(group = "org.jetbrains.kotlin")
    }

    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion") {
        exclude(group = "org.jetbrains.kotlin")
        exclude(group = "org.junit.platform")
    }

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:$junitPlatformVersion") {
        because("Needed to run tests IDEs that bundle an older version")
    }
    testImplementation(gradleTestKit())
}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines("spek2")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}