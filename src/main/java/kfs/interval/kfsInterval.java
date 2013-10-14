package kfs.interval;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author pavedrim
 */
public class kfsInterval {

    private Long span;
    private final kfsOneIntervalList intervals;

    public kfsInterval() {
        intervals = new kfsOneIntervalList();
        span = -1l;
    }

    public kfsInterval plus(Date from, Date to) {
        intervals.plus(from, to);
        span = -1l;
        return this;
    }

    public kfsInterval minus(Date from, Date to) {
        intervals.add(new kfsOneInterval(from, to, kfsISign.minus));
        span = -1l;
        return this;
    }

    public Long getSpan() {
        if (span >= 0l) {
            return span;
        }
        ArrayList<kfsOneInterval> outLst = getNormalized(intervals);
        span = 0l;
        for (kfsOneInterval ii : outLst) {
            span += ii.getSeconds();
        }
        return span;
    }

    private static ArrayList<kfsOneInterval> getNormalized(ArrayList<kfsOneInterval> inLst) {
        ArrayList<kfsOneInterval> outLst = new ArrayList<kfsOneInterval>();
        if (inLst.isEmpty()) {
            return outLst;
        }
        if (inLst.size() == 1) {
            outLst.add(inLst.get(0));
            return outLst;
        }
        for (kfsOneInterval ii : inLst) {
            if (ii.isPlus()) {
                outLst.add(ii);
            }
        }

/*
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

 */        
        
        return outLst;
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
