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

    val hadoopConf1 = SparkHadoopUtil.get.newConfiguration();

    // Tell HAWQInputFormat where the HAWQ master is, which user to use, the schema, and the table
    HAWQInputFormat.setInput(hadoopConf1, "sandbox.hortonworks.com:10432/postgres", "gpadmin", "public", "test")

    // Create an RDD from the HAWQ table -- _._2 refers to the value (classOf[HAWQRecord]) member of the RDD
    val rdd1 = sc.newAPIHadoopRDD(hadoopConf1, classOf[HAWQInputFormat], classOf[Void], classOf[HAWQRecord]).map(_._2);

    // Cast the result set to a string and print each item
    val employees = rdd1.map(p => (p.getString(1))).collect().foreach(println);
  }
}
