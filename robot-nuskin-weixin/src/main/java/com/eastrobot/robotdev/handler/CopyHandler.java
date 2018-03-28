package com.eastrobot.robotdev.handler;

import org.springframework.stereotype.Component;

import com.eastrobot.robotnerve.Channel;
import com.eastrobot.robotnerve.Handler;
import com.eastrobot.robotnerve.HandlerScope;
import com.eastrobot.robotnerve.annotations.handler.Scope;
import com.eastrobot.robotnerve.annotations.handler.StartWith;
import com.eastrobot.robotnerve.app.HandlerHelper;
import com.eastrobot.robotnerve.events.CommandEvent;

@Component("hhhh")
@StartWith(commands={"hhhh"})
@Scope(HandlerScope.SESSION)
public class CopyHandler implements Handler<CommandEvent>{

	private HandlerHelper help = HandlerHelper.getInstance();
	@Override
	public void handle(Channel channel, CommandEvent event) {
		// TODO Auto-generated method stub
		channel.send("test");
		channel.send("sss");
	}

	private void toAi()
	{
		help.nav.toAI();
	}
	
	private void exit()
	{
		help.ss.x();
	}
}
