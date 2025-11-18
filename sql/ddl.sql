-- switch to cd database from command line
-- psql -h [hostname] -U [username] -d [database] -f [file]
-- Example:
-- psql -h localhost -U postgres -d host_agent -f ddl.sql

-- Table: cd.members

CREATE TABLE IF NOT EXISTS public.members
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

-- Table: cd.facilities

CREATE TABLE IF NOT EXISTS public.facilities
(
    facid integer NOT NULL,
    name character varying(100) NOT NULL,
    membercost numeric NOT NULL,
    guestcost numeric NOT NULL,
    initialoutlay numeric NOT NULL,
    monthlymaintenance numeric NOT NULL,
    CONSTRAINT facilities_pkey PRIMARY KEY (facid)
    );

-- Table: cd.bookings

CREATE TABLE IF NOT EXISTS public.bookings
(
    bookid integer NOT NULL,
    facid integer NOT NULL,
    memid integer NOT NULL,
    starttime timestamp NOT NULL,
    slots integer NOT NULL,
    CONSTRAINT bookings_pkey PRIMARY KEY (bookid),
    CONSTRAINT fk_bookings_facid FOREIGN KEY (facid)
    REFERENCES public.facilities (facid),
    CONSTRAINT fk_bookings_memid FOREIGN KEY (memid)
    REFERENCES public.members (memid)
    );