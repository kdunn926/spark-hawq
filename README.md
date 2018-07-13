# Apache Spark RDD with Apache HAWQ table
A simple example creating an Apache Spark RDD from an Apache HAWQ table 
using the `HAWQInputFormat` class and the `newAPIHadoopRDD` API.

## Ensure permissions
Note: the job _must_ be submitted by gpadmin or something like `HADOOP_USER_NAME=gpadmin` 
must be used with `spark-submit` AND the following options *added* in Ambari's 
custom core-site.xml section:
```
<property>
  <name>hadoop.proxyuser.gpadmin.groups</name>
  <value>*</value>
</property>

<property>
  <name>hadoop.proxyuser.gpadmin.hosts</name>
  <value>*</value>
</property>
```

## Build HAWQ dependencies
```
$ git clone https://github.com/apache/incubator-hawq.git
$ cd incubator-hawq/contrib/hawq-hadoop/
$ mvn package install -DskipTests
```

## Create a fat JAR using Scala Build Tool Assembly
```
$ sbt assembly
```


## Run with Spark on YARN
```
$ spark-submit --class "SparkHawqApp" target/scala-2.11/SparkHawqApp-assembly-1.0.jar --master yarn
```


