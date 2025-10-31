# Introduction
In this project, I learned SQL fundamentals using the pgAdmin4 desktop app alongside a Docker-based
PostgreSQL 16 (Alpine) instance. I began by creating the tables with a ddl.sql file. I also loaded a 
data set into the database with the script `clubdata.sql`, populating it with test records. With this 
environment, I created and executed a series of SQL queries designed to answer specific analysis 
questions. This hands-on practice helped me deepen my understanding of relational database concepts, such 
as DDL, modifying data, joins, table structure, filtering, aggregation, and strings, and improved my 
confidence in writing effective, well-structured SQL commands.

# SQL Queries

###### Table Setup (DDL)
```sql
-- Table: members

CREATE TABLE IF NOT EXISTS public.members (
    memid integer NOT NULL,
    surname character varying(200) NOT NULL,
    firstname character varying(200) NOT NULL,
    address character varying(300) NOT NULL,
    zipcode integer NOT NULL,
    telephone character varying(20) NOT NULL,
    recommendedby integer,
    joindate timestamp NOT NULL,
    CONSTRAINT members_pk PRIMARY KEY (memid),
    CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby) REFERENCES public.members (memid) ON DELETE
    SET
    NULL
    );

```

```sql
-- Table: bookings

CREATE TABLE IF NOT EXISTS public.bookings (
    bookid integer NOT NULL,
    facid integer NOT NULL,
    memid integer NOT NULL,
    starttime timestamp NOT NULL,
    slots integer NOT NULL,
    CONSTRAINT bookings_pkey PRIMARY KEY (bookid),
    CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES public.facilities (facid),
    CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES public.members (memid)
    );

```

```sql
-- Table: facilities

CREATE TABLE IF NOT EXISTS public.facilities (
    facid integer NOT NULL,
    name character varying(100) NOT NULL,
    membercost numeric NOT NULL,
    guestcost numeric NOT NULL,
    initialoutlay numeric NOT NULL,
    monthlymaintenance numeric NOT NULL,
    CONSTRAINT facilities_pkey PRIMARY KEY (facid)
    );

```

##### Modifying Data
###### Question #1
```sql
INSERT INTO cd.facilities (
facid, name, membercost, guestcost,
initialoutlay, monthlymaintenance
)
VALUES
(9, 'Spa', 20, 30, 100000, 800.);
```

###### Question #2
```sql
INSERT INTO cd.facilities (
facid, name, membercost, guestcost,
initialoutlay, monthlymaintenance
)
VALUES
(
(
SELECT
max(facid)
from
cd.facilities
) + 1,
'Spa',
20,
30,
100000,
800.
);
```

###### Question #3
```sql
UPDATE
cd.facilities
SET
initialoutlay = 10000
WHERE
name = 'Tennis Court 2';
```

###### Question #4
```sql
UPDATE
cd.facilities
SET
membercost = (
SELECT
membercost
from
cd.facilities
WHERE
name = 'Tennis Court 1'
) * 1.1,
guestcost = (
SELECT
guestcost
from
cd.facilities
WHERE
name = 'Tennis Court 1'
) * 1.1
WHERE
name = 'Tennis Court 2';
```

###### Question #5
```sql
DELETE FROM
cd.bookings;
```

###### Question #6
```sql
DELETE FROM
cd.members
WHERE
memid = 37;
```

##### Basics
###### Question #7
```sql
SELECT
facid,
name,
membercost,
monthlymaintenance
FROM
cd.facilities
WHERE
membercost < (monthlymaintenance / 50)
AND membercost > 0;
```

###### Question #8
```sql
SELECT
*
FROM
cd.facilities
WHERE
name LIKE '%Tennis%';
```

###### Question #9
```sql
SELECT
*
FROM
cd.facilities
WHERE
facid IN (1, 5);
```

###### Question #10
```sql
SELECT
memid,
surname,
firstname,
joindate
FROM
cd.members
WHERE
joindate > '2012-08-31 23:59:59';
```

###### Question #11
```sql
SELECT
surname
FROM
cd.members
UNION
SELECT
name
FROM
cd.facilities;
```

##### Join
###### Question #12
```sql
SELECT
starttime
FROM
cd.bookings
JOIN cd.members ON cd.bookings.memid = cd.members.memid
WHERE
cd.members.firstname = 'David'
and cd.members.surname = 'Farrell';
```

###### Question #13
```sql
SELECT
starttime AS start,
name
FROM
cd.bookings
JOIN cd.facilities ON cd.bookings.facid = cd.facilities.facid
WHERE
name LIKE 'Tennis Court%'
AND starttime >= '2012-09-21 00:00:00'
AND starttime <= '2012-09-22 00:00:00'
ORDER BY
start ASC;
```

###### Question #14
```sql
SELECT
mems.firstname AS memfname,
mems.surname AS memsname,
recs.firstname as recfname,
recs.surname AS recsname
FROM
cd.members mems
LEFT JOIN cd.members recs ON mems.recommendedby = recs.memid
ORDER BY
memsname,
memfname ASC;
```

###### Question #15
```sql
SELECT
DISTINCT recs.firstname AS firstname,
recs.surname AS surname
FROM
cd.members recs
JOIN cd.members mems ON recs.memid = mems.recommendedby
ORDER BY
surname,
firstname;
```

###### Question #16
```sql
SELECT
DISTINCT mems.firstname || ' ' || mems.surname AS member,
(
SELECT
recs.firstname || ' ' || recs.surname AS recommender
FROM
cd.members recs
WHERE
mems.recommendedby = recs.memid
)
FROM
cd.members mems;
```

##### Aggregation
###### Question #17
```sql
SELECT
recommendedby,
COUNT(*)
FROM
cd.members
WHERE
recommendedby IS NOT NULL
GROUP BY
recommendedby
ORDER BY
recommendedby;
```

###### Question #18
```sql
SELECT
facid,
SUM(slots)
FROM
cd.bookings
GROUP BY
facid
ORDER BY
facid ASC;
```

###### Question #19
```sql
SELECT
facid,
SUM(slots) AS "Total Slots"
FROM
cd.bookings
WHERE
starttime >= '2012-09-01 00:00:00'
AND starttime < '2012-10-01 00:00:00'
GROUP BY
facid
ORDER BY
"Total Slots" ASC;
```

###### Question #20
```sql
SELECT
facid,
EXTRACT(
MONTH
FROM
starttime
) AS month,
SUM(slots) AS "Total Slots"
FROM
cd.bookings
WHERE
EXTRACT(
YEAR
FROM
starttime
) = 2012
GROUP BY
facid,
month;
```

###### Question #21
```sql
SELECT
COUNT(DISTINCT memid)
FROM
cd.bookings;
```

###### Question #22
```sql
SELECT
surname,
firstname,
m.memid,
MIN(starttime)
FROM
cd.bookings b
LEFT JOIN cd.members m ON b.memid = m.memid
WHERE
starttime >= '2012-09-01 00:00:00'
GROUP BY
m.memid
ORDER BY
memid;
```

###### Question #23
```sql
SELECT
COUNT(memid) OVER (),
firstname,
surname
FROM
cd.members;
```

###### Question #24
```sql
SELECT
ROW_NUMBER() OVER (),
firstname,
surname
FROM
cd.members
ORDER BY
joindate ASC;
```

###### Question #25
```sql
SELECT
facid,
total
FROM
(
SELECT
facid,
SUM(slots) AS total,
RANK() OVER (
ORDER BY
SUM(slots) DESC
)
FROM
cd.bookings
GROUP BY
facid
)
WHERE
rank = 1;
```

##### String
###### Question #26
```sql
SELECT
surname || ', ' || firstname
FROM
cd.members;
```

###### Question #27
```sql
SELECT
memid,
telephone
FROM
cd.members
WHERE
telephone LIKE '%(%)%'
ORDER BY
memid ASC;
```

###### Question #28
```sql
SELECT
SUBSTR(surname, 1, 1) AS Letter,
COUNT(*)
FROM
cd.members
GROUP BY
Letter
ORDER BY
Letter;
```

