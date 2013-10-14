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
    
    void joinPlus() {
        
    }
    
    public void add(Date from, Date to) {
        kfsOneInterval ta = new kfsOneInterval(from, to);
        for (int inx1 = 0; inx1 < lst.size(); inx1++) {
            kfsOneInterval ti = lst.get(inx1);
            
        }
        /*

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
         
        */        
        
        lst.add(new kfsOneInterval(from, to));
    }

    public Iterator<kfsOneInterval> iterator() {
        return lst.iterator();
    }
    
    public kfsOneIntervalList normalize() {
        return this;
    }
}
