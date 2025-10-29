# Introduction

# SQL Queries

###### Table Setup (DDL)
The tables were setup with the pgAdmin4 GUI. The following is the DDL that 
was used to setup the tables.
```sql
-- Table: members

CREATE TABLE IF NOT EXISTS cd.members
(
    memid integer NOT NULL,
    surname character varying(200) NOT NULL,
    firstname character varying(200) NOT NULL,
    address character varying(300) NOT NULL,
    zipcode integer NOT NULL,
    telephone character varying(20) NOT NULL,
    recommendedby integer,
    joindate timestamp NOT NULL,
    CONSTRAINT members_pk PRIMARY KEY (memid),
    CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby)
        REFERENCES public.members (memid) ON DELETE SET NULL
);
```

```sql
-- Table: bookings

CREATE TABLE IF NOT EXISTS public.bookings
(
    bookid integer NOT NULL,
    facid integer NOT NULL,
    memid integer NOT NULL,
    starttime timestamp NOT NULL,
    slots integer NOT NULL,
    CONSTRAINT bookings_pkey PRIMARY KEY (bookid),
    CONSTRAINT fk_bookings_facid FOREIGN KEY (facid)
    REFERENCES public.facilites (facid),
    CONSTRAINT fk_bookings_memid FOREIGN KEY (memid)
    REFERENCES public.members (memid)
    );
```

```sql
-- Table: facilites

CREATE TABLE IF NOT EXISTS public.facilites
(
    facid integer NOT NULL,
    name character varying(100) NOT NULL,
    membercost numeric NOT NULL,
    guestcost numeric NOT NULL,
    initialoutlay numeric NOT NULL,
    monthlymaintenance numeric NOT NULL,
    CONSTRAINT facilites_pkey PRIMARY KEY (facid)
    );
```
###### Question 1: Show all members 

```sql
SELECT *
FROM cd.members
```

###### Question 2: Lorem ipsum...

```sql
SELECT blah blah 
```

