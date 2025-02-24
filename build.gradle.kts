plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.ai"
version = "1.0-SNAPSHOT"

repositories {
//    mavenCentral()
    maven {
        //阿里云Maven仓库地址
        url = uri("https://maven.aliyun.com/repository/public")
    }

    maven {
        url = uri("https://repo.spring.io/milestone")
    }

    maven {
        url = uri("https://repo.spring.io/snapshot")
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2024.1.7")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

val jacksonDataformatCborVersion: String by project
val rsyntaxtextareaVersion: String by project
val flexmarkVersion: String by project
val springAiVersion: String by project

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:${jacksonDataformatCborVersion}") // to remove after class detection fix in Spring
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonDataformatCborVersion}") {
//        exclude group: 'org.jetbrains.kotlin', module: 'kotlin-reflect'
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
    }
    implementation("com.fifesoft:rsyntaxtextarea:${rsyntaxtextareaVersion}")
    implementation("com.vladsch.flexmark:flexmark:${flexmarkVersion}")
    implementation("com.vladsch.flexmark:flexmark-ext-tables:${flexmarkVersion}")
    implementation("com.vladsch.flexmark:flexmark-html2md-converter:${flexmarkVersion}")

    implementation("org.springframework.ai:spring-ai-core:${springAiVersion}")
    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter:${springAiVersion}")
    implementation("org.springframework.ai:spring-ai-azure-openai-spring-boot-starter:${springAiVersion}")
    implementation("org.springframework.ai:spring-ai-anthropic-spring-boot-starter:${springAiVersion}")
    implementation("org.springframework.ai:spring-ai-ollama:${springAiVersion}")
    implementation("org.springframework.ai:spring-ai-qianfan:${springAiVersion}")

    implementation("de.plushnikov.lombok-intellij-plugin:intellij-facade-factory:0.5")
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("241")
        untilBuild.set("243.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
