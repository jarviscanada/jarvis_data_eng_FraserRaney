import dlt
from pyspark.sql.functions import col, to_date

@dlt.table(
    name="silver_stock_prices",
    comment="Cleaned and typed stock data"
)
def silver_stock_prices():
    df = dlt.read("bronze_stock_prices")

    return (
        df
        .withColumn("date", to_date(col("date")))
        .withColumn("open", col("open").cast("double"))
        .withColumn("high", col("high").cast("double"))
        .withColumn("low", col("low").cast("double"))
        .withColumn("close", col("close").cast("double"))
        .withColumn("volume", col("volume").cast("long"))
        .dropna()
    )