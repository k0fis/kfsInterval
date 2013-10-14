drop TYPE body kfs_interv_one;
/
drop type kfs_list_interv_one;
/
drop type kfs_interv_one;
/
drop type kfs_interv;
/
drop type body kfs_interv;
/

create or replace
TYPE kfs_interv_one AS OBJECT
(
  m_from DATE,
  m_to   DATE,
  m_sign NUMBER(1),
  CONSTRUCTOR FUNCTION kfs_interv_one (
    v_from DATE,
    v_to DATE,
    v_sign NUMBER
  ) RETURN SELF AS RESULT,
  MEMBER FUNCTION is_touch(interv_one kfs_interv_one)
    RETURN BOOLEAN

) INSTANTIABLE FINAL;
/

create or replace
TYPE BODY kfs_interv_one
IS
  CONSTRUCTOR FUNCTION kfs_interv_one (
    v_from DATE,
    v_to DATE,
    v_sign NUMBER
  ) RETURN SELF AS RESULT IS
  BEGIN
    SELF.m_from := v_from;
    SELF.m_to   := v_to;
    SELF.m_sign := v_sign;
    RETURN;
  END kfs_interv_one;

  MEMBER FUNCTION is_touch(interv_one kfs_interv_one) RETURN BOOLEAN IS
   bout BOOLEAN;
  BEGIN
    bout := not ((SELF.m_from > interv_one.m_to) or (SELF.m_to   <
interv_one.m_from) );
    RETURN bout;
  END is_touch;

END; -- BODY kfs_interv_one
/

create or replace TYPE kfs_list_interv_one IS TABLE OF kfs_interv_one;
/

create or replace
TYPE kfs_interv AS OBJECT
(
  m_span NUMBER(15),
  m_lst  kfs_list_interv_one,

  CONSTRUCTOR FUNCTION kfs_interv RETURN SELF AS RESULT,

  MEMBER FUNCTION plus_interval(v_from DATE, v_to DATE) RETURN NUMBER,
  MEMBER FUNCTION minus_interval(v_from DATE, v_to DATE) RETURN NUMBER,
  MEMBER FUNCTION get_span RETURN NUMBER

)  INSTANTIABLE FINAL;
/

CREATE OR REPLACE type BODY kfs_interv IS
  constructor FUNCTION kfs_interv RETURN self AS result AS
  BEGIN
    SELF.m_span := -1;
    RETURN;
  END kfs_interv;

  member FUNCTION plus_interval(v_from DATE,   v_to DATE) RETURN NUMBER AS
  BEGIN
    RETURN 0;
  END plus_interval;

  member FUNCTION minus_interval(v_from DATE,   v_to DATE) RETURN NUMBER AS
  BEGIN
    RETURN 0;
  END minus_interval;

  member FUNCTION get_span RETURN NUMBER AS
  BEGIN
    RETURN m_span;
  END get_span;

END; -- body kfs_interv
/
