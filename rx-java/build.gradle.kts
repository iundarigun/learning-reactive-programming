plugins {
	kotlin("jvm") version "1.4.31"
}

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//	implementation("io.reactivex.rxjava3:rxjava:3.0.11")
	implementation("io.reactivex.rxjava2:rxjava:2.2.21")
	implementation("ch.qos.logback:logback-classic:1.2.3")
}
