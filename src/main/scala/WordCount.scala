import com.typesafe.config.ConfigFactory
import org.apache.spark.{SparkConf, SparkContext}
/*
Simple word count example running on mesos
 */

object WordCount extends App{
  val config = ConfigFactory.load()
  val filePath = config.getString("file_path")
  val sparkMaster = config.getString("SPARK_MASTER")
  val sparkAppName = config.getString("SPARK_APPNAME")

  val conf = new SparkConf().setMaster(sparkMaster).setAppName(sparkAppName)
  val sc = new SparkContext(conf)
  sc.setLogLevel("ERROR")

  val lines = sc.textFile(filePath)
  val words = lines.flatMap(line => line.split(" "))
  val counts = words.map(word => (word, 1)).reduceByKey { case (x, y) => x + y }

  println("foreach over collect will output in same order of textfile")
  counts.collect().foreach(println)

  sc.stop()
}
