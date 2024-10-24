plugins {
    java
    id("com.diffplug.spotless") version "6.25.0"
    id("org.sonarqube") version "5.1.0.4882"
}

group = 'uk.gov.di'
version = '1.0-SNAPSHOT'

sonar {
    properties {
        property("sonar.projectKey", "govuk-one-login_authentication-acceptance-tests")
        property("sonar.organization", "govuk-one-login")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

spotless {
    if (project.hasProperty("ratchetFrom")) ratchetFrom(project.property("ratchetFrom") as String?)
    String ref = project.properties["ratchetFrom"]
    if (ref != null) {
        ref = ref.trim()
        if (ref.length() > 0) {
            ratchetFrom ref
        }
    }
    java {
        target '**/*.java'
        googleJavaFormat('1.11.0').aosp()
        importOrder '', 'javax', 'java', '\\#'
    }
    groovyGradle {
        target "**/*.gradle"
        greclipse().configFile("tools/spotless-gradle.properties")
    }
}

repositories {
    mavenCentral()
}

def dependencyVersions = [
        junit_version: '5.11.3',
        cucumber_version: '7.20.1',
        axe_version: '3.0',
        selenium_java_version: '4.25.0',
        aws_sdk_v2_version: "2.28.29",
        json_version: '20240303',
        rest_assured: '5.5.0'
]

dependencies {
    implementation("org.apache.maven.plugins:maven-surefire-plugin:3.2.5")

    testImplementation(platform("software.amazon.awssdk:bom:${dependencyVersions.aws_sdk_v2_version}"))
    testImplementation(platform("io.cucumber:cucumber-bom:${dependencyVersions.cucumber_version}"))
    testImplementation(platform("org.seleniumhq.selenium:selenium-dependencies-bom:${dependencyVersions.selenium_java_version}"))

    testImplementation("software.amazon.awssdk:sso")
    testImplementation("software.amazon.awssdk:ssooidc")
    testImplementation("software.amazon.awssdk:dynamodb")
    testImplementation("software.amazon.awssdk:dynamodb-enhanced")
    testImplementation("software.amazon.awssdk:ssm")

    testImplementation("io.cucumber:cucumber-java")
    testImplementation("io.cucumber:cucumber-junit")
    testImplementation("io.cucumber:cucumber-picocontainer")
    testImplementation("io.cucumber:cucumber-junit-platform-engine")

    testImplementation("org.seleniumhq.selenium:selenium-java")
    testImplementation("com.deque:axe-selenium:${dependencyVersions.axe_version}")

    testImplementation(platform("org.junit:junit-bom:${dependencyVersions.junit_version}"))
    testImplementation("org.junit.platform:junit-platform-suite")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("org.apache.commons:commons-text:1.12.0")

    testImplementation("commons-codec:commons-codec:1.17.1")

    testImplementation("com.google.guava:guava:33.3.1-jre")

    testImplementation("org.bouncycastle:bcpkix-jdk18on:1.78.1")
    testImplementation("org.springframework.security:spring-security-crypto:6.3.4")

    testImplementation("org.json:json:${dependencyVersions.json_version}")
    testImplementation("io.rest-assured:rest-assured:${dependencyVersions.rest_assured}")
}

group = 'acceptance-tests'
version = '1.0.0'
description = 'acceptance-tests'

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

configurations {
    cucumberRuntime {
        extendsFrom testImplementation
    }
}

publishing {
    publications {
        maven(MavenPublication) {
            from(components.java)
        }
    }
}

tasks.withType(JavaCompile) {
    options.encoding = 'UTF-8'
}

//task cucumber() {
//    dependsOn assemble, testClasses
//    doLast {
//        javaexec {
//            main = "io.cucumber.core.cli.Main"
//            classpath = configurations.cucumberRuntime + sourceSets.main.output + sourceSets.test.output
//            def no_of_threads = System.getenv('PARALLEL_BROWSERS') ?: 1
//            args = [
//                    '--plugin',
//                    'html:target/cucumber-report/index.html',
//                    '--plugin',
//                    'pretty',
//                    '--glue',
//                    'uk.gov.di.test.step_definitions',
//                    'src/test/resources',
//                    '--threads',
//                    no_of_threads
//            ]
//        }
//    }
//}

tasks {
    test {
        useJUnitPlatform(
            excludeTags("disabled")
            if (project.hasProperty("includeTags")) includeTags(project.property("includeTags") as String?)
        )
        systemProperty("cucumber.junit-platform.naming-strategy", "long")
    }
}
