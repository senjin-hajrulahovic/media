--
-- PostgreSQL database dump
--

-- Dumped from database version 11.3 (Debian 11.3-1.pgdg90+1)
-- Dumped by pg_dump version 11.3 (Debian 11.3-1.pgdg90+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: media; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.media (
    id integer NOT NULL,
    title character(50) NOT NULL
);


ALTER TABLE public.media OWNER TO postgres;

--
-- Name: mediaz; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mediaz (
    id integer NOT NULL,
    title character(50) NOT NULL
);


ALTER TABLE public.mediaz OWNER TO postgres;

--
-- Data for Name: media; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.media (id, title) FROM stdin;
\.


--
-- Data for Name: mediaz; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.mediaz (id, title) FROM stdin;
\.


--
-- Name: media media_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.media
    ADD CONSTRAINT media_pkey PRIMARY KEY (id);


--
-- Name: mediaz mediaz_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mediaz
    ADD CONSTRAINT mediaz_pkey PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--

