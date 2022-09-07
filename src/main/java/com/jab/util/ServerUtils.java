package com.jab.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


// TODO: Auto-generated Javadoc
/**
 * @description: TODO
 * @author mike
 * @date 2022/9/7 14:18
 * @version 1.0
 */
public class ServerUtils {

	/** The Constant GERONIMO_CLASS. */
	public static final String __GERONIMO_CLASS = "/org/apache/geronimo/system/main/Daemon.class";

	/** The Constant JBOSS_CLASS. */
	public static final String __JBOSS_CLASS = "/org/jboss/Main.class";

	/** The Constant JETTY_CLASS. */
	public static final String __JETTY_CLASS = "/org/mortbay/jetty/Server.class";

	/** The Constant JONAS_CLASS. */
	public static final String __JONAS_CLASS = "/org/objectweb/jonas/server/Server.class";

	/** The Constant OC4J_CLASS. */
	public static final String __OC4J_CLASS = "/oracle/jsp/oc4jutil/Oc4jUtil.class";

	/** The Constant ORION_CLASS. */
	public static final String __ORION_CLASS = "/com/evermind/server/ApplicationServer.class";

	/** The Constant PRAMATI_CLASS. */
	public static final String __PRAMATI_CLASS = "/com/pramati/Server.class";

	/** The Constant RESIN_CLASS. */
	public static final String __RESIN_CLASS = "/com/caucho/server/resin/Resin.class";

	/** The Constant REXIP_CLASS. */
	public static final String __REXIP_CLASS = "/com/tcc/Main.class";

	/** The Constant SUN7_CLASS. */
	public static final String __SUN7_CLASS = "/com/iplanet/ias/tools/cli/IasAdminMain.class";

	/** The Constant SUN8_CLASS. */
	public static final String __SUN8_CLASS = "/com/sun/enterprise/cli/framework/CLIMain.class";

	/** The Constant TOMCAT_CLASS. */
	public static final String __TOMCAT_CLASS = "/org/apache/catalina/startup/Bootstrap.class";

	/** The Constant WEBLOGIC_CLASS. */
	public static final String __WEBLOGIC_CLASS = "/weblogic/Server.class";

	/** The Constant WEBSPHERE_CLASS. */
	public static final String __WEBSPHERE_CLASS = "/com/ibm/websphere/product/VersionInfo.class";

	/** The _log. */
	private static Log _log = LogFactory.getLog(ServerUtils.class);

	/** The _instance. */
	private static ServerUtils __instance = new ServerUtils();

	/** The _server id. */
	private String __serverId;

	/** The _geronimo. */
	private Boolean __geronimo;

	/** The _j boss. */
	private Boolean __jBoss;

	/** The _jetty. */
	private Boolean __jetty;

	/** The _jonas. */
	private Boolean __jonas;

	/** The _oc4j. */
	private Boolean __oc4j;

	/** The _orion. */
	private Boolean __orion;

	/** The _pramati. */
	private Boolean __pramati;

	/** The _resin. */
	private Boolean __resin;

	/** The _rex ip. */
	private Boolean __rexIP;

	/** The _sun7. */
	private Boolean __sun7;

	/** The _sun8. */
	private Boolean __sun8;

	/** The _tomcat. */
	private Boolean __tomcat;

	/** The _web logic. */
	private Boolean __webLogic;

	/** The _web sphere. */
	private Boolean __webSphere;

	/**
	 * Gets the server id.
	 * 
	 * @return the server id
	 */
	public static String getServerId() {
		ServerUtils sd = __instance;

		if (sd.__serverId == null) {
			if (isGeronimo()) {
				sd.__serverId = "geronimo";
			} else if (isJBoss()) {
				sd.__serverId = "jboss";
			} else if (isJOnAS()) {
				sd.__serverId = "jonas";
			} else if (isOC4J()) {
				sd.__serverId = "oc4j";
			} else if (isOrion()) {
				sd.__serverId = "orion";
			} else if (isResin()) {
				sd.__serverId = "resin";
			} else if (isWebLogic()) {
				sd.__serverId = "weblogic";
			} else if (isWebSphere()) {
				sd.__serverId = "websphere";
			}

			if (isJetty()) {
				if (sd.__serverId == null) {
					sd.__serverId = "jetty";
				} else {
					sd.__serverId += "-jetty";
				}
			} else if (isTomcat()) {
				if (sd.__serverId == null) {
					sd.__serverId = "tomcat";
				} else {
					sd.__serverId += "-tomcat";
				}
			}

			if ((!_log.isInfoEnabled()) || (sd.__serverId == null)) {
				sd.__serverId = "no server";
			}
		}

		return sd.__serverId;
	}

	/**
	 * Checks if is geronimo.
	 * 
	 * @return true, if is geronimo
	 */
	public static boolean isGeronimo() {
		ServerUtils sd = __instance;

		if (sd.__geronimo == null) {
			Class c = sd.getClass();

			if (c.getResource("/org/apache/geronimo/system/main/Daemon.class") != null) {
				sd.__geronimo = Boolean.TRUE;
			} else {
				sd.__geronimo = Boolean.FALSE;
			}
		}

		return sd.__geronimo.booleanValue();
	}

	/**
	 * Checks if is j boss.
	 * 
	 * @return true, if is j boss
	 */
	public static boolean isJBoss() {
		ServerUtils sd = __instance;

		if (sd.__jBoss == null) {
			Class c = sd.getClass();

			if (c.getResource("/org/jboss/Main.class") != null) {
				sd.__jBoss = Boolean.TRUE;
			} else {
				sd.__jBoss = Boolean.FALSE;
			}
		}

		return sd.__jBoss.booleanValue();
	}

	/**
	 * Checks if is jetty.
	 * 
	 * @return true, if is jetty
	 */
	public static boolean isJetty() {
		ServerUtils sd = __instance;

		if (sd.__jetty == null) {
			Class c = sd.getClass();

			if (c.getResource("/org/mortbay/jetty/Server.class") != null) {
				sd.__jetty = Boolean.TRUE;
			} else {
				sd.__jetty = Boolean.FALSE;
			}
		}

		return sd.__jetty.booleanValue();
	}

	/**
	 * Checks if is j on as.
	 * 
	 * @return true, if is j on as
	 */
	public static boolean isJOnAS() {
		ServerUtils sd = __instance;

		if (sd.__jonas == null) {
			Class c = sd.getClass();

			if (c.getResource("/org/objectweb/jonas/server/Server.class") != null) {
				sd.__jonas = Boolean.TRUE;
			} else {
				sd.__jonas = Boolean.FALSE;
			}
		}

		return sd.__jonas.booleanValue();
	}

	/**
	 * Checks if is o c4 j.
	 * 
	 * @return true, if is o c4 j
	 */
	public static boolean isOC4J() {
		ServerUtils sd = __instance;

		if (sd.__oc4j == null) {
			Class c = sd.getClass();

			if (c.getResource("/oracle/jsp/oc4jutil/Oc4jUtil.class") != null) {
				sd.__oc4j = Boolean.TRUE;
			} else {
				sd.__oc4j = Boolean.FALSE;
			}
		}

		return sd.__oc4j.booleanValue();
	}

	/**
	 * Checks if is orion.
	 * 
	 * @return true, if is orion
	 */
	public static boolean isOrion() {
		ServerUtils sd = __instance;

		if (sd.__orion == null) {
			Class c = sd.getClass();

			if (c.getResource("/com/evermind/server/ApplicationServer.class") != null) {
				sd.__orion = Boolean.TRUE;
			} else {
				sd.__orion = Boolean.FALSE;
			}
		}

		return sd.__orion.booleanValue();
	}

	/**
	 * Checks if is pramati.
	 * 
	 * @return true, if is pramati
	 */
	public static boolean isPramati() {
		ServerUtils sd = __instance;

		if (sd.__pramati == null) {
			Class c = sd.getClass();

			if (c.getResource("/com/pramati/Server.class") != null) {
				sd.__pramati = Boolean.TRUE;
			} else {
				sd.__pramati = Boolean.FALSE;
			}
		}

		return sd.__pramati.booleanValue();
	}

	/**
	 * Checks if is resin.
	 * 
	 * @return true, if is resin
	 */
	public static boolean isResin() {
		ServerUtils sd = __instance;

		if (sd.__resin == null) {
			Class c = sd.getClass();

			if (c.getResource("/com/caucho/server/resin/Resin.class") != null) {
				sd.__resin = Boolean.TRUE;
			} else {
				sd.__resin = Boolean.FALSE;
			}
		}

		return sd.__resin.booleanValue();
	}

	/**
	 * Checks if is rex ip.
	 * 
	 * @return true, if is rex ip
	 */
	public static boolean isRexIP() {
		ServerUtils sd = __instance;

		if (sd.__rexIP == null) {
			Class c = sd.getClass();

			if (c.getResource("/com/tcc/Main.class") != null) {
				sd.__rexIP = Boolean.TRUE;
			} else {
				sd.__rexIP = Boolean.FALSE;
			}
		}

		return sd.__rexIP.booleanValue();
	}

	/**
	 * Checks if is sun.
	 * 
	 * @return true, if is sun
	 */
	public static boolean isSun() {
		return (isSun7()) || (isSun8());
	}

	/**
	 * Checks if is sun7.
	 * 
	 * @return true, if is sun7
	 */
	public static boolean isSun7() {
		ServerUtils sd = __instance;

		if (sd.__sun7 == null) {
			Class c = sd.getClass();

			if (c.getResource("/com/iplanet/ias/tools/cli/IasAdminMain.class") != null) {
				sd.__sun7 = Boolean.TRUE;
			} else {
				sd.__sun7 = Boolean.FALSE;
			}
		}

		return sd.__sun7.booleanValue();
	}

	/**
	 * Checks if is sun8.
	 * 
	 * @return true, if is sun8
	 */
	public static boolean isSun8() {
		ServerUtils sd = __instance;

		if (sd.__sun8 == null) {
			Class c = sd.getClass();

			if (c.getResource("/com/sun/enterprise/cli/framework/CLIMain.class") != null) {
				sd.__sun8 = Boolean.TRUE;
			} else {
				sd.__sun8 = Boolean.FALSE;
			}
		}

		return sd.__sun8.booleanValue();
	}

	/**
	 * Checks if is tomcat.
	 * 
	 * @return true, if is tomcat
	 */
	public static boolean isTomcat() {
		ServerUtils sd = __instance;

		if (sd.__tomcat == null) {
			Class c = sd.getClass();

			if (c.getResource("/org/apache/catalina/startup/Bootstrap.class") != null) {
				sd.__tomcat = Boolean.TRUE;
			} else {
				sd.__tomcat = Boolean.FALSE;
			}
		}

		return sd.__tomcat.booleanValue();
	}

	/**
	 * Checks if is web logic.
	 * 
	 * @return true, if is web logic
	 */
	public static boolean isWebLogic() {
		ServerUtils sd = __instance;

		if (sd.__webLogic == null) {
			Class c = sd.getClass();

			if (c.getResource("/weblogic/Server.class") != null) {
				sd.__webLogic = Boolean.TRUE;
			} else {
				sd.__webLogic = Boolean.FALSE;
			}
		}

		return sd.__webLogic.booleanValue();
	}

	/**
	 * Checks if is web sphere.
	 * 
	 * @return true, if is web sphere
	 */
	public static boolean isWebSphere() {
		ServerUtils sd = __instance;

		if (sd.__webSphere == null) {
			Class c = sd.getClass();

			if (c.getResource("/com/ibm/websphere/product/VersionInfo.class") != null) {
				sd.__webSphere = Boolean.TRUE;
			} else {
				sd.__webSphere = Boolean.FALSE;
			}
		}

		return sd.__webSphere.booleanValue();
	}

}
