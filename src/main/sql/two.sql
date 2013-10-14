
drop type kfs_interv;
/
drop type body kfs_interv;
/
drop type kfs_list_interv_one;
/
drop type kfs_interv_one;
/
drop TYPE body kfs_interv_one;
/

create or replace TYPE kfs_interv_one AS OBJECT
(
  m_from DATE,
  m_to   DATE,
  m_sign NUMBER,
  CONSTRUCTOR FUNCTION kfs_interv_one (m_from IN DATE, m_to IN DATE, m_sign IN NUMBER )
    RETURN SELF AS RESULT,
  MEMBER FUNCTION is_touch(interv_one kfs_interv_one) RETURN BOOLEAN,
  MEMBER FUNCTION get_from RETURN NUMBER,
  MEMBER FUNCTION get_to RETURN NUMBER

) INSTANTIABLE FINAL;
/

create or replace
TYPE BODY kfs_interv_one
IS
  CONSTRUCTOR FUNCTION kfs_interv_one( m_from IN DATE, m_to IN DATE, m_sign IN NUMBER ) RETURN SELF AS RESULT IS
  BEGIN
    self.m_from := m_from;
    self.m_to   := m_to;
    self.m_sign := m_sign;
    RETURN;
  END kfs_interv_one;

  MEMBER FUNCTION is_touch(interv_one kfs_interv_one) RETURN BOOLEAN IS
  BEGIN
    if ( m_from > interv_one.m_to) then return false; end if;
    if ( m_to   < interv_one.m_from) then return false; end if;
    return true;
  END is_touch;

  MEMBER FUNCTION get_from RETURN NUMBER IS
  inx NUMBER;
  BEGIN
    inx := to_number( to_char(m_from, 'SSSSS'));
    RETURN inx;--TO_NUMBER(m_from);
  END get_from;

  MEMBER FUNCTION get_to RETURN NUMBER IS
  BEGIN
    RETURN to_number( to_char(m_to, 'SSSSS'));
  END get_to;

END; -- BODY kfs_interv_one
/



create or replace TYPE kfs_list_interv_one IS TABLE OF kfs_interv_one;
/

create or replace TYPE kfs_interv AS OBJECT
(
  m_span NUMBER,
  m_lst  kfs_list_interv_one,

  CONSTRUCTOR FUNCTION kfs_interv RETURN SELF AS RESULT,

  MEMBER PROCEDURE plus_interval(v_from DATE, v_to DATE),
  MEMBER PROCEDURE minus_interval(v_from DATE, v_to DATE),
  MEMBER FUNCTION get_span(self in out kfs_interv) RETURN NUMBER,

  STATIC FUNCTION get_normalized(v_lst kfs_list_interv_one)
    RETURN kfs_list_interv_one,
  STATIC PROCEDURE realy_minus(v_lst IN OUT kfs_list_interv_one, v_interv kfs_interv_one)

)  INSTANTIABLE FINAL;
/

create or replace
type BODY kfs_interv IS
  constructor FUNCTION kfs_interv RETURN self AS result AS
  BEGIN
    SELF.m_span := -1;
    SELF.m_lst := kfs_list_interv_one();
    RETURN;
  END kfs_interv;

  member procedure plus_interval(v_from DATE, v_to DATE)  AS
  BEGIN
    m_lst.EXTEND;
    m_lst(m_lst.LAST) := kfs_interv_one(v_from, v_to, 1);
  END plus_interval;

  member procedure minus_interval(v_from DATE,   v_to DATE) AS
  BEGIN
    m_lst.EXTEND;
    m_lst(m_lst.LAST) := kfs_interv_one(v_from, v_to, -1);
  END minus_interval;

  member FUNCTION get_span(self in out kfs_interv) RETURN NUMBER AS
    v_lst kfs_list_interv_one;
    inx BINARY_INTEGER;
  BEGIN
    IF (m_span = -1) THEN
      if (m_lst.count <= 0) THEN
        return -1;
      END IF;
      v_lst := kfs_interv.get_normalized(m_lst);
      m_span := 0;

      FOR inx IN v_lst.first..v_lst.last LOOP
        if (v_lst.exists(inx)) then
        /*
        dbms_output.put('t1('||inx||'): '|| m_span);
        dbms_output.put(' ' || v_lst(inx).m_to);
        dbms_output.put(' - ' || v_lst(inx).m_from);
        */
        m_span := m_span + v_lst(inx).get_to - v_lst(inx).get_from;
        --dbms_output.put_line(' = ' || m_span);
        end if;
      END LOOP;
    END IF;
    RETURN m_span;
  END get_span;

  STATIC FUNCTION get_normalized(v_lst kfs_list_interv_one) RETURN kfs_list_interv_one AS
    p_lst kfs_list_interv_one;
    inx1 BINARY_INTEGER;
    inx2 BINARY_INTEGER;
    rep1 BOOLEAN;
  BEGIN
    p_lst := kfs_list_interv_one();

    if (v_lst.count <= 0) then
      return p_lst;
    end if;

    if (v_lst.count = 1) then
      p_lst.extend;
      p_lst(p_lst.last) := v_lst(v_lst.last);
      return p_lst;
    end if;

    -- get all plus intervals
    for inx1 in v_lst.first .. v_lst.last loop
      if (v_lst(inx1).m_sign > 0) then
        p_lst.extend;
        p_lst(p_lst.last) := v_lst(inx1);
      end if;
    end loop;

    -- join touching intervals (+-sec ;-)
    loop
      rep1 := FALSE;
      for inx1 IN p_lst.first .. p_lst.last loop
        for inx2 IN p_lst.first .. p_lst.last loop
          if (inx1 != inx2) then
            --
            if (p_lst(inx1).is_touch(p_lst(inx2))) then
              -- from
              if (p_lst(inx1).m_from < p_lst(inx2).m_from) then
                p_lst(inx1).m_from := p_lst(inx1).m_from;
              else
                p_lst(inx1).m_from := p_lst(inx2).m_from;
              end if; -- from

              -- to
              if (p_lst(inx1).m_to >  p_lst(inx2).m_to) then
                p_lst(inx1).m_to := p_lst(inx1).m_to;
              else
                p_lst(inx1).m_to := p_lst(inx2).m_to;
              end if; -- to

              p_lst.delete(inx2);
              rep1 := TRUE;
              exit;
            end if; -- inx1 != inx2
            --
          end if;
        end loop;
        if (rep1) then exit; end if;
      end loop;
      if (not rep1) then exit; end if;
    end loop;

    for inx1 IN v_lst.first .. v_lst.last loop
      if (v_lst(inx1).m_sign < 0) then
        realy_minus(p_lst, v_lst(inx1));
      end if;
    end loop;

    loop
      rep1 := false;
      for inx1 in p_lst.first .. p_lst.last loop
        if ((p_lst(inx1).get_to - p_lst(inx1).get_from)  <= 0) then
          p_lst.delete(inx1);
          rep1 := true;
        end if;
      end loop;
      if (not rep1) then exit; end if;
    end loop;

    RETURN p_lst;
  END get_normalized;


/*
  a:   ----------F--------------------------T----------   // base
 MINUS
  c1:  -F--T-----|--------------------------|----------   // not intersect
  c2:  ----------|--------------------------|---F-T----   // not intersect

  b1:  ---F------|---------T----------------|----------   // intersect
  b2:  -------F--|--------------------------|--T-------   // intersect
  b3:  ----------|-----------------F--------|--T-------   // intersect
  b4:  ----------|-----F---------T----------|----------   // intersect
*/

  STATIC PROCEDURE realy_minus(v_lst IN OUT  kfs_list_interv_one, v_interv kfs_interv_one) AS
    rep1 BOOLEAN;
    inx1 BINARY_INTEGER;
  BEGIN
     loop
       rep1 := false;
       for inx1 in v_lst.first .. v_lst.last loop
         if (v_lst.exists(inx1)) then
         -- NOT c1, c2
         if (not v_lst(inx1).is_touch(v_interv)) then
           goto end_for;
         end if;

         -- b1
         if ((v_lst(inx1).get_from > v_interv.get_from) and (v_lst(inx1).get_to > v_interv.get_to)) then
           v_lst(inx1).m_from := v_interv.m_to;
           --dbms_output.put_line('b1 '||inx1);
           goto end_for;
         end if;

         -- b2
         if ((v_lst(inx1).get_from > v_interv.get_from) and (v_lst(inx1).get_to < v_interv.get_to)) then
           --dbms_output.put_line('b2');
           v_lst.delete(inx1);
           rep1 := true;
           --exit;
           goto end_for;
         end if;

         -- b3
         if ((v_lst(inx1).get_from < v_interv.get_from) and (v_lst(inx1).get_to < v_interv.get_to)) then
           --dbms_output.put_line('b3 ' || inx1);
           v_lst(inx1).m_to := v_interv.m_from;
           goto end_for;
         end if;

         -- b4
         if ((v_lst(inx1).get_from < v_interv.get_from) and (v_lst(inx1).get_to > v_interv.get_to)) then
            --dbms_output.put_line('b4');
            v_lst.extend;
            v_lst(v_lst.last) := kfs_interv_one(v_lst(inx1).m_from, v_interv.m_from, 1);
            v_lst.extend;
            v_lst(v_lst.last) := kfs_interv_one(v_interv.m_to, v_lst(inx1).m_to, 1);

            v_lst.delete(inx1);
            rep1 := true;
            --exit;
          end if;

         <<end_for>>
    /*
         for inx2 in v_lst.first .. v_lst.last loop
           if (v_lst.exists(inx2)) then
           dbms_output.put_line(inx1||' v_lst('||inx2||') : '||
              v_lst(inx2).m_to || ' - ' || v_lst(inx2).m_from );
           end if;
         end loop;
      */
         null;
         end if;
       end loop;
       if (not rep1) then exit; end if;
     end loop;
  END realy_minus;

END; -- body kfs_interv

/


--
declare
 aa kfs_interv;
 bb number;
begin
 aa := kfs_interv;
 aa.plus_interval(
  to_date('2008.08.11 11:20:30', 'yyyy.MM.dd hh24:mi:ss'),
  to_date('2008.08.11 14:20:10', 'yyyy.MM.dd hh24:mi:ss')
  );
 aa.minus_interval(
  to_date('2008.08.11 12:20:30', 'yyyy.MM.dd hh24:mi:ss'),
  to_date('2008.08.11 13:20:30', 'yyyy.MM.dd hh24:mi:ss')
  );

 aa.plus_interval(
  to_date('2008.08.11 15:00:00', 'yyyy.MM.dd hh24:mi:ss'),
  to_date('2008.08.11 16:20:00', 'yyyy.MM.dd hh24:mi:ss')
  );

 aa.minus_interval(
  to_date('2008.08.11 14:00:30', 'yyyy.MM.dd hh24:mi:ss'),
  to_date('2008.08.11 16:00:30', 'yyyy.MM.dd hh24:mi:ss')
  );


 bb := aa.get_span;

 dbms_output.enable(0);

 dbms_output.put_line('bb:=' ||to_date(bb, 'SSSSS'));
end;
/
