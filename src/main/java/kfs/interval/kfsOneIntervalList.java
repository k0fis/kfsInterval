package kfs.interval;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author pavedrim
 */
public class kfsOneIntervalList implements Iterable<kfsOneInterval> {

    private ArrayList<kfsOneInterval> lst;

    public void add(Date from, Date to) {
        lst.add(new kfsOneInterval(from, to));
    }

    public Iterator<kfsOneInterval> iterator() {
        return lst.iterator();
    }

    public kfsOneIntervalList normalize() {
        ArrayList<kfsOneInterval> outLst = new ArrayList<kfsOneInterval>();
        if (lst.size() <= 1) {
            return this;
        }
        
        for (int inx1 = 0; inx1 < lst.size(); inx1++) {
            kfsOneInterval t1 = lst.get(inx1);
            for (int inx2 = 0; inx2 < lst.size(); inx2++) {
                if (inx1 != inx2) {
                    kfsOneInterval t2 = lst.get(inx2);
                }
            }            
        }

        /*
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
         */
        return this;
    }
    
    public kfsOneIntervalList minus(kfsOneInterval mVal) {
        return this;
    }
    
            

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
/*
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

         <<end_for>>*/
    /*
     *    for inx2 in v_lst.first .. v_lst.last loop
     *      if (v_lst.exists(inx2)) then
     *      dbms_output.put_line(inx1||' v_lst('||inx2||') : '||
     *         v_lst(inx2).m_to || ' - ' || v_lst(inx2).m_from );
     *      end if;
     *    end loop;
     */ /*
         null;
         end if;
       end loop;
       if (not rep1) then exit; end if;
     end loop;
  END realy_minus;
  */    
}
