plugins {
    kotlin("jvm")
}
val buildToolsVersion by extra("35.0.0")
val defaultMinSdkVersion by extra(32)
dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(8)
}