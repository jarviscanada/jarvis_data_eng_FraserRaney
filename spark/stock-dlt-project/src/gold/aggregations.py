import dlt
from pyspark.sql.window import Window
from pyspark.sql.functions import col, lag, avg

@dlt.table(name="gold_wide_trends")
def gold_price_trends():

    # PCT change
    window = Window.partitionBy("company").orderBy("date")

    df = dlt.read("silver_stock_prices")

    df_gold = (
        df
        .withColumn("prev_close", lag("close", 1).over(window))
        .withColumn("close_7d", lag("close", 7).over(window))
        .withColumn("close_30d", lag("close", 30).over(window))
        .withColumn("close_90d", lag("close", 90).over(window))

        # Daily change
        .withColumn("daily_change", col("close") - col("prev_close"))
        .withColumn("daily_pct_change", col("daily_change") / col("prev_close"))

        # Rolling changes
        .withColumn("change_7d", col("close") - col("close_7d"))
        .withColumn("change_30d", col("close") - col("close_30d"))
        .withColumn("change_90d", col("close") - col("close_90d"))

        # Percentage
        .withColumn("pct_change_7d", col("change_7d") / col("close_7d"))
        .withColumn("pct_change_30d", col("change_30d") / col("close_30d"))
        .withColumn("pct_change_90d", col("change_90d") / col("close_90d"))
    )

    # Volume
    window_7 = Window.partitionBy("company").orderBy("date").rowsBetween(-7, 0)
    window_30 = Window.partitionBy("company").orderBy("date").rowsBetween(-30, 0)
    window_90 = Window.partitionBy("company").orderBy("date").rowsBetween(-90, 0)

    df_gold = (
        df_gold
        .withColumn("avg_volume_7d", avg("volume").over(window_7))
        .withColumn("avg_volume_30d", avg("volume").over(window_30))
        .withColumn("avg_volume_90d", avg("volume").over(window_90))
    )
    return df_gold