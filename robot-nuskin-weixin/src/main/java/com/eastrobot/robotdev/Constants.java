package com.eastrobot.robotdev;


public interface Constants {
	public static byte[] CACHE_LOCK = new byte[0];
	public static final int FreshMan_Gift = 12;
	public static final int Full_QuoTa_Gift = 11;
	public static final int LUCKY_DRAW = 1;
	public static final String DO_NOW = "@do_now@";
	
	public static final String ENCODE = "utf-8";
	public static final String NUSKIN = "@system:nuskin@";
	public static final String QUARTER = "quarter";
	public static final String YEAR = "year";
	public static final String TOKEN = "token";
	public static final String NO_CONTENT = "no_content";
	public static final String DATE_FORMAT = "yyyy年MM月dd日HH:mm";
	
	public static final String IMG_TXT_MSG = "@IMG_TXT_MSG@";
	public static final String LOGIN = "login";
		
	public static interface BindInfo{
		public static final String partnerId = "partnerId";
		public static final String password = "password";
		public static final String externalId = "externalId";
		public static final String externalTypeCd = "externalTypeCd";
	}
	
	public static interface Platform{
		public static final String WEIXIN = "weixin";
		public static final String WEB = "web";
		public static final String ALL = "all";
	}
	
	public static interface ImgTxtMsg{
		public static final String TITLE = "Title";
		public static final String DESCRIPTION = "Description";
		public static final String PIC_URL = "PicUrl";
		public static final String URL = "Url";
	}
}
