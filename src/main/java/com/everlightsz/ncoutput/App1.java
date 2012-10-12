package com.everlightsz.ncoutput;

import org.quartz.impl.StdScheduler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.everlightsz.datahub.imple.DataHub;


/**
 * Hello world!
 *
 */
public class App1 
{
    public static void main( String[] args ) throws Exception
    {	
    	
    	if(args.length<1){
    		throw new Exception("need args");
    	}
    	javax.swing.JOptionPane.showMessageDialog(null,args[0]);
    	ApplicationContext cx = new ClassPathXmlApplicationContext("applicationContext.xml");
    	StdScheduler s=(StdScheduler) cx.getBean("schedulerTrigger");
    	s.pauseAll();
    	javax.swing.JOptionPane.showMessageDialog(null,"停止了");
    	String style=args[0];
    	if(style.equals("quartz")){
    		s.resumeAll();
    	}
    	else if(style.equals("now")){
    		javax.swing.JOptionPane.showMessageDialog(null,"执行中……");
    		DataHub dh=(DataHub)cx.getBean("DataHub");
        	dh.hub();
    	}
    	else{
    		javax.swing.JOptionPane.showMessageDialog(null,"退出了"+args[0]);
    		System.exit(1);
    		throw new Exception("args error");
    	}
    	
    }
}
