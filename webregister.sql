--
-- PostgreSQL database dump
--

-- Dumped from database version 10.1
-- Dumped by pg_dump version 10.1

-- Started on 2018-03-26 00:22:49 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3164 (class 1262 OID 16393)
-- Name: register; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE register WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'C' LC_CTYPE = 'C';


\connect register

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 13241)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 3166 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_with_oids = false;

--
-- TOC entry 205 (class 1259 OID 16666)
-- Name: achievements; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE achievements (
    id integer NOT NULL,
    achievement_desc character varying(255),
    achievement_name character varying(255),
    group_id integer
);


--
-- TOC entry 204 (class 1259 OID 16664)
-- Name: achievements_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE achievements_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3167 (class 0 OID 0)
-- Dependencies: 204
-- Name: achievements_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE achievements_id_seq OWNED BY achievements.id;


--
-- TOC entry 197 (class 1259 OID 16508)
-- Name: assignment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE assignment (
    group_role integer,
    group_id integer NOT NULL,
    user_id integer NOT NULL
);


--
-- TOC entry 199 (class 1259 OID 16546)
-- Name: groups; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE groups (
    group_id integer NOT NULL,
    group_name character varying(64),
    group_desc character varying(256),
    group_vacancies integer
);


--
-- TOC entry 198 (class 1259 OID 16544)
-- Name: groups_group_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE groups_group_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3168 (class 0 OID 0)
-- Dependencies: 198
-- Name: groups_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE groups_group_id_seq OWNED BY groups.group_id;


--
-- TOC entry 201 (class 1259 OID 16554)
-- Name: presence; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE presence (
    id integer NOT NULL,
    user_id integer,
    group_id integer,
    date date,
    presence boolean
);


--
-- TOC entry 200 (class 1259 OID 16552)
-- Name: presence_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE presence_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3169 (class 0 OID 0)
-- Dependencies: 200
-- Name: presence_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE presence_id_seq OWNED BY presence.id;


--
-- TOC entry 196 (class 1259 OID 16394)
-- Name: tokens; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE tokens (
    token character varying NOT NULL,
    expiration timestamp(4) with time zone NOT NULL,
    user_id integer DEFAULT 0,
    fingerprint character varying(128)
);


--
-- TOC entry 206 (class 1259 OID 16680)
-- Name: user_achievements; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE user_achievements (
    user_id integer NOT NULL,
    achievement_id integer NOT NULL
);


--
-- TOC entry 203 (class 1259 OID 16562)
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE users (
    id integer NOT NULL,
    mail character varying(64) NOT NULL,
    password character varying(64),
    type integer NOT NULL,
    firstname character varying(64),
    lastname character varying(64)
);


--
-- TOC entry 202 (class 1259 OID 16560)
-- Name: users_id_seq1; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE users_id_seq1
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3170 (class 0 OID 0)
-- Dependencies: 202
-- Name: users_id_seq1; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE users_id_seq1 OWNED BY users.id;


--
-- TOC entry 3023 (class 2604 OID 16669)
-- Name: achievements id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY achievements ALTER COLUMN id SET DEFAULT nextval('achievements_id_seq'::regclass);


--
-- TOC entry 3020 (class 2604 OID 16549)
-- Name: groups group_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY groups ALTER COLUMN group_id SET DEFAULT nextval('groups_group_id_seq'::regclass);


--
-- TOC entry 3021 (class 2604 OID 16557)
-- Name: presence id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY presence ALTER COLUMN id SET DEFAULT nextval('presence_id_seq'::regclass);


--
-- TOC entry 3022 (class 2604 OID 16565)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq1'::regclass);


--
-- TOC entry 3035 (class 2606 OID 16674)
-- Name: achievements achievements_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY achievements
    ADD CONSTRAINT achievements_pkey PRIMARY KEY (id);


--
-- TOC entry 3027 (class 2606 OID 16512)
-- Name: assignment assignment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY assignment
    ADD CONSTRAINT assignment_pkey PRIMARY KEY (group_id, user_id);


--
-- TOC entry 3029 (class 2606 OID 16551)
-- Name: groups groups_pkey1; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY groups
    ADD CONSTRAINT groups_pkey1 PRIMARY KEY (group_id);


--
-- TOC entry 3031 (class 2606 OID 16559)
-- Name: presence presence_pkey1; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY presence
    ADD CONSTRAINT presence_pkey1 PRIMARY KEY (id);


--
-- TOC entry 3025 (class 2606 OID 16447)
-- Name: tokens tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tokens
    ADD CONSTRAINT tokens_pkey PRIMARY KEY (token);


--
-- TOC entry 3037 (class 2606 OID 16684)
-- Name: user_achievements user_achievements_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_achievements
    ADD CONSTRAINT user_achievements_pkey PRIMARY KEY (user_id, achievement_id);


--
-- TOC entry 3033 (class 2606 OID 16567)
-- Name: users users_pkey1; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey1 PRIMARY KEY (id, mail);


--
-- TOC entry 3038 (class 2606 OID 16675)
-- Name: achievements fk_achievements_group_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY achievements
    ADD CONSTRAINT fk_achievements_group_id FOREIGN KEY (group_id) REFERENCES groups(group_id);


-- Completed on 2018-03-26 00:22:50 CEST

--
-- PostgreSQL database dump complete
--

