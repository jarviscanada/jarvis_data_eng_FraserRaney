import dlt
import requests
import pandas as pd
from pyspark.sql.functions import lit
from functools import reduce
import time

from config.config import API_KEY, BASE_URL, COMPANIES, RATE_LIMIT_SECONDS

def fetch_stock_data(symbol):
    url = BASE_URL
    params = {
        "function": "TIME_SERIES_DAILY",
        "symbol": symbol,
        "apikey": API_KEY
    }
    return requests.get(url, params=params).json()

def clean_stock_data(json_data):
    ts = json_data.get("Time Series (Daily)", {})
    pdf = pd.DataFrame.from_dict(ts, orient="index")
    pdf.reset_index(inplace=True)
    pdf.columns = ["date", "open", "high", "low", "close", "volume"]
    return pdf

@dlt.table(
    name="bronze_stock_prices",
    comment="Raw stock price data from Alpha Vantage API"
)
def bronze_stock_prices():
    dfs = []

    for company in COMPANIES:
        json_data = fetch_stock_data(company)
        pdf = clean_stock_data(json_data)

        df = spark.createDataFrame(pdf)
        df = df.withColumn("company", lit(company))

        dfs.append(df)
        time.sleep(RATE_LIMIT_SECONDS)  # API rate limit

    return reduce(lambda df1, df2: df1.union(df2), dfs)