package tn.insat.tp21;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.Arrays;

import static jersey.repackaged.com.google.common.base.Preconditions.checkArgument;
public class WordCountTask {
      private static final Logger LOGGER = LoggerFactory.getLogger(WordCountTask.class);

      public static void main(String[] args) {
          checkArgument(args.length > 1, "Please provide the path of input file and output dir as parameters.");
          new WordCountTask().run(args[0], args[1]);
      }

      public void run(String inputFilePath, String outputDir) {
          String master = "local[*]";
          /*SparkConf conf = new SparkConf()
                  .setAppName(WordCountTask.class.getName())
                  .setMaster(master);*/
          SparkConf conf = new SparkConf()
                  .setAppName(WordCountTask.class.getName());
          
          JavaSparkContext sc = new JavaSparkContext(conf);

          JavaRDD<String> textFile = sc.textFile(inputFilePath);
          JavaPairRDD<String, Integer> counts = textFile
                  //.flatMap(s -> Arrays.asList(s.split(" ")).iterator())
        		  .flatMap(s -> Arrays.asList(s.split("\t")).iterator())
                  .mapToPair(word -> new Tuple2<>(word, 1))
                  .reduceByKey((a, b) -> a + b);
          counts.saveAsTextFile(outputDir);
      }
  }

