name := "jvisor"

version := "0.1"

scalaVersion := "2.13.7"

resolvers += "Central Repository" at "https://repo1.maven.org/maven2/"

libraryDependencies += "org.yaml" % "snakeyaml" % "1.8"
libraryDependencies += "net.jcazevedo" %% "moultingyaml" % "0.4.2"
libraryDependencies += "commons-io" % "commons-io" % "2.6"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-s3" % "1.12.131"
libraryDependencies += "org.gridkit.jvmtool" % "hprof-heap" % "0.10.1"
libraryDependencies += "org.gridkit.jvmtool" % "sjk-core" % "0.20"