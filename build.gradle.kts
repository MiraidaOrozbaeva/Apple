plugins {
    id("java")
    id("io.qameta.allure") version "2.12.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

// Source: https://mvnrepository.com/artifact/io.github.bonigarcia/webdrivermanager
    implementation("io.github.bonigarcia:webdrivermanager:6.3.3")

// Source: https://mvnrepository.com/artifact/com.codeborne/selenide
    implementation("com.codeborne:selenide:7.14.0")

    // Source: https://mvnrepository.com/artifact/org.projectlombok/lombok
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

// Source: https://mvnrepository.com/artifact/com.github.javafaker/javafaker
    implementation("com.github.javafaker:javafaker:1.0.2")

// Source: https://mvnrepository.com/artifact/org.aeonbits.owner/owner-java8
    implementation("org.aeonbits.owner:owner-java8:1.0.12")

// Source: https://mvnrepository.com/artifact/io.rest-assured/rest-assured
    implementation("io.rest-assured:rest-assured:6.0.0")

// Source: https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
    implementation("org.apache.logging.log4j:log4j-core:2.25.3")
    implementation("org.apache.logging.log4j:log4j-api:2.25.3")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.25.3")

// Source: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.21.2")

// Source: https://mvnrepository.com/artifact/org.assertj/assertj-core
    testImplementation("org.assertj:assertj-core:3.27.7")

// Source: https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.7.10")

// Source: https://mvnrepository.com/artifact/commons-dbutils/commons-dbutils
    implementation("commons-dbutils:commons-dbutils:1.8.1")

    // Source: https://mvnrepository.com/artifact/io.qameta.allure/allure-junit5
    testImplementation("io.qameta.allure:allure-junit5:2.33.0")
    // Source: https://mvnrepository.com/artifact/io.qameta.allure/allure-java-commons
    implementation("io.qameta.allure:allure-java-commons:2.33.0")

// Source: https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java
    implementation("org.seleniumhq.selenium:selenium-java:4.41.0")

    // Source: https://mvnrepository.com/artifact/org.aspectj/aspectjweaver
    runtimeOnly("org.aspectj:aspectjweaver:1.9.25.1")
    // Source: https://mvnrepository.com/artifact/org.aspectj/aspectjtools
    implementation("org.aspectj:aspectjtools:1.9.25.1")

    // Source: https://mvnrepository.com/artifact/net.datafaker/datafaker
//    implementation("net.datafaker:datafaker:2.5.4")

}
tasks.withType<Test> {
    useJUnitPlatform()
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    outputs.upToDateWhen { false }
    jvmArgs("-javaagent:${configurations.runtimeClasspath.get()
        .find { it.name.contains("aspectjweaver") }}")  // ← добавить это
}

tasks.register<Test>("allTests") {
    useJUnitPlatform {
        includeTags("UI | API | SMOKE | REGRESSION | E2E")
    }
}

tasks.register<Test>("uiTests") {
    useJUnitPlatform {
        includeTags("UI")
    }
}

tasks.register<Test>("apiTests") {
    useJUnitPlatform {
        includeTags("API")
    }
}

tasks.register<Test>("smokeTests") {
    useJUnitPlatform {
        includeTags("SMOKE")
    }
}

tasks.register<Test>("regressionTests") {
    useJUnitPlatform {
        includeTags("REGRESSION")
    }
}

tasks.register<Test>("e2eTests") {
    useJUnitPlatform {
        includeTags("E2E")
    }
}