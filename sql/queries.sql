-- Modifying Data
-- Question #1
INSERT INTO cd.facilities (
    facid, name, membercost, guestcost,
    initialoutlay, monthlymaintenance
)
VALUES
    (9, 'Spa', 20, 30, 100000, 800.);

-- Question #2
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

-- Question #3
UPDATE
    cd.facilities
SET
    initialoutlay = 10000
WHERE
    name = 'Tennis Court 2';

-- Question #4
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

-- Question #5
DELETE FROM
    cd.bookings;

-- Question #6
DELETE FROM
    cd.members
WHERE
    memid = 37;

-- Basics
-- Question #7
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

-- Question #8
SELECT
    *
FROM
    cd.facilities
WHERE
    name LIKE '%Tennis%';

-- Question #9
SELECT
    *
FROM
    cd.facilities
WHERE
    facid IN (1, 5);

-- Question #10
SELECT
    memid,
    surname,
    firstname,
    joindate
FROM
    cd.members
WHERE
    joindate > '2012-08-31 23:59:59';

-- Question #11
SELECT
    surname
FROM
    cd.members
UNION
SELECT
    name
FROM
    cd.facilities;

-- Join
-- Question #12
SELECT
    starttime
FROM
    cd.bookings
        JOIN cd.members ON cd.bookings.memid = cd.members.memid
WHERE
    cd.members.firstname = 'David'
  and cd.members.surname = 'Farrell';

-- Question #13
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

-- Question #14
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

-- Question #15
SELECT
    DISTINCT recs.firstname AS firstname,
             recs.surname AS surname
FROM
    cd.members recs
        JOIN cd.members mems ON recs.memid = mems.recommendedby
ORDER BY
    surname,
    firstname;

-- Question #16
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

-- Aggregation
-- Question #17
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

-- Question #18
SELECT
    facid,
    SUM(slots)
FROM
    cd.bookings
GROUP BY
    facid
ORDER BY
    facid ASC;

-- Question #19
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

-- Question #20
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

-- Question #21
SELECT
    COUNT(DISTINCT memid)
FROM
    cd.bookings;

-- Question #22
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

-- Question #23
SELECT
    COUNT(memid) OVER (),
    firstname,
    surname
FROM
    cd.members;

-- Question #24
SELECT
    ROW_NUMBER() OVER (),
    firstname,
    surname
FROM
    cd.members
ORDER BY
    joindate ASC;

-- Question #25
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

-- String
-- Question #26
SELECT
    surname || ', ' || firstname
FROM
    cd.members;

-- Question #27
SELECT
    memid,
    telephone
FROM
    cd.members
WHERE
    telephone LIKE '%(%)%'
ORDER BY
    memid ASC;

-- Question #28
SELECT
    SUBSTR(surname, 1, 1) AS Letter,
    COUNT(*)
FROM
    cd.members
GROUP BY
    Letter
ORDER BY
    Letter;
