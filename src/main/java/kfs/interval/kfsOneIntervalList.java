package kfs.interval;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author pavedrim
 */
public class kfsOneIntervalList extends ArrayList<kfsOneInterval>{
    
    public void plus(Date from, Date to) {
        
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
        
        add(new kfsOneInterval(from, to, kfsISign.plus));
    }    
}
