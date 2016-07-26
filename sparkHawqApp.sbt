import sbt._
import Keys._
import sbtassembly.AssemblyPlugin.autoImport._

name := "SparkHawqApp"

version := "1.0"

scalaVersion := "2.10.5"

resolvers += Resolver.mavenLocal

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.2" % "provided"
libraryDependencies += "com.pivotal.hawq" % "hawq-hadoop" % "1.1.0" 
libraryDependencies += "com.pivotal.hawq" % "hawq-mapreduce-ao" % "1.1.0" 
libraryDependencies += "com.pivotal.hawq" % "hawq-mapreduce-common" % "1.1.0" 
libraryDependencies += "com.pivotal.hawq" % "hawq-mapreduce-parquet" % "1.1.0" 
libraryDependencies += "com.pivotal.hawq" % "hawq-mapreduce-tool" % "1.1.0" 

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
    case "application.conf" => MergeStrategy.concat
    case "unwanted.txt"     => MergeStrategy.discard
    case x if x.contains("converters") => MergeStrategy.first
    case x if x.contains("collections") => MergeStrategy.first
    case x if x.contains("locale") => MergeStrategy.first
    case x if x.contains("beanutils") => MergeStrategy.first
    case x if x.contains("netty") => MergeStrategy.first
    case x if x.contains("jboss") => MergeStrategy.first
    case x => old(x)
  }
}
