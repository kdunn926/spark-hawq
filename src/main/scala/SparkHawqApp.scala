/* SparkHawqApp.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object SparkHawqApp {
  def main(args: Array[String]) {
    import com.pivotal.hawq.mapreduce.HAWQInputFormat;
    import com.pivotal.hawq.mapreduce.HAWQRecord;
    import com.pivotal.hawq.mapreduce.util.HAWQJdbcUtils;
    import org.apache.spark.deploy.{LocalSparkCluster, SparkHadoopUtil};

    // Set a name for the application
    val conf = new SparkConf().setAppName("Spark HAWQ Application")

    // Create a new SparkContext using the configuration
    val sc = new SparkContext(conf)

    val hadoopConf = SparkHadoopUtil.get.newConfiguration();

    // The HAWQ test table
    //   postgres=# \d+ sparkTest                                                                                                         Append-Only Table "public.sparktest"
    //    Column  |  Type   | Modifiers | Storage  | Description
    //   ---------+---------+-----------+----------+-------------
    //    myid    | integer |           | plain    |
    //    myvalue | text    |           | extended |
    //   Compression Type: None
    //   Compression Level: 0
    //   Block Size: 32768
    //   Checksum: f
    //   Has OIDs: no
    //   Options: appendonly=true
    //   Distributed randomly

    // Tell HAWQInputFormat where the HAWQ master is, which user to use, the schema, and the table
    HAWQInputFormat.setInput(hadoopConf, "sandbox.hortonworks.com:10432/postgres", "gpadmin", "public", "sparktest")

    // Create an RDD from the HAWQ table -- _._2 refers to the value (classOf[HAWQRecord]) member of the RDD
    val rdd = sc.newAPIHadoopRDD(hadoopConf, classOf[HAWQInputFormat], classOf[Void], classOf[HAWQRecord]).map(_._2);

    // Cast the result set (myId -> int, myValue -> string) and print each tuple
    rdd.map(row => (row.getInt(1), row.getString(2))).collect().foreach(println);
  }
}
