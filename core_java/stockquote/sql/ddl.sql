-- switch to database from command line
-- psql -h [hostname] -U [username] -d [database] -f [file]
-- Example:
-- psql -h localhost -U postgres -d stock_quote -f sql/ddl.sql

-- Note: We will be using the finnhub Quote Endpoint API to fetch stock info, so we
-- will no longer have fields in the quote object for latest_trading_day & volume.

DROP TABLE IF EXISTS quote;
CREATE TABLE quote (
                       symbol              VARCHAR(10) PRIMARY KEY,
                       open                DECIMAL(10, 2) NOT NULL,
                       high                DECIMAL(10, 2) NOT NULL,
                       low                 DECIMAL(10, 2) NOT NULL,
                       price               DECIMAL(10, 2) NOT NULL,
                       previous_close      DECIMAL(10, 2) NOT NULL,
                       change              DECIMAL(10, 2) NOT NULL,
                       change_percent      VARCHAR(10) NOT NULL,
                       timestamp           TIMESTAMP DEFAULT CURRENT_TIMESTAMP  NOT NULL
);

DROP TABLE IF EXISTS position;
CREATE TABLE position (
                          symbol                VARCHAR(10) PRIMARY KEY,
                          number_of_shares      INT NOT NULL,
                          value_paid            DECIMAL(10, 2) NOT NULL,
                          CONSTRAINT symbol_fk	FOREIGN KEY (symbol) REFERENCES quote(symbol)
);